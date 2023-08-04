package org.openmrs.module.mycarehub.api.db;


import java.util.Date;
import java.util.List;
import org.openmrs.Obs;
import org.openmrs.module.mycarehub.model.AppointmentRequests;

/**
 * A scheduled appointment in KenyaEMR. All appointments are synchronized to myCareHub for viewing
 * on the platform. Appointments can also be rescheduled from myCareHub.
 */
public interface AppointmentDao {

  /**
   * Retrieves all appointments created since the lastSyncDate.
   *
   * @param lastSyncDate The last time a successful synchronization was done.
   * @return A list of appointments as observations.
   */
  List<Obs> getAppointmentsByLastSyncDate(Date lastSyncDate);

  /**
   * Retrieves all appointment requests.
   *
   * @return A list of appointment requests.
   */
  List<AppointmentRequests> getAllAppointmentRequests();

  /**
   * Retrieves all appointments since the lastSyncDate.
   *
   * @param lastSyncDate The last time a successful synchronization was done.
   * @return A list of appointments.
   */
  List<AppointmentRequests> getAllAppointmentRequestsByLastSyncDate(Date lastSyncDate);

  /**
   * Saves the provided appointment requests.
   *
   * @param appointmentRequests A list of appointment requests to save.
   * @return The list of the saved appointment requests.
   */
  List<AppointmentRequests> saveAppointmentRequests(List<AppointmentRequests> appointmentRequests);

  /**
   * Saves a single appointment request.
   *
   * @param appointmentRequest The appointment request to save.
   * @return Nothing.
   */
  AppointmentRequests saveAppointmentRequests(AppointmentRequests appointmentRequest);

  /**
   * Retrieves an appointment by its myCareHubID.
   *
   * @param mycarehubId A unique identifier for the appointment on myCareHub.
   * @return An appointment request.
   */
  AppointmentRequests getAppointmentRequestByMycarehubId(String mycarehubId);

  /**
   * Retrieves an appointment by its UUID.
   *
   * @param uuid The unique identifier of the appointment in KenyaEMR.
   * @return An appointment request.
   */
  AppointmentRequests getAppointmentRequestByUuid(String uuid);

  /**
   * Returns the total number of appointments based on the search criteria.
   *
   * @param searchString The search param.
   * @return A number of the total appointments.
   */
  Number countAppointments(String searchString);

  /**
   * Retrieves appointment requests in a page format.
   *
   * @param searchString Is the search param.
   * @param pageNumber The page being viewed.
   * @param pageSize The number of items in this page.
   * @return A list of appointment requests.
   */
  List<AppointmentRequests> getPagedAppointments(
      String searchString, Integer pageNumber, Integer pageSize);

  /**
   * Retrieves a single observation for the specified encounterId and conceptId.
   *
   * @param encounterId A unique identifier for a visit.
   * @param conceptId A concept ID for the observation to retrieve.
   * @return An observation.
   */
  Obs getObsByEncounterAndConcept(Integer encounterId, Integer conceptId);
}
