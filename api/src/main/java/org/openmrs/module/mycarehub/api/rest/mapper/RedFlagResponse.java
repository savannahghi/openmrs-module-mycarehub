package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

public class RedFlagResponse {
	
	@SerializedName("success")
	private Boolean success;
	
	public RedFlagResponse(Boolean success) {
		this.success = success;
		
	}
}
