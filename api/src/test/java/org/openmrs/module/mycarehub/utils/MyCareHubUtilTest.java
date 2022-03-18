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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.openmrs.module.mycarehub.utils.Constants.EMPTY;
import static org.openmrs.module.mycarehub.utils.Constants.GP_DEFAULT_LOCATION_MFL_CODE;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_DEFAULT_PASSWORD;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_DEFAULT_USERNAME;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_PASSWORD;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_TOKEN;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_TOKEN_EXPIRY_TIME;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_URL;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_USERNAME;
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
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_STATUS_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_TIME_SLOT_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_TYPE_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_UUID_KEY;
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
	
	private String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	
	private SimpleDateFormat sf = new SimpleDateFormat(pattern);
	
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
		Date lastSyncTime = new Date(10000);
		Date newSyncTime = new Date();
		
		MyCareHubUtil.getNewMyCareHubClientCccIdentifiers(lastSyncTime, newSyncTime);
		verify(myCareHubSettingsService, times(1)).createMyCareHubSetting(MYCAREHUB_CLIENT_REGISTRATIONS, newSyncTime);
	}
	
	@Test
	public void fetchPatientAppointments_shouldCreateCorrectSyncTimeSetting() {
		Date lastSyncTime = new Date(0);
		Date newSyncTime = new Date();
		
		MyCareHubUtil.fetchPatientAppointments(lastSyncTime, newSyncTime);
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
		
		PatientRegistration patientRegistration = new PatientRegistration();
		patientRegistration.setName("Dummy Patient");
		patientRegistration.setCccNumber("123456");
		patientRegistration.setClientType("KenyaEMR");
		patientRegistration.setCounseled("False");
		patientRegistration.setPhoneNumber("0788888888");
		patientRegistration.setGender("M");
		
		patientRegistration.setDateOfBirth(sf.format(new Date(0)));
		patientRegistration.setBirthdateEstimated(false);
		patientRegistration.setEnrollmentDate(sf.format(new Date()));
		
		JsonObject nextOfKin = new JsonObject();
		nextOfKin.addProperty(NEXT_OF_KIN_NAME_KEY, "Kin Name");
		nextOfKin.addProperty(NEXT_OF_KIN_CONTACTS_KEY, "0789999999");
		nextOfKin.addProperty(NEXT_OF_KIN_RELATIONSHIP_KEY, "Father");
		patientRegistration.setNextOfKin(nextOfKin.toString());
		
		List<PatientRegistration> patientRegistrations = new ArrayList<PatientRegistration>();
		patientRegistrations.add(patientRegistration);
		
		PatientRegistrationRequest request = new PatientRegistrationRequest();
		request.setFacility(MyCareHubUtil.getDefaultLocationMflCode());
		request.setPatientRegistrations(patientRegistrations);
		
		Date newSyncTime = new Date();
		MyCareHubUtil.uploadPatientRegistrationRecords(request, newSyncTime);
		
		verify(myCareHubSettingsService, times(1)).createMyCareHubSetting(KENYAEMR_PATIENT_REGISTRATIONS, newSyncTime);
	}
	
	@Test
	public void uploadPatientAppointments_shouldCreateCorrectSyncTimeSetting() {
		JsonObject appointmentObject = new JsonObject();
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		appointmentObject.addProperty(APPOINTMENT_UUID_KEY, "f67a4ad9-4ac4-4183-9852-1dd75b04aff5");
		
		Date appointmentStartDate = new Date();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 30);
		Date appointmentEndDate = cal.getTime();
		
		appointmentObject.addProperty(APPOINTMENT_DATE_KEY, dateFormat.format(appointmentStartDate));
		appointmentObject.addProperty(APPOINTMENT_TIME_SLOT_KEY,
		    timeFormat.format(appointmentStartDate) + " " + timeFormat.format(appointmentEndDate));
		appointmentObject.addProperty(APPOINTMENT_TYPE_KEY, "TypeName");
		appointmentObject.addProperty(APPOINTMENT_STATUS_KEY, "StatusName");
		appointmentObject.addProperty(CCC_NUMBER, "12345");
		
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
		appointmentObject.addProperty(APPOINTMENT_PROGRESS_BY_KEY, "User's Name");
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
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setCccNumber("12345");
		
		List<MyCareHubVitalSign> vitalSigns = new ArrayList<MyCareHubVitalSign>();
		vitalSigns.add(new MyCareHubVitalSign() {
			
			{
				setConcept(TEMPERATURE_CONCEPT_KEY);
				setObsDatetime(new Date());
				setValue("39.1");
			}
		});
		vitalSigns.add(new MyCareHubVitalSign() {
			
			{
				setConcept(WEIGHT_CONCEPT_KEY);
				setObsDatetime(new Date());
				setValue("80");
			}
		});
		vitalSigns.add(new MyCareHubVitalSign() {
			
			{
				setConcept(HEIGHT_CONCEPT_KEY);
				setObsDatetime(new Date());
				setValue("171");
			}
		});
		vitalSigns.add(new MyCareHubVitalSign() {
			
			{
				setConcept(BMI_CONCEPT_KEY);
				setObsDatetime(new Date());
				setValue("25.2");
			}
		});
		
		vitalSigns.add(new MyCareHubVitalSign() {
			
			{
				setConcept(SPO2_CONCEPT_KEY);
				setObsDatetime(new Date());
				setValue("96");
			}
		});
		vitalSigns.add(new MyCareHubVitalSign() {
			
			{
				setConcept(PULSE_CONCEPT_KEY);
				setObsDatetime(new Date());
				setValue("80");
			}
		});
		vitalSigns.add(new MyCareHubVitalSign() {
			
			{
				setConcept(CD4_CONCEPT_KEY);
				setObsDatetime(new Date());
				setValue("500.0");
			}
		});
		vitalSigns.add(new MyCareHubVitalSign() {
			
			{
				setConcept(VIRAL_LOAD_CONCEPT_KEY);
				setObsDatetime(new Date());
				setValue("1000");
			}
		});
		vitalSigns.add(new MyCareHubVitalSign() {
			
			{
				setConcept(RESPIRATORY_RATE_CONCEPT_KEY);
				setObsDatetime(new Date());
				setValue("26");
			}
		});
		medicalRecord.setVitalSigns(vitalSigns);
		
		List<MyCareHubTestOrder> myCareHubTestOrders = new ArrayList<MyCareHubTestOrder>();
		myCareHubTestOrders.add(new MyCareHubTestOrder() {
			
			{
				setOrderDateTime(new Date());
				setOrderedTestName("Complete Blood Count");
			}
		});
		medicalRecord.setTestOrders(myCareHubTestOrders);
		
		List<MyCareHubTest> myCareHubTests = new ArrayList<MyCareHubTest>();
		myCareHubTests.add(new MyCareHubTest() {
			
			{
				setTestName("HEMOGLOBIN");
				setTestDateTime(new Date());
				setResult("15.1");
			}
		});
		medicalRecord.setTests(myCareHubTests);
		
		List<MyCareHubMedication> myCareHubMedications = new ArrayList<MyCareHubMedication>();
		myCareHubMedications.add(new MyCareHubMedication() {
			
			{
				setMedicationName("Current medication");
				setMedicationDateTime(new Date());
				setValue("DIDANOSINE");
			}
		});
		medicalRecord.setMedications(myCareHubMedications);
		
		List<MyCareHubAllergy> allergies = new ArrayList<MyCareHubAllergy>();
		allergies.add(new MyCareHubAllergy() {
			
			{
				setAllergyName("Caffeine");
				setReaction("Arrhythmia");
				setOtherReaction("Free text description of other reaction");
				setSeverity("Severe");
				setAllergyDateTime(new Date());
			}
		});
		medicalRecord.setAllergies(allergies);
		
		List<MedicalRecord> medicalRecords = new ArrayList<MedicalRecord>();
		medicalRecords.add(medicalRecord);
		
		MedicalRecordsRequest medicalRecordsRequest = new MedicalRecordsRequest();
		medicalRecordsRequest.setFacility(MyCareHubUtil.getDefaultLocationMflCode());
		medicalRecordsRequest.setMedicalRecords(medicalRecords);
		
		Date newSyncTime = new Date();
		MyCareHubUtil.uploadPatientMedicalRecords(medicalRecordsRequest, newSyncTime);
		
		verify(myCareHubSettingsService, times(1)).createMyCareHubSetting(KENYAEMR_MEDICAL_RECORDS, newSyncTime);
		
	}
	
}
