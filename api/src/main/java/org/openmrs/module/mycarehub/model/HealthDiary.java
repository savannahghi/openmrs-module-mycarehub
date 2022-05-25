package org.openmrs.module.mycarehub.model;

import org.openmrs.BaseOpenmrsData;

import java.util.Date;

public class HealthDiary extends BaseOpenmrsData {
	
	private Integer id;
	
	private String cccNumber;
	
	private String mood;
	
	private String note;
	
	private Date dateRecorded;
	
	private String entryType;
	
	private Date sharedOn;
	
	private String clientContact;
	
	private String clientName;
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getMood() {
		return mood;
	}
	
	public void setMood(String mood) {
		this.mood = mood;
	}
	
	public Date getDateRecorded() {
		return dateRecorded;
	}
	
	public void setDateRecorded(Date dateRecorded) {
		this.dateRecorded = dateRecorded;
	}
	
	public String getCccNumber() {
		return cccNumber;
	}
	
	public void setCccNumber(String cccNumber) {
		this.cccNumber = cccNumber;
	}
	
	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getEntryType() {
		return entryType;
	}
	
	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}
	
	public Date getSharedOn() {
		return sharedOn;
	}
	
	public void setSharedOn(Date sharedOn) {
		this.sharedOn = sharedOn;
	}
	
	public String getClientContact() {
		return clientContact;
	}
	
	public void setClientContact(String clientContact) {
		this.clientContact = clientContact;
	}
	
	public String getClientName() {
		return clientName;
	}
	
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
}
