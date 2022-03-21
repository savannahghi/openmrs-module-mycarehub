package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

public class PatientRegistration {
	
	@SerializedName("MFLCODE")
	private Integer MFLCODE;
	
	@SerializedName("cccNumber")
	private String cccNumber;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("dateOfBirth")
	private String dateOfBirth;
	
	@SerializedName("clientType")
	private String clientType;
	
	@SerializedName("phoneNumber")
	private String phoneNumber;
	
	@SerializedName("enrollmentDate")
	private String enrollmentDate;
	
	@SerializedName("birthdateEstimated")
	private boolean birthdateEstimated;
	
	@SerializedName("gender")
	private String gender;
	
	@SerializedName("counselled")
	private boolean counseled;
	
	@SerializedName("nextOfKin")
	private Object nextOfKin;
	
	public void setCccNumber(String cccNumber) {
		this.cccNumber = cccNumber;
	}
	
	public String getCccNumber() {
		return cccNumber;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	
	public String getClientType() {
		return clientType;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setEnrollmentDate(String enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}
	
	public String getEnrollmentDate() {
		return enrollmentDate;
	}
	
	public void setBirthdateEstimated(boolean birthdateEstimated) {
		this.birthdateEstimated = birthdateEstimated;
	}
	
	public boolean getBirthdateEstimated() {
		return birthdateEstimated;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setCounseled(boolean counseled) {
		this.counseled = counseled;
	}
	
	public boolean getCounseled() {
		return counseled;
	}
	
	public void setNextOfKin(Object nextOfKin) {
		this.nextOfKin = nextOfKin;
	}
	
	public Object getNextOfKin() {
		return nextOfKin;
	}
	
	public Integer getMFLCODE() {
		return MFLCODE;
	}
	
	public void setMFLCODE(Integer MFLCODE) {
		this.MFLCODE = MFLCODE;
	}
}
