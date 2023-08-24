package org.openmrs.module.mycarehub.api.db.hibernate;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycarehub.api.db.HealthDiaryDao;
import org.openmrs.module.mycarehub.model.HealthDiary;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

public class HibernateHealthDiaryDaoTest extends BaseModuleContextSensitiveTest {
  private HealthDiaryDao healthDiaryDao;

  protected static final String INITIAL_HEALTHDIARY_XML_FILENAME =
      "org.openmrs.module.mycarehub/api/include/HealthDiary_Initial.xml";

  @Autowired public DbSessionFactory sessionFactory;

  @Override
  public Boolean useInMemoryDatabase() {
    // Defaults to true. If you want to use your own database specified in your runtime properties
    // return false instead, otherwise return super.useInMemoryDatabase();.
    return super.useInMemoryDatabase();
  }

  @Before
  public void setup() throws Exception {
    healthDiaryDao = new HibernateHealthDiaryDao(sessionFactory);

    executeDataSet(INITIAL_HEALTHDIARY_XML_FILENAME);
  }

  @Test
  public void saveHealthDiaries_shouldReturnOne() {
    HealthDiary healthDiary = new HealthDiary();
    healthDiary.setId(234);
    healthDiary.setCccNumber("123456789");
    healthDiary.setClientContact("254112244");
    healthDiary.setMood("Happy");
    healthDiary.setClientName("happy");

    List<HealthDiary> healthDiaries = new ArrayList<HealthDiary>();
    healthDiaries.add(healthDiary);

    List<HealthDiary> results = healthDiaryDao.saveHealthDiaries(healthDiaries);
    assertEquals(1, results.size());
  }

  @Test
  public void countHealthDiaries_shouldReturnOne() {
    Number results = healthDiaryDao.countHealthDiaries("VERY SAD");
    assertEquals(1L, results);
  }

  @Test
  public void getPagedHealthDiaries() {
    List<HealthDiary> healthDiaryList = healthDiaryDao.getPagedHealthDiaries("VERY SAD", 1, 10);
    assertEquals(1, healthDiaryList.size());
  }

  @Test
  public void getPagedHealthDiaries_shouldReturnZeroEntries() {
    List<HealthDiary> healthDiaryList = healthDiaryDao.getPagedHealthDiaries("HELLO", 1, 10);
    assertEquals(0, healthDiaryList.size());
  }
}
