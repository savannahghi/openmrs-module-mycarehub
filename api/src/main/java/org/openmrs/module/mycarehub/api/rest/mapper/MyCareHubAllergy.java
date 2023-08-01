package org.openmrs.module.mycarehub.api.rest.mapper;

import static org.openmrs.module.mycarehub.utils.Constants.mycarehubDateTimePattern;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyCareHubAllergy {
	
	@SerializedName("name")
	private String allergyName;
	
	@SerializedName("allergyConceptId")
	private String allergyConceptIdStr;
	
	private transient BigInteger allergyAnswerConceptId;
	
	@SerializedName("reaction")
	private String reaction;
	
	@SerializedName("reactionConceptId")
	private String reactionConceptIdStr;
	
	private transient BigInteger reactionAnswerConceptId;
	
	@SerializedName("other_reaction")
	private String otherReaction;
	
	@SerializedName("severity")
	private String severity;
	
	@SerializedName("severityConceptId")
	private String severityConceptIdStr;
	
	private transient BigInteger severityAnswerConceptId;
	
	@SerializedName("allergyDateTime")
	private String allergyDateTimeStr;
	
	private transient Date allergyDateTimeObj;
	
	public void setAllergyName(String allergyName) {
		this.allergyName = allergyName;
	}
	
	public String getAllergyName() {
		return allergyName;
	}
	
	public void setAllergyAnswerConceptId(BigInteger allergyAnswerConceptId) {
		this.allergyAnswerConceptId = allergyAnswerConceptId;
		
		if (allergyAnswerConceptId != null)
			this.allergyConceptIdStr = String.valueOf(allergyAnswerConceptId);
	}
	
	public String getAllergyConceptIdStr() {
		return allergyConceptIdStr;
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
	
	public void setReactionAnswerConceptId(BigInteger reactionAnswerConceptId) {
		this.reactionAnswerConceptId = reactionAnswerConceptId;
		
		if (reactionAnswerConceptId != null)
			this.reactionConceptIdStr = String.valueOf(reactionAnswerConceptId);
	}
	
	public BigInteger getReactionAnswerConceptId() {
		return reactionAnswerConceptId;
	}
	
	public String getReactionConceptIdStr() {
		return reactionConceptIdStr;
	}
	
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	
	public String getSeverity() {
		return severity;
	}
	
	public void setSeverityAnswerConceptId(BigInteger severityAnswerConceptId) {
		this.severityAnswerConceptId = severityAnswerConceptId;
		
		if (severityAnswerConceptId != null)
			this.severityConceptIdStr = String.valueOf(severityAnswerConceptId);
	}
	
	public BigInteger getSeverityAnswerConceptId() {
		return severityAnswerConceptId;
	}
	
	public String getSeverityConceptIdStr() {
		return severityConceptIdStr;
	}
	
	public void setAllergyDateTimeObj(Date allergyDateTimeObj) {
		this.allergyDateTimeObj = allergyDateTimeObj;
		
		if (allergyDateTimeObj != null)
			allergyDateTimeStr = new SimpleDateFormat(mycarehubDateTimePattern).format(allergyDateTimeObj);
	}
	
	public String getAllergyDateTimeStr() {
		return allergyDateTimeStr;
	}
}
