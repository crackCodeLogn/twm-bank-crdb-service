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
  void testGetExternalIdAndGetIdAndDoesBankExistAndAddBankAndGetBankAndDeleteBank() {
    bankDao.deleteBank("TESTB-010");
    assertFalse(bankDao.doesBankExist("TESTB-010"));
    assertFalse(bankDao.getExternalId("TESTB-010").isPresent());

    BankProto.Bank testBank =
        getTestBank("TESTB-010", "Test bank", "CA", BankProto.BankType.PRIVATE);
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

  @Test
  void testGetAllBanksAndGetAllBanksByMatchingCombinations() {
    BankProto.BankList bankList = getTestBanks();
    for (BankProto.Bank bank : bankList.getBanksList()) {
      assertTrue(bankDao.deleteBank(bank.getIFSC()));
      assertTrue(bankDao.addBank(bank));
    }

    Optional<BankProto.BankList> resList = bankDao.getAllBanks();
    assertTrue(resList.isPresent());
    assertTrue(resList.get().getBanksCount() >= 4);

    resList = bankDao.getAllBanksByMatchingName("Test bank");
    assertTrue(resList.isPresent());
    assertEquals(2, resList.get().getBanksCount());

    resList = bankDao.getAllBankByMatchingIfsc("TESTB-01.*");
    assertTrue(resList.isPresent());
    assertEquals(3, resList.get().getBanksCount());

    resList = bankDao.getAllBanksByMatchingType("PRIVATE");
    assertTrue(resList.isPresent());
    assertTrue(resList.get().getBanksCount() >= 2);

    resList = bankDao.getAllBanksByCountryCode("JP");
    assertTrue(resList.isPresent());
    assertEquals(1, resList.get().getBanksCount());

    bankList.getBanksList().forEach(bank -> assertTrue(bankDao.deleteBank(bank.getIFSC())));
  }

  private BankProto.BankList getTestBanks() {
    return BankProto.BankList.newBuilder()
        .addBanks(getTestBank("TESTB-010", "Test bank", "CA", BankProto.BankType.GOVT))
        .addBanks(getTestBank("TESTB-011", "Test bank 011", "CA", BankProto.BankType.PRIVATE))
        .addBanks(getTestBank("TESTB-012", "BANK 012", "CA", BankProto.BankType.GOVT))
        .addBanks(getTestBank("TESTB-023", "Setup", "JP", BankProto.BankType.PRIVATE))
        .build();
  }

  private BankProto.Bank getTestBank(
      String ifsc, String name, String countryCode, BankProto.BankType type) {
    return BankProto.Bank.newBuilder()
        .setIFSC(ifsc)
        .setName(name)
        .setCountryCode(countryCode)
        .setBankType(type)
        .setExternalId(BankHelperUtil.generateSha512Hash(ifsc))
        .build();
  }
}
