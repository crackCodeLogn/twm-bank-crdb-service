package com.vv.personal.twm.crdb.v1.data.dao.impl;

import static com.vv.personal.twm.crdb.v1.util.BankHelperUtil.*;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.data.dao.BankAccountDao;
import com.vv.personal.twm.crdb.v1.data.entity.BankAccountEntity;
import com.vv.personal.twm.crdb.v1.data.repository.BankAccountRepository;
import com.vv.personal.twm.crdb.v1.util.BankHelperUtil;
import java.time.Instant;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Vivek
 * @since 2024-12-23
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BankAccountDaoImpl implements BankAccountDao {
  private final BankAccountRepository bankAccountRepository;

  @Override
  public String getExternalId(String uuid) {
    Optional<BankAccountEntity> bankAccountEntity =
        bankAccountRepository.findById(UUID.fromString(uuid));
    return bankAccountEntity.map(BankAccountEntity::getExternalId).orElse("");
  }

  @Override
  public Optional<UUID> getId(String externalId) {
    return getBankAccount(externalId).map(v -> UUID.fromString(v.getId()));
  }

  @Override
  public boolean doesAccountExist(String externalId) {
    try {
      return bankAccountRepository.existsByExternalId(externalId);
    } catch (Exception e) {
      log.error("Failed to check bank account existence with external id: {}", externalId, e);
    }
    return false;
  }

  @Override
  public boolean addBankAccount(BankProto.BankAccount bankAccount) {
    if (doesAccountExist(bankAccount.getExternalId())) {
      log.warn("Account already exists with id {}", bankAccount.getId());
      return false;
    }

    Instant currentTime = Instant.now();
    try {
      BankAccountEntity bankAccountEntity = generateBankAccountEntity(bankAccount, currentTime);
      bankAccountRepository.saveAndFlush(bankAccountEntity);
      return true;
    } catch (Exception e) {
      log.error("Failed to add bank account with id: {}", bankAccount.getId(), e);
    }
    return false;
  }

  @Override
  public Optional<BankProto.BankAccount> getBankAccount(String externalId) {
    try {
      Optional<BankAccountEntity> bankAccountOptional =
          bankAccountRepository.findByExternalId(externalId);
      if (bankAccountOptional.isPresent()) {
        return Optional.of(generateBankAccount(bankAccountOptional.get()));
      }
    } catch (Exception e) {
      log.error("Failed to get bank account with id: {}", externalId, e);
    }
    return Optional.empty();
  }

  @Override
  public Optional<BankProto.BankAccounts> getAllBankAccounts() {
    try {
      List<BankProto.BankAccount> bankAccounts =
          bankAccountRepository.findAll().stream().map(this::generateBankAccount).toList();

      return Optional.of(BankProto.BankAccounts.newBuilder().addAllAccounts(bankAccounts).build());
    } catch (Exception e) {
      log.error("Failed to get all bank accounts", e);
    }
    return Optional.empty();
  }

  @Override
  public Optional<BankProto.BankAccounts> getAllBankAccountsByMatchingName(String name) {
    try {
      List<BankProto.BankAccount> bankAccounts =
          bankAccountRepository.getAllByMatchingName(name).stream()
              .map(this::generateBankAccount)
              .toList();

      return Optional.of(BankProto.BankAccounts.newBuilder().addAllAccounts(bankAccounts).build());
    } catch (Exception e) {
      log.error("Failed to get all bank accounts matching name: {}", name, e);
    }
    return Optional.empty();
  }

  @Override
  public Optional<BankProto.BankAccounts> getAllBankAccountsByMatchingIfsc(String ifsc) {
    try {
      List<BankProto.BankAccount> bankAccounts =
          bankAccountRepository.getAllByMatchingIfsc(ifsc).stream()
              .map(this::generateBankAccount)
              .toList();

      return Optional.of(BankProto.BankAccounts.newBuilder().addAllAccounts(bankAccounts).build());
    } catch (Exception e) {
      log.error("Failed to get all bank accounts matching ifsc: {}", ifsc, e);
    }
    return Optional.empty();
  }

  @Override
  public Optional<BankProto.BankAccounts> getAllBankAccountsByCcy(BankProto.CurrencyCode ccy) {
    try {
      return Optional.of(
          BankProto.BankAccounts.newBuilder()
              .addAllAccounts(
                  bankAccountRepository.getAllByCurrencyCode(ccy.name()).stream()
                      .map(this::generateBankAccount)
                      .toList())
              .build());
    } catch (Exception e) {
      log.error("Failed to get bank accounts by ccy: {}", ccy, e);
    }
    return Optional.empty();
  }

  @Override
  public boolean updateBankAccountBalance(String externalId, double amount) {
    try {
      Optional<BankAccountEntity> bankAccountEntityOptional =
          bankAccountRepository.findByExternalId(externalId);
      if (bankAccountEntityOptional.isPresent()) {
        BankAccountEntity bankAccountEntity = bankAccountEntityOptional.get();
        bankAccountEntity.setBalance(amount);
        bankAccountEntity.setLastUpdatedTimestamp(Instant.now());
        bankAccountRepository.saveAndFlush(bankAccountEntity);
        return true;
      } else {
        log.error("No bank account found with id: {} to update balance to {}", externalId, amount);
      }
    } catch (Exception e) {
      log.error("Failed to update bank account balance with id: {}", externalId, e);
    }
    return false;
  }

  @Override
  public OptionalDouble getBankAccountBalance(String externalId) {
    return getBankAccount(externalId)
        .map(bankAccount -> OptionalDouble.of(bankAccount.getBalance()))
        .orElseGet(OptionalDouble::empty);
  }

  @Override
  public boolean deleteBankAccount(String externalId) {
    try {
      Optional<UUID> id = getId(externalId);
      if (id.isEmpty()) {
        log.warn("Bank account with external id {} not found", externalId);
        return false;
      }
      bankAccountRepository.deleteById(id.get());
      return true;
    } catch (Exception e) {
      log.error("Failed to delete bank account with id: {}", externalId, e);
    }
    return false;
  }

  @Override
  public List<BankAccountEntity> getBankAccountsInEntityFormat() {
    return bankAccountRepository.findAll();
  }

  private BankAccountEntity generateBankAccountEntity(
      BankProto.BankAccount bankAccount, Instant currentTime) {
    return BankAccountEntity.builder()
        .id(getUUID(bankAccount.getId()))
        .externalId(BankHelperUtil.generateSha512Hash(bankAccount.getId()))
        .bankIfsc(bankAccount.getBank().getIFSC())
        .accountNumber(bankAccount.getNumber())
        .name(bankAccount.getName())
        .transitNumber(bankAccount.getTransitNumber())
        .institutionNumber(bankAccount.getInstitutionNumber())
        .balance(bankAccount.getBalance())
        .bankAccountTypes(getStringBankAccountTypes(bankAccount.getBankAccountTypesList()))
        //                .metaData(bankAccount.getMetaDataMap().toString()) // todo - use better
        // map -> string converter
        .overdraftBalance(bankAccount.getOverdraftBalance())
        .interestRate(bankAccount.getInterestRate())
        .isActive(bankAccount.getIsActive())
        .note(bankAccount.getNote())
        .currencyCode(bankAccount.getCcy().name())
        .createdTimestamp(currentTime)
        .lastUpdatedTimestamp(currentTime)
        .build();
  }

  private BankProto.BankAccount generateBankAccount(BankAccountEntity bankAccountEntity) {
    return BankProto.BankAccount.newBuilder()
        .setBank(BankProto.Bank.newBuilder().setIFSC(bankAccountEntity.getBankIfsc()))
        .setId(getUUIDString(bankAccountEntity.getId()))
        .setExternalId(bankAccountEntity.getExternalId())
        .setNumber(bankAccountEntity.getAccountNumber())
        .setName(bankAccountEntity.getName())
        .setTransitNumber(bankAccountEntity.getTransitNumber())
        .setInstitutionNumber(bankAccountEntity.getInstitutionNumber())
        .setBalance(bankAccountEntity.getBalance())
        .addAllBankAccountTypes(getListBankAccountTypes(bankAccountEntity.getBankAccountTypes()))
        .setOverdraftBalance(bankAccountEntity.getOverdraftBalance())
        .setInterestRate(bankAccountEntity.getInterestRate())
        .setIsActive(bankAccountEntity.getIsActive())
        .setNote(bankAccountEntity.getNote())
        .setCcy(BankProto.CurrencyCode.valueOf(bankAccountEntity.getCurrencyCode()))
        .setCreatedAt(fromInstant(bankAccountEntity.getCreatedTimestamp()))
        .setLastUpdatedAt(fromInstant(bankAccountEntity.getLastUpdatedTimestamp()))
        .build();
  }
}
