package com.vv.personal.twm.crdb.v1.data.repository;

import com.vv.personal.twm.crdb.v1.data.entity.BankAccountEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Vivek
 * @since 2024-12-23
 */
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, UUID> {

  @Query(value = "SELECT * from bank_account where account_name ~ :name", nativeQuery = true)
  List<BankAccountEntity> getAllByMatchingName(@Param("name") String name);

  @Query(value = "SELECT * from bank_account where bank_ifsc ~ :ifsc", nativeQuery = true)
  List<BankAccountEntity> getAllByMatchingIfsc(@Param("ifsc") String ifsc);

  List<BankAccountEntity> getAllByCurrencyCode(String currencyCode);

  Optional<BankAccountEntity> findByExternalId(String externalId);

  boolean existsByExternalId(String externalId);
}
