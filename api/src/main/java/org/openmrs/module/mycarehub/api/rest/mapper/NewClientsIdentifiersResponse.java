package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewClientsIdentifiersResponse {
	
	@SerializedName("success")
	private Boolean success;
	
	@SerializedName("facility")
	private String facility;
	
	@SerializedName("patients")
	private List<String> patientsIdentifiers;
	
	public NewClientsIdentifiersResponse(Boolean success, String facility, List<String> patientsIdentifiers) {
		this.success = success;
		this.facility = facility;
		this.patientsIdentifiers = patientsIdentifiers;
	}
	
	public Boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	public String getFacility() {
		return facility;
	}
	
	public void setFacility(String facility) {
		this.facility = facility;
	}
	
	public List<String> getPatientsIdentifiers() {
		return patientsIdentifiers;
	}
	
	public void setPatientsIdentifiers(List<String> patientsIdentifiers) {
		this.patientsIdentifiers = patientsIdentifiers;
	}
}
