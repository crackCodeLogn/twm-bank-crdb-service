package com.vv.personal.twm.crdb.v1.remote;

import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import com.vv.personal.twm.crdb.v1.service.FixedDeposit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Vivek
 * @since 15/01/22
 */
@Slf4j
@RequiredArgsConstructor
@RestController("bank-fd-controller")
@RequestMapping("/crdb/bank/")
public class BankFixedDepositController {
    private final FixedDeposit fixedDeposit;

    @PostMapping("/fixed-deposits")
    public String addFixedDeposits(@RequestBody FixedDepositProto.FixedDepositList fixedDepositList) {
        log.info("Received request to add '{}' FDs into db", fixedDepositList.getFixedDepositCount());
        try {
            int added = fixedDeposit.addFixedDeposits(fixedDepositList);
            if (added == fixedDepositList.getFixedDepositCount()) {
                log.info("Added {} FDs!", added);
                return "OK";
            }
            return "FAILED";
        } catch (Exception e) {
            log.error("Failed to write all data correctly. ", e);
        }
        return "FAILED";
    }

    @GetMapping("/fixed-deposits")
    public FixedDepositProto.FixedDepositList getFixedDeposits(@RequestParam("field") String field, //BANK, USER, ORIGINAL_USER, KEY, EMPTY - return all if EMPTY
                                                               @RequestParam(value = "value", required = false) String value) {
        log.info("Received '{}' to list Fixed Deposits for field '{}'", value, field);
        FixedDepositProto.FixedDepositList.Builder fixedDeposits = FixedDepositProto.FixedDepositList.newBuilder();
        try {
            FixedDepositProto.FilterBy filterBy = FixedDepositProto.FilterBy.valueOf(field);
            switch (filterBy) {
                case BANK:
                    fixedDeposits.addAllFixedDeposit(fixedDeposit.getAllByBank(value));
                    break;
                case USER:
                    fixedDeposits.addAllFixedDeposit(fixedDeposit.getAllByUser(value));
                    break;
                case ORIGINAL_USER:
                    fixedDeposits.addAllFixedDeposit(fixedDeposit.getAllByOriginalUser(value));
                    break;
                case KEY:
                    fixedDeposits.addAllFixedDeposit(fixedDeposit.getAllByFdNumber(value));
                    break;
                default:
                    fixedDeposits.addAllFixedDeposit(fixedDeposit.getFixedDeposits().getFixedDepositList());
            }
        } catch (Exception e) {
            log.error("Failed to list {}: {} from crdb! ", field, value, e);
        }
        return fixedDeposits.build();
    }

    @DeleteMapping("/fixed-deposits/{fd-number}")
    public boolean deleteFixedDeposit(@PathVariable("fd-number") String fdNumber) {
        log.info("Received request to del FD for fd number: {}", fdNumber);
        return fixedDeposit.deleteFixedDeposit(fdNumber);
    }

    @DeleteMapping("/fixed-deposits")
    public boolean deleteFixedDeposits() {
        log.info("Received request to del all FDs");
        return fixedDeposit.deleteFixedDeposits();
    }

    @GetMapping("/test-fd")
    public void test() {
        FixedDepositProto.FixedDeposit data = FixedDepositProto.FixedDeposit.newBuilder()
                .setFdNumber("012345")
                .setUser("RUS")
                .setOriginalUser("RUS")
                .setCustomerId("Z")
                .setBankIFSC("1234567891")
                .setDepositAmount(1694.25)
                .setRateOfInterest(5.6)
                .setStartDate("20220310")
                .setEndDate("20320311")
                .setMonths(120)
                .setDays(1)
                .setInterestType(FixedDepositProto.InterestType.ON_MATURITY)
                .setNominee("USSR")
                .setIsFdActive(true)
                .build();

        fixedDeposit.addFixedDeposit(data);
        fixedDeposit.addFixedDeposits(FixedDepositProto.FixedDepositList.newBuilder()
                .addFixedDeposit(data)
                .build());
    }
}