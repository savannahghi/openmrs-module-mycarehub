package org.openmrs.module.mycarehub.api.rest;

import com.google.gson.JsonObject;
import okhttp3.ResponseBody;
import org.openmrs.module.mycarehub.api.rest.mapper.AppointmentResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.LoginRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.LoginResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.MedicalRecordResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.MedicalRecordsRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.NewClientsIdentifiersRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.NewClientsIdentifiersResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistrationRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistrationResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.RedFlagResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RestApiService {
	
	@Headers({ "Accept: application/json", "Content-Type: application/json" })
	@POST("login")
	Call<LoginResponse> login(@Body LoginRequest request);
	
	@Headers({ "Accept: application/json", "Content-Type: application/json" })
	@POST("kenya-emr/client_registration")
	Call<PatientRegistrationResponse> uploadPatientRegistrations(@Header("Authorization") String authorization,
	        @Body PatientRegistrationRequest request);
	
	@Headers({ "Accept: application/json", "Content-Type: application/json" })
	@GET("kenya-emr/patients")
	Call<NewClientsIdentifiersResponse> getNewClientsIdentifiers(@Header("Authorization") String authorization,
	        @Body NewClientsIdentifiersRequest request);
	
	@Headers({ "Accept: application/json", "Content-Type: application/json" })
	@GET
	Call<NewClientsIdentifiersResponse> getNewClientsIdentifiers(@Header("Authorization") String authorization,
	        @Url String url);
	
	@Headers({ "Accept: application/json", "Content-Type: application/json" })
	@POST("kenya-emr/appointment")
	Call<AppointmentResponse> uploadPatientAppointments(@Header("Authorization") String authorization,
	        @Body JsonObject request);
	
	@Headers({ "Accept: application/json", "Content-Type: application/json" })
	@POST("kenya-emr/appointment-service-request")
	Call<AppointmentResponse> uploadPatientAppointmentRequests(@Header("Authorization") String authorization,
	        @Body JsonObject request);
	
	@Headers({ "Accept: application/json", "Content-Type: application/json" })
	@GET
	Call<ResponseBody> fetchPatientAppointmentRequests(@Header("Authorization") String authorization, @Url String url);
	
	@Headers({ "Accept: application/json", "Content-Type: application/json" })
	@POST("kenya-emr/red-flag-service-request")
	Call<RedFlagResponse> postPatientRedFlags(@Header("Authorization") String authorization, @Body JsonObject request);
	
	@Headers({ "Accept: application/json", "Content-Type: application/json" })
	@GET
	Call<ResponseBody> fetchPatientRedFlags(@Header("Authorization") String authorization, @Url String url);
	
	@POST("kenya-emr/obs")
	Call<MedicalRecordResponse> uploadMedicalRecords(@Header("Authorization") String authorization,
	        @Body MedicalRecordsRequest request);
	
	@Headers({ "Accept: application/json", "Content-Type: application/json" })
	@GET
	Call<ResponseBody> fetchPatientHealthDiaries(@Header("Authorization") String authorization, @Url String url);
}
