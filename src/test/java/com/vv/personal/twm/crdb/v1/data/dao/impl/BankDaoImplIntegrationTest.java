package com.vv.personal.twm.crdb.v1.data.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.data.dao.BankDao;
import com.vv.personal.twm.crdb.v1.data.repository.BankRepository;
import com.vv.personal.twm.crdb.v1.util.BankHelperUtil;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Vivek
 * @since 2025-02-17
 */
@SpringBootTest
class BankDaoImplIntegrationTest {
  @Autowired private BankRepository bankRepository;

  private BankDao bankDao;

  @BeforeEach
  public void setUp() {
    bankDao = new BankDaoImpl(bankRepository);
  }

  @Test
  void testGetExternalIdAndGetIdAndAddBankAndDeleteBankAndDoesBankExistAndGetBank() {
    bankDao.deleteBank("TESTB-010");
    assertFalse(bankDao.doesBankExist("TESTB-010"));
    assertFalse(bankDao.getExternalId("TESTB-010").isPresent());

    BankProto.Bank testBank = getTestBank();
    String externalId = testBank.getExternalId();

    assertTrue(bankDao.addBank(testBank));
    assertTrue(bankDao.doesBankExist("TESTB-010"));

    Optional<String> id = bankDao.getExternalId("TESTB-010");
    assertTrue(id.isPresent());
    assertEquals(externalId, id.get());

    id = bankDao.getId(externalId);
    assertTrue(id.isPresent());
    assertEquals("TESTB-010", id.get());

    Optional<BankProto.Bank> result = bankDao.getBank("TESTB-010");
    assertTrue(result.isPresent());
    assertEquals(externalId, result.get().getExternalId());
    assertEquals("Test bank", result.get().getName());

    assertTrue(bankDao.deleteBank("TESTB-010"));
  }

  private BankProto.Bank getTestBank() {
    return BankProto.Bank.newBuilder()
        .setIFSC("TESTB-010")
        .setName("Test bank")
        .setExternalId(BankHelperUtil.generateSha512Hash("TESTB-010")) // sin but it is what it is
        .build();
  }
}
