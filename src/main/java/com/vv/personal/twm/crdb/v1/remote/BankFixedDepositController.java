package com.vv.personal.twm.crdb.v1.remote;

import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import com.vv.personal.twm.crdb.v1.service.BankService;
import com.vv.personal.twm.crdb.v1.service.FixedDepositService;
import com.vv.personal.twm.crdb.v1.util.BankHelperUtil;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
  private final FixedDepositService fixedDepositService;
  private final BankService bankService;

  @PostMapping("/fixed-deposit")
  public String addFixedDeposit(@RequestBody FixedDepositProto.FixedDeposit fixedDepositData) {
    log.info("Received request to add '{}' FD into db", fixedDepositData.getFdNumber());
    try {
      boolean added = fixedDepositService.addFixedDeposit(fixedDepositData);
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
      int added = fixedDepositService.addFixedDeposits(fixedDepositList);
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
  public FixedDepositProto.FixedDepositList getFixedDeposits(
      @RequestParam(value = "field", required = false, defaultValue = "ALL")
          String field, // BANK, USER, ORIGINAL_USER, KEY, ALL
      @RequestParam(value = "value", required = false) String value) {
    log.info("Received '{}' to list Fixed Deposits for field '{}'", value, field);
    FixedDepositProto.FixedDepositList.Builder fixedDeposits =
        FixedDepositProto.FixedDepositList.newBuilder();
    try {
      FixedDepositProto.FilterBy filterBy = FixedDepositProto.FilterBy.valueOf(field);
      switch (filterBy) {
        case BANK:
          fixedDeposits.addAllFixedDeposit(fixedDepositService.getAllByBank(value));
          break;
        case USER:
          fixedDeposits.addAllFixedDeposit(fixedDepositService.getAllByUser(value));
          break;
        case ORIGINAL_USER:
          fixedDeposits.addAllFixedDeposit(fixedDepositService.getAllByOriginalUser(value));
          break;
        case KEY:
          fixedDeposits.addAllFixedDeposit(fixedDepositService.getAllByFdNumber(value));
          break;
        case CCY:
          String countryCode = BankHelperUtil.getCountryForCcy(value);

          Set<String> ccyBankIfscs = new HashSet<>();
          bankService
              .getAllByCountryCode(countryCode)
              .ifPresent(
                  list -> list.getBanksList().forEach(bank -> ccyBankIfscs.add(bank.getIFSC())));

          List<FixedDepositProto.FixedDeposit> ccyFixedDeposits =
              fixedDepositService.getFixedDeposits().getFixedDepositList().stream()
                  .filter(fixedDeposit -> ccyBankIfscs.contains(fixedDeposit.getBankIFSC()))
                  .toList();
          fixedDeposits.addAllFixedDeposit(ccyFixedDeposits);
          break;

        default:
          fixedDeposits.addAllFixedDeposit(
              fixedDepositService.getFixedDeposits().getFixedDepositList());
      }
    } catch (Exception e) {
      log.error("Failed to list {}: {} from crdb! ", field, value, e);
    }
    return fixedDeposits.build();
  }

  @PutMapping("/fixed-deposits")
  public String updateFixedDeposit(@RequestBody FixedDepositProto.FixedDeposit fixedDeposit) {
    log.info(
        "Going to replace FD with key: {} with updated information", fixedDeposit.getFdNumber());
    if (deleteFixedDeposit(fixedDeposit.getFdNumber())) {
      if (addFixedDeposits(
              FixedDepositProto.FixedDepositList.newBuilder().addFixedDeposit(fixedDeposit).build())
          .equals("OK")) {
        log.info("Added updated FD details in crdb for key: {}", fixedDeposit.getFdNumber());
        return "OK";
      } else {
        log.error("Failed to update FD with key: {}", fixedDeposit.getFdNumber());
      }
    }
    return "FAILED";
  }

  @PutMapping("/fixed-deposits/{fd}")
  public String updateFixedDeposit(
      @PathVariable("fd") String fdNumber, @RequestParam("active") Boolean isActive) {
    log.info("Going to update FD with key: {} with active status: {}", fdNumber, isActive);
    if (fixedDepositService.updateFixedDepositActiveStatus(fdNumber, isActive)) return "OK";
    return "FAILED";
  }

  @PutMapping("/fixed-deposits/{fd}/freeze")
  public String freezeTotalAmount(
      @PathVariable("fd") String fdNumber, @RequestParam("totalAmount") Double totalAmount) {
    log.info("Going to freeze FD with key: {} with total amount: {}", fdNumber, totalAmount);
    if (fixedDepositService.freezeTotalAmount(fdNumber, totalAmount)) return "OK";
    return "FAILED";
  }

  @PutMapping("/fixed-deposits/{fd}/expire/nr")
  public String expireNrFd(@PathVariable("fd") String fdNumber) {
    log.info("Going to expire NR FD with key: {}", fdNumber);
    if (fixedDepositService.expireNrFd(fdNumber)) return "OK";
    return "FAILED";
  }

  @DeleteMapping("/fixed-deposits/{fd-number}")
  public boolean deleteFixedDeposit(@PathVariable("fd-number") String fdNumber) {
    log.info("Received request to del FD for fd number: {}", fdNumber);
    return fixedDepositService.deleteFixedDeposit(fdNumber);
  }

  @DeleteMapping("/fixed-deposits")
  public boolean deleteFixedDeposits() {
    log.info("Received request to del all FDs");
    return fixedDepositService.deleteFixedDeposits();
  }

  @GetMapping("/fixed-deposits/backup")
  public boolean backUp(
      @RequestParam("destinationFolder") String destinationFolder,
      @RequestParam("delimiter") String delimiter) {
    String zonedDateTime = BankHelperUtil.getZonedDateTimeForFileName();
    log.info(
        "Received request to initiate writing of db content as backup to {} using {} separated, at time {}",
        destinationFolder,
        delimiter,
        zonedDateTime);
    String destinationFileName = String.format("%s-%s.csv", "FixedDeposits", zonedDateTime);
    boolean result =
        BankHelperUtil.writeToFile(
            fixedDepositService.extractDataInDelimitedFormat(delimiter),
            destinationFolder,
            destinationFileName);
    log.info("Backup creation result: {} for {}", result, destinationFileName);
    return result;
  }

  @GetMapping("/test-fd")
  public void test() {
    FixedDepositProto.FixedDeposit data =
        FixedDepositProto.FixedDeposit.newBuilder()
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

    fixedDepositService.addFixedDeposit(data);
    fixedDepositService.addFixedDeposits(
        FixedDepositProto.FixedDepositList.newBuilder().addFixedDeposit(data).build());
  }
}
