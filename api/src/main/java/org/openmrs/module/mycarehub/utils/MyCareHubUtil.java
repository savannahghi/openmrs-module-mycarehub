package org.openmrs.module.mycarehub.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import okhttp3.ResponseBody;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycarehub.api.rest.ApiClient;
import org.openmrs.module.mycarehub.api.rest.RestApiService;
import org.openmrs.module.mycarehub.api.rest.mapper.ApiError;
import org.openmrs.module.mycarehub.api.rest.mapper.LoginRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.LoginResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.MedicalRecordResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.MedicalRecordsRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.NewClientsIdentifiersRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.NewClientsIdentifiersResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistrationRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistrationResponse;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.exception.AuthenticationException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.mycarehub.utils.Constants.APPOINTMENT_DATE_CONCEPT_ID;
import static org.openmrs.module.mycarehub.utils.Constants.CCC_NUMBER_IDENTIFIER_TYPE_UUID;
import static org.openmrs.module.mycarehub.utils.Constants.EMPTY;
import static org.openmrs.module.mycarehub.utils.Constants.GP_DEFAULT_LOCATION_MFL_CODE;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_DEFAULT_PASSWORD;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_DEFAULT_USERNAME;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_PASSWORD;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_TOKEN;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_TOKEN_EXPIRY_TIME;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_URL;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_USERNAME;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.Medications.REGIMEN;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.Tests.HIV_POLYMERASE;
import static org.openmrs.module.mycarehub.utils.Constants.MedicalRecordConcepts.Tests.WIDAL;
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
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.NEXT_OF_KIN_CONTACT;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.NEXT_OF_KIN_NAME;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.NEXT_OF_KIN_RELATIONSHIP;
import static org.openmrs.module.mycarehub.utils.Constants._PersonAttributeType.TELEPHONE_CONTACT;

public class MyCareHubUtil {
	
	private static final String TAG = MyCareHubUtil.class.getSimpleName();
	
	private static final Log log = LogFactory.getLog(MyCareHubUtil.class);
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String syncTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	
	private static final SimpleDateFormat syncTimeFormat = new SimpleDateFormat(syncTimePattern);
	
	public static String getApiUrl() {
		AdministrationService as = Context.getAdministrationService();
		return as.getGlobalProperty(GP_MYCAREHUB_API_URL, EMPTY);
	}
	
	public static String getApiUsername() {
		AdministrationService as = Context.getAdministrationService();
		return as.getGlobalProperty(GP_MYCAREHUB_API_USERNAME, GP_MYCAREHUB_API_DEFAULT_USERNAME);
	}
	
	public static String getApiPassword() {
		AdministrationService as = Context.getAdministrationService();
		return as.getGlobalProperty(GP_MYCAREHUB_API_PASSWORD, GP_MYCAREHUB_API_DEFAULT_PASSWORD);
	}
	
	public static String getApiToken() throws AuthenticationException {
		AdministrationService as = Context.getAdministrationService();
		String expiryTime = as.getGlobalProperty(GP_MYCAREHUB_API_TOKEN_EXPIRY_TIME, EMPTY);
		String token = as.getGlobalProperty(GP_MYCAREHUB_API_TOKEN, EMPTY);
		// Check if we have an expiry time.
		if (expiryTime != null && !expiryTime.isEmpty() && token != null && !token.isEmpty()) {
			//check if its past now or within 5 secs of expiring due to latency in making calls
			try {
				Date dtExpiryTime = dateTimeFormat.parse(expiryTime);
				Calendar calNow = Calendar.getInstance();
				Calendar calExpiryTime = Calendar.getInstance();
				calExpiryTime.setTime(dtExpiryTime);
				calExpiryTime.add(Calendar.SECOND, -5);
				if (calNow.before(calExpiryTime))
					return token;
			}
			catch (ParseException e) {
				log.error(e.getMessage());
			}
		}
		// if we are here we MUST get a new token and on failure return EMPTY
		try {
			authenticateMyCareHub();
		}
		catch (AuthenticationException e) {
			as.saveGlobalProperty(new GlobalProperty(GP_MYCAREHUB_API_TOKEN, EMPTY));
			as.saveGlobalProperty(new GlobalProperty(GP_MYCAREHUB_API_TOKEN_EXPIRY_TIME, EMPTY));
			throw e;
		}
		token = as.getGlobalProperty(GP_MYCAREHUB_API_TOKEN, EMPTY);
		return token;
	}
	
