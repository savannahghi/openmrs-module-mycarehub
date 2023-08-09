package org.openmrs.module.mycarehub.api.rest.mapper;


import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MedicalRecordsRequest {

  @SerializedName("MFLCODE")
  private String facility;

  @SerializedName("records")
  private List<MedicalRecord> medicalRecords;

  public void setFacility(String facility) {
    this.facility = facility;
  }

  public String getFacility() {
    return facility;
  }

  public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
    this.medicalRecords = medicalRecords;
  }

  public List<MedicalRecord> getMedicalRecords() {
    return medicalRecords;
  }
}
