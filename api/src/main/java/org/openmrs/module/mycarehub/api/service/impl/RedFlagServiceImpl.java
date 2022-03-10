package org.openmrs.module.mycarehub.api.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycarehub.api.db.MyCareHubRedFlagDao;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.api.service.RedFlagService;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;
import org.openmrs.module.mycarehub.model.RedFlags;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_RED_FLAGS_REQUESTS_GET;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_RED_FLAGS_REQUESTS_POST;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.fetchPatientAppointments;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.getDefaultLocationMflCode;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.postPatientRedFlags;

public class RedFlagServiceImpl extends BaseOpenmrsService implements RedFlagService {
	
	MyCareHubRedFlagDao dao;
	
	SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	public RedFlagServiceImpl(MyCareHubRedFlagDao dao) {
		this.dao = dao;
	}
	
	@Override
	public List<RedFlags> getAllRedFlagRequests() {
		return dao.getAllRedFlagRequests();
	}
	
	@Override
	public List<RedFlags> getAllRedFlagRequestsByLastSyncDate(Date lastSyncDate) {
		return dao.getAllRedFlagRequestsByLastSyncDate(lastSyncDate);
	}

	@Override
	public List<RedFlags> getPagedRedFlagsByRequestType(String requestType, Integer pageNumber, Integer pageSize) {
		return dao.getPagedRedFlagsByRequestType(requestType, pageNumber, pageSize);
	}

	@Override
	public List<RedFlags> saveRedFlagRequests(List<RedFlags> redFlags) {
		return dao.saveRedFlagRequests(redFlags);
	}
	
	@Override
	public RedFlags getRedFlagRequestByMycarehubId(String mycarehubId) {
		return dao.getRedFlagRequestByMycarehubId(mycarehubId);
	}

	@Override
	public Number countRedFlagsByType(String requestType) {
		return dao.countRedFlagsByType(requestType);
	}

	@Override
	public void syncPatientRedFlagRequests() {
		MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
		MyCareHubSetting setting = settingsService.getMyCareHubSettingByType(PATIENT_RED_FLAGS_REQUESTS_POST);
		if (setting != null) {
			List<RedFlags> redFlags = dao.getAllRedFlagRequestsByLastSyncDate(setting.getLastSyncTime());
			Date newSyncDate = new Date();
			
			JsonObject containerObject = new JsonObject();
			JsonArray redFlagArray = new JsonArray();
			JsonObject redFlagObject = new JsonObject();
			if (redFlags.size() > 0) {
				for (RedFlags redFlag : redFlags) {
					redFlagObject = new JsonObject();
					redFlagObject.addProperty("ID", redFlag.getMycarehubId());
					redFlagObject.addProperty("status", redFlag.getStatus());
					redFlagObject.addProperty("RequestType", redFlag.getRequestType());
					if (redFlag.getProgressDate() != null) {
						redFlagObject.addProperty("InProgressAt", dateTimeFormat.format(redFlag.getProgressDate()));
					} else {
						redFlagObject.addProperty("InProgressAt", "null");
					}
					redFlagObject.addProperty("InProgressBy", redFlag.getProgressBy());
					if (redFlag.getDateResolved() != null) {
						redFlagObject.addProperty("ResolvedAt", dateTimeFormat.format(redFlag.getDateResolved()));
					} else {
						redFlagObject.addProperty("ResolvedAt", "null");
					}
					redFlagObject.addProperty("ResolvedBy", redFlag.getResolvedBy());
					
					redFlagArray.add(redFlagObject);
				}
				
				containerObject.add("appointment-request", redFlagArray);
				
				postPatientRedFlags(containerObject, newSyncDate);
				
			} else {
				setting = new MyCareHubSetting();
				setting.setSettingType(PATIENT_RED_FLAGS_REQUESTS_POST);
				setting.setLastSyncTime(newSyncDate);
				settingsService.saveMyCareHubSettings(setting);
			}
		} else {
			MyCareHubSetting newPatientAppointmengtSyncDateSetting = new MyCareHubSetting();
			newPatientAppointmengtSyncDateSetting.setSettingType(PATIENT_RED_FLAGS_REQUESTS_POST);
			newPatientAppointmengtSyncDateSetting.setLastSyncTime(new Date());
			settingsService.saveMyCareHubSettings(newPatientAppointmengtSyncDateSetting);
		}
	}
	
	@Override
	public void fetchPatientRedFlagRequests() {
		MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
		MyCareHubSetting setting = settingsService.getMyCareHubSettingByType(PATIENT_RED_FLAGS_REQUESTS_GET);
		if (setting != null) {
			Date newSyncDate = new Date();
			
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("MFLCODE", getDefaultLocationMflCode());
			jsonObject.addProperty("lastSyncTime", dateTimeFormat.format(setting.getLastSyncTime()));
			
			fetchPatientAppointments(jsonObject, newSyncDate);
		} else {
			MyCareHubSetting newPatientAppointmengtSyncDateSetting = new MyCareHubSetting();
			newPatientAppointmengtSyncDateSetting.setSettingType(PATIENT_RED_FLAGS_REQUESTS_GET);
			newPatientAppointmengtSyncDateSetting.setLastSyncTime(new Date());
			settingsService.saveMyCareHubSettings(newPatientAppointmengtSyncDateSetting);
		}
	}
}
