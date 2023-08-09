package org.openmrs.module.mycarehub.fragment.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycarehub.api.service.AppointmentService;
import org.openmrs.module.mycarehub.api.service.HealthDiaryService;
import org.openmrs.module.mycarehub.api.service.RedFlagService;
import org.openmrs.module.mycarehub.model.AppointmentRequests;
import org.openmrs.module.mycarehub.model.HealthDiary;
import org.openmrs.module.mycarehub.model.RedFlags;
import org.openmrs.ui.framework.UiUtils;
import org.springframework.web.bind.annotation.RequestParam;

public class MyCareHubUtilsFragmentController {

  protected static final Log log = LogFactory.getLog(MyCareHubUtilsFragmentController.class);

  public Map<String, Object> getAppointmentRequests(
      final @RequestParam(value = "pageNumber") Integer pageNumber,
      final @RequestParam(value = "pageSize") Integer pageSize,
      UiUtils ui) {
    return getPagedAppointments(null, pageSize, pageNumber);
  }

  public Map<String, Object> searchAppointmentRequests(
      final @RequestParam(value = "searchString") String searchString,
      final @RequestParam(value = "pageNumber") Integer pageNumber,
      final @RequestParam(value = "pageSize") Integer pageSize,
      UiUtils ui) {
    return getPagedAppointments(searchString, pageSize, pageNumber);
  }

  private Map<String, Object> getPagedAppointments(
      String searchString, Integer pageSize, Integer pageNumber) {
    Map<String, Object> response = new HashMap<String, Object>();
    if (Context.isAuthenticated()) {
      AppointmentService appointmentService = Context.getService(AppointmentService.class);
      int totalItems = appointmentService.countAppointments(searchString).intValue();
      int pages = (totalItems + pageSize - 1) / pageSize;
      List<Object> objects = new ArrayList<Object>();

      List<AppointmentRequests> appointmentRequests =
          appointmentService.getPagedAppointments(searchString, pageNumber, pageSize);

      for (AppointmentRequests appointmentRequest : appointmentRequests) {
        objects.add(convertAppointmentRequestToJsonMap(appointmentRequest));
      }
      response.put("pages", pages);
      response.put("totalItems", totalItems);
      response.put("objects", objects);
    }
    return response;
  }

  private Map<String, Object> convertAppointmentRequestToJsonMap(
      final AppointmentRequests appointmentRequest) {
    Map<String, Object> map = new HashMap<String, Object>();
    if (appointmentRequest != null) {
      map.put("uuid", appointmentRequest.getUuid());
      map.put("appointmentUuid", appointmentRequest.getAppointmentUUID());
      map.put("appointmentReason", appointmentRequest.getAppointmentReason());
      map.put(
          "requestedDate", Context.getDateFormat().format(appointmentRequest.getRequestedDate()));
      map.put("status", appointmentRequest.getStatus());
      if (appointmentRequest.getProgressDate() != null) {
        map.put("progressDate", appointmentRequest.getProgressDate().toString());
      } else {
        map.put("progressDate", "");
      }
      map.put("progressBy", appointmentRequest.getProgressBy());
      if (appointmentRequest.getDateResolved() != null) {
        map.put("dateResolved", appointmentRequest.getDateResolved().toString());
      } else {
        map.put("dateResolved", "");
      }
      map.put("resolvedBy", appointmentRequest.getResolvedBy());
      map.put("clientName", appointmentRequest.getClientName());
      map.put("clientContact", appointmentRequest.getClientContact());
      map.put("cccNumber", appointmentRequest.getCccNumber());
    }
    return map;
  }

  public Map<String, Object> getHealthDiaries(
      final @RequestParam(value = "pageNumber") Integer pageNumber,
      final @RequestParam(value = "pageSize") Integer pageSize) {
    return getPagedHealthDiaries(null, pageNumber, pageSize);
  }

  public Map<String, Object> searchHealthDiaries(
      final @RequestParam(value = "searchString") String searchString,
      final @RequestParam(value = "pageNumber") Integer pageNumber,
      final @RequestParam(value = "pageSize") Integer pageSize) {
    return getPagedHealthDiaries(searchString, pageNumber, pageSize);
  }

  public Map<String, Object> getPagedHealthDiaries(
      String searchString, Integer pageNumber, Integer pageSize) {
    Map<String, Object> response = new HashMap<String, Object>();
    if (Context.isAuthenticated()) {
      HealthDiaryService healthDiaryService = Context.getService(HealthDiaryService.class);
      int totalItems = healthDiaryService.countHealthDiaries(searchString).intValue();
      int pages = (totalItems + pageSize - 1) / pageSize;
      List<Object> objects = new ArrayList<Object>();

      for (HealthDiary healthDiary :
          healthDiaryService.getPagedHealthDiaries(searchString, pageNumber, pageSize)) {
        objects.add(convertHealthDiariesToJsonMap(healthDiary));
      }
      response.put("pages", pages);
      response.put("totalItems", totalItems);
      response.put("objects", objects);
    }
    return response;
  }

  private Map<String, Object> convertHealthDiariesToJsonMap(final HealthDiary healthDiary) {
    Map<String, Object> map = new HashMap<String, Object>();
    if (healthDiary != null) {
      map.put("cccNumber", healthDiary.getCccNumber());
      map.put("mood", healthDiary.getMood());
      map.put("note", healthDiary.getNote());
      map.put("entryType", healthDiary.getEntryType());
      if (healthDiary.getDateRecorded() != null) {
        map.put("dateRecorded", Context.getDateTimeFormat().format(healthDiary.getDateRecorded()));
      } else {
        map.put("dateRecorded", "");
      }
      if (healthDiary.getSharedOn() != null) {
        map.put("sharedOn", Context.getDateTimeFormat().format(healthDiary.getSharedOn()));
      } else {
        map.put("sharedOn", "");
      }
    }
    return map;
  }

