package org.openmrs.module.mycarehub.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycarehub.api.rest.ApiClient;
import org.openmrs.module.mycarehub.api.rest.RestApiService;
import org.openmrs.module.mycarehub.api.rest.mapper.AppointmentResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.LoginRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.LoginResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.NewClientsIdentifiersRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.NewClientsIdentifiersResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistrationRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistrationResponse;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;
import retrofit2.Call;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.Date;

import static org.openmrs.module.mycarehub.utils.Constants.*;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_APPOINTMENTS;

public class MyCareHubUtil {
	
	private static final String TAG = MyCareHubUtil.class.getSimpleName();
	
	private static final Log log = LogFactory.getLog(MyCareHubUtil.class);
	
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

	public static String getApiTokenExpiryTime() {
		AdministrationService as = Context.getAdministrationService();
		return as.getGlobalProperty(GP_MYCAREHUB_API_TOKEN_EXPIRY_TIME, EMPTY);
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
	
	public static List<String> getNewMyCareHubClientCccIdentifiers(Date lastSynyTime) {
		List<String> cccList = new ArrayList<String>();
		RestApiService restApiService = ApiClient.getRestService();
		if (restApiService == null) {
			log.error(TAG, new Throwable("Cant create REST API service"));
			return cccList;
		}
		
		String facility = getDefaultLocationMflCode();
		String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		Call<NewClientsIdentifiersResponse> call = restApiService.getNewClientsIdentifiers(new NewClientsIdentifiersRequest(
		        facility, sf.format(lastSynyTime)));
		
		try {
			Response<NewClientsIdentifiersResponse> response = call.execute();
			if (response.isSuccessful()) {
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
			log.error("Error uploading patient registration record: " + throwable.getMessage());
		}
		return cccList;
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
			if (!response.isSuccessful()) {
				MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
				settingsService.saveMyCareHubSettings(setting);
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
