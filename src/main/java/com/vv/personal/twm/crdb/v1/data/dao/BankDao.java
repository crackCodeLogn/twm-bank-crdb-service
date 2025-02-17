package com.vv.personal.twm.crdb.v1.data.dao;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.crdb.v1.data.entity.BankEntity;
import java.util.List;
import java.util.Optional;

/**
 * @author Vivek
 * @since 2025-02-17
 */
public interface BankDao {

  Optional<String> getExternalId(String ifsc);

  Optional<String> getId(String externalId);

  boolean doesBankExist(String ifsc);

  boolean addBank(BankProto.Bank bank);

  Optional<BankProto.Bank> getBank(String ifsc);

  Optional<BankProto.BankList> getAllBanks();

  Optional<BankProto.BankList> getAllBanksByMatchingName(String name);

  Optional<BankProto.BankList> getAllBanksByMatchingType(String name);

  Optional<BankProto.BankList> getAllBankByMatchingIfsc(String ifsc);

  Optional<BankProto.BankList> getAllBanksByCountryCode(String countryCode);

  boolean deleteBank(String ifsc);

  List<BankEntity> getBanksInEntityFormat();
}
