package org.openmrs.module.mycarehub.api.db;

import org.openmrs.module.appointmentscheduling.Appointment;
import org.openmrs.module.mycarehub.model.AppointmentRequests;

import java.util.Date;
import java.util.List;

public interface AppointmentDao {
	
	List<Appointment> getAppointmentsByLastSyncDate(Date lastSyncDate);
	
	List<AppointmentRequests> getAllAppointmentRequests();
	
	List<AppointmentRequests> getAllAppointmentRequestsByLastSyncDate(Date lastSyncDate);
	
	List<AppointmentRequests> saveAppointmentRequests(List<AppointmentRequests> appointmentRequests);
	
	AppointmentRequests saveAppointmentRequests(AppointmentRequests appointmentRequest);
	
	AppointmentRequests getAppointmentRequestByMycarehubId(String mycarehubId);
	
	AppointmentRequests getAppointmentRequestByUuid(String uuid);
	
	Number countAppointments();
	
	List<AppointmentRequests> getPagedAppointments(Integer pageNumber, Integer pageSize);
}
