package org.openmrs.module.mycarehub.api.service.impl;

import static org.openmrs.module.mycarehub.utils.Constants.*;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_APPOINTMENTS;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_APPOINTMENTS_REQUESTS_GET;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_APPOINTMENTS_REQUESTS_POST;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENTS_CONTAINER_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_DATE_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_ID_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_REASON_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentRequestObjectKeys.*;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.GeneralKeys.CCC_NUMBER;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.GeneralKeys.FACILITY_MFL_CODE;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.GeneralKeys.MYCAREHUB_ID_KEY;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Obs;
import org.openmrs.PatientIdentifierType;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycarehub.api.db.AppointmentDao;
import org.openmrs.module.mycarehub.api.service.AppointmentService;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.AppointmentRequests;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;
import org.openmrs.module.mycarehub.utils.MyCareHubUtil;

public class AppointmentServiceImpl extends BaseOpenmrsService implements AppointmentService {

  private static final Log log = LogFactory.getLog(AppointmentServiceImpl.class);

  private final AppointmentDao dao;

  private final SimpleDateFormat mycarehubDateTimeFormatter =
      new SimpleDateFormat(mycarehubDateTimePattern);

  private final SimpleDateFormat dateFormat = new SimpleDateFormat(YEAR_MONTH_DAY_PATTERN);

  public AppointmentServiceImpl(AppointmentDao dao) {
    this.dao = dao;
  }

  @Override
  public List<Obs> getAppointmentsByLastSyncDate(Date lastSyncDate) {
    return dao.getAppointmentsByLastSyncDate(lastSyncDate);
  }

  @Override
  public List<AppointmentRequests> getAllAppointmentRequests() {
    return dao.getAllAppointmentRequests();
  }

  @Override
  public AppointmentRequests getAppointmentRequestByUuid(String uuid) {
    return dao.getAppointmentRequestByUuid(uuid);
  }

  @Override
  public List<AppointmentRequests> getAllAppointmentRequestsByLastSyncDate(Date lastSyncDate) {
    return dao.getAllAppointmentRequestsByLastSyncDate(lastSyncDate);
  }

  @Override
  public List<AppointmentRequests> saveAppointmentRequests(
      List<AppointmentRequests> appointmentRequests) {
    return dao.saveAppointmentRequests(appointmentRequests);
  }

  @Override
  public AppointmentRequests saveAppointmentRequests(AppointmentRequests appointmentRequest) {
    return dao.saveAppointmentRequests(appointmentRequest);
  }

  @Override
  public AppointmentRequests getAppointmentRequestByMycarehubId(String mycarehubId) {
    return dao.getAppointmentRequestByMycarehubId(mycarehubId);
  }

  @Override
  public Number countAppointments(String searchString) {
    return dao.countAppointments(searchString);
  }

  @Override
  public List<AppointmentRequests> getPagedAppointments(
      String searchString, Integer pageNumber, Integer pageSize) {
    return dao.getPagedAppointments(searchString, pageNumber, pageSize);
  }

  @Override
  public void syncPatientAppointments() {
    MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
    MyCareHubSetting setting =
        settingsService.getLatestMyCareHubSettingByType(PATIENT_APPOINTMENTS);

    if (setting != null) {
      List<Obs> appointments = dao.getAppointmentsByLastSyncDate(setting.getLastSyncTime());
      Date newSyncDate = new Date();
      JsonObject containerObject = createContainerObject(appointments);

      if (!appointments.isEmpty()) {
        MyCareHubUtil.uploadPatientAppointments(containerObject, newSyncDate);
      } else {
        settingsService.createMyCareHubSetting(PATIENT_APPOINTMENTS, newSyncDate);
      }
    } else {
      settingsService.createMyCareHubSetting(PATIENT_APPOINTMENTS, new Date());
    }
  }

  private JsonObject createContainerObject(List<Obs> appointments) {
    JsonObject containerObject = new JsonObject();
    JsonArray appointmentsArray = new JsonArray();
    PatientIdentifierType cccPatientIdentifierType = MyCareHubUtil.getcccPatientIdentifierType();
    List<Integer> encounterIds = new ArrayList<Integer>();

    for (Obs appointment : appointments) {
      Integer encounterId = appointment.getEncounter().getEncounterId();
      if (!encounterIds.contains(encounterId)) {
        JsonObject appointmentObject =
            createAppointmentObject(appointment, cccPatientIdentifierType);
        appointmentsArray.add(appointmentObject);
        encounterIds.add(encounterId);
      }
    }

    containerObject.addProperty(FACILITY_MFL_CODE, MyCareHubUtil.getDefaultLocationMflCode());
    containerObject.add(APPOINTMENTS_CONTAINER_KEY, appointmentsArray);

    return containerObject;
  }

