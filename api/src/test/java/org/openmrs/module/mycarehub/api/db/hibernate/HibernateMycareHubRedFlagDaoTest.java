package org.openmrs.module.mycarehub.api.db.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycarehub.api.db.MyCareHubRedFlagDao;
import org.openmrs.module.mycarehub.model.RedFlags;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

public class HibernateMycareHubRedFlagDaoTest extends BaseModuleContextSensitiveTest {
  private MyCareHubRedFlagDao myCareHubRedFlagDao;

  @Autowired DbSessionFactory sessionFactory;

  protected static final String MCH_RED_FLAGS_XML_FILENAME =
      "org.openmrs.module.mycarehub/api/include/MycareHubRedFlags.xml";

  @Override
  public Boolean useInMemoryDatabase() {
    // Defaults to true. If you want to use your own database specified in your runtime properties
    // return false instead, otherwise return super.useInMemoryDatabase();.
    return super.useInMemoryDatabase();
  }

  @Before
  public void setUp() throws Exception {
    executeDataSet(MCH_RED_FLAGS_XML_FILENAME);

    myCareHubRedFlagDao = new HibernateMyCareHubRedFlagDao(sessionFactory);
  }

  @Test
  public void getAllRedFlagRequests() {
    List<RedFlags> redFlags = myCareHubRedFlagDao.getAllRedFlagRequests();
    assertEquals(1, redFlags.size());
  }

  @Test
  public void getRedFlagByUuid_shouldReturnOneRedFlag() {
    RedFlags redFlags =
        myCareHubRedFlagDao.getRedFlagByUuid("cf25712e-f193-48d5-a85a-5fd8d0244c71");
    assertEquals("GBV", redFlags.getRequest());
  }

  @Test
  public void getRedFlagByUuid_shouldReturnNull() {
    RedFlags redFlags = myCareHubRedFlagDao.getRedFlagByUuid("cf25712e");
    assertNull(redFlags);
  }

  @Test
  public void getAllRedFlagRequestsByLastSyncDate_shouldReturnZero() {
    List<RedFlags> redFlags = myCareHubRedFlagDao.getAllRedFlagRequestsByLastSyncDate(new Date());
    assertEquals(0, redFlags.size());
  }

  @Test
  public void getPagedRedFlagsByRequestType_shouldReturnOne() {
    List<RedFlags> redFlags =
        myCareHubRedFlagDao.getPagedRedFlagsByRequestType("MtuKazi", "type", 1, 10);
    assertEquals(1, redFlags.size());
  }

  @Test
  public void getPagedRedFlagsByRequestType_shouldReturnZeroRedFlags() {
    List<RedFlags> redFlags =
        myCareHubRedFlagDao.getPagedRedFlagsByRequestType("pooh", "pooh", 1, 10);
    assertEquals(0, redFlags.size());
  }

  @Test
  public void saveAListOfRedFlagRequests() {
    RedFlags redFlag = new RedFlags();
    redFlag.setId(1);
    redFlag.setCccNumber("12345");
    redFlag.setRequest("test");
    redFlag.setClientContact("12345");

    List<RedFlags> redFlagsList = new ArrayList<RedFlags>();
    redFlagsList.add(redFlag);

    List<RedFlags> redFlags = myCareHubRedFlagDao.saveRedFlagRequests(redFlagsList);
    assertEquals(1, redFlags.size());
  }

  @Test
  public void saveOneRedFlagRequests() {
    RedFlags redFlag = new RedFlags();
    redFlag.setId(1);
    redFlag.setCccNumber("12345");
    redFlag.setRequest("test");
    redFlag.setClientContact("12345");

    RedFlags redFlags = myCareHubRedFlagDao.saveRedFlagRequests(redFlag);
    assertEquals("12345", redFlags.getCccNumber());
  }

  @Test
  public void getRedFlagRequestByMycarehubId_shouldReturnNone() {
    RedFlags redFlags = myCareHubRedFlagDao.getRedFlagRequestByMycarehubId("111");
    assertNull(redFlags);
  }

  @Test
  public void getRedFlagRequestByMycarehubId_shouldReturnOne() {
    RedFlags redFlags = myCareHubRedFlagDao.getRedFlagRequestByMycarehubId("1");
    assertEquals("1", redFlags.getMycarehubId());
  }

  @Test
  public void countRedFlagsByType_shouldReturnZero() {
    Number redFlagsNumber = myCareHubRedFlagDao.countRedFlagsByType("padmore", "padmore");
    assertEquals(0L, redFlagsNumber);
  }

  @Test
  public void countRedFlagsByType_shouldReturnOne() {
    Number redFlagsNumber = myCareHubRedFlagDao.countRedFlagsByType("MtuKazi", "type");
    assertEquals(1L, redFlagsNumber);
  }
}
