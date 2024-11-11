package com.vv.personal.twm.crdb.v1.data.repository;

import com.vv.personal.twm.crdb.v1.data.entity.BankFixedDepositEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Vivek
 * @since 17/02/22
 */
@Repository
public interface BankFixedDepositRepository extends JpaRepository<BankFixedDepositEntity, String> {

    @Query(value = "SELECT * from bank_fd where ifsc_bank ~ :bank", nativeQuery = true)
    List<BankFixedDepositEntity> getAllByMatchingBank(@Param("bank") String bank);

    @Query(value = "SELECT * from bank_fd where user_fd ~ :user", nativeQuery = true)
    List<BankFixedDepositEntity> getAllByMatchingUser(@Param("user") String user);

    @Query(value = "SELECT * from bank_fd where orig_user_fd ~ :origUser", nativeQuery = true)
    List<BankFixedDepositEntity> getAllByMatchingOriginalUser(@Param("origUser") String origUser);

    @Query(value = "SELECT * from bank_fd where fd_number ~ :fdNumber", nativeQuery = true)
    List<BankFixedDepositEntity> getAllByMatchingFdNumber(@Param("fdNumber") String fdNumber);

    @Modifying
    @Transactional
    @Query(value = "UPDATE bank_fd SET is_active = :isActive where fd_number = :fdNumber", nativeQuery = true)
    int updateFixedDepositActiveStatus(@Param("fdNumber") String fdNumber,
                                       @Param("isActive") Boolean isActive);

    @Modifying
    @Transactional
    @Query(value = "UPDATE bank_fd SET fd_number = :newFdNumber where fd_number = :fdNumber", nativeQuery = true)
    int expireNrFd(@Param("fdNumber") String fdNumber, @Param("newFdNumber") String newFdNumber);

    @Modifying
    @Transactional
    @Query(value = "UPDATE bank_fd SET int_exp = :interest, amt_exp = :amount, freeze = 1 where fd_number = :fdNumber", nativeQuery = true)
    int freezeTotalAmount(@Param("fdNumber") String fdNumber, @Param("interest") Double interest,
                          @Param("amount") Double amount);
}