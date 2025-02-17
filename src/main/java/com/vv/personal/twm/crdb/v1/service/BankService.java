package com.vv.personal.twm.crdb.v1.service;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import java.util.Optional;

/**
 * @author Vivek
 * @since 09/03/22
 */
public interface BankService extends BackUpAndRestore {
  boolean addBank(BankProto.Bank bank);

  Optional<BankProto.Bank> getBank(String ifsc);

  boolean deleteBank(String ifsc);

  Optional<BankProto.BankList> getAllBanks();

  Optional<BankProto.BankList> getAllByName(String name);

  Optional<BankProto.BankList> getAllByType(String type);

  Optional<BankProto.BankList> getAllByIfsc(String ifsc);

  Optional<BankProto.BankList> getAllByCountryCode(String ccy);
}
