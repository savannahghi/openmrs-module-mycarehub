package org.openmrs.module.mycarehub.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.PatientIdentifierType;
import org.openmrs.User;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycarehub.api.rest.ApiClient;
import org.openmrs.module.mycarehub.api.rest.RestApiService;
import org.openmrs.module.mycarehub.api.rest.mapper.AppointmentResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.LoginRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.LoginResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistrationRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistrationResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.RedFlagResponse;
import org.openmrs.module.mycarehub.api.service.AppointmentService;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.api.service.RedFlagService;
import org.openmrs.module.mycarehub.model.AppointmentRequests;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;
import org.openmrs.module.mycarehub.model.RedFlags;
import retrofit2.Call;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.openmrs.module.mycarehub.utils.Constants.CCC_NUMBER_IDENTIFIER_TYPE_UUID;
import static org.openmrs.module.mycarehub.utils.Constants.EMPTY;
import static org.openmrs.module.mycarehub.utils.Constants.GP_DEFAULT_LOCATION_MFL_CODE;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_DEFAULT_PASSWORD;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_DEFAULT_USERNAME;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_PASSWORD;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_TOKEN;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_URL;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_USERNAME;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_APPOINTMENTS;

public class MyCareHubUtil {
	
	private static final String TAG = MyCareHubUtil.class.getSimpleName();
	
	private static final Log log = LogFactory.getLog(MyCareHubUtil.class);
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
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
	
	public static String getApiToken() {
		AdministrationService as = Context.getAdministrationService();
		return as.getGlobalProperty(GP_MYCAREHUB_API_TOKEN, EMPTY);
	}
	
	public static void uploadPatientRegistrationRecord(PatientRegistrationRequest patientRegistrationRequest) {
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null) {
			log.error(TAG, new Throwable("Cant create REST API service"));
			return;
		}
		
		Call<PatientRegistrationResponse> call = restApiService.uploadPatientRegistration(patientRegistrationRequest);
		
