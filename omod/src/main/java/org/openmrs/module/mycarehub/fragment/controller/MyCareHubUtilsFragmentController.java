package org.openmrs.module.mycarehub.fragment.controller;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCareHubUtilsFragmentController {
	
	protected static final Log log = LogFactory.getLog(MyCareHubUtilsFragmentController.class);
	
	public Map<String, Object> getAppointmentRequests(final @RequestParam(value = "pageNumber") Integer pageNumber,
	        final @RequestParam(value = "pageSize") Integer pageSize, UiUtils ui) {
		Map<String, Object> response = new HashMap<String, Object>();
		if (Context.isAuthenticated()) {
			AppointmentService appointmentService = Context.getService(AppointmentService.class);
			int pages = (appointmentService.countAppointments().intValue() + pageSize - 1) / pageSize;
			List<Object> objects = new ArrayList<Object>();
			
			List<AppointmentRequests> appointmentRequests = appointmentService.getPagedAppointments(pageNumber, pageSize);
			
			for (AppointmentRequests appointmentRequest : appointmentRequests) {
				objects.add(convertAppointmentRequestToJsonMap(appointmentRequest));
			}
			
			response.put("pages", pages);
			response.put("totalItems", appointmentService.countAppointments().intValue());
			response.put("objects", objects);
		}
		return response;
	}
	
	private Map<String, Object> convertAppointmentRequestToJsonMap(final AppointmentRequests appointmentRequest) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (appointmentRequest != null) {
			map.put("uuid", appointmentRequest.getUuid());
			map.put("appointmentUuid", appointmentRequest.getAppointmentUUID());
			map.put("appointmentReason", appointmentRequest.getAppointmentReason());
			map.put("requestedDate", Context.getDateFormat().format(appointmentRequest.getRequestedDate()));
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
	
	public Map<String, Object> getHealthDiaries(final @RequestParam(value = "pageNumber") Integer pageNumber,
	        final @RequestParam(value = "pageSize") Integer pageSize) {
		Map<String, Object> response = new HashMap<String, Object>();
		if (Context.isAuthenticated()) {
			HealthDiaryService healthDiaryService = Context.getService(HealthDiaryService.class);
			int pages = (healthDiaryService.countHealthDiaries().intValue() + pageSize - 1) / pageSize;
			List<Object> objects = new ArrayList<Object>();
			
			for (HealthDiary healthDiary : healthDiaryService.getPagedHealthDiaries(pageNumber, pageSize)) {
				objects.add(convertHealthDiariesToJsonMap(healthDiary));
			}
			response.put("pages", pages);
			response.put("totalItems", healthDiaryService.countHealthDiaries().intValue());
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
	
	public Map<String, Object> getRedFlagsByType(final @RequestParam(value = "requestType") String requestType,
	        final @RequestParam(value = "pageNumber") Integer pageNumber,
	        final @RequestParam(value = "pageSize") Integer pageSize) {
		Map<String, Object> response = new HashMap<String, Object>();
		if (Context.isAuthenticated()) {
			RedFlagService redFlagService = Context.getService(RedFlagService.class);
			int pages = (redFlagService.countRedFlagsByType(requestType).intValue() + pageSize - 1) / pageSize;
			List<Object> objects = new ArrayList<Object>();
			
			for (RedFlags redFlags : redFlagService.getPagedRedFlagsByRequestType(requestType, pageNumber, pageSize)) {
				objects.add(convertRedFlagsToJsonMap(redFlags));
			}
			
			response.put("pages", pages);
			response.put("totalItems", redFlagService.countRedFlagsByType(requestType).intValue());
			response.put("objects", objects);
		}
		return response;
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
}
