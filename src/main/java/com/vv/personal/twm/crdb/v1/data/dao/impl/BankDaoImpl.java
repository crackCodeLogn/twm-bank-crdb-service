package com.vv.personal.twm.crdb.v1.data.dao.impl;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.data.dao.BankDao;
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
public class BankDaoImpl implements BankDao {
  private final BankRepository bankRepository;

  @Override
  public Optional<String> getExternalId(String ifsc) {
    return bankRepository.findById(ifsc).map(BankEntity::getExternalId);
  }

  @Override
  public Optional<String> getId(String externalId) {
    return bankRepository.findByExternalId(externalId).map(BankEntity::getIfsc);
  }

  @Override
  public boolean doesBankExist(String ifsc) {
    return getBank(ifsc).isPresent();
  }

  @Override
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

  @Override
  public Optional<BankProto.Bank> getBank(String ifsc) {
    Optional<BankEntity> bankAccountOptional = bankRepository.findById(ifsc);
    return bankAccountOptional.map(this::generateBank);
  }

  @Override
  public Optional<BankProto.BankList> getAllBanks() {
    return Optional.of(
        BankProto.BankList.newBuilder()
            .addAllBanks(
                bankRepository.findAll().stream()
                    .map(this::generateBank)
                    .collect(Collectors.toList()))
            .build());
  }

  @Override
  public Optional<BankProto.BankList> getAllBanksByMatchingName(String name) {
    return Optional.of(
        BankProto.BankList.newBuilder()
            .addAllBanks(
                bankRepository.getAllByMatchingName(name).stream()
                    .map(this::generateBank)
                    .collect(Collectors.toList()))
            .build());
  }

  @Override
  public Optional<BankProto.BankList> getAllBanksByMatchingType(String type) {
    return Optional.of(
        BankProto.BankList.newBuilder()
            .addAllBanks(
                bankRepository.getAllByMatchingType(type).stream()
                    .map(this::generateBank)
                    .collect(Collectors.toList()))
            .build());
  }

  @Override
  public Optional<BankProto.BankList> getAllBankByMatchingIfsc(String ifsc) {
    return Optional.of(
        BankProto.BankList.newBuilder()
            .addAllBanks(
                bankRepository.getAllByMatchingIfsc(ifsc).stream()
                    .map(this::generateBank)
                    .collect(Collectors.toList()))
            .build());
  }

  @Override
  public Optional<BankProto.BankList> getAllBanksByCountryCode(String countryCode) {
    return Optional.of(
        BankProto.BankList.newBuilder()
            .addAllBanks(
                bankRepository.getAllByCountryCode(countryCode).stream()
                    .map(this::generateBank)
                    .toList())
            .build());
  }

  @Override
  public boolean deleteBank(String ifsc) {
    try {
      bankRepository.deleteById(ifsc);
    } catch (Exception e) {
      log.error("Error on deleting bank of ifsc: {}. ", ifsc, e);
      return false;
    }
    return true;
  }

  /**
   * Doing the sin of shipping out the entities in list outside the DAO. Required for pure backup
   * scenario. The proto obj doesn't contain audit column like cre_ts;
   */
  @Override
  public List<BankEntity> getBanksInEntityFormat() {
    return bankRepository.findAll();
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
