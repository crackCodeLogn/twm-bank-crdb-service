package com.vv.personal.twm.crdb.v1.data.repository;

import com.vv.personal.twm.crdb.v1.data.entity.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Vivek
 * @since 17/02/22
 */
@Repository
public interface BankRepository extends JpaRepository<BankEntity, String> {

}