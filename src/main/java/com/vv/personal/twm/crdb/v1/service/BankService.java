package com.vv.personal.twm.crdb.v1.service;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import java.util.List;

/**
 * @author Vivek
 * @since 09/03/22
 */
public interface BankService extends BackUpAndRestore {
  boolean addBank(BankProto.Bank bank);

  int addBanks(BankProto.BankList bankList);

  BankProto.Bank getBank(String ifsc);

  BankProto.BankList getBanks();

  boolean deleteBank(String ifsc);

  boolean deleteBanks();

  List<BankProto.Bank> getAllByName(String name);

  List<BankProto.Bank> getAllByType(String type);

  List<BankProto.Bank> getAllByIfsc(String ifsc);

  List<BankProto.Bank> getAllByCountryCode(String ccy);
}
