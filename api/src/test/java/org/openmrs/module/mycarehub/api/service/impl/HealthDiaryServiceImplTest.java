package org.openmrs.module.mycarehub.api.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.module.mycarehub.api.db.HealthDiaryDao;
import org.openmrs.module.mycarehub.model.HealthDiary;

@RunWith(MockitoJUnitRunner.class)
public class HealthDiaryServiceImplTest {

  @Mock private HealthDiaryDao fakeHealthDiaryDao;

  @InjectMocks private HealthDiaryServiceImpl healthDiaryImpl;

  private Date currentDate;

  private static final String searchString = "RED_FLAG";

  @Before
  public void setUp() {
    currentDate = new Date();
  }

  @Test
  public void testSaveHealthDiaries() {
    HealthDiary entryOne = createHealthDiary(currentDate, 1);
    HealthDiary entryTwo = createHealthDiary(currentDate, 2);

    List<HealthDiary> healthDiaries = Arrays.asList(entryOne, entryTwo);
    when(fakeHealthDiaryDao.saveHealthDiaries(healthDiaries)).thenReturn(healthDiaries);

    List<HealthDiary> savedHealthDiaryEntries = healthDiaryImpl.saveHealthDiaries(healthDiaries);
    assertEquals(2, savedHealthDiaryEntries.size());
  }

  @Test
  public void testSaveHealthDiaries_EmptyList() {
    List<HealthDiary> healthDiaries = Arrays.asList();
    when(fakeHealthDiaryDao.saveHealthDiaries(healthDiaries)).thenReturn(healthDiaries);

    List<HealthDiary> savedHealthDiaryEntries = healthDiaryImpl.saveHealthDiaries(healthDiaries);
    assertEquals(0, savedHealthDiaryEntries.size());
  }

  @Test
  public void testSaveHealthDiaries_SingleEntry() {
    HealthDiary entryOne = createHealthDiary(currentDate, 1);
    List<HealthDiary> healthDiaries = Arrays.asList(entryOne);

    when(fakeHealthDiaryDao.saveHealthDiaries(healthDiaries)).thenReturn(healthDiaries);

    List<HealthDiary> savedHealthDiaryEntries = healthDiaryImpl.saveHealthDiaries(healthDiaries);
    assertEquals(1, savedHealthDiaryEntries.size());
  }

  @Test
  public void testCountHealthDiaries() {
    Number healthDiariesCount = 5;

    when(fakeHealthDiaryDao.countHealthDiaries(searchString)).thenReturn(healthDiariesCount);

    Number healthDiaries = healthDiaryImpl.countHealthDiaries(searchString);
    assertEquals(5, healthDiaries);
  }

  @Test
  public void testGetPagedHealthDiaries() {
    Integer pageNumber = 1;
    Integer pageSize = 5;

    HealthDiary entryOne = createHealthDiary(currentDate, 1);
    HealthDiary entryTwo = createHealthDiary(currentDate, 2);

    List<HealthDiary> pagedHealthDiaries = Arrays.asList(entryOne, entryTwo);
    when(fakeHealthDiaryDao.getPagedHealthDiaries(searchString, pageNumber, pageSize))
        .thenReturn(pagedHealthDiaries);

    List<HealthDiary> fetchedHealthDiaries =
        healthDiaryImpl.getPagedHealthDiaries(searchString, pageNumber, pageSize);
    assertEquals(2, fetchedHealthDiaries.size());
  }

  // Helper method to create a HealthDiary object
  private static HealthDiary createHealthDiary(Date date, int id) {
    HealthDiary healthDiary = new HealthDiary();
    healthDiary.setId(id);
    healthDiary.setCccNumber("123456");
    healthDiary.setMood("VERY_SAD");
    healthDiary.setNote("I am very sad");
    healthDiary.setDateRecorded(date);
    healthDiary.setEntryType("health_diary");
    healthDiary.setSharedOn(date);
    healthDiary.setClientContact("+254711223344");
    healthDiary.setClientName("test");

    return healthDiary;
  }
}
