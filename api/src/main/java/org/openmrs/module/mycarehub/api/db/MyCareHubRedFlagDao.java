package org.openmrs.module.mycarehub.api.db;

import org.openmrs.module.mycarehub.model.RedFlags;

import java.util.Date;
import java.util.List;

/**
 * Middleware for handling client red flags.
 *
 * A red flag is a service request created when a patient enters MEH, SAD or VERY_SAD as their mood in their health diary.
 *
 * There are various types of red flags.
 */
public interface MyCareHubRedFlagDao {
	/**
	 * Retrieves all red flags that are available.
	 *
	 * @return A list of red flags.
	 */
	List<RedFlags> getAllRedFlagRequests();

	/**
	 * Retrieves a specific red flag by its uuid.
	 *
	 * @param uuid the unique identifier for the red flag.
	 * @return A single red flag.
	 */
	RedFlags getRedFlagByUuid(String uuid);

	/**
	 * Retrieves all red flags received since the last successful synchronization.
	 *
	 * @param lastSyncDate The last time a successful synchronization was done.
	 * @return A list of red flags.
	 */
	List<RedFlags> getAllRedFlagRequestsByLastSyncDate(Date lastSyncDate);

	/**
	 * Returns red flags in a page format
	 *
	 * @param searchString The search param e.g "John Doe".
	 * @param requestType The type of request to show.
	 * @param pageNumber The page that is currently being viewed.
	 * @param pageSize The total number of items in the current page.
	 * @return A list of red flags.
	 */
	List<RedFlags> getPagedRedFlagsByRequestType(String searchString, String requestType, Integer pageNumber,
	        Integer pageSize);

	/**
	 * Saves the provided red flags.
	 *
	 * @param redFlags The red flags to save.
	 * @return The saved list of red flags.
	 */
	List<RedFlags> saveRedFlagRequests(List<RedFlags> redFlags);

	/**
	 * Saves a single red flag request.
	 *
	 * @param redFlag The red flag to save.
	 * @return Nothing.
	 */
	RedFlags saveRedFlagRequests(RedFlags redFlag);

	/**
	 * Retrieves a red flag by its myCareHub ID
	 * @param mycarehubId A myCareHub ID for the red flag to be retrieved
	 * @return
	 */
	RedFlags getRedFlagRequestByMycarehubId(String mycarehubId);

	/**
	 * Gets the total number of red flags by the search param
	 *
	 * @param searchString The search param
	 * @param requestType The type of red flag being searched for
	 * @return A number
	 */
	Number countRedFlagsByType(String searchString, String requestType);
}
