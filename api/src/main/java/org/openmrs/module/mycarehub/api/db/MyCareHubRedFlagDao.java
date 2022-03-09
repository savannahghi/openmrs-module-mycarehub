package org.openmrs.module.mycarehub.api.db;

import org.openmrs.module.mycarehub.model.RedFlags;

import java.util.Date;
import java.util.List;

public interface MyCareHubRedFlagDao {
	
	List<RedFlags> getAllRedFlagRequests();
	
	List<RedFlags> getAllRedFlagRequestsByLastSyncDate(Date lastSyncDate);

	List<RedFlags> getPagedRedFlagsByRequestType(String requestType, Integer pageNumber, Integer pageSize);

	List<RedFlags> saveRedFlagRequests(List<RedFlags> redFlags);
	
	RedFlags getRedFlagRequestByMycarehubId(String mycarehubId);

	Number countRedFlagsByType(String requestType);
}
