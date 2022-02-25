package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
	
	@SerializedName("success")
	private Boolean success;
	
	@SerializedName("data")
	private AuthData data;
	
	@SerializedName("message")
	private String message;
	
	public LoginResponse(Boolean success, AuthData data, String message) {
		this.success = success;
		this.data = data;
		this.message = message;
	}
	
	public Boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	public AuthData getData() {
		return data;
	}
	
	public void setData(AuthData data) {
		this.data = data;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public static class AuthData {
		
		@SerializedName("token")
		private String token;
		
		@SerializedName("name")
		private String name;
		
		public AuthData(String token, String name) {
			this.token = token;
			this.name = name;
		}
		
		public String getToken() {
			return token;
		}
		
		public void setToken(String token) {
			this.token = token;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
	}
}
