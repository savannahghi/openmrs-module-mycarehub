package org.openmrs.module.mycarehub.api.service.impl;

import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_RED_FLAGS_REQUESTS_GET;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_RED_FLAGS_REQUESTS_POST;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.GeneralKeys.MYCAREHUB_ID_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.REdFlagsObjectKeys.RED_FLAG_CONTAINER;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.REdFlagsObjectKeys.RED_FLAG_PROGRESS_BY_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.REdFlagsObjectKeys.RED_FLAG_PROGRESS_DATE_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.REdFlagsObjectKeys.RED_FLAG_REQUEST_TYPE_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.REdFlagsObjectKeys.RED_FLAG_RESOLVED_BY_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.REdFlagsObjectKeys.RED_FLAG_RESOLVED_DATE_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.REdFlagsObjectKeys.RED_FLAG_STATUS_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.mycarehubDateTimePattern;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.getPatientRedFlagRequests;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.postPatientRedFlags;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycarehub.api.db.MyCareHubRedFlagDao;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.api.service.RedFlagService;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;
import org.openmrs.module.mycarehub.model.RedFlags;

public class RedFlagServiceImpl extends BaseOpenmrsService implements RedFlagService {

  private static final Log log = LogFactory.getLog(RedFlagServiceImpl.class);

  private MyCareHubRedFlagDao dao;

  private final SimpleDateFormat mycarehubDateTimeFormatter =
      new SimpleDateFormat(mycarehubDateTimePattern);

  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  public RedFlagServiceImpl(MyCareHubRedFlagDao dao) {
    this.dao = dao;
  }

  @Override
  public List<RedFlags> getAllRedFlagRequests() {
    return dao.getAllRedFlagRequests();
  }

  @Override
  public RedFlags getRedFlagByUuid(String uuid) {
    return dao.getRedFlagByUuid(uuid);
  }

  @Override
  public List<RedFlags> getAllRedFlagRequestsByLastSyncDate(Date lastSyncDate) {
    return dao.getAllRedFlagRequestsByLastSyncDate(lastSyncDate);
  }

  @Override
  public List<RedFlags> getPagedRedFlagsByRequestType(
      String searchString, String requestType, Integer pageNumber, Integer pageSize) {
    return dao.getPagedRedFlagsByRequestType(searchString, requestType, pageNumber, pageSize);
  }

  @Override
  public List<RedFlags> saveRedFlagRequests(List<RedFlags> redFlags) {
    return dao.saveRedFlagRequests(redFlags);
  }

  @Override
  public RedFlags saveRedFlagRequests(RedFlags redFlags) {
    return dao.saveRedFlagRequests(redFlags);
  }

  @Override
  public RedFlags getRedFlagRequestByMycarehubId(String mycarehubId) {
    return dao.getRedFlagRequestByMycarehubId(mycarehubId);
  }

  @Override
  public Number countRedFlagsByType(String searchString, String requestType) {
    return dao.countRedFlagsByType(searchString, requestType);
  }

  @Override
  public void syncPatientRedFlagRequests() {
    MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
    MyCareHubSetting setting =
        settingsService.getLatestMyCareHubSettingByType(PATIENT_RED_FLAGS_REQUESTS_POST);

    Date newSyncDate = new Date();
    JsonObject containerObject = new JsonObject();
    JsonArray redFlagArray = new JsonArray();

    if (setting != null) {
      List<RedFlags> redFlags = dao.getAllRedFlagRequestsByLastSyncDate(setting.getLastSyncTime());

      if (!redFlags.isEmpty()) {
        for (RedFlags redFlag : redFlags) {
          JsonObject redFlagObject = createRedFlagJsonObject(redFlag);
          redFlagArray.add(redFlagObject);
        }

        containerObject.add(RED_FLAG_CONTAINER, redFlagArray);
        postPatientRedFlags(containerObject, newSyncDate);

      } else {
        settingsService.createMyCareHubSetting(PATIENT_RED_FLAGS_REQUESTS_POST, newSyncDate);
      }

    } else {
      settingsService.createMyCareHubSetting(PATIENT_RED_FLAGS_REQUESTS_POST, new Date());
    }
  }

