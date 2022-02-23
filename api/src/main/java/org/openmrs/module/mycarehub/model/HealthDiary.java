package org.openmrs.module.mycarehub.model;

import org.openmrs.BaseOpenmrsData;

import java.util.Date;

public class HealthDiary extends BaseOpenmrsData {
	
	private int patientId;
	
	private String mood;
	
	private String elaboration;
	
	private Date dateRecorded;
	
	@Override
	public Integer getId() {
		return null;
	}
	
	@Override
	public void setId(Integer integer) {
		
	}
	
	public int getPatientId() {
		return patientId;
	}
	
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	
	public String getMood() {
		return mood;
	}
	
	public void setMood(String mood) {
		this.mood = mood;
	}
	
	public String getElaboration() {
		return elaboration;
	}
	
	public void setElaboration(String elaboration) {
		this.elaboration = elaboration;
	}
	
	public Date getDateRecorded() {
		return dateRecorded;
	}
	
	public void setDateRecorded(Date dateRecorded) {
		this.dateRecorded = dateRecorded;
	}
}
