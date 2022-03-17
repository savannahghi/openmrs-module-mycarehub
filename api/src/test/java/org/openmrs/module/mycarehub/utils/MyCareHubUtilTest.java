package org.openmrs.module.mycarehub.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycarehub.api.rest.RestApiService;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistration;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistrationRequest;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.openmrs.module.mycarehub.utils.Constants.EMPTY;
import static org.openmrs.module.mycarehub.utils.Constants.GP_DEFAULT_LOCATION_MFL_CODE;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_DEFAULT_PASSWORD;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_DEFAULT_USERNAME;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_PASSWORD;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_URL;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_USERNAME;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.KENYAEMR_PATIENT_REGISTRATIONS;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.MYCAREHUB_CLIENT_REGISTRATIONS;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_APPOINTMENTS_REQUESTS_GET;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_APPOINTMENTS_REQUESTS_POST;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_HEALTH_DIARY_GET;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_RED_FLAGS_REQUESTS_GET;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENTS_CONTAINER_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_DATE_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_STATUS_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_TIME_SLOT_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_TYPE_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_UUID_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.GeneralKeys.CCC_NUMBER;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.GeneralKeys.FACILITY_MFL_CODE;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.NextOfKinPatientRegistrationKeys.NEXT_OF_KIN_CONTACTS_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.NextOfKinPatientRegistrationKeys.NEXT_OF_KIN_NAME_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.NextOfKinPatientRegistrationKeys.NEXT_OF_KIN_RELATIONSHIP_KEY;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
@PowerMockIgnore("javax.net.ssl.*")
public class MyCareHubUtilTest {
	
	private final static String testServerUrl = "https://mycarehub-testing.savannahghi.org/kenya-emr/";
	
	private final static String username = "kenya-emr@savannahinformatics.com";
	
	private final static String password = "#kenya-EMR#";
	
	private final static String mflCode = "1234";
	
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
		JsonObject containerObject = new JsonObject();
		JsonArray appointmentsArray = new JsonArray();
		JsonObject appointmentObject = new JsonObject();
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		appointmentObject.addProperty(APPOINTMENT_UUID_KEY, "f67a4ad9-4ac4-4183-9852-1dd75b04aff5");
		
		Date appointmentStartDate = new Date();
		Date appointmentEndDate = new Date(appointmentStartDate.getTime() + 20 * 60 * 1000);
		appointmentObject.addProperty(APPOINTMENT_DATE_KEY, dateFormat.format(appointmentStartDate));
		appointmentObject.addProperty(APPOINTMENT_TIME_SLOT_KEY,
		    timeFormat.format(timeFormat.format(appointmentStartDate) + " " + timeFormat.format(appointmentEndDate)));
		appointmentObject.addProperty(APPOINTMENT_TYPE_KEY, "TypeName");
		appointmentObject.addProperty(APPOINTMENT_STATUS_KEY, "StatusName");
		appointmentObject.addProperty(CCC_NUMBER, "12345");
		appointmentsArray.add(appointmentObject);
		
		containerObject.addProperty(FACILITY_MFL_CODE, MyCareHubUtil.getDefaultLocationMflCode());
		containerObject.add(APPOINTMENTS_CONTAINER_KEY, appointmentsArray);
		
		Date newSyncDate = new Date();
		MyCareHubUtil.uploadPatientAppointments(containerObject, newSyncDate);
		
		verify(myCareHubSettingsService, times(1)).createMyCareHubSetting(PATIENT_APPOINTMENTS_REQUESTS_POST, newSyncDate);
		
	}
	
}