  public Map<String, Object> getRedFlagsByType(
      final @RequestParam(value = "requestType") String requestType,
      final @RequestParam(value = "pageNumber") Integer pageNumber,
      final @RequestParam(value = "pageSize") Integer pageSize) {
    return getPagedRedFlagsByRequestType(null, requestType, pageNumber, pageSize);
  }

  public Map<String, Object> searchRedFlagsByType(
      final @RequestParam(value = "searchString") String searchString,
      final @RequestParam(value = "requestType") String requestType,
      final @RequestParam(value = "pageNumber") Integer pageNumber,
      final @RequestParam(value = "pageSize") Integer pageSize) {
    return getPagedRedFlagsByRequestType(searchString, requestType, pageNumber, pageSize);
  }

  private Map<String, Object> getPagedRedFlagsByRequestType(
      String searchString, String requestType, Integer pageNumber, Integer pageSize) {
    Map<String, Object> response = new HashMap<String, Object>();
    if (Context.isAuthenticated()) {
      RedFlagService redFlagService = Context.getService(RedFlagService.class);
      int totalItems = redFlagService.countRedFlagsByType(searchString, requestType).intValue();
      int pages = (totalItems + pageSize - 1) / pageSize;
      List<Object> objects = new ArrayList<Object>();

      List<RedFlags> redFlagsList =
          redFlagService.getPagedRedFlagsByRequestType(
              searchString, requestType, pageNumber, pageSize);
      for (RedFlags redFlag : redFlagsList) {
        objects.add(convertRedFlagsToJsonMap(redFlag));
      }

      response.put("pages", pages);
      response.put("totalItems", totalItems);
      response.put("objects", objects);
    }
    return response;
  }

  private Map<String, Object> convertRedFlagsToJsonMap(final RedFlags redFlags) {
    Map<String, Object> map = new HashMap<String, Object>();
    if (redFlags != null) {
      map.put("uuid", redFlags.getUuid());
      map.put("Request", redFlags.getRequest());
      map.put("RequestType", redFlags.getRequestType());
      map.put("ScreeningToolName", redFlags.getScreeningTool());
      map.put("ScreeningToolScore", redFlags.getScreeningScore());
      map.put("Status", redFlags.getStatus());
      if (redFlags.getProgressDate() != null) {
        map.put("progressDate", redFlags.getProgressDate().toString());
      } else {
        map.put("progressDate", "");
      }
      map.put("progressBy", redFlags.getProgressBy());
      if (redFlags.getDateResolved() != null) {
        map.put("dateResolved", redFlags.getDateResolved().toString());
      } else {
        map.put("dateResolved", "");
      }
      map.put("resolvedBy", redFlags.getResolvedBy());
      map.put("clientName", redFlags.getClientName());
      map.put("clientContact", redFlags.getClientContact());
      map.put("cccNumber", redFlags.getCccNumber());
      map.put("MFLCode", redFlags.getMflCode());
    }
    return map;
  }

  public Map<String, Object> setRedFlagInProgress(final @RequestParam(value = "uuid") String uuid) {
    Map<String, Object> response = new HashMap<String, Object>();
    if (Context.isAuthenticated()) {
      RedFlagService redFlagService = Context.getService(RedFlagService.class);
      RedFlags redFlag = redFlagService.getRedFlagByUuid(uuid);
      redFlag.setProgressDate(new Date());
      redFlag.setStatus("IN_PROGRESS");
      redFlag.setProgressBy(Context.getAuthenticatedUser().getUsername());
      redFlagService.saveRedFlagRequests(redFlag);
    }
    return response;
  }

  public Map<String, Object> setRedFlagResolved(final @RequestParam(value = "uuid") String uuid) {
    Map<String, Object> response = new HashMap<String, Object>();
    if (Context.isAuthenticated()) {
      RedFlagService redFlagService = Context.getService(RedFlagService.class);
      RedFlags redFlag = redFlagService.getRedFlagByUuid(uuid);
      redFlag.setDateResolved(new Date());
      redFlag.setStatus("RESOLVED");
      redFlag.setResolvedBy(Context.getAuthenticatedUser().getUsername());
      redFlagService.saveRedFlagRequests(redFlag);
    }
    return response;
  }

  public Map<String, Object> setAppointmentInProgress(
      final @RequestParam(value = "uuid") String uuid) {
    Map<String, Object> response = new HashMap<String, Object>();
    if (Context.isAuthenticated()) {
      AppointmentService appointmentService = Context.getService(AppointmentService.class);
      AppointmentRequests appointmentRequest = appointmentService.getAppointmentRequestByUuid(uuid);
      appointmentRequest.setProgressDate(new Date());
      appointmentRequest.setStatus("IN_PROGRESS");
      appointmentRequest.setProgressBy(Context.getAuthenticatedUser().getUsername());
      appointmentService.saveAppointmentRequests(appointmentRequest);
    }
    return response;
  }

  public Map<String, Object> setAppointmentResolved(
      final @RequestParam(value = "uuid") String uuid) {
    Map<String, Object> response = new HashMap<String, Object>();
    if (Context.isAuthenticated()) {
      AppointmentService appointmentService = Context.getService(AppointmentService.class);
      AppointmentRequests appointmentRequest = appointmentService.getAppointmentRequestByUuid(uuid);
      appointmentRequest.setDateResolved(new Date());
      appointmentRequest.setStatus("RESOLVED");
      appointmentRequest.setResolvedBy(Context.getAuthenticatedUser().getUsername());
      appointmentService.saveAppointmentRequests(appointmentRequest);
    }
    return response;
  }
}
