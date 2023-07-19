package org.openmrs.module.mycarehub.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycarehub.api.rest.RestApiService;
import org.openmrs.module.mycarehub.api.rest.mapper.LoginResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.MedicalRecord;
import org.openmrs.module.mycarehub.api.rest.mapper.MedicalRecordsRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubAllergy;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubMedication;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubTest;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubTestOrder;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubVitalSign;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistration;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistrationRequest;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.exception.AuthenticationException;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.openmrs.module.mycarehub.utils.Constants.APPOINTMENT_DATE_CONCEPT_ID;
import static org.openmrs.module.mycarehub.utils.Constants.EMPTY;
import static org.openmrs.module.mycarehub.utils.Constants.GP_DEFAULT_LOCATION_MFL_CODE;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_DEFAULT_PASSWORD;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_DEFAULT_USERNAME;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_PASSWORD;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_TOKEN;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_TOKEN_EXPIRY_TIME;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_URL;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_USERNAME;
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
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_APPOINTMENTS_REQUESTS_GET;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_APPOINTMENTS_REQUESTS_POST;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_HEALTH_DIARY_GET;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_RED_FLAGS_REQUESTS_GET;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_RED_FLAGS_REQUESTS_POST;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENTS_CONTAINER_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_DATE_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_ID_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_REASON_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentRequestObjectKeys.APPOINTMENT_PROGRESS_BY_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentRequestObjectKeys.APPOINTMENT_PROGRESS_DATE_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentRequestObjectKeys.APPOINTMENT_REQUEST_CONTAINER;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentRequestObjectKeys.APPOINTMENT_REQUEST_STATUS_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentRequestObjectKeys.APPOINTMENT_RESOLVED_BY_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentRequestObjectKeys.APPOINTMENT_RESOLVED_DATE_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.GeneralKeys.CCC_NUMBER;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.GeneralKeys.FACILITY_MFL_CODE;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.GeneralKeys.MYCAREHUB_ID_KEY;
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
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.REdFlagsObjectKeys.RED_FLAG_CONTAINER;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.REdFlagsObjectKeys.RED_FLAG_PROGRESS_BY_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.REdFlagsObjectKeys.RED_FLAG_PROGRESS_DATE_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.REdFlagsObjectKeys.RED_FLAG_REQUEST_TYPE_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.REdFlagsObjectKeys.RED_FLAG_RESOLVED_BY_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.REdFlagsObjectKeys.RED_FLAG_RESOLVED_DATE_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.REdFlagsObjectKeys.RED_FLAG_STATUS_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.mycarehubDateTimePattern;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
@PowerMockIgnore("javax.net.ssl.*")
public class MyCareHubUtilTest {
	
	private final static String testServerUrl = "https://mycarehub-testing.savannahghi.org/";
	
	private final static String username = "kenya-emr@savannahinformatics.com";
	
	private final static String password = "#kenya-EMR#";
	
	private final static String mflCode = "232343434";
	
	private static final Log log = LogFactory.getLog(MyCareHubUtilTest.class);
	
	private RestApiService restApiService;
	
	private AdministrationService administrationService;
	
	private MyCareHubSettingsService myCareHubSettingsService;
	
	private String pattern = "yyyy-MM-dd";
	
	private SimpleDateFormat sf = new SimpleDateFormat(pattern);
	
	private SimpleDateFormat myCareHubDateTimeFormatter = new SimpleDateFormat(mycarehubDateTimePattern);
	
