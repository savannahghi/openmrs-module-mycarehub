package org.openmrs.module.mycarehub.api.rest.mapper;


import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MedicalRecord {

  @SerializedName("ccc_number")
  private String cccNumber;

  @SerializedName("registration")
  private PatientRegistration registration;

  @SerializedName("vitalSigns")
  private List<MyCareHubVitalSign> vitalSigns;

  @SerializedName("medications")
  private List<MyCareHubMedication> medications;

  @SerializedName("testOrders")
  private List<MyCareHubTestOrder> testOrders;

  @SerializedName("testResults")
  private List<MyCareHubTest> tests;

  @SerializedName("allergies")
  private List<MyCareHubAllergy> allergies;

  public void setCccNumber(String cccNumber) {
    this.cccNumber = cccNumber;
  }

  public String getCccNumber() {
    return cccNumber;
  }

  public void setRegistration(PatientRegistration registration) {
    this.registration = registration;
  }

  public PatientRegistration getRegistration() {
    return registration;
  }

  public void setVitalSigns(List<MyCareHubVitalSign> vitalSigns) {
    this.vitalSigns = vitalSigns;
  }

  public List<MyCareHubVitalSign> getVitalSigns() {
    return vitalSigns;
  }

  public void setMedications(List<MyCareHubMedication> medications) {
    this.medications = medications;
  }

  public List<MyCareHubMedication> getMedications() {
    return medications;
  }

  public void setTestOrders(List<MyCareHubTestOrder> testOrders) {
    this.testOrders = testOrders;
  }

  public List<MyCareHubTestOrder> getTestOrders() {
    return testOrders;
  }

  public void setTests(List<MyCareHubTest> tests) {
    this.tests = tests;
  }

  public List<MyCareHubTest> getTests() {
    return tests;
  }

  public void setAllergies(List<MyCareHubAllergy> allergies) {
    this.allergies = allergies;
  }

  public List<MyCareHubAllergy> getAllergies() {
    return allergies;
  }
}
