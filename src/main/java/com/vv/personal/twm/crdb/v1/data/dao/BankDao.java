package com.vv.personal.twm.crdb.v1.data.dao;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.data.entity.BankEntity;
import com.vv.personal.twm.crdb.v1.data.repository.BankRepository;
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
public class BankDao {
    private final BankRepository bankRepository;

    public boolean addBank(BankProto.Bank bank) {
        Instant currentTime = Instant.now();
        try {
            BankEntity bankEntity = generateBankEntity(bank, currentTime);
            bankRepository.saveAndFlush(bankEntity);
        } catch (Exception e) { //TODO - streamline exception later
            log.error("Error on bank save. ", e);
            return false;
        }
        return true;
    }

    public int addBanks(BankProto.BankList bankList) {
        Instant instant = Instant.now();
        try {
            List<BankEntity> bankEntities = bankList.getBanksList().stream()
                    .map(bank -> generateBankEntity(bank, instant))
                    .collect(Collectors.toList());
            return bankRepository.saveAll(bankEntities).size();
        } catch (Exception e) { //TODO - streamline exception later
            log.error("Error on bulk bank save. ", e);
            return 0;
        }
    }

    public BankProto.Bank getBank(String ifsc) {
        return generateBank(bankRepository.getOne(ifsc));
    }

    public List<BankProto.Bank> getBanks() {
        return bankRepository.findAll().stream()
                .map(this::generateBank)
                .collect(Collectors.toList());
    }

    public boolean deleteBank(String ifsc) {
        try {
            bankRepository.deleteById(ifsc);
        } catch (Exception e) {
            log.error("Error on deleting bank of ifsc: {}. ", ifsc, e);
            return false;
        }
        return true;
    }

    public boolean deleteBanks() {
        try {
            bankRepository.deleteAll();
        } catch (Exception e) {
            log.error("Error on deleting all banks. ", e);
            return false;
        }
        return true;
    }

    BankProto.Bank generateBank(BankEntity bank) {
        return BankProto.Bank.newBuilder()
                .setName(bank.getBankName())
                .setIFSC(bank.getIfsc())
                .setBankType(BankProto.BankType.valueOf(bank.getBankType()))
                .setContactNumber(bank.getContactNumber())
                .setIsActive(bank.isActive())
                .build();
    }

    BankEntity generateBankEntity(BankProto.Bank bank, Instant instant) {
        return new BankEntity()
                .setBankName(bank.getName())
                .setBankType(bank.getBankType().name())
                .setIfsc(bank.getIFSC())
                .setContactNumber(bank.getContactNumber())
                .setActive(bank.getIsActive())
                .setCreatedTimestamp(instant);
    }
}