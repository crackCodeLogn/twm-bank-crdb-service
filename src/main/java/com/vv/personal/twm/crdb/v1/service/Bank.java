package com.vv.personal.twm.crdb.v1.service;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;

/**
 * @author Vivek
 * @since 09/03/22
 */
public interface Bank {
    boolean addBank(BankProto.Bank bank);

    int addBanks(BankProto.BankList bankList);

    BankProto.Bank getBank(String ifsc);

    BankProto.BankList getBanks();

    boolean deleteBank(String ifsc);

    boolean deleteBanks();
}