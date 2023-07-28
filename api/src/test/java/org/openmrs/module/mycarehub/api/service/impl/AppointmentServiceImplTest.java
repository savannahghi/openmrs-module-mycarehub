package org.openmrs.module.mycarehub.api.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.Obs;
import org.openmrs.User;
import org.openmrs.module.mycarehub.api.db.AppointmentDao;
import org.openmrs.module.mycarehub.model.AppointmentRequests;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceImplTest {
	
	@Mock
	private AppointmentDao appointmentDao;
	
	@InjectMocks
	private AppointmentServiceImpl fakeAppointmentImpl;
	
	public static final User USER = testUserFactory();
	
	public static final Date CURRENT_DATE = new Date();

	
	@Test
	public void testGetAppointmentsByLastSyncDate() {
		Obs recordOne = createObs();
		
		List<Obs> obsList = Arrays.asList(recordOne);
		when(appointmentDao.getAppointmentsByLastSyncDate(recordOne.getDateCreated())).thenReturn(obsList);
		
		List<Obs> obsRecords = fakeAppointmentImpl.getAppointmentsByLastSyncDate(recordOne.getDateCreated());
		assertEquals(1, obsRecords.size());
	}
	
	@Test
	public void testGetAllAppointmentRequests() {
		List<AppointmentRequests> requests = createAppointmentRequests();
		when(appointmentDao.getAllAppointmentRequests()).thenReturn(requests);
		
		List<AppointmentRequests> appointmentRequests = fakeAppointmentImpl.getAllAppointmentRequests();
		assertEquals(1, appointmentRequests.size());
	}
	
	@Test
	public void testGetAllAppointmentRequests_Not_more_than_one() {
		List<AppointmentRequests> requests = createAppointmentRequests();
		when(appointmentDao.getAllAppointmentRequests()).thenReturn(requests);
		
		List<AppointmentRequests> appointmentRequests = fakeAppointmentImpl.getAllAppointmentRequests();
		assertNotEquals(4, appointmentRequests.size());
	}
	
	@Test
	public void testGetAppointmentRequestByUuid() {
		List<AppointmentRequests> requests = createAppointmentRequests();
		when(appointmentDao.getAppointmentRequestByUuid(requests.get(0).getAppointmentUUID())).thenReturn(requests.get(0));
		
		AppointmentRequests appointmentRequests = fakeAppointmentImpl.getAppointmentRequestByUuid(requests.get(0)
		        .getAppointmentUUID());
		assertEquals(appointmentRequests, requests.get(0));
	}
	
	@Test
	public void testGetAllAppointmentRequestsByLastSyncDate() {
		List<AppointmentRequests> requests = createAppointmentRequests();
		when(appointmentDao.getAllAppointmentRequestsByLastSyncDate(CURRENT_DATE)).thenReturn(requests);
		
		List<AppointmentRequests> appointmentRequestsList = fakeAppointmentImpl
		        .getAllAppointmentRequestsByLastSyncDate(CURRENT_DATE);
		assertEquals(appointmentRequestsList, requests);
		assertEquals(1, appointmentRequestsList.size());
	}
	
	@Test
	public void testSaveAppointmentRequests() {
		List<AppointmentRequests> requests = createAppointmentRequests();
		when(appointmentDao.saveAppointmentRequests(requests)).thenReturn(requests);
		
		List<AppointmentRequests> savedAppointments = fakeAppointmentImpl.saveAppointmentRequests(requests);
		assertEquals(savedAppointments, requests);
	}
	
	@Test
	public void testGetAppointmentRequestByMycarehubId() {
		List<AppointmentRequests> requests = createAppointmentRequests();
		when(appointmentDao.getAppointmentRequestByMycarehubId(requests.get(0).getMycarehubId()))
		        .thenReturn(requests.get(0));
		
		AppointmentRequests appointmentList = fakeAppointmentImpl.getAppointmentRequestByMycarehubId(requests.get(0)
		        .getMycarehubId());
		assertEquals(appointmentList, requests.get(0));
	}
	
	@Test
	public void testCountAppointments() {
		List<AppointmentRequests> requests = createAppointmentRequests();
		when(appointmentDao.countAppointments("TEST")).thenReturn(requests.size());
		
		Number appointments = fakeAppointmentImpl.countAppointments("TEST");
		assertEquals(1, appointments);
	}
	
	@Test
	public void testGetPagedAppointments() {
		List<AppointmentRequests> requests = createAppointmentRequests();
		when(appointmentDao.getPagedAppointments("TEST", 1, 5)).thenReturn(requests);
		
		List<AppointmentRequests> appointments = fakeAppointmentImpl.getPagedAppointments("TEST", 1, 5);
		assertEquals(1, appointments.size());
		assertEquals(appointments, requests);
	}
	
	private static Obs createObs() {
		Obs ob = new Obs();

		ob.setCreator(USER);
		ob.setId(1);
		ob.setObsId(1);
		ob.setComment("test");
		ob.setDateCreated(CURRENT_DATE);
		
		return ob;
	}
	
	private static List<AppointmentRequests> createAppointmentRequests() {
		AppointmentRequests appointment = new AppointmentRequests();
		
		appointment.setAppointmentReason("Very far");
		appointment.setAppointmentUUID(String.valueOf(UUID.randomUUID()));
		appointment.setCccNumber("12345");
		appointment.setId(12);
		appointment.setCreator(USER);
		
		return Arrays.asList(appointment);
	}

	private static User testUserFactory() {
		User user = new User();
		user.setId(1);

		return user;
	}
}
