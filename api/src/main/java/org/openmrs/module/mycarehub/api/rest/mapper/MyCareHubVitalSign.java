package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MyCareHubVitalSign {
	
	@SerializedName("name")
	private String conceptName;
	
	@SerializedName("conceptId")
	private String conceptId;
	
	@SerializedName("obsDatetime")
	private Date obsDatetime;
	
	@SerializedName("value")
	private String value;
	
	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
	
	public String getConceptName() {
		return conceptName;
	}
	
	public void setConceptId(Integer conceptId) {
		this.conceptId = String.valueOf(conceptId);
	}
	
	public void setConceptId(String conceptId) {
		this.conceptId = conceptId;
	}
	
	public String getConceptId() {
		return conceptId;
	}
	
	public void setObsDatetime(Date obsDatetime) {
		this.obsDatetime = obsDatetime;
	}
	
	public Date getObsDatetime() {
		return obsDatetime;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
