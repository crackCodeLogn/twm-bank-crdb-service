package com.vv.personal.twm.crdb.v1.remote;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.service.BankService;
import com.vv.personal.twm.crdb.v1.util.BankHelperUtil;
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
  private final BankService bankService;

  @PostMapping("/bank")
  public String addBank(@RequestBody BankProto.Bank bankData) {
    log.info("Received request to add '{}' bank into db", bankData.getIFSC());
    try {
      boolean result = bankService.addBank(bankData);
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
      int added = bankService.addBanks(bankList);
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
  public BankProto.BankList getBanks(
      @RequestParam("field") String field, // NAME, TYPE, IFSC, EMPTY - return all if EMPTY
      @RequestParam(value = "value", required = false) String value) {
    log.info("Received '{}' to list Banks for field '{}'", value, field);
    BankProto.BankList.Builder banks = BankProto.BankList.newBuilder();
    try {
      switch (field) {
        case "NAME":
          banks.addAllBanks(bankService.getAllByName(value));
          break;
        case "TYPE":
          banks.addAllBanks(bankService.getAllByType(value));
          break;
        case "IFSC":
          banks.addAllBanks(bankService.getAllByIfsc(value));
          break;
        default:
          banks.addAllBanks(bankService.getBanks().getBanksList());
      }
    } catch (Exception e) {
      log.error("Failed to list {}: {} from crdb! ", field, value, e);
    }
    return banks.build();
  }

  @DeleteMapping("/banks/{ifsc}")
  public boolean deleteBank(@PathVariable("ifsc") String ifsc) {
    log.info("Received request to del bank for ifsc: {}", ifsc);
    return bankService.deleteBank(ifsc);
  }

  @DeleteMapping("/banks")
  public boolean deleteBanks() {
    log.info("Received request to del all banks");
    return bankService.deleteBanks();
  }

  @GetMapping("/banks/backup")
  public boolean backUp(
      @RequestParam("destinationFolder") String destinationFolder,
      @RequestParam("delimiter") String delimiter) {
    String zonedDateTime = BankHelperUtil.getZonedDateTimeForFileName();
    log.info(
        "Received request to initiate writing of db content as backup to {} using {} separated, at time {}",
        destinationFolder,
        delimiter,
        zonedDateTime);
    String destinationFileName = String.format("%s-%s.csv", "Banks", zonedDateTime);
    boolean result =
        BankHelperUtil.writeToFile(
            bankService.extractDataInDelimitedFormat(delimiter),
            destinationFolder,
            destinationFileName);
    log.info("Backup creation result: {} for {}", result, destinationFileName);
    return result;
  }

  @GetMapping("/test")
  public void test() {
    BankProto.Bank data =
        BankProto.Bank.newBuilder()
            .setIsActive(true)
            .setBankType(BankProto.BankType.PRIVATE)
            .setName("HSBC")
            .setContactNumber("215444")
            .setIFSC("1234567891")
            .setCountryCode("IN")
            .build();

    // bank.addBank(data);
    bankService.addBanks(BankProto.BankList.newBuilder().addBanks(data).build());
  }
}
