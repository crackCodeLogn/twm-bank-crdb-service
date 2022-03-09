package com.vv.personal.twm.crdb.v1.service.impl;

import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import com.vv.personal.twm.crdb.v1.data.dao.BankFixedDepositDao;
import com.vv.personal.twm.crdb.v1.service.FixedDeposit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Vivek
 * @since 09/03/22
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FixedDepositImpl implements FixedDeposit {
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
        FixedDepositProto.FixedDepositList fixedDepositList = FixedDepositProto.FixedDepositList.newBuilder()
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
}