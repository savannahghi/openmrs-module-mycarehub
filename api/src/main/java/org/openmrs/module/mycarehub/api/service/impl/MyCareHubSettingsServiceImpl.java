package org.openmrs.module.mycarehub.api.service.impl;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycarehub.api.db.MyCareHubSettingsDao;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;

import java.util.Date;

import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.KENYAEMR_MEDICAL_RECORDS;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.KENYAEMR_PATIENT_REGISTRATIONS;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.MYCAREHUB_CLIENT_REGISTRATIONS;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_APPOINTMENTS;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_RED_FLAGS_REQUESTS_GET;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_RED_FLAGS_REQUESTS_POST;

public class MyCareHubSettingsServiceImpl extends BaseOpenmrsService implements MyCareHubSettingsService {
	
	MyCareHubSettingsDao dao;
	
	public MyCareHubSettingsServiceImpl(MyCareHubSettingsDao dao) {
		this.dao = dao;
	}
	
	public MyCareHubSettingsDao getDao() {
		return dao;
	}
	
	public void setDao(MyCareHubSettingsDao dao) {
		this.dao = dao;
	}
	
	@Override
	public MyCareHubSetting getMyCareHubSettingByType(String settingType) {
		return dao.getMyCareHubSettingByType(settingType);
	}
	
	@Override
	public MyCareHubSetting createMyCareHubSetting(String settingType, Date syncTime) {
		MyCareHubSetting setting = new MyCareHubSetting(KENYAEMR_PATIENT_REGISTRATIONS, syncTime);
		return dao.saveOrUpdateMyCareHubSetting(setting);
	}
}
