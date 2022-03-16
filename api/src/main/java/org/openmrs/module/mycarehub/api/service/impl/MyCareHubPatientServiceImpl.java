package org.openmrs.module.mycarehub.api.service.impl;

import com.google.gson.JsonObject;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycarehub.api.db.MyCareHubPatientDao;
import org.openmrs.module.mycarehub.api.rest.mapper.MedicalRecordRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubAllergy;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubMedication;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubTest;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubTestOrder;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubVitalSign;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistrationRequest;
import org.openmrs.module.mycarehub.api.service.MyCareHubPatientService;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;
import org.openmrs.module.mycarehub.utils.MyCareHubUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.openmrs.module.mycarehub.utils.Constants.CCC_NUMBER_IDENTIFIER_TYPE_UUID;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.BMI;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.CD4_COUNT;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.HEIGHT;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.PULSE;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.RESPIRATORY_RATE;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.SPO2;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.TEMPERATURE;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.VIRAL_LOAD;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.VitalSigns.WEIGHT;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.KENYAEMR_MEDICAL_RECORDS;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.KENYAEMR_PATIENT_REGISTRATIONS;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.MYCAREHUB_CLIENT_REGISTRATIONS;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.NEXT_OF_KIN_CONTACT;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.NEXT_OF_KIN_NAME;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.NEXT_OF_KIN_RELATIONSHIP;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.TELEPHONE_CONTACT;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.getDefaultLocationMflCode;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.uploadPatientMedicalRecords;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.uploadPatientRegistrationRecords;

public class MyCareHubPatientServiceImpl extends BaseOpenmrsService implements MyCareHubPatientService {
	
	MyCareHubPatientDao myCareHubPatientDao;
	
	MyCareHubPatientServiceImpl(MyCareHubPatientDao myCareHubPatientDao) {
		this.myCareHubPatientDao = myCareHubPatientDao;
	}
	
	@Override
	public void syncPatientData() {
		uploadNewOrUpdatedPatientDemographicsSinceLastSyncDate();
		fetchRegisteredClientIdentifiersSinceLastSyncDate();
		uploadUpdatedPatientsMedicalRecordsSinceLastSyncDate();
	}
	
	public void uploadNewOrUpdatedPatientDemographicsSinceLastSyncDate() {
		MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
		MyCareHubSetting setting = settingsService.getLatestMyCareHubSettingByType(KENYAEMR_PATIENT_REGISTRATIONS);
		
		if (setting != null) {
			List<PatientRegistrationRequest> patientRegistrationRequests = new ArrayList<PatientRegistrationRequest>();
			Date newSyncTime = new Date();
			List<Patient> patients = myCareHubPatientDao.getCccPatientsCreatedOrUpdatedSinceDate(setting.getLastSyncTime());
			
			for (Patient patient : patients) {
				PatientRegistrationRequest registrationRequest = new PatientRegistrationRequest();
				registrationRequest.setName(patient.getFamilyName() + patient.getGivenName());
				registrationRequest.setClientType("KenyaEMR");
				registrationRequest.setCounseled("false");
				registrationRequest.setGender(patient.getGender());
				
				registrationRequest.setFacility(getDefaultLocationMflCode());
				
				PatientIdentifierType cccIdentifierType = Context.getPatientService().getPatientIdentifierTypeByUuid(
				    CCC_NUMBER_IDENTIFIER_TYPE_UUID);
				registrationRequest.setCccNumber(patient.getPatientIdentifier(cccIdentifierType).getIdentifier());
				
				String pattern = "yyyy-MM-dd";
				SimpleDateFormat sf = new SimpleDateFormat(pattern);
				registrationRequest.setDateOfBirth(sf.format(patient.getBirthdate()));
				registrationRequest.setBirthdateEstimated(patient.getBirthdateEstimated());
				
				registrationRequest.setEnrollmentDate(sf.format(patient.getDateCreated()));
				
				PersonAttributeType phoneNumberAttributeType = Context.getPersonService().getPersonAttributeTypeByUuid(
				    TELEPHONE_CONTACT);
				if (phoneNumberAttributeType != null) {
					PersonAttribute phoneNumberAttribute = patient.getAttribute(phoneNumberAttributeType);
					if (phoneNumberAttribute != null) {
						registrationRequest.setPhoneNumber(phoneNumberAttribute.getValue());
					}
				}
				
				JsonObject nextOfKinJsonObject = new JsonObject();
				
				PersonAttributeType nextOfKinNameAttributeType = Context.getPersonService().getPersonAttributeTypeByUuid(
				    NEXT_OF_KIN_NAME);
				if (nextOfKinNameAttributeType != null) {
					PersonAttribute nextOfKinNamePersonAttribute = patient.getAttribute(nextOfKinNameAttributeType);
					if (nextOfKinNamePersonAttribute != null) {
						nextOfKinJsonObject.addProperty("next_of_kin_name", nextOfKinNamePersonAttribute.getValue());
					}
				}
				
				PersonAttributeType nextOfKinContactAttributeType = Context.getPersonService().getPersonAttributeTypeByUuid(
				    NEXT_OF_KIN_CONTACT);
				if (nextOfKinContactAttributeType != null) {
					PersonAttribute nextOfKinContactPersonAttribute = patient.getAttribute(nextOfKinContactAttributeType);
					if (nextOfKinContactPersonAttribute != null) {
						nextOfKinJsonObject.addProperty("contacts_of_next_of_kin",
						    nextOfKinContactPersonAttribute.getValue());
					}
				}
				
				PersonAttributeType nextOfKinRelationshipAttributeType = Context.getPersonService()
				        .getPersonAttributeTypeByUuid(NEXT_OF_KIN_RELATIONSHIP);
				if (nextOfKinRelationshipAttributeType != null) {
					PersonAttribute nextOfKinRelationshipPersonAttribute = patient
					        .getAttribute(nextOfKinRelationshipAttributeType);
					if (nextOfKinRelationshipPersonAttribute != null) {
						nextOfKinJsonObject.addProperty("relationship_to_next_of_kin",
						    nextOfKinRelationshipPersonAttribute.getValue());
					}
				}
				
				registrationRequest.setNextOfKin(nextOfKinJsonObject.toString());
				patientRegistrationRequests.add(registrationRequest);
			}
			if (patientRegistrationRequests.size() > 0) {
				uploadPatientRegistrationRecords(patientRegistrationRequests, newSyncTime);
			} else {
				settingsService.createMyCareHubSetting(KENYAEMR_PATIENT_REGISTRATIONS, newSyncTime);
			}
		} else {
			settingsService.createMyCareHubSetting(KENYAEMR_PATIENT_REGISTRATIONS, new Date());
		}
	}
	
