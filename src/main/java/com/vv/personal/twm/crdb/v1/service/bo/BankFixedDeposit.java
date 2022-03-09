package com.vv.personal.twm.crdb.v1.service.bo;

import lombok.Builder;
import lombok.Getter;

import java.sql.Date;
import java.time.Instant;

/**
 * @author Vivek
 * @since 09/03/22
 */
@Builder
@Getter
@Deprecated
public class BankFixedDeposit {

    private String fdNumber;
    private String userFd;
    private String customerId;
    private String bankIfsc;
    private Double depositAmount;
    private Double rateOfInterest;
    private Date startDate;
    private Date endDate;
    private Integer months;
    private String nominee;
    private Double expectedAmount; //nullable
    private Double expectedInterest; //nullable
    private String originalUserFd;
    private Boolean isActive;
    private Instant createdTimestamp;
}