	public static LoginResponse authenticateMyCareHub() throws AuthenticationException {
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null)
			throw new AuthenticationException("Cant create myCareHub REST API service");
		
		Call<LoginResponse> call = restApiService.login(new LoginRequest(EMPTY, getApiUsername(), getApiPassword()));
		LoginResponse loginResponse = null;
		try {
			Response<LoginResponse> response = call.execute();
			if (response.isSuccessful()) {
				log.info("Successful authentication");
				loginResponse = response.body();
				if (loginResponse != null) {
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.SECOND, loginResponse.getExpiryTime().intValue());
					Date dt = cal.getTime();
					
					AdministrationService as = Context.getAdministrationService();
					as.saveGlobalProperty(new GlobalProperty(GP_MYCAREHUB_API_TOKEN, loginResponse.getAccessToken()));
					as.saveGlobalProperty(new GlobalProperty(GP_MYCAREHUB_API_TOKEN_EXPIRY_TIME, dateTimeFormat.format(dt)));
				} else
					throw new AuthenticationException("Received a null response despite successfully authenticating");
			} else {
				if (response.errorBody() != null) {
					ApiError apiError = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
					throw new AuthenticationException(apiError.getMessage());
				} else
					throw new AuthenticationException(response.message());
			}
		}
		catch (Throwable throwable) {
			throw new AuthenticationException(throwable);
		}
		return loginResponse;
	}
	
	public static String getDefaultLocationMflCode() {
		AdministrationService as = Context.getAdministrationService();
		return as.getGlobalProperty(GP_DEFAULT_LOCATION_MFL_CODE, EMPTY);
	}
	
	public static void uploadPatientRegistrationRecords(PatientRegistrationRequest patientRegistrationRequest,
	        Date newSyncTime) {
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null) {
			log.error(TAG, new Throwable("Cant create REST API service"));
			return;
		}
		
		try {
			Call<ResponseBody> call = restApiService.uploadPatientRegistrations(getBearer(getApiToken()),
			    patientRegistrationRequest);
			Response<ResponseBody> response = call.execute();
			if (response.isSuccessful()) {
				Context.getService(MyCareHubSettingsService.class).createMyCareHubSetting(KENYAEMR_PATIENT_REGISTRATIONS,
				    newSyncTime);
			} else {
				try {
					if (response.errorBody() != null) {
						log.error(response.errorBody().charStream());
					} else {
						log.error(response.message());
					}
				}
				catch (NullPointerException e) {
					log.error(response.message());
				}
				catch (JsonParseException e) {
					log.error(response.message());
				}
			}
		}
		catch (Throwable throwable) {
			log.error("Error uploading patient registration record: " + throwable.getMessage());
		}
	}
	
	public static List<String> getNewMyCareHubClientCccIdentifiers(Date lastSycTime, Date newSyncTime) {
		List<String> cccList = new ArrayList<String>();
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null) {
			log.error(TAG, new Throwable("Cant create REST API service"));
			return cccList;
		}
		
		try {
			String url = getApiUrl() + "kenya-emr/patients?MFLCODE=" + getDefaultLocationMflCode() + "&lastSyncTime="
			        + URLEncoder.encode(syncTimeFormat.format(lastSycTime), "UTF-8");
			String accessToken = getApiToken();
			Call<NewClientsIdentifiersResponse> call = restApiService.getNewClientsIdentifiers(getBearer(accessToken), url);
			
			Response<NewClientsIdentifiersResponse> response = call.execute();
			
			if (response.isSuccessful()) {
				Context.getService(MyCareHubSettingsService.class).createMyCareHubSetting(MYCAREHUB_CLIENT_REGISTRATIONS,
				    newSyncTime);
				
				cccList = response.body().getPatientsIdentifiers();
			} else {
				try {
					if (response.errorBody() != null) {
						log.error(response.errorBody().charStream());
					} else
						log.error(response.message());
				}
				catch (NullPointerException e) {
					log.error(response.message());
				}
				catch (JsonParseException e) {
					log.error(response.message());
				}
			}
		}
		catch (Throwable throwable) {
			log.error("Error uploading patient registration record: ", throwable);
		}
		return cccList;
	}
	
	public static void uploadPatientAppointments(JsonObject appointmentRequests, Date newSyncTime) {
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null) {
			log.error(TAG, new Throwable("Cant create REST API service"));
			return;
		}
		
		try {
			Call<ResponseBody> call = restApiService
			        .uploadPatientAppointments(getBearer(getApiToken()), appointmentRequests);
			Response<ResponseBody> response = call.execute();
			if (response.isSuccessful()) {
				MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
				settingsService.createMyCareHubSetting(PATIENT_APPOINTMENTS_REQUESTS_POST, newSyncTime);
			} else {
				try {
					if (response.errorBody() != null) {
						log.error(response.errorBody().charStream());
					} else
						log.error(response.message());
				}
				catch (NullPointerException e) {
					log.error(response.message());
				}
				catch (JsonParseException e) {
					log.error(response.message());
				}
			}
		}
		catch (Throwable throwable) {
			log.error("Error uploading patient registration record: " + throwable.getMessage());
		}
	}
	
	public static void uploadPatientAppointmentRequests(JsonObject appointmentRequests, Date newSyncTime) {
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null) {
			log.error(TAG, new Throwable("Cant create REST API service"));
			return;
		}
		
		try {
			Call<ResponseBody> call = restApiService.uploadPatientAppointmentRequests(getBearer(getApiToken()),
			    appointmentRequests);
			Response<ResponseBody> response = call.execute();
			if (response.isSuccessful()) {
				
				MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
				settingsService.createMyCareHubSetting(PATIENT_APPOINTMENTS_REQUESTS_POST, newSyncTime);
			} else {
				try {
					if (response.errorBody() != null) {
						log.error(response.errorBody().charStream());
					} else
						log.error(response.message());
				}
				catch (NullPointerException e) {
					log.error(response.message());
				}
				catch (JsonParseException e) {
					log.error(response.message());
				}
			}
		}
		catch (Throwable throwable) {
			log.error("Error uploading patient registration record: " + throwable.getMessage());
		}
	}
	
	public static JsonArray fetchPatientAppointmentRequests(Date lastSyncTime, Date newSyncTime) {
		JsonArray jsonArray = new JsonArray();
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null) {
			log.error(TAG, new Throwable("Cant create REST API service"));
			return jsonArray;
		}
		
		try {
			String url = getApiUrl() + "kenya-emr/appointment-service-request?MFLCODE=" + getDefaultLocationMflCode()
			        + "&lastSyncTime=" + URLEncoder.encode(syncTimeFormat.format(lastSyncTime), "UTF-8");
			
			Call<ResponseBody> call = restApiService.fetchPatientAppointmentRequests(getBearer(getApiToken()), url);
			Response<ResponseBody> response = call.execute();
			
			if (response.isSuccessful()) {
				MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
				settingsService.createMyCareHubSetting(PATIENT_APPOINTMENTS_REQUESTS_GET, newSyncTime);
				
				if (response.body() != null) {
					JsonObject jsonResponse = new JsonParser().parse(response.body().string()).getAsJsonObject();
					jsonArray = jsonResponse.getAsJsonArray("Results");
				}
			} else {
				try {
					if (response.errorBody() != null) {
						log.error(response.errorBody().charStream());
					} else
						log.error(response.message());
				}
				catch (NullPointerException e) {
					log.error(response.message());
				}
				catch (JsonParseException e) {
					log.error(response.message());
				}
			}
		}
		catch (Throwable throwable) {
			log.error("Error uploading patient registration record: " + throwable.getMessage());
		}
		return jsonArray;
	}
	
	public static void postPatientRedFlags(JsonObject redflags, Date newSyncTime) {
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null) {
			log.error(TAG, new Throwable("Cant create REST API service"));
			return;
		}
		
		try {
			Call<ResponseBody> call = restApiService.postPatientRedFlags(getBearer(getApiToken()), redflags);
			Response<ResponseBody> response = call.execute();
			if (response.isSuccessful()) {
				
				MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
				settingsService.createMyCareHubSetting(PATIENT_RED_FLAGS_REQUESTS_POST, newSyncTime);
			} else {
				try {
					if (response.errorBody() != null) {
						log.error(response.errorBody().charStream());
					} else
						log.error(response.message());
				}
				catch (NullPointerException e) {
					log.error(response.message());
				}
				catch (JsonParseException e) {
					log.error(response.message());
				}
			}
		}
		catch (Throwable throwable) {
			log.error("Error uploading patient registration record: " + throwable.getMessage());
		}
	}
	
	public static JsonArray getPatientRedFlagRequests(Date lastSyncTime, Date newSyncTime) {
		JsonArray jsonArray = new JsonArray();
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null) {
			log.error(TAG, new Throwable("Cant create REST API service"));
			return jsonArray;
		}
		
		try {
			String url = getApiUrl() + "kenya-emr/service_request?MFLCODE=" + getDefaultLocationMflCode() + "&lastSyncTime="
			        + URLEncoder.encode(syncTimeFormat.format(lastSyncTime), "UTF-8");
			
			Call<ResponseBody> call = restApiService.fetchPatientRedFlags(getBearer(getApiToken()), url);
			Response<ResponseBody> response = call.execute();
			if (response.isSuccessful()) {
				MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
				settingsService.createMyCareHubSetting(PATIENT_RED_FLAGS_REQUESTS_GET, newSyncTime);
				
				if (response.body() != null) {
					JsonObject jsonResponse = new JsonParser().parse(response.body().string()).getAsJsonObject();
					jsonArray = jsonResponse.getAsJsonArray("redFlags");
				}
			} else {
				try {
					if (response.errorBody() != null) {
						log.error(response.errorBody().charStream());
					} else
						log.error(response.message());
				}
				catch (NullPointerException e) {
					log.error(response.message());
				}
				catch (JsonParseException e) {
					log.error(response.message());
				}
			}
		}
		catch (Throwable throwable) {
			log.error("Error getting red flags: " + throwable.getMessage());
		}
		return jsonArray;
	}
	
	public static void uploadPatientMedicalRecords(MedicalRecordsRequest request, Date newSyncTime) {
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null) {
			log.error(TAG, new Throwable("Cant create REST API service"));
			return;
		}
		
		try {
			Call<MedicalRecordResponse> call = restApiService.uploadMedicalRecords(getBearer(getApiToken()), request);
			Response<MedicalRecordResponse> response = call.execute();
			if (response.isSuccessful()) {
				Context.getService(MyCareHubSettingsService.class).createMyCareHubSetting(KENYAEMR_MEDICAL_RECORDS,
				    newSyncTime);
			} else {
				try {
					if (response.errorBody() != null) {
						log.error(response.errorBody().charStream());
					} else
						log.error(response.message());
				}
				catch (NullPointerException e) {
					log.error(response.message());
				}
				catch (JsonParseException e) {
					log.error(response.message());
				}
			}
		}
		catch (Throwable throwable) {
			log.error("Error uploading medical record: " + throwable.getMessage());
		}
	}
	
	public static JsonArray getPatientHealthDiaries(Date lastSyncTime, Date newSyncTime) {
		JsonArray jsonArray = new JsonArray();
		
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null) {
			log.error(TAG, new Throwable("Cant create REST API service"));
			return jsonArray;
		}
		
		try {
			String url = getApiUrl() + "kenya-emr/health_diary?MFLCODE=" + getDefaultLocationMflCode() + "&lastSyncTime="
			        + URLEncoder.encode(syncTimeFormat.format(lastSyncTime), "UTF-8");
			
			Call<ResponseBody> call = restApiService.fetchPatientHealthDiaries(getBearer(getApiToken()), url);
			
			Response<ResponseBody> response = call.execute();
			if (response.isSuccessful()) {
				MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
				settingsService.createMyCareHubSetting(PATIENT_HEALTH_DIARY_GET, newSyncTime);
				
				if (response.body() != null) {
					JsonObject jsonResponse = new JsonParser().parse(response.body().string()).getAsJsonObject();
					jsonArray = jsonResponse.getAsJsonArray("healthDiaries");
				}
				
			} else {
				try {
					if (response.errorBody() != null) {
						log.error(response.errorBody().charStream());
					} else {
						log.error(response.message());
					}
				}
				catch (NullPointerException e) {
					log.error(response.message());
				}
				catch (JsonParseException e) {
					log.error(response.message());
				}
			}
		}
		catch (Throwable throwable) {
			log.error("Error uploading patient registration record: " + throwable.getMessage());
		}
		return jsonArray;
	}
	
	public static PatientIdentifierType getcccPatientIdentifierType() {
		PatientIdentifierType cccIdentifierType = Context.getPatientService().getPatientIdentifierTypeByUuid(
		    CCC_NUMBER_IDENTIFIER_TYPE_UUID);
		return cccIdentifierType;
	}
	
	public static String getBearer(String token) {
		return "Bearer ".concat(token);
	}
}
