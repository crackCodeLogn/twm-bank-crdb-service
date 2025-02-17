package com.vv.personal.twm.crdb.v1.data.repository;

import com.vv.personal.twm.crdb.v1.data.entity.BankEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Vivek
 * @since 17/02/22
 */
@Repository
public interface BankRepository extends JpaRepository<BankEntity, String> {

  @Query(value = "SELECT * from bank where name ~ :name", nativeQuery = true)
  List<BankEntity> getAllByMatchingName(@Param("name") String name);

  @Query(value = "SELECT * from bank where type ~ :type", nativeQuery = true)
  List<BankEntity> getAllByMatchingType(@Param("type") String type);

  @Query(value = "SELECT * from bank where ifsc ~ :ifsc", nativeQuery = true)
  List<BankEntity> getAllByMatchingIfsc(@Param("ifsc") String ifsc);

  List<BankEntity> getAllByCountryCode(String countryCode);

  Optional<BankEntity> findByExternalId(String externalId);
}
