package com.vv.personal.twm.crdb.v1.service;

import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;

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
}