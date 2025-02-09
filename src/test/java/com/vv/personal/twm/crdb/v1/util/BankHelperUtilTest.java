package com.vv.personal.twm.crdb.v1.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

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

  @Test
  void testGetZonedDateTimeForFileName() {
    String result = BankHelperUtil.getZonedDateTimeForFileName();
    assertThat(result).matches("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{6}");
  }

  @Test
  void testGenerateSha512Hash() {
    String result = BankHelperUtil.generateSha512Hash("123456");
    System.out.println(result);
    assertEquals(128, result.length());
  }
}
