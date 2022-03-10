package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MyCareHubTestOrder {
	
	@SerializedName("orderDateTime")
	private Date orderDateTime;
	
	@SerializedName("orderedTestName")
	private String orderedTestName;
	
	public void setOrderDateTime(Date orderDateTime) {
		this.orderDateTime = orderDateTime;
	}
	
	public Date getOrderDateTime() {
		return orderDateTime;
	}
	
	public void setOrderedTestName(String orderedTestName) {
		this.orderedTestName = orderedTestName;
	}
	
	public String getOrderedTestName() {
		return orderedTestName;
	}
}
