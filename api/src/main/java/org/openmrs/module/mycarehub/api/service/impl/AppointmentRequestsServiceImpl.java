package org.openmrs.module.mycarehub.api.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.appointmentscheduling.Appointment;
import org.openmrs.module.mycarehub.api.db.AppointmentRequestsDao;
import org.openmrs.module.mycarehub.api.service.AppointmentRequestsService;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.AppointmentRequests;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;

import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.getDefaultLocationMflCode;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.getcccPatientIdentifierType;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.uploadPatientAppointments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_APPOINTMENTS;

public class AppointmentRequestsServiceImpl extends BaseOpenmrsService implements AppointmentRequestsService {
	
	AppointmentRequestsDao dao;
	
	public AppointmentRequestsServiceImpl(AppointmentRequestsDao dao) {
		this.dao = dao;
	}
	
	@Override
	public List<Appointment> getAppointmentsByLastSyncDate(Date lastSyncDate) {
		return dao.getAppointmentsByLastSyncDate(lastSyncDate);
	}
	
	@Override
	public List<AppointmentRequests> getAllAppointmentRequests() {
		return dao.getAllAppointmentRequests();
	}
	
	@Override
	public List<AppointmentRequests> saveAppointmentRequests(List<AppointmentRequests> appointmentRequests) {
		return dao.saveAppointmentRequests(appointmentRequests);
	}
	
	@Override
	public void syncPatientAppointments() {
		MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
		MyCareHubSetting setting = settingsService.getMyCareHubSettingByType(PATIENT_APPOINTMENTS);
		if (setting != null) {
			List<Appointment> appointments = dao.getAppointmentsByLastSyncDate(setting.getLastSyncTime());
			Date newSyncDate = new Date();
			
			JsonObject containerObject = new JsonObject();
			JsonObject appointmentsObject = new JsonObject();
			JsonObject appointmentObject = new JsonObject();
			PatientIdentifierType cccPatientIdentifierType = getcccPatientIdentifierType();
			if (appointments.size() > 0) {
				for (Appointment appointment : appointments) {
					java.util.Date date = new java.util.Date();
					SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
					SimpleDateFormat dateFormat = new SimpleDateFormat();
					
					appointmentObject = new JsonObject();
					appointmentObject.addProperty("appointment_uuid", appointment.getUuid());
					appointmentObject.addProperty("appointment_date",
					    dateFormat.format(appointment.getTimeSlot().getStartDate()));
					appointmentObject.addProperty(
					    "time_slot",
					    timeFormat.format(timeFormat.format(appointment.getTimeSlot().getStartDate()) + " "
					            + timeFormat.format(appointment.getTimeSlot().getEndDate())));
					appointmentObject.addProperty("appointment_type", appointment.getAppointmentType().getName());
					appointmentObject.addProperty("status", appointment.getStatus().getName());
					appointmentsObject.add(appointment.getPatient().getPatientIdentifier(cccPatientIdentifierType)
					        .getIdentifier(), appointmentObject);
				}
				
				containerObject.addProperty("MFLCODE", getDefaultLocationMflCode());
				containerObject.add("appointments", appointmentsObject);
				
				uploadPatientAppointments(containerObject, newSyncDate);
				
			}
		} else {
			MyCareHubSetting newPatientAppointmengtSyncDateSetting = new MyCareHubSetting(PATIENT_APPOINTMENTS, new Date());
			settingsService.saveMyCareHubSettings(newPatientAppointmengtSyncDateSetting);
		}
	}
	
	public AppointmentRequestsDao getDao() {
		return dao;
	}
	
	public void setDao(AppointmentRequestsDao dao) {
		this.dao = dao;
	}
}
