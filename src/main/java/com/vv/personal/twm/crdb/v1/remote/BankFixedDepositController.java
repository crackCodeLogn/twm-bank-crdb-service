package com.vv.personal.twm.crdb.v1.remote;

import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import com.vv.personal.twm.crdb.v1.service.FixedDeposit;
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
@RestController("bank-fd-controller")
@RequestMapping("/crdb/bank/")
public class BankFixedDepositController {
    private final FixedDeposit fixedDeposit;

    @PostMapping("/fixed-deposit")
    public String addFixedDeposit(@RequestBody FixedDepositProto.FixedDeposit fixedDepositData) {
        log.info("Received request to add '{}' FD into db", fixedDepositData.getFdNumber());
        try {
            boolean added = fixedDeposit.addFixedDeposit(fixedDepositData);
            if (added) return "Done";
            return "Failed";
        } catch (Exception e) {
            log.error("Failed to write all data correctly. ", e);
        }
        return "Failed";
    }

    @PostMapping("/fixed-deposits")
    public String addFixedDeposits(@RequestBody FixedDepositProto.FixedDepositList fixedDepositList) {
        log.info("Received request to add '{}' FDs into db", fixedDepositList.getFixedDepositCount());
        try {
            int added = fixedDeposit.addFixedDeposits(fixedDepositList);
            if (added == fixedDepositList.getFixedDepositCount()) {
                log.info("Added {} FDs!", added);
                return "Done";
            }
            return "Failed";
        } catch (Exception e) {
            log.error("Failed to write all data correctly. ", e);
        }
        return "Failed";
    }

    @GetMapping("/fixed-deposits")
    public FixedDepositProto.FixedDepositList readAllFixedDeposits() {
        log.info("Received request to read all FDs from db");
        return fixedDeposit.getFixedDeposits();
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