		try {
			Response<PatientRegistrationResponse> response = call.execute();
			if (!response.isSuccessful()) {
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
	
	public static void authenticateMyCareHub() {
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null) {
			log.error(TAG, new Throwable("Cant create REST API service"));
			return;
		}
		
		Call<LoginResponse> call = restApiService.login(new LoginRequest(getApiUsername(), getApiPassword()));
		
		try {
			Response<LoginResponse> response = call.execute();
			if (response.isSuccessful()) {
				log.error("Success subscription");
				LoginResponse loginResponse = response.body();
				if (loginResponse != null) {
					Gson gson = new Gson();
					log.error(gson.toJson(loginResponse));
					
					AdministrationService as = Context.getAdministrationService();
					//TODO: @Savai and @Mokaya I feel we should save this in settings
					as.saveGlobalProperty(new GlobalProperty(GP_MYCAREHUB_API_TOKEN, loginResponse.getData().getToken()));
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
			log.error("Error authenticating: " + throwable.getMessage());
		}
	}
	
	public static String getDefaultLocationMflCode() {
		AdministrationService as = Context.getAdministrationService();
		return as.getGlobalProperty(GP_DEFAULT_LOCATION_MFL_CODE, EMPTY);
	}
	
	public static void uploadPatientAppointments(JsonObject appointmentRequests, MyCareHubSetting setting) {
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null) {
			log.error(TAG, new Throwable("Cant create REST API service"));
			return;
		}
		
		Call<AppointmentResponse> call = restApiService.uploadPatientAppointments(appointmentRequests);
		
		try {
			Response<AppointmentResponse> response = call.execute();
			if (response.isSuccessful()) {
				MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
				settingsService.saveMyCareHubSettings(setting);
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
	
	public static void uploadPatientAppointmentRequests(JsonObject appointmentRequests, MyCareHubSetting setting) {
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null) {
			log.error(TAG, new Throwable("Cant create REST API service"));
			return;
		}
		
		Call<AppointmentResponse> call = restApiService.uploadPatientAppointments(appointmentRequests);
		
		try {
			Response<AppointmentResponse> response = call.execute();
			if (response.isSuccessful()) {
				MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
				settingsService.saveMyCareHubSettings(setting);
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
	
	public static void fetchPatientAppointments(JsonObject jsonObject, MyCareHubSetting setting) {
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null) {
			log.error(TAG, new Throwable("Cant create REST API service"));
			return;
		}
		
		Call<JsonObject> call = restApiService.fetchPatientAppointmentRequests(jsonObject);
		
		try {
			Response<JsonObject> response = call.execute();
			if (response.isSuccessful()) {
				AppointmentService appointmentService = Context.getService(AppointmentService.class);
				MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
				settingsService.saveMyCareHubSettings(setting);
				JsonObject jsonResponse = response.body().getAsJsonObject();
				JsonArray jsonArray = jsonResponse.getAsJsonArray("appointments");
				List<AppointmentRequests> appointmentRequests = new ArrayList<AppointmentRequests>();
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
					AppointmentRequests appointmentRequest = new AppointmentRequests();
					String mycarehubId = jsonObject1.get("ID").toString();
					AppointmentRequests existingRequests = appointmentService
					        .getAppointmentRequestByMycarehubId(mycarehubId);
					if (existingRequests != null && existingRequests.getMycarehubId() != null) {
						appointmentRequest = existingRequests;
					} else {
						appointmentRequest.setCreator(new User(1));
						appointmentRequest.setDateCreated(new Date());
						appointmentRequest.setUuid(UUID.randomUUID().toString());
						appointmentRequest.setVoided(false);
					}
					
					appointmentRequest.setAppointmentUUID(jsonObject1.get("appointmentUuid").toString());
					appointmentRequest.setMycarehubId(jsonObject1.get("ID").toString());
					appointmentRequest.setAppointmentType(jsonObject1.get("AppointmentType").toString());
					appointmentRequest.setAppointmentReason(jsonObject1.get("AppointmentReason").toString());
					appointmentRequest.setProvider(jsonObject1.get("AppointmentReason").toString());
					appointmentRequest.setAppointmentReason(jsonObject1.get("Provider").toString());
					appointmentRequest.setRequestedTimeSlot(jsonObject1.get("SuggestedTime").toString());
					appointmentRequest.setRequestedDate(dateFormat.parse(jsonObject1.get("SuggestedDate").getAsString()));
					appointmentRequest.setStatus(jsonObject1.get("Status").toString());
					if (jsonObject1.get("InProgressAt").toString() != null) {
						appointmentRequest.setProgressDate(dateFormat.parse(jsonObject1.get("InProgressAt").toString()));
					}
					appointmentRequest.setProgressBy(jsonObject1.get("InProgressBy").toString());
					if (jsonObject1.get("ResolvedAt").toString() != null) {
						appointmentRequest.setProgressDate(dateFormat.parse(jsonObject1.get("ResolvedAt").toString()));
					}
					
					appointmentRequest.setResolvedBy(jsonObject1.get("ResolvedBy").toString());
					appointmentRequest.setClientName(jsonObject1.get("ClientName").toString());
					appointmentRequest.setClientContact(jsonObject1.get("ClientContact").toString());
					appointmentRequest.setCccNumber(jsonObject1.get("CCCNumber").toString());
					appointmentRequest.setMflCode(jsonObject1.get("MFLCODE").toString());
					
					appointmentRequests.add(appointmentRequest);
					
				}
				appointmentService.saveAppointmentRequests(appointmentRequests);
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
	
	public static void postPatientRedFlags(JsonObject redflags, MyCareHubSetting setting) {
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null) {
			log.error(TAG, new Throwable("Cant create REST API service"));
			return;
		}
		
		Call<RedFlagResponse> call = restApiService.postPatientRedFlags(redflags);
		
		try {
			Response<RedFlagResponse> response = call.execute();
			if (response.isSuccessful()) {
				MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
				settingsService.saveMyCareHubSettings(setting);
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
	
	public static void fetchPatientRedFlagRequests(JsonObject jsonObject, MyCareHubSetting setting) {
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null) {
			log.error(TAG, new Throwable("Cant create REST API service"));
			return;
		}
		
		Call<JsonObject> call = restApiService.fetchPatientRedFlags(jsonObject);
		
		try {
			Response<JsonObject> response = call.execute();
			if (response.isSuccessful()) {
				RedFlagService redFlagService = Context.getService(RedFlagService.class);
				MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
				settingsService.saveMyCareHubSettings(setting);
				JsonObject jsonResponse = response.body().getAsJsonObject();
				JsonArray jsonArray = jsonResponse.getAsJsonArray("redFlags");
				List<RedFlags> redFlags = new ArrayList<RedFlags>();
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
					RedFlags redFlag = new RedFlags();
					String mycarehubId = jsonObject1.get("ID").toString();
					RedFlags existingRequests = redFlagService.getRedFlagRequestByMycarehubId(mycarehubId);
					if (existingRequests != null && existingRequests.getMycarehubId() != null) {
						redFlag = existingRequests;
					} else {
						redFlag.setCreator(new User(1));
						redFlag.setDateCreated(new Date());
						redFlag.setUuid(UUID.randomUUID().toString());
						redFlag.setVoided(false);
					}
					
					redFlag.setMycarehubId(jsonObject1.get("ID").toString());
					redFlag.setRequest(jsonObject1.get("Request").toString());
					redFlag.setRequestType(jsonObject1.get("RequestType").toString());
					redFlag.setScreeningTool(jsonObject1.get("ScreeningToolName").toString());
					redFlag.setScreeningScore(jsonObject1.get("ScreeningToolScore").toString());
					if (jsonObject1.get("InProgressAt").toString() != null) {
						redFlag.setProgressDate(dateFormat.parse(jsonObject1.get("InProgressAt").toString()));
					}
					redFlag.setProgressBy(jsonObject1.get("InProgressBy").toString());
					if (jsonObject1.get("ResolvedAt").toString() != null) {
						redFlag.setProgressDate(dateFormat.parse(jsonObject1.get("ResolvedAt").toString()));
					}
					
					redFlag.setResolvedBy(jsonObject1.get("ResolvedBy").toString());
					redFlag.setClientName(jsonObject1.get("ClientName").toString());
					redFlag.setClientContact(jsonObject1.get("ClientContact").toString());
					redFlag.setCccNumber(jsonObject1.get("CCCNumber").toString());
					redFlag.setMflCode(jsonObject1.get("MFLCODE").toString());
					
					redFlags.add(redFlag);
					
				}
				redFlagService.saveRedFlagRequests(redFlags);
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
	
	public static PatientIdentifierType getcccPatientIdentifierType() {
		PatientIdentifierType cccIdentifierType = Context.getPatientService().getPatientIdentifierTypeByUuid(
		    CCC_NUMBER_IDENTIFIER_TYPE_UUID);
		return cccIdentifierType;
	}
	
}
