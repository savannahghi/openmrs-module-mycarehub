/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.mycarehub.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycarehub.api.service.AppointmentService;
import org.openmrs.module.mycarehub.api.service.RedFlagService;
import org.openmrs.module.mycarehub.model.AppointmentRequests;
import org.openmrs.module.mycarehub.model.RedFlags;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyCareHubController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(method = RequestMethod.GET, value = "/module/mycarehub/mycarehub.list")
	public void view() {
		// do nothing here, the rest will be handled by angular
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/module/mycarehub/appointmentRequests.json")
	public Map<String, Object> getAppointmentRequests(final @RequestParam(value = "pageNumber") Integer pageNumber,
	        final @RequestParam(value = "pageSize") Integer pageSize) {
		Map<String, Object> response = new HashMap<String, Object>();
		if (Context.isAuthenticated()) {
			AppointmentService appointmentService = Context.getService(AppointmentService.class);
			int pages = (appointmentService.countAppointments().intValue() + pageSize - 1) / pageSize;
			List<Object> objects = new ArrayList<Object>();
			
			for (AppointmentRequests appointmentRequest : appointmentService.getPagedAppointments(pageNumber, pageSize)) {
				objects.add(convertToJsonMap(appointmentRequest));
			}
			
			response.put("pages", pages);
			response.put("totalItems", appointmentService.countAppointments().intValue());
			response.put("objects", objects);
		}
		return response;
	}
	
	Map<String, Object> convertToJsonMap(final AppointmentRequests appointmentRequest) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (appointmentRequest != null) {
			map.put("uuid", appointmentRequest.getUuid());
			map.put("appointmentUuid", appointmentRequest.getAppointmentUUID());
			map.put("appointmentType", appointmentRequest.getAppointmentType());
			map.put("appointmentReason", appointmentRequest.getAppointmentReason());
			map.put("provider", appointmentRequest.getProvider());
			map.put("requestedDate", Context.getDateTimeFormat().format(appointmentRequest.getRequestedDate()));
			map.put("requestedTimeSlot", appointmentRequest.getRequestedTimeSlot());
			map.put("status", appointmentRequest.getStatus());
			if (appointmentRequest.getProgressDate() != null) {
				map.put("progressDate", appointmentRequest.getProgressDate());
			} else {
				map.put("progressDate", "");
			}
			map.put("progressBy", appointmentRequest.getProgressBy());
			if (appointmentRequest.getDateResolved() != null) {
				map.put("dateResolved", appointmentRequest.getDateResolved());
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
}
