package org.openmrs.module.mycarehub.api.db;

import org.openmrs.module.appointmentscheduling.Appointment;
import org.openmrs.module.mycarehub.model.AppointmentRequests;

import java.util.Date;
import java.util.List;

public interface AppointmentRequestsDao {
	
	public List<Appointment> getAppointmentsByLastSyncDate(Date lastSyncDate);
	
	public List<AppointmentRequests> getAllAppointmentRequests();
	
	public List<AppointmentRequests> saveAppointmentRequests(List<AppointmentRequests> appointmentRequests);
}
