package com.vv.personal.twm.crdb.v1.data.dao;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.data.entity.BankAccountEntity;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;

/**
 * @author Vivek
 * @since 2024-12-23
 */
public interface BankAccountDao {

  String getExternalId(String uuid);

  Optional<UUID> getId(String externalId);

  boolean doesAccountExist(String externalId);

  boolean addBankAccount(BankProto.BankAccount bankAccount);

  Optional<BankProto.BankAccount> getBankAccount(String externalId);

  Optional<BankProto.BankAccounts> getAllBankAccounts();

  Optional<BankProto.BankAccounts> getAllBankAccountsByMatchingName(String name);

  Optional<BankProto.BankAccounts> getAllBankAccountsByMatchingIfsc(String ifsc);

  Optional<BankProto.BankAccounts> getAllBankAccountsByCcy(BankProto.CurrencyCode ccy);

  boolean updateBankAccountBalance(String externalId, double amount);

  OptionalDouble getBankAccountBalance(String externalId);

  boolean deleteBankAccount(String externalId);

  List<BankAccountEntity> getBankAccountsInEntityFormat();
}
