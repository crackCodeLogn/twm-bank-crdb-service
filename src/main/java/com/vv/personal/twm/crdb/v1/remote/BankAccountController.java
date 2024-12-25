package com.vv.personal.twm.crdb.v1.remote;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.service.BankAccountService;
import com.vv.personal.twm.crdb.v1.util.BankHelperUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Vivek
 * @since 2024-12-24
 */
@Slf4j
@RequiredArgsConstructor
@RestController("bank-account-controller")
@RequestMapping("/crdb/bank/")
public class BankAccountController {

  private final BankAccountService bankAccountService;

  @PostMapping("/bank-account")
  public String addBankAccount(@RequestBody BankProto.BankAccount bankAccount) {
    log.info("Received request to add '{}' bank-account into db", bankAccount.getId());
    try {
      boolean result = bankAccountService.addBankAccount(bankAccount);
      if (result) return "Done";
      return "Failed";
    } catch (Exception e) {
      log.error("Failed to write all data correctly. ", e);
    }
    return "Failed";
  }

  @PostMapping("/bank-accounts")
  public String addBankAccounts(@RequestBody BankProto.BankAccounts bankAccounts) {
    log.info("Received request to add '{}' bank accounts into db", bankAccounts.getAccountsCount());
    try {
      List<String> failedBankAccountIds = new ArrayList<>();
      for (BankProto.BankAccount bankAccount : bankAccounts.getAccountsList()) {
        if (!bankAccountService.addBankAccount(bankAccount)) {
          failedBankAccountIds.add(bankAccount.getId());
        }
      }
      if (failedBankAccountIds.isEmpty()) {
        log.info("Added {} bank-accounts!", bankAccounts.getAccountsCount());
        return "OK";
      }
      log.info(
          "Failed to add {} bank-accounts => {}",
          failedBankAccountIds.size(),
          failedBankAccountIds);
      return "FAILED";
    } catch (Exception e) {
      log.error("Failed to write all data correctly. ", e);
    }
    return "FAILED";
  }

  @GetMapping("/bank-accounts")
  public BankProto.BankAccounts getBankAccounts(
      @RequestParam("field") String field, // NAME, IFSC, EMPTY - return
      // all if EMPTY
      @RequestParam(value = "value", required = false) String value) {
    log.info("Received '{}' to list bank accounts for field '{}'", value, field);
    BankProto.BankAccounts.Builder bankAccounts = BankProto.BankAccounts.newBuilder();
    Optional<BankProto.BankAccounts> optionalBankAccounts;
    try {
      optionalBankAccounts =
          switch (field) {
            case "NAME" -> bankAccountService.getAllBankAccountsByMatchingName(value);
            case "IFSC" -> bankAccountService.getAllBankAccountsByMatchingIfsc(value);
            default -> bankAccountService.getAllBankAccounts();
          };
      if (optionalBankAccounts.isPresent()) {
        bankAccounts.addAllAccounts(optionalBankAccounts.get().getAccountsList());
        return bankAccounts.build();
      } else {
        log.info("Bank account list is empty");
      }
    } catch (Exception e) {
      log.error("Failed to list {}: {} from crdb! ", field, value, e);
    }
    return bankAccounts.build();
  }

  @GetMapping("/bank-account/{id}")
  public BankProto.BankAccount getBankAccount(@PathVariable("id") String id) {
    log.info("Received request to get bank account for id: {}", id);
    return bankAccountService.getBankAccount(id).orElse(BankProto.BankAccount.newBuilder().build());
  }

  @GetMapping("/bank-account/{id}/balance")
  public BankProto.BankAccount getBankAccountBalance(@PathVariable("id") String id) {
    log.info("Received request to get balance of bank account for id: {}", id);
    return bankAccountService
        .getBankAccountBalance(id)
        .orElse(BankProto.BankAccount.newBuilder().build());
  }

  @PatchMapping("/bank-account/{id}/balance")
  public boolean updateBankAccountBalance(
      @PathVariable("id") String id, @RequestBody BankProto.BankAccount bankAccount) {
    log.info(
        "Received request to update balance of bank account for id: {} to {}",
        id,
        bankAccount.getBalance());
    return bankAccountService.updateBankAccountBalance(id, bankAccount.getBalance());
  }

  @DeleteMapping("/bank-account/{id}")
  public boolean deleteBankAccount(@PathVariable("id") String id) {
    log.info("Received request to del bank account for id: {}", id);
    return bankAccountService.deleteBankAccount(id);
  }

  @GetMapping("/bank-accounts/backup")
  public boolean backUp(
      @RequestParam("destinationFolder") String destinationFolder,
      @RequestParam("delimiter") String delimiter) {
    String zonedDateTime = BankHelperUtil.getZonedDateTimeForFileName();
    log.info(
        "Received request to initiate writing of db content as backup to {} using {} separated, at time {}",
        destinationFolder,
        delimiter,
        zonedDateTime);
    String destinationFileName = String.format("%s-%s.csv", "BankAccounts", zonedDateTime);
    boolean result =
        BankHelperUtil.writeToFile(
            bankAccountService.extractDataInDelimitedFormat(delimiter),
            destinationFolder,
            destinationFileName);
    log.info("Backup creation result: {} for {}", result, destinationFileName);
    return result;
  }
}
