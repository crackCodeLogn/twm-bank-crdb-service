package twm.crdb.v1.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twm.crdb.v1.data.entity.BankEntity;
import twm.crdb.v1.data.entity.BankFixedDepositEntity;

/**
 * @author Vivek
 * @since 17/02/22
 */
@Repository
public interface BankFixedDepositRepository extends JpaRepository<BankFixedDepositEntity, String> {

}