  private JsonObject createRedFlagJsonObject(RedFlags redFlag) {
    JsonObject redFlagObject = new JsonObject();
    redFlagObject.addProperty(MYCAREHUB_ID_KEY, redFlag.getMycarehubId());
    redFlagObject.addProperty(RED_FLAG_STATUS_KEY, redFlag.getStatus());
    redFlagObject.addProperty(RED_FLAG_REQUEST_TYPE_KEY, redFlag.getRequestType());

    redFlagObject.addProperty(RED_FLAG_PROGRESS_DATE_KEY, formatDate(redFlag.getProgressDate()));
    redFlagObject.addProperty(RED_FLAG_PROGRESS_BY_KEY, redFlag.getProgressBy());

    redFlagObject.addProperty(RED_FLAG_RESOLVED_DATE_KEY, formatDate(redFlag.getDateResolved()));
    redFlagObject.addProperty(RED_FLAG_RESOLVED_BY_KEY, redFlag.getResolvedBy());

    return redFlagObject;
  }

  private String formatDate(Date date) {
    return date != null ? mycarehubDateTimeFormatter.format(date) : "null";
  }

  @Override
  public void fetchPatientRedFlagRequests() {
    MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
    MyCareHubSetting setting =
        settingsService.getLatestMyCareHubSettingByType(PATIENT_RED_FLAGS_REQUESTS_GET);
    if (setting != null) {
      Date newSyncDate = new Date();

      JsonArray jsonArray = getPatientRedFlagRequests(setting.getLastSyncTime(), newSyncDate);
      List<RedFlags> redFlags = new ArrayList<RedFlags>();
      if (jsonArray != null) {
        for (int i = 0; i < jsonArray.size(); i++) {
          JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
          RedFlags redFlag = new RedFlags();
          String mycarehubId = jsonObject1.get("id").getAsString();
          RedFlags existingRequests = getRedFlagRequestByMycarehubId(mycarehubId);
          if (existingRequests != null && existingRequests.getMycarehubId() != null) {
            redFlag = existingRequests;
          } else {
            redFlag.setCreator(new User(1));
            redFlag.setDateCreated(new Date());
            redFlag.setUuid(UUID.randomUUID().toString());
            redFlag.setVoided(false);
          }

          redFlag.setMycarehubId(jsonObject1.get("id").getAsString());
          redFlag.setRequest(jsonObject1.get("request").getAsString());
          redFlag.setRequestType(jsonObject1.get("requestType").getAsString());
          redFlag.setStatus(jsonObject1.get("status").getAsString());
          redFlag.setScreeningTool(jsonObject1.get("screeningToolName").getAsString());
          redFlag.setScreeningScore(jsonObject1.get("screeningToolScore").getAsString());
          if (!jsonObject1.get("inProgressAt").isJsonNull()) {
            try {
              redFlag.setProgressDate(
                  dateFormat.parse(jsonObject1.get("inProgressAt").getAsString()));
            } catch (ParseException e) {
              log.error("Cannot parse InProgressAt date", e);
            }
          }

          if (!jsonObject1.get("inProgressBy").isJsonNull())
            redFlag.setProgressBy(jsonObject1.get("inProgressBy").getAsString());
          if (!jsonObject1.get("resolvedAt").isJsonNull()) {
            try {
              redFlag.setProgressDate(
                  dateFormat.parse(jsonObject1.get("resolvedAt").getAsString()));
            } catch (ParseException e) {
              log.error("Cannot parse ResolvedAt date", e);
            }
          }
          if (!jsonObject1.get("resolvedBy").isJsonNull())
            redFlag.setResolvedBy(jsonObject1.get("resolvedBy").getAsString());
          redFlag.setClientName(jsonObject1.get("clientName").getAsString());
          redFlag.setClientContact(jsonObject1.get("clientContact").getAsString());
          redFlag.setCccNumber(jsonObject1.get("cccNumber").getAsString());

          redFlags.add(redFlag);
        }
      }
      if (!redFlags.isEmpty()) {
        saveRedFlagRequests(redFlags);
      }
    } else {
      settingsService.createMyCareHubSetting(PATIENT_RED_FLAGS_REQUESTS_GET, new Date());
    }
  }
}
