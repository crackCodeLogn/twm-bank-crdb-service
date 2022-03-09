package com.vv.personal.twm.crdb.v1.util;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Vivek
 * @since 10/03/22
 */
class BankHelperUtilTest {

    @Test
    void testGetDateInYyyyMmDd() {
        String date = BankHelperUtil.getDateInYyyyMmDd(Date.valueOf(LocalDate.of(2022, 3, 10)));
        assertThat(date).hasToString("20220310");
    }

    @Test
    void testGetDateFromYyyyMmDd() {
        Date date = BankHelperUtil.getDateFromYyyyMmDd("20220310");
        assertThat(date).hasToString("2022-03-10");
    }
}