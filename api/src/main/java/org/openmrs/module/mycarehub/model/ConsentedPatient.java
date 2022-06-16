package org.openmrs.module.mycarehub.model;

import org.openmrs.BaseOpenmrsData;

public class ConsentedPatient extends BaseOpenmrsData {
	
	private Integer id;
	
	private int patientId;
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public int getPatientId() {
		return patientId;
	}
	
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
}