	@Before
	public void setup() {
		restApiService = mock(RestApiService.class);
		administrationService = mock(AdministrationService.class);
		myCareHubSettingsService = mock(MyCareHubSettingsService.class);
		
		mockStatic(Context.class);
		when(Context.getService(MyCareHubSettingsService.class)).thenReturn(myCareHubSettingsService);
		
		when(Context.getAdministrationService()).thenReturn(administrationService);
		when(administrationService.getGlobalProperty(GP_MYCAREHUB_API_URL, EMPTY)).thenReturn(testServerUrl);
		when(administrationService.getGlobalProperty(GP_MYCAREHUB_API_USERNAME, GP_MYCAREHUB_API_DEFAULT_USERNAME))
		        .thenReturn(username);
		when(administrationService.getGlobalProperty(GP_MYCAREHUB_API_PASSWORD, GP_MYCAREHUB_API_DEFAULT_PASSWORD))
		        .thenReturn(password);
		when(administrationService.getGlobalProperty(GP_DEFAULT_LOCATION_MFL_CODE, EMPTY)).thenReturn(mflCode);
		
		String accessToken = "";
		try {
			LoginResponse response = MyCareHubUtil.authenticateMyCareHub();
			if (response != null) {
				accessToken = response.getAccessToken();
				when(administrationService.getGlobalProperty(GP_MYCAREHUB_API_TOKEN, EMPTY)).thenReturn(accessToken);
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.SECOND, response.getExpiryTime().intValue());
				Date dt = cal.getTime();
				SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				when(administrationService.getGlobalProperty(GP_MYCAREHUB_API_TOKEN_EXPIRY_TIME, EMPTY)).thenReturn(
				    dateTimeFormat.format(dt));
			}
		}
		catch (AuthenticationException e) {
			log.error("Error authenticating", e);
		}
	}
	
	@Test
	public void getNewMyCareHubClientCccIdentifiers_shouldCreateCorrectSyncTimeSetting() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -2);
		Date lastSyncTime = cal.getTime();
		Date newSyncTime = new Date();
		
		MyCareHubUtil.getNewMyCareHubClientCccIdentifiers(lastSyncTime, newSyncTime);
		verify(myCareHubSettingsService, times(1)).createMyCareHubSetting(MYCAREHUB_CLIENT_REGISTRATIONS, newSyncTime);
	}
	
	@Test
	public void fetchPatientAppointmentsRequests_shouldCreateCorrectSyncTimeSetting() {
		Date lastSyncTime = new Date(0);
		Date newSyncTime = new Date();
		
		MyCareHubUtil.fetchPatientAppointmentRequests(lastSyncTime, newSyncTime);
		verify(myCareHubSettingsService, times(1)).createMyCareHubSetting(PATIENT_APPOINTMENTS_REQUESTS_GET, newSyncTime);
	}
	
	@Test
	public void getPatientRedFlagRequests_shouldCreateCorrectSyncTimeSetting() {
		Date lastSyncTime = new Date(0);
		Date newSyncTime = new Date();
		
		MyCareHubUtil.getPatientRedFlagRequests(lastSyncTime, newSyncTime);
		verify(myCareHubSettingsService, times(1)).createMyCareHubSetting(PATIENT_RED_FLAGS_REQUESTS_GET, newSyncTime);
	}
	
	@Test
	public void getPatientHealthDiaries_shouldCreateCorrectSyncTimeSetting() {
		Date lastSyncTime = new Date(0);
		Date newSyncTime = new Date();
		
		MyCareHubUtil.getPatientHealthDiaries(lastSyncTime, newSyncTime);
		verify(myCareHubSettingsService, times(1)).createMyCareHubSetting(PATIENT_HEALTH_DIARY_GET, newSyncTime);
	}
	
	@Test
	public void uploadPatientRegistrationRecords_shouldCreateCorrectSyncTimeSetting() {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		
		List<PatientRegistration> patientRegistrations = new ArrayList<PatientRegistration>();
		PatientRegistration patientRegistration = createDummyPatientRegistration();
		patientRegistrations.add(patientRegistration);
		
		PatientRegistrationRequest request = new PatientRegistrationRequest();
		request.setFacility(MyCareHubUtil.getDefaultLocationMflCode());
		request.setPatientRegistrations(patientRegistrations);
		
		Date newSyncTime = new Date();
		MyCareHubUtil.uploadPatientRegistrationRecords(request, newSyncTime);
		
		verify(myCareHubSettingsService, times(1)).createMyCareHubSetting(KENYAEMR_PATIENT_REGISTRATIONS, newSyncTime);
	}
	
	private PatientRegistration createDummyPatientRegistration() {
		Random random = new Random();
		int number = random.nextInt(99999999);
		PatientRegistration patientRegistration = new PatientRegistration();
		patientRegistration.setName("Dummy Patient");
		patientRegistration.setCccNumber(String.valueOf(number));
		patientRegistration.setClientType("PMTCT");
		patientRegistration.setCounseled(true);
		patientRegistration.setPhoneNumber("07" + String.format("%08d", random.nextInt(99999999)));
		patientRegistration.setGender("male");
		patientRegistration.setMFLCODE(MyCareHubUtil.getDefaultLocationMflCode());
		
		patientRegistration.setDateOfBirth(sf.format(new Date(0)));
		patientRegistration.setBirthdateEstimated(false);
		patientRegistration.setEnrollmentDate(sf.format(new Date()));
		
		List<String> relationshipList = new ArrayList<String>();
		relationshipList.add("Doctor");
		relationshipList.add("Sibling");
		relationshipList.add("Parent");
		relationshipList.add("Child");
		relationshipList.add("Aunt");
		relationshipList.add("Uncle");
		relationshipList.add("Niece");
		relationshipList.add("Nephew");
		Random randomizer = new Random();
		String relationship = relationshipList.get(randomizer.nextInt(relationshipList.size()));
		
		JsonObject nextOfKin = new JsonObject();
		nextOfKin.addProperty(NEXT_OF_KIN_NAME_KEY, "Dummy Relative");
		nextOfKin.addProperty(NEXT_OF_KIN_CONTACTS_KEY, "07" + String.format("%08d", random.nextInt(99999999)));
		nextOfKin.addProperty(NEXT_OF_KIN_RELATIONSHIP_KEY, relationship);
		patientRegistration.setNextOfKin(nextOfKin);
		
		return patientRegistration;
	}
	
	@Test
	public void uploadPatientAppointments_shouldCreateCorrectSyncTimeSetting() {
		JsonObject appointmentObject = new JsonObject();
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		appointmentObject.addProperty(APPOINTMENT_ID_KEY, "58655");
		
		Date appointmentDate = new Date();
		
		appointmentObject.addProperty(APPOINTMENT_DATE_KEY, dateFormat.format(appointmentDate));
		appointmentObject.addProperty(APPOINTMENT_REASON_KEY, "Appointment Reason");
		appointmentObject.addProperty(CCC_NUMBER, "12345555");
		
		JsonArray appointmentsArray = new JsonArray();
		appointmentsArray.add(appointmentObject);
		
		JsonObject containerObject = new JsonObject();
		containerObject.addProperty(FACILITY_MFL_CODE, MyCareHubUtil.getDefaultLocationMflCode());
		containerObject.add(APPOINTMENTS_CONTAINER_KEY, appointmentsArray);
		
		Date newSyncDate = new Date();
		MyCareHubUtil.uploadPatientAppointments(containerObject, newSyncDate);
		
		verify(myCareHubSettingsService, times(1)).createMyCareHubSetting(PATIENT_APPOINTMENTS_REQUESTS_POST, newSyncDate);
	}
	
	@Test
	public void uploadPatientAppointmentRequests_shouldCreateCorrectSyncTimeSetting() {
		JsonObject appointmentObject = new JsonObject();
		appointmentObject.addProperty(MYCAREHUB_ID_KEY, "47e887f5-0cb9-428d-b699-41ef0572e296");
		appointmentObject.addProperty(APPOINTMENT_REQUEST_STATUS_KEY, "IN_PROGRESS");
		
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		appointmentObject.addProperty(APPOINTMENT_PROGRESS_DATE_KEY, dateTimeFormat.format(new Date()));
		appointmentObject.addProperty(APPOINTMENT_PROGRESS_BY_KEY, "User Name");
		appointmentObject.addProperty(APPOINTMENT_RESOLVED_DATE_KEY, "null");
		appointmentObject.addProperty(APPOINTMENT_RESOLVED_BY_KEY, "");
		
		JsonArray appointmentsObject = new JsonArray();
		appointmentsObject.add(appointmentObject);
		
		JsonObject containerObject = new JsonObject();
		containerObject.add(APPOINTMENT_REQUEST_CONTAINER, appointmentsObject);
		
		Date newSyncDate = new Date();
		MyCareHubUtil.uploadPatientAppointmentRequests(containerObject, newSyncDate);
		
		verify(myCareHubSettingsService, times(1)).createMyCareHubSetting(PATIENT_APPOINTMENTS_REQUESTS_POST, newSyncDate);
	}
	
	@Test
	public void postPatientRedFlags_shouldCreateCorrectSyncTimeSetting() {
		JsonObject redFlagObject = new JsonObject();
		redFlagObject.addProperty(MYCAREHUB_ID_KEY, "47e887f5-0cb9-428d-b699-41ef0572e296");
		redFlagObject.addProperty(RED_FLAG_STATUS_KEY, "IN_PROGRESS");
		redFlagObject.addProperty(RED_FLAG_REQUEST_TYPE_KEY, "RED_FLAG");
		
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		redFlagObject.addProperty(RED_FLAG_PROGRESS_DATE_KEY, dateTimeFormat.format(new Date()));
		redFlagObject.addProperty(RED_FLAG_PROGRESS_BY_KEY, "User's Name");
		redFlagObject.addProperty(RED_FLAG_RESOLVED_DATE_KEY, "null");
		redFlagObject.addProperty(RED_FLAG_RESOLVED_BY_KEY, "");
		
		JsonArray redFlagArray = new JsonArray();
		redFlagArray.add(redFlagObject);
		
		JsonObject containerObject = new JsonObject();
		containerObject.add(RED_FLAG_CONTAINER, redFlagArray);
		
		Date newSyncDate = new Date();
		MyCareHubUtil.postPatientRedFlags(containerObject, newSyncDate);
		
		verify(myCareHubSettingsService, times(1)).createMyCareHubSetting(PATIENT_RED_FLAGS_REQUESTS_POST, newSyncDate);
	}
	
	@Test
	public void uploadPatientMedicalRecords_shouldCreateCorrectSyncTimeSetting() {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy");
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setCccNumber("25880678");
		
		PatientRegistration patientRegistration = createDummyPatientRegistration();
		medicalRecord.setRegistration(patientRegistration);
		
		List<MyCareHubVitalSign> vitalSigns = new ArrayList<MyCareHubVitalSign>();
		
		vitalSigns.add(new MyCareHubVitalSign(TEMPERATURE_CONCEPT_KEY, String.valueOf(TEMPERATURE),
		        myCareHubDateTimeFormatter.format(new Date()), "39.1"));
		vitalSigns.add(new MyCareHubVitalSign(WEIGHT_CONCEPT_KEY, String.valueOf(WEIGHT), myCareHubDateTimeFormatter
		        .format(new Date()), "80"));
		vitalSigns.add(new MyCareHubVitalSign(HEIGHT_CONCEPT_KEY, String.valueOf(HEIGHT), myCareHubDateTimeFormatter
		        .format(new Date()), "171"));
		vitalSigns.add(new MyCareHubVitalSign(BMI_CONCEPT_KEY, String.valueOf(BMI), myCareHubDateTimeFormatter
		        .format(new Date()), "25.2"));
		vitalSigns.add(new MyCareHubVitalSign(SPO2_CONCEPT_KEY, String.valueOf(SPO2), myCareHubDateTimeFormatter
		        .format(new Date()), "96"));
		vitalSigns.add(new MyCareHubVitalSign(PULSE_CONCEPT_KEY, String.valueOf(PULSE), myCareHubDateTimeFormatter
		        .format(new Date()), "80"));
		vitalSigns.add(new MyCareHubVitalSign(CD4_CONCEPT_KEY, String.valueOf(CD4_COUNT), myCareHubDateTimeFormatter
		        .format(new Date()), "500"));
		vitalSigns.add(new MyCareHubVitalSign(VIRAL_LOAD_CONCEPT_KEY, String.valueOf(VIRAL_LOAD), myCareHubDateTimeFormatter
		        .format(new Date()), "1000"));
		vitalSigns.add(new MyCareHubVitalSign(RESPIRATORY_RATE_CONCEPT_KEY, String.valueOf(RESPIRATORY_RATE),
		        myCareHubDateTimeFormatter.format(new Date()), "26"));
		
		vitalSigns.add(new MyCareHubVitalSign(APPOINTMENT_DATE_KEY, String.valueOf(APPOINTMENT_DATE_CONCEPT_ID),
		        myCareHubDateTimeFormatter.format(new Date()), dateTimeFormat.format(new Date())));
		
		medicalRecord.setVitalSigns(vitalSigns);
		
		List<MyCareHubTestOrder> myCareHubTestOrders = new ArrayList<MyCareHubTestOrder>();
		MyCareHubTestOrder myCareHubTestOrder = new MyCareHubTestOrder();
		myCareHubTestOrder.setOrderDateTime(myCareHubDateTimeFormatter.format(new Date()));
		myCareHubTestOrder.setOrderedTestName("Complete Blood Count");
		myCareHubTestOrder.setConceptId(1019);
		myCareHubTestOrders.add(myCareHubTestOrder);
		medicalRecord.setTestOrders(myCareHubTestOrders);
		
		List<MyCareHubTest> myCareHubTests = new ArrayList<MyCareHubTest>();
		
		//This test has no test result concept ID
		MyCareHubTest myCareHubTest = new MyCareHubTest();
		myCareHubTest.setTestName("HEMOGLOBIN");
		myCareHubTest.setTestConceptId(21);
		myCareHubTest.setTestDateTime(myCareHubDateTimeFormatter.format(new Date()));
		myCareHubTest.setResult("15.1");
		myCareHubTests.add(myCareHubTest);
		
		MyCareHubTest myCareHubTest2 = new MyCareHubTest();
		myCareHubTest2.setTestName("URINE PREGNANCY TEST");
		myCareHubTest2.setTestConceptId(45);
		myCareHubTest2.setTestDateTime(myCareHubDateTimeFormatter.format(new Date()));
		myCareHubTest2.setResult("POSITIVE");
		myCareHubTest2.setResultConceptId(703);
		myCareHubTests.add(myCareHubTest2);
		
		medicalRecord.setTests(myCareHubTests);
		
		List<MyCareHubMedication> myCareHubMedications = new ArrayList<MyCareHubMedication>();
		MyCareHubMedication myCareHubMedication = new MyCareHubMedication();
		myCareHubMedication.setMedicationName("CURRENT DRUGS USED");
		myCareHubMedication.setMedicationConceptId(1193);
		myCareHubMedication.setMedicationDateTime(myCareHubDateTimeFormatter.format(new Date()));
		myCareHubMedication.setValue("DIDANOSINE");
		myCareHubMedication.setDrugConceptId(796);
		myCareHubMedications.add(myCareHubMedication);
		medicalRecord.setMedications(myCareHubMedications);
		
		List<MyCareHubAllergy> allergies = new ArrayList<MyCareHubAllergy>();
		MyCareHubAllergy myCareHubAllergy = new MyCareHubAllergy();
		myCareHubAllergy.setAllergyName("Caffeine");
		myCareHubAllergy.setAllergyAnswerConceptId(BigInteger.valueOf(72609));
		myCareHubAllergy.setReaction("Arrhythmia");
		myCareHubAllergy.setReactionAnswerConceptId(BigInteger.valueOf(120148));
		myCareHubAllergy.setOtherReaction("Free text description of other reaction");
		myCareHubAllergy.setSeverity("Severe");
		myCareHubAllergy.setSeverityAnswerConceptId(BigInteger.valueOf(160756));
		myCareHubAllergy.setAllergyDateTimeObj(new Date());
		allergies.add(myCareHubAllergy);
		medicalRecord.setAllergies(allergies);
		
		List<MedicalRecord> medicalRecords = new ArrayList<MedicalRecord>();
		medicalRecords.add(medicalRecord);
		
		MedicalRecordsRequest medicalRecordsRequest = new MedicalRecordsRequest();
		medicalRecordsRequest.setFacility(MyCareHubUtil.getDefaultLocationMflCode());
		medicalRecordsRequest.setMedicalRecords(medicalRecords);
		
		Date newSyncTime = new Date();
		MyCareHubUtil.uploadPatientMedicalRecords(medicalRecordsRequest, newSyncTime);
		
		// TODO: fix test case
		//		verify(myCareHubSettingsService, times(1)).createMyCareHubSetting(KENYAEMR_MEDICAL_RECORDS, newSyncTime);
		
	}
	
}
