package org.openmrs.module.mycarehub.api.service.impl;

import static org.openmrs.module.mycarehub.utils.Constants.APPOINTMENT_DATE_CONCEPT_ID;
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
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.MedicalRecordKeys.APPOINTMENT_DATE_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.MedicalRecordKeys.BMI_CONCEPT_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.MedicalRecordKeys.CD4_CONCEPT_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.MedicalRecordKeys.HEIGHT_CONCEPT_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.MedicalRecordKeys.PULSE_CONCEPT_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.MedicalRecordKeys.RESPIRATORY_RATE_CONCEPT_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.MedicalRecordKeys.SPO2_CONCEPT_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.MedicalRecordKeys.TEMPERATURE_CONCEPT_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.MedicalRecordKeys.VIRAL_LOAD_CONCEPT_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.MedicalRecordKeys.WEIGHT_CONCEPT_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.NextOfKinPatientRegistrationKeys.NEXT_OF_KIN_CONTACTS_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.NextOfKinPatientRegistrationKeys.NEXT_OF_KIN_NAME_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.NextOfKinPatientRegistrationKeys.NEXT_OF_KIN_RELATIONSHIP_KEY;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.NEXT_OF_KIN_CONTACT;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.NEXT_OF_KIN_NAME;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.NEXT_OF_KIN_RELATIONSHIP;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.TELEPHONE_CONTACT;
import static org.openmrs.module.mycarehub.utils.Constants.mycarehubDateTimePattern;

import com.google.gson.JsonObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycarehub.api.db.MyCareHubPatientDao;
import org.openmrs.module.mycarehub.api.rest.mapper.MedicalRecord;
import org.openmrs.module.mycarehub.api.rest.mapper.MedicalRecordsRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubAllergy;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubMedication;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubTest;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubTestOrder;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubVitalSign;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistration;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistrationRequest;
import org.openmrs.module.mycarehub.api.service.MyCareHubPatientService;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.ConsentedPatient;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;
import org.openmrs.module.mycarehub.utils.MyCareHubUtil;

