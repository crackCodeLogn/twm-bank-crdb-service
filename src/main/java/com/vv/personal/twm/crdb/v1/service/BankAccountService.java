package com.vv.personal.twm.crdb.v1.service;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import java.util.Optional;

/**
 * @author Vivek
 * @since 2024-12-23
 */
public interface BankAccountService extends BackUpAndRestore {

  boolean doesAccountExist(String id);

  boolean addBankAccount(BankProto.BankAccount bankAccount);

  Optional<BankProto.BankAccount> getBankAccount(String id);

  Optional<BankProto.BankAccounts> getAllBankAccounts();

  Optional<BankProto.BankAccounts> getAllBankAccountsByMatchingName(String name);

  Optional<BankProto.BankAccounts> getAllBankAccountsByMatchingIfsc(String ifsc);

  boolean updateBankAccountBalance(String id, double amount);

  Double getBankAccountBalance(String id);

  boolean deleteBankAccount(String id);
}
