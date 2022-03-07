package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MyCareHubMedication {
	
	@SerializedName("medication")
	private String medicationName;
	
	@SerializedName("medicationDateTime")
	private Date medicationDateTime;
	
	@SerializedName("value")
	private String value;
	
	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
	}
	
	public String getMedicationName() {
		return medicationName;
	}
	
	public void setMedicationDateTime(Date medicationDateTime) {
		this.medicationDateTime = medicationDateTime;
	}
	
	public Date getMedicationDateTime() {
		return medicationDateTime;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
