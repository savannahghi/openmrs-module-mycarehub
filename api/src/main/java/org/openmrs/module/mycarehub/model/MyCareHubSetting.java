package org.openmrs.module.mycarehub.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openmrs.BaseOpenmrsData;
import java.util.Date;

/**
 * Holds configuration for the scheduler task.
 * It provides key information used for synchronization like the [settingType] and [lastSyncTime]
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyCareHubSetting extends BaseOpenmrsData {
	
	private String settingType;
	
	private Integer id;
	
	private Date lastSyncTime;
	
	// Used by hibernate
	public MyCareHubSetting() {
	}
	
	public MyCareHubSetting(String settingType, Date lastSyncTime) {
		this.settingType = settingType;
		this.lastSyncTime = lastSyncTime;
	}
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getSettingType() {
		return settingType;
	}
	
	public void setSettingType(String settingType) {
		this.settingType = settingType;
	}
	
	public Date getLastSyncTime() {
		return lastSyncTime;
	}
	
	public void setLastSyncTime(Date lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		
		MyCareHubSetting setting = (MyCareHubSetting) o;
		
		if (settingType != null ? !settingType.equals(setting.settingType) : setting.settingType != null)
			return false;
		return lastSyncTime != null ? lastSyncTime.equals(setting.lastSyncTime) : setting.lastSyncTime == null;
	}
	
	@Override
	public String toString() {
		return "MyCareHubSetting{" + "id=" + this.getId() + ", " + "settingType='" + settingType + "'" + "lastSyncTime='"
		        + lastSyncTime + "'" + '}';
	}
	
}
