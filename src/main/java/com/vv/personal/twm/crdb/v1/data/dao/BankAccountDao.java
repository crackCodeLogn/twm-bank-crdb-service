package com.vv.personal.twm.crdb.v1.data.dao;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.data.entity.BankAccountEntity;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * @author Vivek
 * @since 2024-12-23
 */
public interface BankAccountDao {

  boolean doesAccountExist(String id);

  boolean addBankAccount(BankProto.BankAccount bankAccount);

  Optional<BankProto.BankAccount> getBankAccount(String id);

  Optional<BankProto.BankAccounts> getAllBankAccounts();

  Optional<BankProto.BankAccounts> getAllBankAccountsByMatchingName(String name);

  Optional<BankProto.BankAccounts> getAllBankAccountsByMatchingIfsc(String ifsc);

  boolean updateBankAccountBalance(String id, double amount);

  OptionalDouble getBankAccountBalance(String id);

  boolean deleteBankAccount(String id);

  List<BankAccountEntity> getBankAccountsInEntityFormat();
}