  private JsonObject createAppointmentObject(
      Obs appointment, PatientIdentifierType cccPatientIdentifierType) {
    JsonObject appointmentObject = new JsonObject();

    if (appointment.getConcept().getId() == APPOINTMENT_DATE_CONCEPT_ID) {
      appointmentObject.addProperty(
          APPOINTMENT_DATE_KEY, dateFormat.format(appointment.getValueDate()));
      Obs obs =
          dao.getObsByEncounterAndConcept(
              appointment.getEncounter().getEncounterId(), APPOINTMENT_REASON_CONCEPT_ID);
      String reason = (obs != null) ? obs.getValueAsString(Locale.ENGLISH) : "";
      appointmentObject.addProperty(APPOINTMENT_REASON_KEY, reason);
    } else {
      Obs obs =
          dao.getObsByEncounterAndConcept(
              appointment.getEncounter().getEncounterId(), APPOINTMENT_DATE_CONCEPT_ID);
      String appointmentDate =
          (obs != null && obs.getValueDatetime() != null)
              ? dateFormat.format(obs.getValueDatetime())
              : "";
      appointmentObject.addProperty(APPOINTMENT_DATE_KEY, appointmentDate);
      appointmentObject.addProperty(
          APPOINTMENT_REASON_KEY, appointment.getValueAsString(Locale.ENGLISH));
    }

    appointmentObject.addProperty(
        APPOINTMENT_ID_KEY, String.valueOf(appointment.getEncounter().getEncounterId()));
    appointmentObject.addProperty(
        CCC_NUMBER,
        appointment
            .getEncounter()
            .getPatient()
            .getPatientIdentifier(cccPatientIdentifierType)
            .getIdentifier());

    return appointmentObject;
  }

  @Override
  public void syncPatientAppointmentRequests() {
    MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
    MyCareHubSetting setting =
        settingsService.getLatestMyCareHubSettingByType(PATIENT_APPOINTMENTS_REQUESTS_POST);

    Date newSyncDate = new Date();

    if (setting == null) {
      settingsService.createMyCareHubSetting(PATIENT_APPOINTMENTS_REQUESTS_POST, newSyncDate);
      return;
    }

    List<AppointmentRequests> appointmentRequests =
        dao.getAllAppointmentRequestsByLastSyncDate(setting.getLastSyncTime());

    if (appointmentRequests.isEmpty()) {
      settingsService.createMyCareHubSetting(PATIENT_APPOINTMENTS_REQUESTS_POST, newSyncDate);
      return;
    }

    JsonObject containerObject = new JsonObject();
    JsonArray appointmentsObject = new JsonArray();

    for (AppointmentRequests appointmentRequest : appointmentRequests) {
      JsonObject appointmentObject = new JsonObject();
      appointmentObject.addProperty(MYCAREHUB_ID_KEY, appointmentRequest.getMycarehubId());
      appointmentObject.addProperty(APPOINTMENT_REQUEST_STATUS_KEY, appointmentRequest.getStatus());

      addFormattedProperty(
          appointmentObject, APPOINTMENT_PROGRESS_DATE_KEY, appointmentRequest.getProgressDate());
      appointmentObject.addProperty(
          APPOINTMENT_PROGRESS_BY_KEY, appointmentRequest.getProgressBy());

      addFormattedProperty(
          appointmentObject, APPOINTMENT_RESOLVED_DATE_KEY, appointmentRequest.getDateResolved());
      appointmentObject.addProperty(
          APPOINTMENT_RESOLVED_BY_KEY, appointmentRequest.getResolvedBy());

      appointmentObject.addProperty(APPOINTMENT_REQUEST_TYPE_KEY, APPOINTMENT_REQUEST_TYPE);

      appointmentsObject.add(appointmentObject);
    }

    containerObject.add(APPOINTMENT_REQUEST_CONTAINER, appointmentsObject);

    MyCareHubUtil.uploadPatientAppointmentRequests(containerObject, newSyncDate);
  }

