package com.vv.personal.twm.crdb.v1.data.dao;

import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import com.vv.personal.twm.crdb.v1.data.entity.BankFixedDepositEntity;
import com.vv.personal.twm.crdb.v1.data.repository.BankFixedDepositRepository;
import com.vv.personal.twm.crdb.v1.util.BankHelperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vivek
 * @since 15/01/22
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BankFixedDepositDao {
    private final BankFixedDepositRepository bankFixedDepositRepository;

    public boolean addFixedDeposit(FixedDepositProto.FixedDeposit fixedDeposit) {
        Instant currentTime = Instant.now();
        try {
            BankFixedDepositEntity fdEntity = generateFixedDepositEntity(fixedDeposit, currentTime);
            bankFixedDepositRepository.saveAndFlush(fdEntity);
        } catch (Exception e) { //TODO - streamline exception later
            log.error("Error on fd save. ", e);
            return false;
        }
        return true;
    }

    public int addFixedDeposits(FixedDepositProto.FixedDepositList fixedDepositList) {
        Instant instant = Instant.now();
        try {
            List<BankFixedDepositEntity> fixedDepositEntities = fixedDepositList.getFixedDepositList().stream()
                    .map(fixedDeposit -> generateFixedDepositEntity(fixedDeposit, instant))
                    .collect(Collectors.toList());
            return bankFixedDepositRepository.saveAll(fixedDepositEntities).size();
        } catch (Exception e) { //TODO - streamline exception later
            log.error("Error on bulk fd save. ", e);
            return 0;
        }
    }

    public FixedDepositProto.FixedDeposit getFixedDeposit(String fdNumber) {
        return generateFixedDeposit(bankFixedDepositRepository.getOne(fdNumber));
    }

    public List<FixedDepositProto.FixedDeposit> getFixedDeposits() {
        return bankFixedDepositRepository.findAll().stream()
                .map(this::generateFixedDeposit)
                .collect(Collectors.toList());
    }

    /**
     * Doing the sin of shipping out the entities in list outside the DAO. Required for pure backup scenario.
     * The proto obj doesn't contain audit column like cre_ts;
     */
    public List<BankFixedDepositEntity> getFixedDepositsInEntityFormat() {
        return bankFixedDepositRepository.findAll();
    }

    public List<FixedDepositProto.FixedDeposit> getAllByBank(String name) {
        return bankFixedDepositRepository.getAllByMatchingBank(name).stream().map(this::generateFixedDeposit).collect(Collectors.toList());
    }

    public List<FixedDepositProto.FixedDeposit> getAllByUser(String user) {
        return bankFixedDepositRepository.getAllByMatchingUser(user).stream().map(this::generateFixedDeposit).collect(Collectors.toList());
    }

    public List<FixedDepositProto.FixedDeposit> getAllByOriginalUser(String originalUser) {
        return bankFixedDepositRepository.getAllByMatchingOriginalUser(originalUser).stream().map(this::generateFixedDeposit).collect(Collectors.toList());
    }

    public List<FixedDepositProto.FixedDeposit> getAllByFdNumber(String fdNumber) {
        return bankFixedDepositRepository.getAllByMatchingFdNumber(fdNumber).stream().map(this::generateFixedDeposit).collect(Collectors.toList());
    }

    public boolean updateFixedDepositActiveStatus(String fdNumber, Boolean isActive) {
        try {
            return bankFixedDepositRepository.updateFixedDepositActiveStatus(fdNumber, isActive) == 1;
        } catch (Exception e) {
            log.error("Failed to updateFixedDepositActiveStatus for {} to {}. ", fdNumber, isActive, e);
        }
        return false;
    }

    public boolean deleteFixedDeposit(String fdNumber) {
        try {
            bankFixedDepositRepository.deleteById(fdNumber);
        } catch (Exception e) {
            log.error("Error on deleting fd of number: {}. ", fdNumber, e);
            return false;
        }
        return true;
    }

    public boolean deleteFixedDeposits() {
        try {
            bankFixedDepositRepository.deleteAll();
        } catch (Exception e) {
            log.error("Error on deleting all fds. ", e);
            return false;
        }
        return true;
    }

    FixedDepositProto.FixedDeposit generateFixedDeposit(BankFixedDepositEntity fixedDeposit) {
        return FixedDepositProto.FixedDeposit.newBuilder()
                .setFdNumber(fixedDeposit.getFdNumber())
                .setUser(fixedDeposit.getUserFd())
                .setOriginalUser(fixedDeposit.getOriginalUserFd())
                .setCustomerId(fixedDeposit.getCustomerId())
                .setBankIFSC(fixedDeposit.getBankIfsc())
                .setDepositAmount(fixedDeposit.getDepositAmount())
                .setRateOfInterest(fixedDeposit.getRateOfInterest())
                .setStartDate(BankHelperUtil.getDateInYyyyMmDd(fixedDeposit.getStartDate()))
                .setEndDate(BankHelperUtil.getDateInYyyyMmDd(fixedDeposit.getEndDate()))
                .setMonths(fixedDeposit.getMonths())
                .setDays(fixedDeposit.getDays())
                .setInterestType(FixedDepositProto.InterestType.valueOf(fixedDeposit.getInterestType()))
                .setNominee(fixedDeposit.getNominee())
                .setIsFdActive(fixedDeposit.getIsActive())
                .setExpectedAmount(fixedDeposit.getExpectedAmount())
                .setExpectedInterest(fixedDeposit.getExpectedInterest())
                .setAccountType(FixedDepositProto.AccountType.forNumber(fixedDeposit.getAccountType()))
                .build();
    }

    BankFixedDepositEntity generateFixedDepositEntity(FixedDepositProto.FixedDeposit fixedDeposit, Instant instant) {
        return BankFixedDepositEntity.builder()
                .fdNumber(fixedDeposit.getFdNumber())
                .accountType(fixedDeposit.getAccountTypeValue())
                .userFd(fixedDeposit.getUser())
                .originalUserFd(fixedDeposit.getOriginalUser())
                .customerId(fixedDeposit.getCustomerId())
                .bankIfsc(fixedDeposit.getBankIFSC())
                .depositAmount(fixedDeposit.getDepositAmount())
                .rateOfInterest(fixedDeposit.getRateOfInterest())
                .startDate(BankHelperUtil.getDateFromYyyyMmDd(fixedDeposit.getStartDate()))
                .endDate(BankHelperUtil.getDateFromYyyyMmDd(fixedDeposit.getEndDate()))
                .months(fixedDeposit.getMonths())
                .days(fixedDeposit.getDays())
                .interestType(fixedDeposit.getInterestType().name())
                .nominee(fixedDeposit.getNominee())
                .isActive(fixedDeposit.getIsFdActive())
                .expectedAmount(fixedDeposit.getExpectedAmount())
                .expectedInterest(fixedDeposit.getExpectedInterest())
                .createdTimestamp(instant)
                .build();
    }
}