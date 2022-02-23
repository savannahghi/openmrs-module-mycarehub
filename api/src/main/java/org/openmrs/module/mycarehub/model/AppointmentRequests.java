package org.openmrs.module.mycarehub.model;

import org.openmrs.BaseOpenmrsData;

import java.util.Date;

public class AppointmentRequests extends BaseOpenmrsData {
	
	private String appointmentUUID;
	
	private int patientId;
	
	private Date requestedDate;
	
	private String requestedTimeSlot;
	
	public AppointmentRequests() {
	}
	
	@Override
	public Integer getId() {
		return null;
	}
	
	@Override
	public void setId(Integer integer) {
		
	}
	
	public String getAppointmentUUID() {
		return appointmentUUID;
	}
	
	public void setAppointmentUUID(String appointmentUUID) {
		this.appointmentUUID = appointmentUUID;
	}
	
	public int getPatientId() {
		return patientId;
	}
	
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	
	public Date getRequestedDate() {
		return requestedDate;
	}
	
	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}
	
	public String getRequestedTimeSlot() {
		return requestedTimeSlot;
	}
	
	public void setRequestedTimeSlot(String requestedTimeSlot) {
		this.requestedTimeSlot = requestedTimeSlot;
	}
}