  private void addFormattedProperty(JsonObject jsonObject, String key, Date date) {
    if (date != null) {
      jsonObject.addProperty(key, mycarehubDateTimeFormatter.format(date));
    } else {
      jsonObject.addProperty(key, "null");
    }
  }

  @Override
  public void fetchPatientAppointmentRequests() {
    MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
    MyCareHubSetting setting =
        settingsService.getLatestMyCareHubSettingByType(PATIENT_APPOINTMENTS_REQUESTS_GET);

    if (setting == null) {
      settingsService.createMyCareHubSetting(PATIENT_APPOINTMENTS_REQUESTS_GET, new Date());
      return;
    }

    Date newSyncDate = new Date();

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("MFLCODE", MyCareHubUtil.getDefaultLocationMflCode());
    jsonObject.addProperty(
        "lastSyncTime", mycarehubDateTimeFormatter.format(setting.getLastSyncTime()));

    JsonArray jsonArray =
        MyCareHubUtil.fetchPatientAppointmentRequests(setting.getLastSyncTime(), newSyncDate);

    List<AppointmentRequests> appointmentRequests = getAppointmentRequests(jsonArray);
    if (!appointmentRequests.isEmpty()) {
      saveAppointmentRequests(appointmentRequests);
    }
  }

  private List<AppointmentRequests> getAppointmentRequests(JsonArray jsonArray) {
    List<AppointmentRequests> appointmentRequests = new ArrayList<AppointmentRequests>();
    if (jsonArray != null) {
      for (int i = 0; i < jsonArray.size(); i++) {
        JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
        AppointmentRequests appointmentRequest = new AppointmentRequests();
        String mycarehubId = jsonObject1.get("id").getAsString();

        AppointmentRequests existingRequests = getAppointmentRequestByMycarehubId(mycarehubId);
        if (existingRequests != null && existingRequests.getMycarehubId() != null) {
          appointmentRequest = existingRequests;
        } else {
          appointmentRequest.setCreator(new User(1));
          appointmentRequest.setDateCreated(new Date());
          appointmentRequest.setUuid(UUID.randomUUID().toString());
          appointmentRequest.setVoided(false);
        }

        appointmentRequest.setAppointmentUUID(jsonObject1.get("AppointmentID").getAsString());
        appointmentRequest.setMycarehubId(jsonObject1.get("id").getAsString());
        appointmentRequest.setAppointmentReason(jsonObject1.get("AppointmentReason").getAsString());

        handleAppointmentException(appointmentRequest, jsonObject1, dateFormat);

        appointmentRequest.setStatus(jsonObject1.get("Status").getAsString());
        appointmentRequest.setClientName(jsonObject1.get("ClientName").getAsString());
        appointmentRequest.setClientContact(jsonObject1.get("ClientContact").getAsString());
        appointmentRequest.setCccNumber(jsonObject1.get("CCCNumber").getAsString());
        appointmentRequest.setMflCode(jsonObject1.get("MFLCODE").getAsString());

        appointmentRequests.add(appointmentRequest);
      }
    }

    return appointmentRequests;
  }

  private static void handleAppointmentException(
      AppointmentRequests appointmentRequest, JsonObject jsonObject1, SimpleDateFormat dateFormat) {
    try {
      appointmentRequest.setRequestedDate(
          dateFormat.parse(jsonObject1.get("SuggestedDate").getAsString()));
    } catch (ParseException e) {
      log.error("Cannot parse SuggestedDate date", e);
    }

    if (!jsonObject1.get("InProgressAt").isJsonNull()) {
      try {
        appointmentRequest.setProgressDate(
            dateFormat.parse(jsonObject1.get("InProgressAt").getAsString()));
      } catch (ParseException e) {
        log.error("Cannot parse InProgressAt date", e);
      }
    }

    if (!jsonObject1.get("InProgressBy").isJsonNull())
      appointmentRequest.setProgressBy(jsonObject1.get("InProgressBy").getAsString());

    if (!jsonObject1.get("ResolvedAt").isJsonNull()) {
      try {
        appointmentRequest.setProgressDate(
            dateFormat.parse(jsonObject1.get("ResolvedAt").getAsString()));
      } catch (ParseException e) {
        log.error("Cannot parse ResolvedAt date", e);
      }
    }

    if (!jsonObject1.get("ResolvedBy").isJsonNull())
      appointmentRequest.setResolvedBy(jsonObject1.get("ResolvedBy").getAsString());
  }
}
