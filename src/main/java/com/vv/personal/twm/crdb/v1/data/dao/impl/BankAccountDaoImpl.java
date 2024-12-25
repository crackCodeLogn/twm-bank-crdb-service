package com.vv.personal.twm.crdb.v1.data.dao.impl;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.data.dao.BankAccountDao;
import com.vv.personal.twm.crdb.v1.data.entity.BankAccountEntity;
import com.vv.personal.twm.crdb.v1.data.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.vv.personal.twm.crdb.v1.util.BankHelperUtil.*;

/**
 * @author Vivek
 * @since 2024-12-23
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BankAccountDaoImpl implements BankAccountDao {
    private final BankAccountRepository bankAccountRepository;

    @Override
    public boolean doesAccountExist(String id) {
        try {
            return bankAccountRepository.existsById(getUUID(id));
        } catch (Exception e) {
            log.error("Failed to check bank account existence with id: {}", id, e);
        }
        return false;
    }

    @Override
    public boolean addBankAccount(BankProto.BankAccount bankAccount) {
        if (doesAccountExist(bankAccount.getId())) {
            log.warn("Account already exists with id {}", bankAccount.getId());
            return false;
        }

        Instant currentTime = Instant.now();
        try {
            BankAccountEntity bankAccountEntity = generateBankAccountEntity(bankAccount, currentTime);
            bankAccountRepository.saveAndFlush(bankAccountEntity);
            return true;
        } catch (Exception e) {
            log.error("Failed to add bank account with id: {}", bankAccount.getId(), e);
        }
        return false;
    }

    @Override
    public Optional<BankProto.BankAccount> getBankAccount(String id) {
        try {
            UUID uuid = getUUID(id);
            Optional<BankAccountEntity> bankAccountOptional = bankAccountRepository.findById(uuid);
            if (bankAccountOptional.isPresent()) {
                return Optional.of(generateBankAccount(bankAccountOptional.get()));
            }
        } catch (Exception e) {
            log.error("Failed to get bank account with id: {}", id, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<BankProto.BankAccounts> getAllBankAccounts() {
        try {
            List<BankProto.BankAccount> bankAccounts =
                    bankAccountRepository.findAll().stream().map(this::generateBankAccount).toList();

            return Optional.of(BankProto.BankAccounts.newBuilder()
                    .addAllAccounts(bankAccounts)
                    .build());
        } catch (Exception e) {
            log.error("Failed to get all bank accounts", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<BankProto.BankAccounts> getAllBankAccountsByMatchingName(String name) {
        try {
            List<BankProto.BankAccount> bankAccounts =
                    bankAccountRepository.getAllByMatchingName(name).stream().map(this::generateBankAccount).toList();

            return Optional.of(BankProto.BankAccounts.newBuilder()
                    .addAllAccounts(bankAccounts)
                    .build());
        } catch (Exception e) {
            log.error("Failed to get all bank accounts matching name: {}", name, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<BankProto.BankAccounts> getAllBankAccountsByMatchingIfsc(String ifsc) {
        try {
            List<BankProto.BankAccount> bankAccounts =
                    bankAccountRepository.getAllByMatchingIfsc(ifsc).stream().map(this::generateBankAccount).toList();

            return Optional.of(BankProto.BankAccounts.newBuilder()
                    .addAllAccounts(bankAccounts)
                    .build());
        } catch (Exception e) {
            log.error("Failed to get all bank accounts matching ifsc: {}", ifsc, e);
        }
        return Optional.empty();
    }

    @Override
    public boolean updateBankAccountBalance(String id, double amount) {
        try {
            UUID uuid = getUUID(id);
            Optional<BankAccountEntity> bankAccountEntityOptional = bankAccountRepository.findById(uuid);
            if (bankAccountEntityOptional.isPresent()) {
                BankAccountEntity bankAccountEntity = bankAccountEntityOptional.get();
                bankAccountEntity.setBalance(amount);
                bankAccountRepository.saveAndFlush(bankAccountEntity);
                return true;
            } else {
                log.error("No bank account found with id: {} to update balance to {}", id, amount);
            }
        } catch (Exception e) {
            log.error("Failed to update bank account balance with id: {}", id, e);
        }
        return false;
    }

    @Override
    public Double getBankAccountBalance(String id) {
        return getBankAccount(id).orElse(BankProto.BankAccount.newBuilder().build())
                .getBalance();
    }

    @Override
    public boolean deleteBankAccount(String id) {
        try {
            bankAccountRepository.deleteById(getUUID(id));
            return true;
        } catch (Exception e) {
            log.error("Failed to delete bank account with id: {}", id, e);
        }
        return false;
    }

    @Override
    public List<BankAccountEntity> getBankAccountsInEntityFormat() {
        return bankAccountRepository.findAll();
    }

    private BankAccountEntity generateBankAccountEntity(BankProto.BankAccount bankAccount, Instant currentTime) {
        return BankAccountEntity.builder()
                .id(getUUID(bankAccount.getId()))
                .bankIfsc(bankAccount.getBank().getIFSC())
                .accountNumber(bankAccount.getNumber())
                .name(bankAccount.getName())
                .transitNumber(bankAccount.getTransitNumber())
                .institutionNumber(bankAccount.getInstitutionNumber())
                .balance(bankAccount.getBalance())
                .bankAccountType(bankAccount.getBankAccountType().name())
//                .metaData(bankAccount.getMetaDataMap().toString()) // todo - use better map -> string converter
                .overdraftBalance(bankAccount.getOverdraftBalance())
                .interestRate(bankAccount.getInterestRate())
                .isActive(bankAccount.getIsActive())
                .createdTimestamp(currentTime)
                .lastUpdatedTimestamp(currentTime)
                .build();
    }

    private BankProto.BankAccount generateBankAccount(BankAccountEntity bankAccountEntity) {
        return BankProto.BankAccount.newBuilder()
                .setBank(BankProto.Bank.newBuilder()
                        .setIFSC(bankAccountEntity.getBankIfsc()))
                .setId(getUUIDString(bankAccountEntity.getId()))
                .setNumber(bankAccountEntity.getAccountNumber())
                .setName(bankAccountEntity.getName())
                .setTransitNumber(bankAccountEntity.getTransitNumber())
                .setInstitutionNumber(bankAccountEntity.getInstitutionNumber())
                .setBalance(bankAccountEntity.getBalance())
                .setBankAccountType(BankProto.BankAccountType.valueOf(bankAccountEntity.getBankAccountType()))
                .setOverdraftBalance(bankAccountEntity.getOverdraftBalance())
                .setInterestRate(bankAccountEntity.getInterestRate())
                .setIsActive(bankAccountEntity.getIsActive())
                .setCreatedAt(fromInstant(bankAccountEntity.getCreatedTimestamp()))
                .setLastUpdatedAt(fromInstant(bankAccountEntity.getLastUpdatedTimestamp()))
                .build();
    }
}
