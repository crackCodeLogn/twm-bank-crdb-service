package com.vv.personal.twm.crdb.v1.data.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

/**
 * @author Vivek
 * @since 17/02/22
 */
@Entity
@Table(name = "bank")
public class BankEntity {

    @Column(name = "name")
    private String bankName;

    @Id
    @Column(name = "ifsc")
    private String ifsc;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "bank_type")
    private String bankType;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "cre_ts")
    private Instant createdTimestamp;

    public BankEntity() {
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("bankName", bankName)
                .append("ifsc", ifsc)
                .append("contactNumber", contactNumber)
                .append("bankType", bankType)
                .append("isActive", isActive)
                .append("createdTimestamp", createdTimestamp)
                .toString();
    }

    public String getBankName() {
        return bankName;
    }

    public BankEntity setBankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public String getIfsc() {
        return ifsc;
    }

    public BankEntity setIfsc(String ifsc) {
        this.ifsc = ifsc;
        return this;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public BankEntity setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        return this;
    }

    public String getBankType() {
        return bankType;
    }

    public BankEntity setBankType(String bankType) {
        this.bankType = bankType;
        return this;
    }

    public Boolean isActive() {
        return isActive;
    }

    public BankEntity setActive(Boolean active) {
        isActive = active;
        return this;
    }

    public Instant getCreatedTimestamp() {
        return createdTimestamp;
    }

    public BankEntity setCreatedTimestamp(Instant createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }
}