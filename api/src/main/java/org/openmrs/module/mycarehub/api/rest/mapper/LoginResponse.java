package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
	
	@SerializedName("id_token")
	private String accessToken;
	
	@SerializedName("expires_in")
	private Long expiryTime;
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public Long getExpiryTime() {
		return expiryTime;
	}
	
	public void setExpiryTime(Long expiryTime) {
		this.expiryTime = expiryTime;
	}
}
