package com.vv.personal.twm.crdb.v1.data.dao;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.data.entity.BankEntity;
import com.vv.personal.twm.crdb.v1.data.repository.BankRepository;
import com.vv.personal.twm.crdb.v1.util.BankHelperUtil;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Vivek
 * @since 15/01/22
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BankDao {
  private final BankRepository bankRepository;

  public boolean addBank(BankProto.Bank bank) {
    Instant currentTime = Instant.now();
    try {
      BankEntity bankEntity = generateBankEntity(bank, currentTime);
      bankRepository.saveAndFlush(bankEntity);
    } catch (Exception e) { // TODO - streamline exception later
      log.error("Error on bank save. ", e);
      return false;
    }
    return true;
  }

  public int addBanks(BankProto.BankList bankList) {
    Instant instant = Instant.now();
    try {
      List<BankEntity> bankEntities =
          bankList.getBanksList().stream()
              .map(bank -> generateBankEntity(bank, instant))
              .collect(Collectors.toList());
      return bankRepository.saveAll(bankEntities).size();
    } catch (Exception e) { // TODO - streamline exception later
      log.error("Error on bulk bank save. ", e);
      return 0;
    }
  }

  public BankProto.Bank getBank(String ifsc) {
    Optional<BankEntity> bankAccountOptional = bankRepository.findById(ifsc);
    if (bankAccountOptional.isPresent()) {
      return generateBank(bankAccountOptional.get());
    }
    return BankProto.Bank.newBuilder().build();
  }

  public List<BankProto.Bank> getBanks() {
    return bankRepository.findAll().stream().map(this::generateBank).collect(Collectors.toList());
  }

  /**
   * Doing the sin of shipping out the entities in list outside the DAO. Required for pure backup
   * scenario. The proto obj doesn't contain audit column like cre_ts;
   */
  public List<BankEntity> getBanksInEntityFormat() {
    return bankRepository.findAll();
  }

  public List<BankProto.Bank> getAllByName(String name) {
    return bankRepository.getAllByMatchingName(name).stream()
        .map(this::generateBank)
        .collect(Collectors.toList());
  }

  public List<BankProto.Bank> getAllByType(String type) {
    return bankRepository.getAllByMatchingType(type).stream()
        .map(this::generateBank)
        .collect(Collectors.toList());
  }

  public List<BankProto.Bank> getAllByIfsc(String ifsc) {
    return bankRepository.getAllByMatchingIfsc(ifsc).stream()
        .map(this::generateBank)
        .collect(Collectors.toList());
  }

  public List<BankProto.Bank> getAllByCountryCode(String countryCode) {
    return bankRepository.getAllByCountryCode(countryCode).stream()
        .map(this::generateBank)
        .toList();
  }

  public boolean deleteBank(String ifsc) {
    try {
      bankRepository.deleteById(ifsc);
    } catch (Exception e) {
      log.error("Error on deleting bank of ifsc: {}. ", ifsc, e);
      return false;
    }
    return true;
  }

  public boolean deleteBanks() {
    try {
      bankRepository.deleteAll();
    } catch (Exception e) {
      log.error("Error on deleting all banks. ", e);
      return false;
    }
    return true;
  }

  BankProto.Bank generateBank(BankEntity bank) {
    return BankProto.Bank.newBuilder()
        .setName(bank.getBankName())
        .setIFSC(bank.getIfsc())
        .setExternalId(bank.getExternalId())
        .setBankType(BankProto.BankType.valueOf(bank.getBankType()))
        .setContactNumber(bank.getContactNumber())
        .setCountryCode(bank.getCountryCode())
        .setIsActive(bank.getIsActive())
        .build();
  }

  BankEntity generateBankEntity(BankProto.Bank bank, Instant instant) {
    return BankEntity.builder()
        .bankName(bank.getName())
        .bankType(bank.getBankType().name())
        .ifsc(bank.getIFSC())
        .externalId(BankHelperUtil.generateSha512Hash(bank.getIFSC()))
        .contactNumber(bank.getContactNumber())
        .isActive(bank.getIsActive())
        .countryCode(bank.getCountryCode())
        .createdTimestamp(instant)
        .build();
  }
}
