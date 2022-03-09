package com.vv.personal.twm.crdb.v1.service.bo;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

/**
 * @author Vivek
 * @since 09/03/22
 */
@Builder
@Getter
@Deprecated
public class Bank {

    private String bankName;
    private String ifsc;
    private String contactNumber;
    private String bankType;
    private Boolean isActive;
    private Instant createdTimestamp;
}