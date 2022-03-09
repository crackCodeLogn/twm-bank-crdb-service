package com.vv.personal.twm.crdb.v1.service.impl;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.data.dao.BankDao;
import com.vv.personal.twm.crdb.v1.service.Bank;
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
public class BankImpl implements Bank {
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
        BankProto.BankList bankList = BankProto.BankList.newBuilder()
                .addAllBanks(bankDao.getBanks())
                .build();
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
}