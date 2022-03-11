package com.vv.personal.twm.crdb.v1.service;

import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;

import java.util.List;

/**
 * @author Vivek
 * @since 09/03/22
 */
public interface FixedDeposit {
    boolean addFixedDeposit(FixedDepositProto.FixedDeposit fixedDeposit);

    int addFixedDeposits(FixedDepositProto.FixedDepositList fixedDepositList);

    FixedDepositProto.FixedDeposit getFixedDeposit(String fdNumber);

    FixedDepositProto.FixedDepositList getFixedDeposits();

    boolean deleteFixedDeposit(String fdNumber);

    boolean deleteFixedDeposits();

    List<FixedDepositProto.FixedDeposit> getAllByBank(String bank);

    List<FixedDepositProto.FixedDeposit> getAllByUser(String user);

    List<FixedDepositProto.FixedDeposit> getAllByOriginalUser(String originalUser);

    List<FixedDepositProto.FixedDeposit> getAllByFdNumber(String fdNumber);

    boolean updateFixedDepositActiveStatus(String fdNumber, Boolean isActive);
}