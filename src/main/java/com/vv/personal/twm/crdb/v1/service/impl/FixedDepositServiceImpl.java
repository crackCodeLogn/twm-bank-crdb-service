package com.vv.personal.twm.crdb.v1.service.impl;

import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import com.vv.personal.twm.crdb.v1.data.dao.BankFixedDepositDao;
import com.vv.personal.twm.crdb.v1.service.FixedDepositService;
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
public class FixedDepositServiceImpl implements FixedDepositService {
  private final BankFixedDepositDao fixedDepositDao;

  @Override
  public boolean addFixedDeposit(FixedDepositProto.FixedDeposit fixedDeposit) {
    boolean addResult = fixedDepositDao.addFixedDeposit(fixedDeposit);
    log.info("FD add result: {}", addResult);
    return addResult;
  }

  @Override
  public int addFixedDeposits(FixedDepositProto.FixedDepositList fixedDepositList) {
    int fdsAdded = fixedDepositDao.addFixedDeposits(fixedDepositList);
    log.info("FDs added: {}", fdsAdded);
    return fdsAdded;
  }

  @Override
  public FixedDepositProto.FixedDeposit getFixedDeposit(String fdNumber) {
    FixedDepositProto.FixedDeposit fixedDeposit = fixedDepositDao.getFixedDeposit(fdNumber);
    log.info("Found FD for number: {}", fixedDeposit);
    return fixedDeposit;
  }

  @Override
  public FixedDepositProto.FixedDepositList getFixedDeposits() {
    FixedDepositProto.FixedDepositList fixedDepositList =
        FixedDepositProto.FixedDepositList.newBuilder()
            .addAllFixedDeposit(fixedDepositDao.getFixedDeposits())
            .build();
    log.info("Retrieved {} FDs from db", fixedDepositList.getFixedDepositCount());
    return fixedDepositList;
  }

  @Override
  public boolean deleteFixedDeposit(String fdNumber) {
    boolean delResult = fixedDepositDao.deleteFixedDeposit(fdNumber);
    log.info("FD del result for {}: {}", fdNumber, delResult);
    return delResult;
  }

  @Override
  public boolean deleteFixedDeposits() {
    boolean delResult = fixedDepositDao.deleteFixedDeposits();
    log.info("All FDs del result: {}", delResult);
    return delResult;
  }

  @Override
  public List<FixedDepositProto.FixedDeposit> getAllByBank(String bank) {
    List<FixedDepositProto.FixedDeposit> result = fixedDepositDao.getAllByBank(bank);
    log.info("Found {} FDs matching {}", result.size(), bank);
    return result;
  }

  @Override
  public List<FixedDepositProto.FixedDeposit> getAllByUser(String user) {
    List<FixedDepositProto.FixedDeposit> result = fixedDepositDao.getAllByUser(user);
    log.info("Found {} FDs matching {}", result.size(), user);
    return result;
  }

  @Override
  public List<FixedDepositProto.FixedDeposit> getAllByOriginalUser(String originalUser) {
    List<FixedDepositProto.FixedDeposit> result =
        fixedDepositDao.getAllByOriginalUser(originalUser);
    log.info("Found {} FDs matching {}", result.size(), originalUser);
    return result;
  }

  @Override
  public List<FixedDepositProto.FixedDeposit> getAllByFdNumber(String fdNumber) {
    List<FixedDepositProto.FixedDeposit> result = fixedDepositDao.getAllByFdNumber(fdNumber);
    log.info("Found {} FDs matching {}", result.size(), fdNumber);
    return result;
  }

  @Override
  public boolean updateFixedDepositActiveStatus(String fdNumber, Boolean isActive) {
    return fixedDepositDao.updateFixedDepositActiveStatus(fdNumber, isActive);
  }

  @Override
  public boolean freezeTotalAmount(String fdNumber, Double totalAmount) {
    FixedDepositProto.FixedDeposit fixedDeposit = getFixedDeposit(fdNumber);
    double interest = totalAmount - fixedDeposit.getDepositAmount();
    return fixedDepositDao.freezeTotalAmount(fdNumber, interest, totalAmount);
  }

  @Override
  public boolean expireNrFd(String fdNumber) {
    String newFdNumber = fdNumber + "_exp";
    return updateFixedDepositActiveStatus(fdNumber, false)
        && fixedDepositDao.expireNrFd(fdNumber, newFdNumber);
  }

  @Override
  public String extractDataInDelimitedFormat(String delimiter) {
    StringBuilder dataLines = new StringBuilder();
    fixedDepositDao
        .getFixedDepositsInEntityFormat()
        .forEach(
            bankFixedDepositEntity ->
                dataLines
                    .append(
                        StringUtils.joinWith(
                            delimiter,
                            bankFixedDepositEntity.getFdNumber(),
                            bankFixedDepositEntity.getUserFd(),
                            bankFixedDepositEntity.getCustomerId(),
                            bankFixedDepositEntity.getBankIfsc(),
                            bankFixedDepositEntity.getDepositAmount(),
                            bankFixedDepositEntity.getRateOfInterest(),
                            bankFixedDepositEntity.getStartDate(),
                            bankFixedDepositEntity.getEndDate(),
                            bankFixedDepositEntity.getMonths(),
                            bankFixedDepositEntity.getDays(),
                            bankFixedDepositEntity.getInterestType(),
                            bankFixedDepositEntity.getNominee(),
                            bankFixedDepositEntity.getExpectedAmount(),
                            bankFixedDepositEntity.getExpectedInterest(),
                            bankFixedDepositEntity.getOriginalUserFd(),
                            bankFixedDepositEntity.getIsActive(),
                            bankFixedDepositEntity.getCreatedTimestamp()))
                    .append("\n"));
    return dataLines.toString();
  }
}
