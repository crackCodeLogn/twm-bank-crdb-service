package com.vv.personal.twm.crdb.v1.remote;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.service.Bank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vivek
 * @since 15/01/22
 */
@Slf4j
@RequiredArgsConstructor
@RestController("bank-controller")
@RequestMapping("/crdb/bank/")
public class BankController {
    private final Bank bank;

    @PostMapping("/banks")
    public String addBanks(@RequestBody BankProto.BankList bankList) {
        log.info("Received request to add '{}' banks into db", bankList.getBanksCount());
        try {
            int added = bank.addBanks(bankList);
            if (added == bankList.getBanksCount()) {
                log.info("Added {} banks!", added);
                return "Done";
            }
            return "Failed";
        } catch (Exception e) {
            log.error("Failed to write all data correctly. ", e);
        }
        return "Failed";
    }

    @GetMapping("/banks")
    public BankProto.BankList readAllBanks() {
        log.info("Received request to read all banks from db");
        return bank.getBanks();
    }

    @DeleteMapping("/banks/{ifsc}")
    public boolean deleteBank(@PathVariable("ifsc") String ifsc) {
        log.info("Received request to del bank for ifsc: {}", ifsc);
        return bank.deleteBank(ifsc);
    }

    @DeleteMapping("/banks")
    public boolean deleteBanks() {
        log.info("Received request to del all banks");
        return bank.deleteBanks();
    }

    @GetMapping("/test")
    public void test() {
        BankProto.Bank data = BankProto.Bank.newBuilder()
                .setIsActive(true)
                .setBankType(BankProto.BankType.PRIVATE)
                .setName("HSBC")
                .setContactNumber("215444")
                .setIFSC("1234567891")
                .build();

        //bank.addBank(data);
        bank.addBanks(BankProto.BankList.newBuilder()
                .addBanks(data)
                .build());
    }
}