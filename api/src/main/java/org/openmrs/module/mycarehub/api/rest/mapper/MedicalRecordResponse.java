package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

public class MedicalRecordResponse {
	
	@SerializedName("success")
	private Boolean success;
	
	public MedicalRecordResponse(Boolean success) {
		this.success = success;
	}
}
