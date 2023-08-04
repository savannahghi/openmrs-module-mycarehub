/**
 * The contents of this file are subject to the OpenMRS Public License Version 1.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at http://license.openmrs.org
 *
 * <p>Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF
 * ANY KIND, either express or implied. See the License for the specific language governing rights
 * and limitations under the License.
 *
 * <p>Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.mycarehub.web.controller;


import java.text.SimpleDateFormat;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyCareHubController {

  /** Logger for this class and subclasses */
  protected final Log log = LogFactory.getLog(getClass());

  SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

  @RequestMapping(method = RequestMethod.GET, value = "/module/mycarehub/mycarehub.list")
  public void view() {
    // do nothing here, the rest will be handled by angular
  }

  @ResponseBody
  @RequestMapping(method = RequestMethod.GET, value = "/module/mycarehub/appointmentRequests.json")
  public Map<String, Object> getAppointmentRequests(
      final @RequestParam(value = "searchString") String searchString,
      final @RequestParam(value = "pageNumber") Integer pageNumber,
      final @RequestParam(value = "pageSize") Integer pageSize) {
    Map<String, Object> response = new HashMap<String, Object>();
    if (Context.isAuthenticated()) {
      AppointmentService appointmentService = Context.getService(AppointmentService.class);
      int totalItems = appointmentService.countAppointments(searchString).intValue();
      int pages = (totalItems + pageSize - 1) / pageSize;
      List<Object> objects = new ArrayList<Object>();

      for (AppointmentRequests appointmentRequest :
          appointmentService.getPagedAppointments(searchString, pageNumber, pageSize)) {
        objects.add(convertAppointmentRequestToJsonMap(appointmentRequest));
      }

      response.put("pages", pages);
      response.put("totalItems", totalItems);
      response.put("objects", objects);
    }
    return response;
  }

  @ResponseBody
  @RequestMapping(method = RequestMethod.GET, value = "/module/mycarehub/serviceRequests.json")
  public Map<String, Object> getRedFlagsByType(
      final @RequestParam(value = "searchString") String searchString,
      final @RequestParam(value = "requestType") String requestType,
      final @RequestParam(value = "pageNumber") Integer pageNumber,
      final @RequestParam(value = "pageSize") Integer pageSize) {
    Map<String, Object> response = new HashMap<String, Object>();
    if (Context.isAuthenticated()) {
      RedFlagService redFlagService = Context.getService(RedFlagService.class);
      int totalItems = redFlagService.countRedFlagsByType(searchString, requestType).intValue();
      int pages = (totalItems + pageSize - 1) / pageSize;
      List<Object> objects = new ArrayList<Object>();

      for (RedFlags redFlags :
          redFlagService.getPagedRedFlagsByRequestType(
              searchString, requestType, pageNumber, pageSize)) {
        objects.add(convertRedFlagsToJsonMap(redFlags));
      }

      response.put("pages", pages);
      response.put("totalItems", totalItems);
      response.put("objects", objects);
    }
    return response;
  }

  @ResponseBody
  @RequestMapping(method = RequestMethod.GET, value = "/module/mycarehub/healthDiaries.json")
  public Map<String, Object> getHealthDiaries(
      final @RequestParam(value = "searchString") String searchString,
      final @RequestParam(value = "pageNumber") Integer pageNumber,
      final @RequestParam(value = "pageSize") Integer pageSize) {
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

  @RequestMapping(
      method = RequestMethod.POST,
      value = "/module/mycarehub/setRedFlagInProgress.json")
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

  @RequestMapping(method = RequestMethod.POST, value = "/module/mycarehub/setRedFlagResolved.json")
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

  @RequestMapping(
      method = RequestMethod.POST,
      value = "/module/mycarehub/setAppointmentInProgress.json")
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

  @RequestMapping(
      method = RequestMethod.POST,
      value = "/module/mycarehub/setAppointmentResolved.json")
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

  Map<String, Object> convertAppointmentRequestToJsonMap(
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

  Map<String, Object> convertRedFlagsToJsonMap(final RedFlags redFlags) {
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

  Map<String, Object> convertHealthDiariesToJsonMap(final HealthDiary healthDiary) {
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
}
