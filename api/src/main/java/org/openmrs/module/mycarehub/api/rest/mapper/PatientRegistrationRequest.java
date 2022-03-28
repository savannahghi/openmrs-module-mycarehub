package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PatientRegistrationRequest {
	
	@SerializedName("MFLCODE")
	private String facility;
	
	@SerializedName("patients")
	private List<PatientRegistration> patients;
	
	public void setFacility(String facility) {
		this.facility = facility;
	}
	
	public String getFacility() {
		return facility;
	}
	
	public void setPatientRegistrations(List<PatientRegistration> patients) {
		this.patients = patients;
	}
	
	public List<PatientRegistration> getPatients() {
		return patients;
	}
	
	@Override
	public String toString() {
		return "MyCareHubSetting{" + "MFLCODE='" + facility + "', " + "patients='" + getPatients() + "'" + '}';
	}
}
