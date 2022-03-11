package com.vv.personal.twm.crdb.v1.remote;

import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import com.vv.personal.twm.crdb.v1.service.FixedDeposit;
import com.vv.personal.twm.crdb.v1.util.BankHelperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;

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

    @PutMapping("/fixed-deposits")
    public String updateFixedDeposit(@RequestBody FixedDepositProto.FixedDeposit fixedDeposit) {
        log.info("Going to replace FD with key: {} with updated information", fixedDeposit.getFdNumber());
        if (deleteFixedDeposit(fixedDeposit.getFdNumber())) {
            if (addFixedDeposits(FixedDepositProto.FixedDepositList.newBuilder().addFixedDeposit(fixedDeposit).build()).equals("OK")) {
                log.info("Added updated FD details in crdb for key: {}", fixedDeposit.getFdNumber());
                return "OK";
            } else {
                log.error("Failed to update FD with key: {}", fixedDeposit.getFdNumber());
            }
        }
        return "FAILED";
    }

    @PutMapping("/fixed-deposits/{fd}")
    public String updateFixedDeposit(@PathVariable("fd") String fdNumber,
                                     @RequestParam("active") Boolean isActive) {
        log.info("Going to update FD with key: {} with active status: {}", fdNumber, isActive);
        if (fixedDeposit.updateFixedDepositActiveStatus(fdNumber, isActive)) return "OK";
        return "FAILED";
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

    @GetMapping("/fixed-deposits/backup")
    public boolean backUp(@RequestParam("destinationFolder") String destinationFolder, @RequestParam("delimiter") String delimiter) {
        String zonedDateTime = ZonedDateTime.now(ZoneId.of("IST", ZoneId.SHORT_IDS)).toString();
        zonedDateTime = zonedDateTime.substring(0, zonedDateTime.indexOf('['));
        log.info("Received request to initiate writing of db content as backup to {} using {} separated, at time {}", destinationFolder, delimiter, zonedDateTime);
        String destinationFileName = String.format("%s-%s.csv", "FixedDeposits", zonedDateTime);
        boolean result = BankHelperUtil.writeToFile(
                fixedDeposit.extractDataInDelimitedFormat(delimiter),
                destinationFolder,
                destinationFileName
        );
        log.info("Backup creation result: {} for {}", result, destinationFileName);
        return result;
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