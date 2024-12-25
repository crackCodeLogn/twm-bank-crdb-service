package com.vv.personal.twm.crdb.v1.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * @author Vivek
 * @since 10/03/22
 */
class BankServiceHelperUtilTest {

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

  @Test
  void testGetZonedDateTimeForFileName() {
    String result = BankHelperUtil.getZonedDateTimeForFileName();
    assertThat(result).matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{6}");
  }
}
