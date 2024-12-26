package com.vv.personal.twm.crdb.v1.data.dao.impl;

import static com.vv.personal.twm.artifactory.generated.bank.BankProto.CurrencyCode.*;
import static org.junit.jupiter.api.Assertions.*;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.data.dao.BankAccountDao;
import com.vv.personal.twm.crdb.v1.data.repository.BankAccountRepository;
import com.vv.personal.twm.crdb.v1.service.BankService;
import com.vv.personal.twm.crdb.v1.util.BankHelperUtil;
import java.time.Instant;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Vivek
 * @since 2024-12-24
 */
@SpringBootTest
class BankServiceAccountDaoImplIntegrationTest {

  @Autowired private BankAccountRepository bankAccountRepository;
  @Autowired private BankService bankService;

  private BankAccountDao bankAccountDao;

  @BeforeEach
  public void setUp() {
    bankAccountDao = new BankAccountDaoImpl(bankAccountRepository);
  }

  @Test
  public void testAllOperations() {
    UUID bankAccountIdUuid1 = UUID.randomUUID();
    String bankAccountId1 = BankHelperUtil.getUUIDString(bankAccountIdUuid1);
    Instant preLocalTime = Instant.now();

    BankProto.BankAccount bankAccount1 =
        BankProto.BankAccount.newBuilder()
            .setId(bankAccountId1)
            .setBank(BankProto.Bank.newBuilder().setIFSC("TESTB-001"))
            .setNumber("123456789012345")
            .setName("Test Bank Account 001")
            .setTransitNumber("999")
            .setInstitutionNumber("9999")
            .setBalance(91.19)
            .setBankAccountType(BankProto.BankAccountType.CHQ)
            .setOverdraftBalance(100)
            .setInterestRate(0.05)
            .setIsActive(true)
            .setNote("Test Note")
            .setCcy(CAD)
            .build();
    bankAccountDao
        .getAllBankAccountsByMatchingIfsc("TESTB-001")
        .ifPresent(
            accounts -> {
              accounts
                  .getAccountsList()
                  .forEach(account -> bankAccountDao.deleteBankAccount(account.getId()));
            });
    bankAccountDao.deleteBankAccount(bankAccountId1);
    assertTrue(bankService.deleteBank("TESTB-001"));
    assertFalse(bankAccountDao.doesAccountExist(bankAccountId1));
    // this is supposed to fail because bank table does not contain the ifsc TESTB-001
    assertFalse(bankAccountDao.addBankAccount(bankAccount1));

    // insert test bank ifsc
    BankProto.Bank testBank =
        BankProto.Bank.newBuilder().setIFSC("TESTB-001").setName("Test Bank 001").build();
    assertTrue(bankService.addBank(testBank));

    // this is now supposed to pass
    assertTrue(bankAccountDao.addBankAccount(bankAccount1));
    assertFalse(bankAccountDao.addBankAccount(bankAccount1)); // should fail as pkey already present

    Optional<BankProto.BankAccount> optionalBankAccount1 =
        bankAccountDao.getBankAccount(bankAccountId1);
    assertTrue(optionalBankAccount1.isPresent());
    BankProto.BankAccount bankAccountFromDb = optionalBankAccount1.get();
    System.out.println(bankAccountFromDb);
    Instant createdAt = BankHelperUtil.fromTimestamp(bankAccountFromDb.getCreatedAt());
    assertTrue(preLocalTime.isBefore(createdAt));

    String bankAccountId2 = UUID.randomUUID().toString();
    BankProto.BankAccount bankAccount2 =
        BankProto.BankAccount.newBuilder()
            .setId(bankAccountId2)
            .setBank(BankProto.Bank.newBuilder().setIFSC("TESTB-001"))
            .setNumber("1234567890")
            .setName("Test Bank Account 002")
            .setTransitNumber("998")
            .setInstitutionNumber("9998")
            .setBalance(5.23)
            .setBankAccountType(BankProto.BankAccountType.HISA)
            .setOverdraftBalance(100)
            .setInterestRate(3.0)
            .setIsActive(true)
            .setNote("")
            .setCcy(BankProto.CurrencyCode.INR)
            .build();
    assertTrue(bankAccountDao.addBankAccount(bankAccount2));

    String bankAccountId3 = UUID.randomUUID().toString();
    BankProto.BankAccount bankAccount3 =
        BankProto.BankAccount.newBuilder()
            .setId(bankAccountId3)
            .setBank(BankProto.Bank.newBuilder().setIFSC("TESTB-001"))
            .setNumber("12345678902324")
            .setName("Sample 002")
            .setTransitNumber("998")
            .setInstitutionNumber("9998")
            .setBalance(5.23)
            .setBankAccountType(BankProto.BankAccountType.SAV)
            .setOverdraftBalance(100)
            .setInterestRate(1.5)
            .setIsActive(true)
            .setNote("Test note 3")
            .setCcy(BankProto.CurrencyCode.USD)
            .build();
    assertTrue(bankAccountDao.addBankAccount(bankAccount3));

    // test various get methods
    Optional<BankProto.BankAccounts> bankAccounts = bankAccountDao.getAllBankAccounts();
    assertTrue(bankAccounts.isPresent());
    assertTrue(bankAccounts.get().getAccountsCount() >= 3);

    Optional<BankProto.BankAccounts> bankAccountsList2 =
        bankAccountDao.getAllBankAccountsByMatchingName("Test ");
    assertTrue(bankAccountsList2.isPresent());
    assertEquals(2, bankAccountsList2.get().getAccountsCount());
    Set<String> bankAccs =
        bankAccountsList2.get().getAccountsList().stream()
            .map(BankProto.BankAccount::getId)
            .collect(Collectors.toSet());
    assertTrue(bankAccs.contains(bankAccountId1));
    assertTrue(bankAccs.contains(bankAccountId2));

    Optional<BankProto.BankAccounts> bankAccountsList3 =
        bankAccountDao.getAllBankAccountsByMatchingIfsc("TESTB");
    assertTrue(bankAccountsList3.isPresent());
    assertEquals(3, bankAccountsList3.get().getAccountsCount());

    // get balance
    OptionalDouble optionalDouble = bankAccountDao.getBankAccountBalance(bankAccountId3);
    assertTrue(optionalDouble.isPresent());
    assertEquals(5.23, optionalDouble.getAsDouble());

    // update balances
    assertTrue(bankAccountDao.updateBankAccountBalance(bankAccountId1, 10001.51));
    optionalDouble = bankAccountDao.getBankAccountBalance(bankAccountId1);
    assertTrue(optionalDouble.isPresent());
    assertEquals(10001.51, optionalDouble.getAsDouble());

    // check newly added columns
    BankProto.BankAccount testBankAccount;
    testBankAccount = bankAccountDao.getBankAccount(bankAccountId1).get();
    assertEquals(CAD, testBankAccount.getCcy());
    assertEquals("Test Note", testBankAccount.getNote());

    testBankAccount = bankAccountDao.getBankAccount(bankAccountId2).get();
    assertEquals(INR, testBankAccount.getCcy());
    assertTrue(testBankAccount.getNote().isBlank());

    testBankAccount = bankAccountDao.getBankAccount(bankAccountId3).get();
    assertEquals(USD, testBankAccount.getCcy());
    assertEquals("Test note 3", testBankAccount.getNote());

    // test bank account get by ccy - CAD
    Optional<BankProto.BankAccounts> cadBankAccountsOptional =
        bankAccountDao.getAllBankAccountsByCcy(CAD);
    assertTrue(cadBankAccountsOptional.isPresent());
    assertTrue(cadBankAccountsOptional.get().getAccountsCount() >= 1);
    assertTrue(
        cadBankAccountsOptional.get().getAccountsList().stream()
            .anyMatch(bankAccount -> bankAccount.getId().equals(bankAccountId1)));
    // test bank account get by ccy - INR
    Optional<BankProto.BankAccounts> inrBankAccountsOptional =
        bankAccountDao.getAllBankAccountsByCcy(INR);
    assertTrue(inrBankAccountsOptional.isPresent());
    assertTrue(inrBankAccountsOptional.get().getAccountsCount() >= 1);
    assertTrue(
        inrBankAccountsOptional.get().getAccountsList().stream()
            .anyMatch(bankAccount -> bankAccount.getId().equals(bankAccountId2)));

    // delete bank account
    assertTrue(bankAccountDao.deleteBankAccount(bankAccountId1));
    assertTrue(bankAccountDao.deleteBankAccount(bankAccountId2));
    assertTrue(bankAccountDao.deleteBankAccount(bankAccountId3));

    // clean up
    assertTrue(bankService.deleteBank("TESTB-001"));
  }
}
