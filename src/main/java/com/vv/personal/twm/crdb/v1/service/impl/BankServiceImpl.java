package com.vv.personal.twm.crdb.v1.service.impl;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.data.dao.BankDao;
import com.vv.personal.twm.crdb.v1.service.BankService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author Vivek
 * @since 09/03/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
  private final BankDao bankDao;

  @Override
  public boolean addBank(BankProto.Bank bank) {
    boolean addResult = bankDao.addBank(bank);
    log.info("Bank add result: {}", addResult);
    return addResult;
  }

  @Override
  public Optional<BankProto.Bank> getBank(String ifsc) {
    return bankDao.getBank(ifsc);
  }

  @Override
  public Optional<BankProto.BankList> getAllBanks() {
    return bankDao.getAllBanks();
  }

  @Override
  public boolean deleteBank(String ifsc) {
    boolean delResult = bankDao.deleteBank(ifsc);
    log.info("Bank del result for {}: {}", ifsc, delResult);
    return delResult;
  }

  @Override
  public Optional<BankProto.BankList> getAllByName(String name) {
    Optional<BankProto.BankList> result = bankDao.getAllBanksByMatchingName(name);
    if (result.isPresent())
      log.info("Found {} banks matching {}", result.get().getBanksCount(), name);
    else log.info("No banks matching name {}", name);
    return result;
  }

  @Override
  public Optional<BankProto.BankList> getAllByType(String type) {
    Optional<BankProto.BankList> result = bankDao.getAllBanksByMatchingType(type);
    if (result.isPresent())
      log.info("Found {} banks matching {}", result.get().getBanksCount(), type);
    else log.info("No banks matching type {}", type);
    return result;
  }

  @Override
  public Optional<BankProto.BankList> getAllByIfsc(String ifsc) {
    Optional<BankProto.BankList> result = bankDao.getAllBankByMatchingIfsc(ifsc);
    if (result.isPresent())
      log.info("Found {} banks matching {}", result.get().getBanksCount(), ifsc);
    else log.info("No banks matching ifsc {}", ifsc);
    return result;
  }

  @Override
  public Optional<BankProto.BankList> getAllByCountryCode(String countryCode) {
    Optional<BankProto.BankList> result = bankDao.getAllBanksByCountryCode(countryCode);
    if (result.isPresent())
      log.info("Found {} banks matching {}", result.get().getBanksCount(), countryCode);
    else log.info("No banks matching countryCode {}", countryCode);
    return result;
  }

  @Override
  public String extractDataInDelimitedFormat(String delimiter) {
    StringBuilder dataLines = new StringBuilder();
    bankDao
        .getBanksInEntityFormat()
        .forEach(
            bankEntity ->
                dataLines
                    .append(
                        StringUtils.joinWith(
                            delimiter,
                            bankEntity.getBankName(),
                            bankEntity.getIfsc(),
                            bankEntity.getContactNumber(),
                            bankEntity.getBankType(),
                            bankEntity.getIsActive(),
                            bankEntity.getCreatedTimestamp()))
                    .append("\n"));
    return dataLines.toString();
  }
}