public class MyCareHubPatientServiceImpl extends BaseOpenmrsService
    implements MyCareHubPatientService {

  MyCareHubPatientDao myCareHubPatientDao;

  MyCareHubPatientServiceImpl(MyCareHubPatientDao myCareHubPatientDao) {
    this.myCareHubPatientDao = myCareHubPatientDao;
  }

  @Override
  public void syncPatientData() {
    uploadUpdatedPatientDemographicsSinceLastSyncDate();
    uploadUpdatedPatientsMedicalRecordsSinceLastSyncDate();

    List<Patient> newMyCareHubClients = fetchRegisteredClientIdentifiersSinceLastSyncDate();
    if (!newMyCareHubClients.isEmpty()) {
      // set last sync time to 3 years back, to get historical records for the period
      // Also set it as the new sync time to avoid conflict with the regular medical record sync
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.YEAR, -3);
      Date lastSyncTime = cal.getTime();
      saveConsentedPatients(newMyCareHubClients);
      uploadPatientsMedicalRecordsSinceDate(newMyCareHubClients, lastSyncTime, lastSyncTime);
    }
  }

  public void saveConsentedPatients(List<Patient> listFromMycareHub) {
    List<Integer> clientPatientIdList = new ArrayList<Integer>();
    for (Patient client : listFromMycareHub) {
      clientPatientIdList.add(client.getId());
    }
    List<ConsentedPatient> previouslySyncedPatients =
        myCareHubPatientDao.getConsentedPatientsInList(clientPatientIdList);
    List<Integer> previouslySyncedPatientIds = new ArrayList<Integer>();
    for (ConsentedPatient consentedPatient : previouslySyncedPatients) {
      previouslySyncedPatientIds.add(consentedPatient.getPatientId());
    }

    clientPatientIdList.removeAll(previouslySyncedPatientIds);
    if (!clientPatientIdList.isEmpty()) {
      for (final Integer patientId : clientPatientIdList) {
        ConsentedPatient cPatient = new ConsentedPatient();
        cPatient.setPatientId(patientId);
        myCareHubPatientDao.saveNewConsentedPatients(cPatient);
      }
    }
  }

  public void uploadUpdatedPatientDemographicsSinceLastSyncDate() {
    MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
    MyCareHubSetting setting =
        settingsService.getLatestMyCareHubSettingByType(KENYAEMR_PATIENT_REGISTRATIONS);

    Date newSyncTime = new Date();

    if (setting != null) {
      List<PatientRegistration> patientRegistrations =
          getUpdatedPatientRegistrationsSinceLastSyncDate(setting.getLastSyncTime());
      if (!patientRegistrations.isEmpty()) {
        PatientRegistrationRequest patientRegistrationRequest = new PatientRegistrationRequest();
        patientRegistrationRequest.setPatientRegistrations(patientRegistrations);
        patientRegistrationRequest.setFacility(MyCareHubUtil.getDefaultLocationMflCode());

        MyCareHubUtil.uploadPatientRegistrationRecords(patientRegistrationRequest, newSyncTime);
      } else {
        settingsService.createMyCareHubSetting(KENYAEMR_PATIENT_REGISTRATIONS, newSyncTime);
      }
    } else {
      settingsService.createMyCareHubSetting(KENYAEMR_PATIENT_REGISTRATIONS, newSyncTime);
    }
  }

  public List<PatientRegistration> getUpdatedPatientRegistrationsSinceLastSyncDate(
      Date lastSyncDate) {

    List<PatientRegistration> patientRegistrationRequests = new ArrayList<PatientRegistration>();
    List<Integer> patientIds =
        myCareHubPatientDao.getConsentedPatientIdsUpdatedSinceDate(lastSyncDate);

    for (Integer patientId : patientIds) {
      Patient patient = Context.getPatientService().getPatient(patientId);
      PatientRegistration registrationRequest = createPatientRegistration(patient);
      patientRegistrationRequests.add(registrationRequest);
    }
    return patientRegistrationRequests;
  }

  private PatientRegistration createPatientRegistration(Patient patient) {
    PatientRegistration registrationRequest = new PatientRegistration();
    registrationRequest.setName(patient.getFamilyName() + " " + patient.getGivenName());
    // TODO: 24/10/2022 Extract these strings into a constants file
    registrationRequest.setClientType("KenyaEMR");
    registrationRequest.setCounseled(false);
    registrationRequest.setMFLCODE(MyCareHubUtil.getDefaultLocationMflCode());
    if ("M".equals(patient.getGender())) {
      registrationRequest.setGender("male");
    } else {
      registrationRequest.setGender("female");
    }

    PatientIdentifierType cccIdentifierType =
        Context.getPatientService().getPatientIdentifierTypeByUuid(CCC_NUMBER_IDENTIFIER_TYPE_UUID);
    registrationRequest.setCccNumber(
        patient
            .getPatientIdentifier(cccIdentifierType.getPatientIdentifierTypeId())
            .getIdentifier());

    // TODO: 24/10/2022 Extract these strings into a constants file
    String pattern = "yyyy-MM-dd";
    SimpleDateFormat sf = new SimpleDateFormat(pattern);
    registrationRequest.setDateOfBirth(sf.format(patient.getBirthdate()));
    registrationRequest.setBirthdateEstimated(patient.getBirthdateEstimated());
    Context.getProgramWorkflowService().getPatientProgram(patient.getPatientId());
    registrationRequest.setEnrollmentDate(sf.format(patient.getDateCreated()));

    PersonAttributeType phoneNumberAttributeType =
        Context.getPersonService().getPersonAttributeTypeByUuid(TELEPHONE_CONTACT);
    if (phoneNumberAttributeType != null) {
      PersonAttribute phoneNumberAttribute = patient.getAttribute(phoneNumberAttributeType);
      if (phoneNumberAttribute != null) {
        registrationRequest.setPhoneNumber(phoneNumberAttribute.getValue());
      }
    }

    JsonObject nextOfKinJsonObject = new JsonObject();

    PersonAttributeType nextOfKinNameAttributeType =
        Context.getPersonService().getPersonAttributeTypeByUuid(NEXT_OF_KIN_NAME);
    if (nextOfKinNameAttributeType != null) {
      PersonAttribute nextOfKinNamePersonAttribute =
          patient.getAttribute(nextOfKinNameAttributeType);
      if (nextOfKinNamePersonAttribute != null) {
        nextOfKinJsonObject.addProperty(
            NEXT_OF_KIN_NAME_KEY, nextOfKinNamePersonAttribute.getValue());
      }
    }

    PersonAttributeType nextOfKinContactAttributeType =
        Context.getPersonService().getPersonAttributeTypeByUuid(NEXT_OF_KIN_CONTACT);
    if (nextOfKinContactAttributeType != null) {
      PersonAttribute nextOfKinContactPersonAttribute =
          patient.getAttribute(nextOfKinContactAttributeType);
      if (nextOfKinContactPersonAttribute != null) {
        nextOfKinJsonObject.addProperty(
            NEXT_OF_KIN_CONTACTS_KEY, nextOfKinContactPersonAttribute.getValue());
      }
    }

    PersonAttributeType nextOfKinRelationshipAttributeType =
        Context.getPersonService().getPersonAttributeTypeByUuid(NEXT_OF_KIN_RELATIONSHIP);
    if (nextOfKinRelationshipAttributeType != null) {
      PersonAttribute nextOfKinRelationshipPersonAttribute =
          patient.getAttribute(nextOfKinRelationshipAttributeType);
      if (nextOfKinRelationshipPersonAttribute != null) {
        nextOfKinJsonObject.addProperty(
            NEXT_OF_KIN_RELATIONSHIP_KEY, nextOfKinRelationshipPersonAttribute.getValue());
      }
    }
    registrationRequest.setNextOfKin(nextOfKinJsonObject);
    return registrationRequest;
  }

  private List<Patient> fetchRegisteredClientIdentifiersSinceLastSyncDate() {
    MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
    MyCareHubSetting setting =
        settingsService.getLatestMyCareHubSettingByType(MYCAREHUB_CLIENT_REGISTRATIONS);
    Date lastSyncTime = new Date(0);
    if (setting != null) {
      lastSyncTime = setting.getLastSyncTime();
    }

    List<String> patientCccs =
        MyCareHubUtil.getNewMyCareHubClientCccIdentifiers(lastSyncTime, new Date());

    List<Patient> patients = new ArrayList<Patient>();

    for (String ccc : patientCccs) {
      List<Integer> patientIdsWithCcc = myCareHubPatientDao.getCccPatientIdsByIdentifier(ccc);
      for (Integer patientId : patientIdsWithCcc) {
        patients.add(Context.getPatientService().getPatient(patientId));
      }
    }

    return patients;
  }

  private void uploadUpdatedPatientsMedicalRecordsSinceLastSyncDate() {
    MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
    MyCareHubSetting setting =
        settingsService.getLatestMyCareHubSettingByType(KENYAEMR_MEDICAL_RECORDS);

    Date newSyncTime = new Date();
    if (setting != null) {
      List<Integer> patientIds =
          myCareHubPatientDao.getConsentedPatientsWithUpdatedMedicalRecordsSinceDate(
              setting.getLastSyncTime());
      List<Patient> patientsWithUpdatedMedicalRecords = new ArrayList<Patient>();
      for (Integer patientId : patientIds) {
        patientsWithUpdatedMedicalRecords.add(Context.getPatientService().getPatient(patientId));
      }
      uploadPatientsMedicalRecordsSinceDate(
          patientsWithUpdatedMedicalRecords, setting.getLastSyncTime(), newSyncTime);
    } else {
      settingsService.createMyCareHubSetting(KENYAEMR_MEDICAL_RECORDS, newSyncTime);
    }
  }

  private void uploadPatientsMedicalRecordsSinceDate(
      List<Patient> patients, Date lastSyncTime, Date newSyncTime) {
    SimpleDateFormat dateTimeFormat = new SimpleDateFormat(mycarehubDateTimePattern);

    List<MedicalRecord> medicalRecords = new ArrayList<MedicalRecord>();
    PatientIdentifierType cccIdentifierType =
        Context.getPatientService().getPatientIdentifierTypeByUuid(CCC_NUMBER_IDENTIFIER_TYPE_UUID);
    for (Patient patient : patients) {
      if (patient.getPatientIdentifier(cccIdentifierType) != null) {
        MedicalRecord medicalRecord = new MedicalRecord();

        List<MyCareHubVitalSign> vitalSigns = new ArrayList<MyCareHubVitalSign>();
        List<Obs> observations =
            myCareHubPatientDao.getUpdatedVitalSignsSinceDate(patient, lastSyncTime);
        for (final Obs obs : observations) {
          switch (obs.getConcept().getConceptId()) {
            case TEMPERATURE:
              vitalSigns.add(
                  new MyCareHubVitalSign(
                      TEMPERATURE_CONCEPT_KEY,
                      String.valueOf(obs.getConcept().getConceptId()),
                      dateTimeFormat.format(obs.getObsDatetime()),
                      obs.getValueAsString(Locale.ENGLISH)));
              break;
            case WEIGHT:
              vitalSigns.add(
                  new MyCareHubVitalSign(
                      WEIGHT_CONCEPT_KEY,
                      String.valueOf(obs.getConcept().getConceptId()),
                      dateTimeFormat.format(obs.getObsDatetime()),
                      obs.getValueAsString(Locale.ENGLISH)));
              break;
            case HEIGHT:
              vitalSigns.add(
                  new MyCareHubVitalSign(
                      HEIGHT_CONCEPT_KEY,
                      String.valueOf(obs.getConcept().getConceptId()),
                      dateTimeFormat.format(obs.getObsDatetime()),
                      obs.getValueAsString(Locale.ENGLISH)));
              break;
            case BMI:
              vitalSigns.add(
                  new MyCareHubVitalSign(
                      BMI_CONCEPT_KEY,
                      String.valueOf(obs.getConcept().getConceptId()),
                      dateTimeFormat.format(obs.getObsDatetime()),
                      obs.getValueAsString(Locale.ENGLISH)));
              break;
            case SPO2:
              vitalSigns.add(
                  new MyCareHubVitalSign(
                      SPO2_CONCEPT_KEY,
                      String.valueOf(obs.getConcept().getConceptId()),
                      dateTimeFormat.format(obs.getObsDatetime()),
                      obs.getValueAsString(Locale.ENGLISH)));
              break;
            case PULSE:
              vitalSigns.add(
                  new MyCareHubVitalSign(
                      PULSE_CONCEPT_KEY,
                      String.valueOf(obs.getConcept().getConceptId()),
                      dateTimeFormat.format(obs.getObsDatetime()),
                      obs.getValueAsString(Locale.ENGLISH)));
              break;
            case CD4_COUNT:
              vitalSigns.add(
                  new MyCareHubVitalSign(
                      CD4_CONCEPT_KEY,
                      String.valueOf(obs.getConcept().getConceptId()),
                      dateTimeFormat.format(obs.getObsDatetime()),
                      obs.getValueAsString(Locale.ENGLISH)));
              break;
            case VIRAL_LOAD:
              vitalSigns.add(
                  new MyCareHubVitalSign(
                      VIRAL_LOAD_CONCEPT_KEY,
                      String.valueOf(obs.getConcept().getConceptId()),
                      dateTimeFormat.format(obs.getObsDatetime()),
                      obs.getValueAsString(Locale.ENGLISH)));
              break;
            case RESPIRATORY_RATE:
              vitalSigns.add(
                  new MyCareHubVitalSign(
                      RESPIRATORY_RATE_CONCEPT_KEY,
                      String.valueOf(obs.getConcept().getConceptId()),
                      dateTimeFormat.format(obs.getObsDatetime()),
                      obs.getValueAsString(Locale.ENGLISH)));
              break;

            case APPOINTMENT_DATE_CONCEPT_ID:
              vitalSigns.add(
                  new MyCareHubVitalSign(
                      APPOINTMENT_DATE_KEY,
                      String.valueOf(obs.getConcept().getConceptId()),
                      dateTimeFormat.format(obs.getObsDatetime()),
                      obs.getValueAsString(Locale.ENGLISH)));
              break;
          }
        }
        medicalRecord.setVitalSigns(vitalSigns);

        List<MyCareHubTestOrder> myCareHubTestOrders = new ArrayList<MyCareHubTestOrder>();
        List<Obs> testOrderObs =
            myCareHubPatientDao.getUpdatedTestOrdersSinceDate(patient, lastSyncTime);
        for (final Obs order : testOrderObs) {
          MyCareHubTestOrder myCareHubTestOrder = new MyCareHubTestOrder();
          myCareHubTestOrder.setOrderDateTime(dateTimeFormat.format(order.getObsDatetime()));
          myCareHubTestOrder.setOrderedTestName(order.getValueAsString(Locale.ENGLISH));
          if (order.getValueCoded() != null) {
            myCareHubTestOrder.setConceptId(order.getValueCoded().getConceptId());
          }
          myCareHubTestOrders.add(myCareHubTestOrder);
        }
        medicalRecord.setTestOrders(myCareHubTestOrders);

        List<MyCareHubTest> myCareHubTests = new ArrayList<MyCareHubTest>();
        List<Obs> tests = myCareHubPatientDao.getUpdatedTestsSinceDate(patient, lastSyncTime);
        for (final Obs test : tests) {
          MyCareHubTest myCareHubTest = new MyCareHubTest();
          myCareHubTest.setTestName(test.getConcept().getName().getName());
          myCareHubTest.setTestConceptId(test.getConcept().getConceptId());
          myCareHubTest.setTestDateTime(dateTimeFormat.format(test.getObsDatetime()));
          myCareHubTest.setResult(test.getValueAsString(Locale.ENGLISH));
          if (test.getValueCoded() != null) {
            myCareHubTest.setResultConceptId(test.getValueCoded().getConceptId());
          }
          myCareHubTests.add(myCareHubTest);
        }
        medicalRecord.setTests(myCareHubTests);

        List<MyCareHubMedication> myCareHubMedications = new ArrayList<MyCareHubMedication>();
        List<Obs> medications =
            myCareHubPatientDao.getUpdatedMedicationsSinceDate(patient, lastSyncTime);
        for (final Obs medication : medications) {
          MyCareHubMedication myCareHubMedication = new MyCareHubMedication();
          myCareHubMedication.setMedicationName(medication.getConcept().getName().getName());
          myCareHubMedication.setMedicationConceptId(medication.getConcept().getConceptId());
          myCareHubMedication.setMedicationDateTime(
              dateTimeFormat.format(medication.getObsDatetime()));
          myCareHubMedication.setValue(medication.getValueAsString(Locale.ENGLISH));
          if (medication.getValueCoded() != null) {
            myCareHubMedication.setDrugConceptId(medication.getValueCoded().getConceptId());
          }
          myCareHubMedications.add(myCareHubMedication);
        }
        medicalRecord.setMedications(myCareHubMedications);

        List<MyCareHubAllergy> allergies =
            myCareHubPatientDao.getUpdatedAllergiesSinceDate(patient, lastSyncTime);
        medicalRecord.setAllergies(allergies);

        medicalRecord.setCccNumber(patient.getPatientIdentifier(cccIdentifierType).getIdentifier());

        PatientRegistration registrationRequest = createPatientRegistration(patient);
        medicalRecord.setRegistration(registrationRequest);
        medicalRecords.add(medicalRecord);
      }
    }
    if (!medicalRecords.isEmpty()) {
      MedicalRecordsRequest medicalRecordsRequest = new MedicalRecordsRequest();
      medicalRecordsRequest.setFacility(MyCareHubUtil.getDefaultLocationMflCode());
      medicalRecordsRequest.setMedicalRecords(medicalRecords);
      MyCareHubUtil.uploadPatientMedicalRecords(medicalRecordsRequest, newSyncTime);
    } else {
      Context.getService(MyCareHubSettingsService.class)
          .createMyCareHubSetting(KENYAEMR_MEDICAL_RECORDS, newSyncTime);
    }
  }
}
