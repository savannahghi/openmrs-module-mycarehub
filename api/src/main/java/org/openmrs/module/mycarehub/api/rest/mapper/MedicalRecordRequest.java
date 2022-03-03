package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MedicalRecordRequest {
    @SerializedName("vitalSigns")
    private List<MyCareHubVitalSign> vitalSigns;

    @SerializedName("medications")
    private List<MyCareHubMedication> medications;

    @SerializedName("tests")
    private List<MyCareHubTest> tests;

    @SerializedName("allergies")
    private List<MyCareHubAllergy> allergies;

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
