package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MyCareHubMedication {
	
	@SerializedName("medication")
	private String medicationName;
	
	@SerializedName("medicationConceptId")
	private String medicationConceptId;
	
	@SerializedName("medicationDateTime")
	private String medicationDateTime;
	
	@SerializedName("value")
	private String value;
	
	@SerializedName("drugConceptId")
	private String drugConceptId;
	
	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
	}
	
	public String getMedicationName() {
		return medicationName;
	}
	
	public void setMedicationConceptId(Integer medicationConceptId) {
		setMedicationConceptId(String.valueOf(medicationConceptId));
	}
	
	public void setMedicationConceptId(String medicationConceptId) {
		this.medicationConceptId = medicationConceptId;
	}
	
	public String getMedicationConceptId() {
		return medicationConceptId;
	}
	
	public void setMedicationDateTime(String medicationDateTime) {
		this.medicationDateTime = medicationDateTime;
	}
	
	public String getMedicationDateTime() {
		return medicationDateTime;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setDrugConceptId(Integer drugConceptId) {
		setDrugConceptId(String.valueOf(drugConceptId));
	}
	
	public void setDrugConceptId(String drugConceptId) {
		this.drugConceptId = drugConceptId;
	}
	
	public String getDrugConceptId() {
		return drugConceptId;
	}
}
