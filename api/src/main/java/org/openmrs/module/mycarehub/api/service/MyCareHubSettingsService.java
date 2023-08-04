package org.openmrs.module.mycarehub.api.service;


import java.util.Date;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;

public interface MyCareHubSettingsService extends OpenmrsService {

  MyCareHubSetting getLatestMyCareHubSettingByType(String settingType);

  MyCareHubSetting createMyCareHubSetting(String settingType, Date syncDate);
}
