package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MyCareHubVitalSign {
	
	@SerializedName("concept")
	private String concept;
	
	@SerializedName("obsDatetime")
	private Date obsDatetime;
	
	@SerializedName("value")
	private String value;
	
	public void setConcept(String concept) {
		this.concept = concept;
	}
	
	public String getConcept() {
		return concept;
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
