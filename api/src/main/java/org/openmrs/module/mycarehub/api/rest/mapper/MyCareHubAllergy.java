package org.openmrs.module.mycarehub.api.rest.mapper;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MyCareHubAllergy {
	
	@SerializedName("name")
	private String allergyName;
	
	@SerializedName("allergyConceptId")
	private String allergyConceptId;
	
	@SerializedName("reaction")
	private String reaction;
	
	@SerializedName("reactionConceptId")
	private String reactionConceptId;
	
	@SerializedName("other_reaction")
	private String otherReaction;
	
	@SerializedName("severity")
	private String severity;
	
	@SerializedName("severityConceptId")
	private String severityConceptId;
	
	@SerializedName("allergyDateTime")
	private Date allergyDateTime;
	
	public void setAllergyName(String allergyName) {
		this.allergyName = allergyName;
	}
	
	public String getAllergyName() {
		return allergyName;
	}
	
	public void setAllergyConceptId(Integer allergyConceptId) {
		setSeverityConceptId(String.valueOf(allergyConceptId));
	}
	
	public void setAllergyConceptId(String allergyConceptId) {
		this.allergyConceptId = allergyConceptId;
	}
	
	public String getAllergyConceptId() {
		return allergyConceptId;
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
	
	public void setReactionConceptId(Integer reactionConceptId) {
		setSeverityConceptId(String.valueOf(reactionConceptId));
	}
	
	public void setReactionConceptId(String reactionConceptId) {
		this.reactionConceptId = reactionConceptId;
	}
	
	public String getReactionConceptId() {
		return reactionConceptId;
	}
	
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	
	public String getSeverity() {
		return severity;
	}
	
	public void setSeverityConceptId(Integer severityConceptId) {
		setSeverityConceptId(String.valueOf(severityConceptId));
	}
	
	public void setSeverityConceptId(String severityConceptId) {
		this.severityConceptId = severityConceptId;
	}
	
	public String getSeverityConceptId() {
		return severityConceptId;
	}
	
	public void setAllergyDateTime(Date allergyDateTime) {
		this.allergyDateTime = allergyDateTime;
	}
	
	public Date getAllergyDateTime() {
		return allergyDateTime;
	}
}
