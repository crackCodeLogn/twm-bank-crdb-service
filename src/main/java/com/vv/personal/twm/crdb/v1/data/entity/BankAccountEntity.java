package com.vv.personal.twm.crdb.v1.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Vivek
 * @since 2024-12-23
 */
@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bank_account")
public class BankAccountEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "bank_ifsc")
    private String bankIfsc;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_name")
    private String name;

    @Column(name = "transit_number")
    private long transitNumber;

    @Column(name = "institution_number")
    private long institutionNumber;

    @Column(name = "balance")
    private double balance;

    @Column(name = "bank_account_type")
    private String bankAccountType;

    @Column(name = "meta_data")
    private String metaData;

    @Column(name = "overdraft_balance")
    private double overdraftBalance;

    @Column(name = "interest_rate")
    private double interestRate;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "cre_ts")
    private Instant createdTimestamp;

    @Column(name = "last_upd_ts")
    private Instant lastUpdatedTimestamp;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("id", id).append("name", name).append("bankIfsc", bankIfsc).append("accountNumber", accountNumber).append("transitNumber", transitNumber).append("institutionNumber", institutionNumber).append("balance", balance).append("bankAccountType", bankAccountType).append("metaData", metaData).append("overdraftBalance", overdraftBalance).append("interestRate", interestRate).append("isActive", isActive).append("createdTimestamp", createdTimestamp).append("lastUpdatedTimestamp", lastUpdatedTimestamp).toString();
    }
}
