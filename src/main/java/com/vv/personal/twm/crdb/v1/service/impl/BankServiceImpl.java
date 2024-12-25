package com.vv.personal.twm.crdb.v1.service.impl;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.data.dao.BankDao;
import com.vv.personal.twm.crdb.v1.service.BankService;
import java.util.List;
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
  public int addBanks(BankProto.BankList bankList) {
    int banksAdded = bankDao.addBanks(bankList);
    log.info("Banks added: {}", banksAdded);
    return banksAdded;
  }

  @Override
  public BankProto.Bank getBank(String ifsc) {
    BankProto.Bank bank = bankDao.getBank(ifsc);
    log.info("Found Bank for ifsc: {}", bank);
    return bank;
  }

  @Override
  public BankProto.BankList getBanks() {
    BankProto.BankList bankList =
        BankProto.BankList.newBuilder().addAllBanks(bankDao.getBanks()).build();
    log.info("Retrieved {} banks from db", bankList.getBanksCount());
    return bankList;
  }

  @Override
  public boolean deleteBank(String ifsc) {
    boolean delResult = bankDao.deleteBank(ifsc);
    log.info("Bank del result for {}: {}", ifsc, delResult);
    return delResult;
  }

  @Override
  public boolean deleteBanks() {
    boolean delResult = bankDao.deleteBanks();
    log.info("All Banks del result: {}", delResult);
    return delResult;
  }

  @Override
  public List<BankProto.Bank> getAllByName(String name) {
    List<BankProto.Bank> result = bankDao.getAllByName(name);
    log.info("Found {} banks matching {}", result.size(), name);
    return result;
  }

  @Override
  public List<BankProto.Bank> getAllByType(String type) {
    List<BankProto.Bank> result = bankDao.getAllByType(type);
    log.info("Found {} banks matching {}", result.size(), type);
    return result;
  }

  @Override
  public List<BankProto.Bank> getAllByIfsc(String ifsc) {
    List<BankProto.Bank> result = bankDao.getAllByIfsc(ifsc);
    log.info("Found {} banks matching {}", result.size(), ifsc);
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
