package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

public class AppointmentResponse {
	
	@SerializedName("success")
	private Boolean success;
	
	public AppointmentResponse(Boolean success) {
		this.success = success;
		
	}
}
