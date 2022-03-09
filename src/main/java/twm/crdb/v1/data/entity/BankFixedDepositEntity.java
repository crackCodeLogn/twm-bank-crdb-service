package twm.crdb.v1.data.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.context.annotation.Primary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;
import java.time.Instant;

/**
 * @author Vivek
 * @since 17/02/22
 */
@Entity
@Table(name = "bank_fd")
public class BankFixedDepositEntity {

    @Primary
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

    @Column(name = "nominee")
    private String nominee;

    @Column(name = "amt_exp")
    private Double expectedAmount; //nullable

    @Column(name = "int_exp")
    private Double expectedInterest; //nullable

    @Column(name = "orig_user_fd")
    private String originalUserFd;

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
                .append("nominee", nominee)
                .append("expectedAmount", expectedAmount)
                .append("expectedInterest", expectedInterest)
                .append("originalUserFd", originalUserFd)
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

    public Instant getCreatedTimestamp() {
        return createdTimestamp;
    }

    public BankFixedDepositEntity setCreatedTimestamp(Instant createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }
}