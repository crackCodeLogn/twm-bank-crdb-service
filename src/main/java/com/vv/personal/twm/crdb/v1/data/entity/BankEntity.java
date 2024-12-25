package com.vv.personal.twm.crdb.v1.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Vivek
 * @since 17/02/22
 */
@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bank")
public class BankEntity {

  @Column(name = "name")
  private String bankName;

  @Id
  @Column(name = "ifsc")
  private String ifsc;

  @Column(name = "country_code")
  private String countryCode;

  @Column(name = "contact_number")
  private String contactNumber;

  @Column(name = "bank_type")
  private String bankType;

  @Column(name = "is_active")
  private Boolean isActive;

  @Column(name = "cre_ts")
  private Instant createdTimestamp;

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
        .append("bankName", bankName)
        .append("ifsc", ifsc)
        .append("countryCode", countryCode)
        .append("contactNumber", contactNumber)
        .append("bankType", bankType)
        .append("isActive", isActive)
        .append("createdTimestamp", createdTimestamp)
        .toString();
  }
}
