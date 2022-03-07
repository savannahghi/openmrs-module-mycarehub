package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MyCareHubAllergy{

    @SerializedName("name")
    private String allergyName;

    @SerializedName("reaction")
    private String reaction;

    @SerializedName("other_reaction")
    private String otherReaction;

    @SerializedName("severity")
    private String severity;

    @SerializedName("allergyDateTime")
    private Date allergyDateTime;

    public void setAllergyName(String allergyName) {
        this.allergyName = allergyName;
    }

    public String getAllergyName() {
        return allergyName;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public void setOtherReaction(String otherReaction) {
        this.otherReaction = otherReaction;
    }

    public String getOtherReaction() {
        return otherReaction;
    }

    public String getReaction() {
        return reaction;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getSeverity() {
        return severity;
    }

    public void setAllergyDateTime(Date allergyDateTime) {
        this.allergyDateTime = allergyDateTime;
    }

    public Date getAllergyDateTime() {
        return allergyDateTime;
    }
}
