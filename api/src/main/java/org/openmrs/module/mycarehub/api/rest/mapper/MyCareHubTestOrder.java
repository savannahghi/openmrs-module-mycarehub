package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

public class MyCareHubTestOrder {
	
	@SerializedName("orderDateTime")
	private String orderDateTime;
	
	@SerializedName("orderedTestName")
	private String orderedTestName;
	
	@SerializedName("conceptId")
	private String conceptId;
	
	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}
	
	public String getOrderDateTime() {
		return orderDateTime;
	}
	
	public void setOrderedTestName(String orderedTestName) {
		this.orderedTestName = orderedTestName;
	}
	
	public String getOrderedTestName() {
		return orderedTestName;
	}
	
	public void setConceptId(String conceptId) {
		this.conceptId = conceptId;
	}
	
	public void setConceptId(Integer conceptId) {
		setConceptId(String.valueOf(conceptId));
	}
	
	public String getConceptId() {
		return conceptId;
	}
}
