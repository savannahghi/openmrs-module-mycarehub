package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MyCareHubVitalSign {
	
	@SerializedName("name")
	private String conceptName;
	
	@SerializedName("conceptId")
	private String conceptId;
	
	@SerializedName("obsDatetime")
	private String obsDatetime;
	
	@SerializedName("value")
	private String value;
	
	public MyCareHubVitalSign(String conceptName, String conceptId, String obsDatetime, String value) {
		this.conceptName = conceptName;
		this.conceptId = conceptId;
		this.obsDatetime = obsDatetime;
		this.value = value;
	}
	
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
	
	public void setObsDatetime(String obsDatetime) {
		this.obsDatetime = obsDatetime;
	}
	
	public String getObsDatetime() {
		return obsDatetime;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
