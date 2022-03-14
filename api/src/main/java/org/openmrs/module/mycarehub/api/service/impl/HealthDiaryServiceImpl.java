package org.openmrs.module.mycarehub.api.service.impl;

import com.google.gson.JsonObject;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycarehub.api.db.HealthDiaryDao;
import org.openmrs.module.mycarehub.api.service.HealthDiaryService;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.HealthDiary;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_HEALTH_DIARY_GET;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.getDefaultLocationMflCode;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.getPatientHealthDiaries;

public class HealthDiaryServiceImpl extends BaseOpenmrsService implements HealthDiaryService {
	
	HealthDiaryDao dao;
	
	SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	public HealthDiaryServiceImpl(HealthDiaryDao dao) {
		this.dao = dao;
	}
	
	@Override
	public List<HealthDiary> saveHealthDiaries(List<HealthDiary> healthDiary) {
		return dao.saveHealthDiaries(healthDiary);
	}
	
	@Override
	public Number countHealthDiaries() {
		return dao.countHealthDiaries();
	}
	
	@Override
	public List<HealthDiary> getPagedHealthDiaries(Integer pageNumber, Integer pageSize) {
		return dao.getPagedHealthDiaries(pageNumber, pageSize);
	}
	
	@Override
	public void fetchPatientHealthDiaries() {
		MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
		MyCareHubSetting setting = settingsService.getMyCareHubSettingByType(PATIENT_HEALTH_DIARY_GET);
		if (setting != null) {
			Date newSyncDate = new Date();
			
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("MFLCODE", getDefaultLocationMflCode());
			jsonObject.addProperty("lastSyncTime", dateTimeFormat.format(setting.getLastSyncTime()));
			
			getPatientHealthDiaries(jsonObject, newSyncDate);
		} else {
			settingsService.createMyCareHubSetting(PATIENT_HEALTH_DIARY_GET, new Date());
		}
	}
}
