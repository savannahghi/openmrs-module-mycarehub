package org.openmrs.module.mycarehub.api.service;


import java.util.Date;
import java.util.List;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycarehub.model.RedFlags;

public interface RedFlagService extends OpenmrsService {

  List<RedFlags> getAllRedFlagRequests();

  RedFlags getRedFlagByUuid(String uuid);

  List<RedFlags> getAllRedFlagRequestsByLastSyncDate(Date lastSyncDate);

  List<RedFlags> getPagedRedFlagsByRequestType(
      String searchString, String requestType, Integer pageNumber, Integer pageSize);

  List<RedFlags> saveRedFlagRequests(List<RedFlags> redFlags);

  RedFlags saveRedFlagRequests(RedFlags redFlags);

  RedFlags getRedFlagRequestByMycarehubId(String mycarehubId);

  Number countRedFlagsByType(String searchString, String requestType);

  void syncPatientRedFlagRequests();

  void fetchPatientRedFlagRequests();
}
