package org.openmrs.module.mycarehub.api.rest;

import com.google.gson.JsonObject;
import org.openmrs.module.mycarehub.api.rest.mapper.AppointmentResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.LoginRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.LoginResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.MedicalRecordRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.MedicalRecordResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.NewClientsIdentifiersRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.NewClientsIdentifiersResponse;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistrationRequest;
import org.openmrs.module.mycarehub.api.rest.mapper.PatientRegistrationResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RestApiService {
	
	@Headers({ "Accept: application/json", "Content-Type: application/json" })
	@POST("login")
	Call<LoginResponse> login(@Body LoginRequest request);
	
	@Headers({ "Accept: application/json", "Content-Type: application/json" })
	@POST("client_registration")
	Call<PatientRegistrationResponse> uploadPatientRegistration(@Body PatientRegistrationRequest request);
	
	@Headers({ "Accept: application/json", "Content-Type: application/json" })
	@GET("clients")
	Call<NewClientsIdentifiersResponse> getNewClientsIdentifiers(@Body NewClientsIdentifiersRequest request);
	
	@Headers({ "Accept: application/json", "Content-Type: application/json" })
	@POST("appointment")
	Call<AppointmentResponse> uploadPatientAppointments(@Body JsonObject request);

	@Headers({ "Accept: application/json", "Content-Type: application/json" })
	@POST("obs")
	Call<MedicalRecordResponse> uploadMedicalRecord(@Body MedicalRecordRequest request);
}
