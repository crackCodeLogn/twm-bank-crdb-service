package com.vv.personal.twm.crdb.v1.remote;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.service.Bank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/bank")
    public String addBank(@RequestBody BankProto.Bank bankData) {
        log.info("Received request to add '{}' bank into db", bankData.getIFSC());
        try {
            boolean result = bank.addBank(bankData);
            if (result) return "Done";
            return "Failed";
        } catch (Exception e) {
            log.error("Failed to write all data correctly. ", e);
        }
        return "Failed";
    }

    @PostMapping("/banks")
    public String addBanks(@RequestBody BankProto.BankList bankList) {
        log.info("Received request to add '{}' banks into db", bankList.getBanksCount());
        try {
            int added = bank.addBanks(bankList);
            if (added == bankList.getBanksCount()) {
                log.info("Added {} banks!", added);
                return "OK";
            }
            return "FAILED";
        } catch (Exception e) {
            log.error("Failed to write all data correctly. ", e);
        }
        return "FAILED";
    }

    @GetMapping("/banks")
    public BankProto.BankList getBanks(@RequestParam("field") String field, //NAME, TYPE, IFSC, EMPTY - return all if EMPTY
                                       @RequestParam(value = "value", required = false) String value) {
        log.info("Received '{}' to list Banks for field '{}'", value, field);
        BankProto.BankList.Builder banks = BankProto.BankList.newBuilder();
        try {
            switch (field) {
                case "NAME":
                    banks.addAllBanks(bank.getAllByName(value));
                    break;
                case "TYPE":
                    banks.addAllBanks(bank.getAllByType(value));
                    break;
                case "IFSC":
                    banks.addAllBanks(bank.getAllByIfsc(value));
                    break;
                default:
                    banks.addAllBanks(bank.getBanks().getBanksList());
            }
        } catch (Exception e) {
            log.error("Failed to list {}: {} from crdb! ", field, value, e);
        }
        return banks.build();
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