	private void fetchRegisteredClientIdentifiersSinceLastSyncDate() {
		MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
		MyCareHubSetting setting = settingsService.getLatestMyCareHubSettingByType(MYCAREHUB_CLIENT_REGISTRATIONS);
		Date lastSyncTime = new Date(0);
		if (setting != null) {
			lastSyncTime = setting.getLastSyncTime();
		}
		
		List<String> patientCccs = MyCareHubUtil.getNewMyCareHubClientCccIdentifiers(lastSyncTime, new Date());
		
		List<Patient> patients = new ArrayList<Patient>();
		
		for (String ccc : patientCccs) {
			List<Patient> patientsWithCcc = myCareHubPatientDao.getCccPatientsByIdentifier(ccc);
			if (patientsWithCcc.size() > 0) {
				patients.addAll(patientsWithCcc);
			}
		}

		//set last sync time to 3 years back, to get historical records for the period
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 3);
		lastSyncTime = cal.getTime();
		Date newSyncTime = new Date();
		
		uploadPatientsMedicalRecordsSinceDate(patients, lastSyncTime, newSyncTime);
	}
	
	private void uploadUpdatedPatientsMedicalRecordsSinceLastSyncDate() {
		MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
		MyCareHubSetting setting = settingsService.getLatestMyCareHubSettingByType(KENYAEMR_MEDICAL_RECORDS);
		
		Date newSyncTime = new Date();
		if (setting != null) {
			List<Patient> patientsWithUpdatedMedicalRecords = myCareHubPatientDao
			        .getCccPatientsWithUpdatedMedicalRecordsSinceDate(setting.getLastSyncTime());
			uploadPatientsMedicalRecordsSinceDate(patientsWithUpdatedMedicalRecords, setting.getLastSyncTime(), newSyncTime);
		} else {
			settingsService.createMyCareHubSetting(KENYAEMR_MEDICAL_RECORDS, newSyncTime);
		}
	}
	
	private void uploadPatientsMedicalRecordsSinceDate(List<Patient> patients, Date lastSyncTime, Date newSyncTime) {
		List<MedicalRecordRequest> medicalRecordRequestList = new ArrayList<MedicalRecordRequest>();
		for (Patient patient : patients) {
			MedicalRecordRequest medicalRecordRequest = new MedicalRecordRequest();
			
			List<MyCareHubVitalSign> vitalSigns = new ArrayList<MyCareHubVitalSign>();
			medicalRecordRequest.setVitalSigns(vitalSigns);
			List<Obs> observations = myCareHubPatientDao.getUpdatedVitalSignsSinceDate(patient, lastSyncTime);
			for (final Obs obs : observations) {
				switch (obs.getConcept().getConceptId()) {
					case TEMPERATURE:
						vitalSigns.add(new MyCareHubVitalSign() {
							
							{
								setConcept("temperature");
								setObsDatetime(obs.getObsDatetime());
								setValue(obs.getValueAsString(Locale.ENGLISH));
							}
						});
						break;
					case WEIGHT:
						vitalSigns.add(new MyCareHubVitalSign() {
							
							{
								setConcept("weight");
								setObsDatetime(obs.getObsDatetime());
								setValue(obs.getValueAsString(Locale.ENGLISH));
							}
						});
						break;
					case HEIGHT:
						vitalSigns.add(new MyCareHubVitalSign() {
							
							{
								setConcept("height");
								setObsDatetime(obs.getObsDatetime());
								setValue(obs.getValueAsString(Locale.ENGLISH));
							}
						});
						break;
					case BMI:
						vitalSigns.add(new MyCareHubVitalSign() {
							
							{
								setConcept("bmi");
								setObsDatetime(obs.getObsDatetime());
								setValue(obs.getValueAsString(Locale.ENGLISH));
							}
						});
						break;
					case SPO2:
						vitalSigns.add(new MyCareHubVitalSign() {
							
							{
								setConcept("spo2");
								setObsDatetime(obs.getObsDatetime());
								setValue(obs.getValueAsString(Locale.ENGLISH));
							}
						});
						break;
					case PULSE:
						vitalSigns.add(new MyCareHubVitalSign() {
							
							{
								setConcept("pulse");
								setObsDatetime(obs.getObsDatetime());
								setValue(obs.getValueAsString(Locale.ENGLISH));
							}
						});
						break;
					case CD4_COUNT:
						vitalSigns.add(new MyCareHubVitalSign() {
							
							{
								setConcept("cd4");
								setObsDatetime(obs.getObsDatetime());
								setValue(obs.getValueAsString(Locale.ENGLISH));
							}
						});
						break;
					case VIRAL_LOAD:
						vitalSigns.add(new MyCareHubVitalSign() {
							
							{
								setConcept("viral_load");
								setObsDatetime(obs.getObsDatetime());
								setValue(obs.getValueAsString(Locale.ENGLISH));
							}
						});
						break;
					case RESPIRATORY_RATE:
						vitalSigns.add(new MyCareHubVitalSign() {
							
							{
								setConcept("respiratory_rate");
								setObsDatetime(obs.getObsDatetime());
								setValue(obs.getValueAsString(Locale.ENGLISH));
							}
						});
						break;
				}
			}
			
			List<MyCareHubTestOrder> myCareHubTestOrders = new ArrayList<MyCareHubTestOrder>();
			medicalRecordRequest.setTestOrders(myCareHubTestOrders);
			List<Obs> testOrderObs = myCareHubPatientDao.getUpdatedTestOrdersSinceDate(patient, lastSyncTime);
			for (final Obs order : testOrderObs) {
				myCareHubTestOrders.add(new MyCareHubTestOrder() {
					
					{
						setOrderDateTime(order.getObsDatetime());
						setOrderedTestName(order.getValueAsString(Locale.ENGLISH));
					}
				});
			}
			
			List<MyCareHubTest> myCareHubTests = new ArrayList<MyCareHubTest>();
			medicalRecordRequest.setTests(myCareHubTests);
			List<Obs> tests = myCareHubPatientDao.getUpdatedTestsSinceDate(patient, lastSyncTime);
			for (final Obs test : tests) {
				myCareHubTests.add(new MyCareHubTest() {
					
					{
						setTestName(test.getConcept().getName().getName());
						setTestDateTime(test.getObsDatetime());
						setResult(test.getValueAsString(Locale.ENGLISH));
					}
				});
			}
			
			List<MyCareHubMedication> myCareHubMedications = new ArrayList<MyCareHubMedication>();
			medicalRecordRequest.setMedications(myCareHubMedications);
			List<Obs> medications = myCareHubPatientDao.getUpdatedMedicationsSinceDate(patient, lastSyncTime);
			for (final Obs medication : medications) {
				myCareHubMedications.add(new MyCareHubMedication() {
					
					{
						setMedicationName(medication.getConcept().getName().getName());
						setMedicationDateTime(medication.getObsDatetime());
						setValue(medication.getValueAsString(Locale.ENGLISH));
					}
				});
			}
			
			List<MyCareHubAllergy> allergies = myCareHubPatientDao.getUpdatedAllergiesSinceDate(patient, lastSyncTime);
			medicalRecordRequest.setAllergies(allergies);
			
			medicalRecordRequestList.add(medicalRecordRequest);
		}
		if (medicalRecordRequestList.size() > 0) {
			uploadPatientMedicalRecords(medicalRecordRequestList, newSyncTime);
		} else {
			Context.getService(MyCareHubSettingsService.class).createMyCareHubSetting(KENYAEMR_MEDICAL_RECORDS, newSyncTime);
		}
	}
}
