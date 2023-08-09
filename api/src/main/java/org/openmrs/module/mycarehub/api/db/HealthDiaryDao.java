package org.openmrs.module.mycarehub.api.db;


import java.util.List;
import org.openmrs.module.mycarehub.model.HealthDiary;

/** CRUD middleware that governs access and retrieval of a patient's health diary. */
public interface HealthDiaryDao {

  /**
   * Saves health diary entries.
   *
   * @param healthDiary This is the actual health diary to be saved.
   * @return The saved diary entries.
   */
  List<HealthDiary> saveHealthDiaries(List<HealthDiary> healthDiary);

  /**
   * Returns the total number of health diaries for the specified search param.
   *
   * @param searchString The search param like "John".
   * @return The Number of health diaries.
   */
  Number countHealthDiaries(String searchString);

  /**
   * Returns health diary entries in a page format.
   *
   * @param searchString Is the search param.
   * @param pageNumber The page being viewed.
   * @param pageSize The number of items in this page.
   * @return A list of health diary entries.
   */
  List<HealthDiary> getPagedHealthDiaries(
      String searchString, Integer pageNumber, Integer pageSize);
}
