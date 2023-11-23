package com.vv.personal.twm.crdb.v1.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.sql.Date;
import java.time.Instant;

/**
 * @author Vivek
 * @since 17/02/22
 */
@Entity
@Table(name = "bank_fd")
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

    @Column(name = "cre_ts")
    private Instant createdTimestamp;

    public BankFixedDepositEntity() {
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("fdNumber", fdNumber)
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
                .append("createdTimestamp", createdTimestamp)
                .toString();
    }

    public String getFdNumber() {
        return fdNumber;
    }

    public BankFixedDepositEntity setFdNumber(String fdNumber) {
        this.fdNumber = fdNumber;
        return this;
    }

    public String getUserFd() {
        return userFd;
    }

    public BankFixedDepositEntity setUserFd(String userFd) {
        this.userFd = userFd;
        return this;
    }

    public String getCustomerId() {
        return customerId;
    }

    public BankFixedDepositEntity setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public String getBankIfsc() {
        return bankIfsc;
    }

    public BankFixedDepositEntity setBankIfsc(String bankIfsc) {
        this.bankIfsc = bankIfsc;
        return this;
    }

    public Double getDepositAmount() {
        return depositAmount;
    }

    public BankFixedDepositEntity setDepositAmount(Double depositAmount) {
        this.depositAmount = depositAmount;
        return this;
    }

    public Double getRateOfInterest() {
        return rateOfInterest;
    }

    public BankFixedDepositEntity setRateOfInterest(Double rateOfInterest) {
        this.rateOfInterest = rateOfInterest;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    public BankFixedDepositEntity setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public BankFixedDepositEntity setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public Integer getMonths() {
        return months;
    }

    public BankFixedDepositEntity setMonths(Integer months) {
        this.months = months;
        return this;
    }

    public Integer getDays() {
        return days;
    }

    public BankFixedDepositEntity setDays(Integer days) {
        this.days = days;
        return this;
    }

    public String getInterestType() {
        return interestType;
    }

    public BankFixedDepositEntity setInterestType(String interestType) {
        this.interestType = interestType;
        return this;
    }

    public String getNominee() {
        return nominee;
    }

    public BankFixedDepositEntity setNominee(String nominee) {
        this.nominee = nominee;
        return this;
    }

    public Double getExpectedAmount() {
        return expectedAmount;
    }

    public BankFixedDepositEntity setExpectedAmount(Double expectedAmount) {
        this.expectedAmount = expectedAmount;
        return this;
    }

    public Double getExpectedInterest() {
        return expectedInterest;
    }

    public BankFixedDepositEntity setExpectedInterest(Double expectedInterest) {
        this.expectedInterest = expectedInterest;
        return this;
    }

    public String getOriginalUserFd() {
        return originalUserFd;
    }

    public BankFixedDepositEntity setOriginalUserFd(String originalUserFd) {
        this.originalUserFd = originalUserFd;
        return this;
    }

    public Boolean isActive() {
        return isActive;
    }

    public BankFixedDepositEntity setActive(Boolean active) {
        isActive = active;
        return this;
    }

    public Instant getCreatedTimestamp() {
        return createdTimestamp;
    }

    public BankFixedDepositEntity setCreatedTimestamp(Instant createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }
}