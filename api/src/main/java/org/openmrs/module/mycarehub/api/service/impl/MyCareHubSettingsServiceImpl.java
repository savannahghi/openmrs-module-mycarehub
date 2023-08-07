package org.openmrs.module.mycarehub.api.service.impl;

import org.openmrs.User;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycarehub.api.db.MyCareHubSettingsDao;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;

import java.util.Date;
import java.util.UUID;

public class MyCareHubSettingsServiceImpl extends BaseOpenmrsService implements MyCareHubSettingsService {
	
	MyCareHubSettingsDao dao;
	
	public MyCareHubSettingsServiceImpl(MyCareHubSettingsDao dao) {
		this.dao = dao;
	}
	
	@Override
	public MyCareHubSetting getLatestMyCareHubSettingByType(String settingType) {
		return dao.getMyCareHubSettingByType(settingType);
	}
	
	@Override
	public MyCareHubSetting createMyCareHubSetting(String settingType, Date syncTime) {
		MyCareHubSetting setting = new MyCareHubSetting(settingType, syncTime);
		setting.setDateCreated(new Date());
		setting.setCreator(new User(1));
		setting.setUuid(UUID.randomUUID().toString());
		setting.setVoided(false);
		return dao.saveOrUpdateMyCareHubSetting(setting);
	}
}
