package com.vv.personal.twm.crdb.v1.service.impl;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.data.dao.BankAccountDao;
import com.vv.personal.twm.crdb.v1.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Vivek
 * @since 2024-12-24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountDao bankAccountDao;

    @Override
    public boolean doesAccountExist(String id) {
        return bankAccountDao.doesAccountExist(id);
    }

    @Override
    public boolean addBankAccount(BankProto.BankAccount bankAccount) {
        return bankAccountDao.addBankAccount(bankAccount);
    }

    @Override
    public Optional<BankProto.BankAccount> getBankAccount(String id) {
        return bankAccountDao.getBankAccount(id);
    }

    @Override
    public Optional<BankProto.BankAccounts> getAllBankAccounts() {
        return bankAccountDao.getAllBankAccounts();
    }

    @Override
    public Optional<BankProto.BankAccounts> getAllBankAccountsByMatchingName(String name) {
        return bankAccountDao.getAllBankAccountsByMatchingName(name);
    }

    @Override
    public Optional<BankProto.BankAccounts> getAllBankAccountsByMatchingIfsc(String ifsc) {
        return bankAccountDao.getAllBankAccountsByMatchingIfsc(ifsc);
    }

    @Override
    public boolean updateBankAccountBalance(String id, double amount) {
        return bankAccountDao.updateBankAccountBalance(id, amount);
    }

    @Override
    public Double getBankAccountBalance(String id) {
        return bankAccountDao.getBankAccountBalance(id);
    }

    @Override
    public boolean deleteBankAccount(String id) {
        return bankAccountDao.deleteBankAccount(id);
    }

    @Override
    public String extractDataInDelimitedFormat(String delimiter) {
        StringBuilder dataLines = new StringBuilder();
        bankAccountDao.getBankAccountsInEntityFormat().forEach(bankAccountEntity ->
                dataLines.append(StringUtils.joinWith(delimiter,
                        bankAccountEntity.getId(), bankAccountEntity.getBankIfsc(), bankAccountEntity.getAccountNumber(),
                        bankAccountEntity.getName(),
                        bankAccountEntity.getTransitNumber(),
                        bankAccountEntity.getInstitutionNumber(),
                        bankAccountEntity.getBalance(),
                        bankAccountEntity.getBankAccountType(),
                        bankAccountEntity.getMetaData(),
                        bankAccountEntity.getOverdraftBalance(),
                        bankAccountEntity.getInterestRate(),
                        bankAccountEntity.getIsActive(),
                        bankAccountEntity.getCreatedTimestamp(),
                        bankAccountEntity.getLastUpdatedTimestamp())
                ).append("\n")
        );
        return dataLines.toString();
    }
}
