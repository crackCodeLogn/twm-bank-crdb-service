package com.vv.personal.twm.crdb.v1.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.sql.Date;
import java.time.Instant;

/**
 * @author Vivek
 * @since 17/02/22
 */
@Getter
@Builder
@Entity
@Table(name = "bank_fd")
@AllArgsConstructor
public class BankFixedDepositEntity {

    @Id
    @Column(name = "fd_number")
    private String fdNumber;

    @Column(name = "user_fd")
    private String userFd;

    @Column(name = "cust_id")
    private String customerId;

    @Column(name = "ifscBank")
    private String bankIfsc;

    @Column(name = "amt_dpt")
    private Double depositAmount;

    @Column(name = "roi")
    private Double rateOfInterest;

    @Column(name = "dt_start")
    private Date startDate;

    @Column(name = "dt_end")
    private Date endDate;

    @Column(name = "months")
    private Integer months;

    @Column(name = "days")
    private Integer days;

    @Column(name = "type_interest")
    private String interestType;

    @Column(name = "nominee")
    private String nominee;

    @Column(name = "amt_exp")
    private Double expectedAmount; //nullable

    @Column(name = "int_exp")
    private Double expectedInterest; //nullable

    @Column(name = "orig_user_fd")
    private String originalUserFd;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "type_acc")
    private Integer accountType;

    @Column(name = "freeze")
    private Integer freeze;

    @Column(name = "cre_ts")
    private Instant createdTimestamp;

    public BankFixedDepositEntity() {
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("fdNumber", fdNumber)
                .append("accType", accountType)
                .append("userFd", userFd)
                .append("customerId", customerId)
                .append("bankIfsc", bankIfsc)
                .append("depositAmount", depositAmount)
                .append("rateOfInterest", rateOfInterest)
                .append("startDate", startDate)
                .append("endDate", endDate)
                .append("months", months)
                .append("days", days)
                .append("interestType", interestType)
                .append("nominee", nominee)
                .append("expectedAmount", expectedAmount)
                .append("expectedInterest", expectedInterest)
                .append("originalUserFd", originalUserFd)
                .append("isActive", isActive)
                .append("freeze", freeze)
                .append("createdTimestamp", createdTimestamp)
                .toString();
    }
}