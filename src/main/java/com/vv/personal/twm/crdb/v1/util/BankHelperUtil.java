package com.vv.personal.twm.crdb.v1.util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Vivek
 * @since 10/03/22
 */
public final class BankHelperUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");

    private BankHelperUtil() {
    }

    public static String getDateInYyyyMmDd(Date date) {
        return date.toLocalDate().format(DATE_TIME_FORMATTER_YYYYMMDD);
    }

    public static Date getDateFromYyyyMmDd(String date) {
        return Date.valueOf(LocalDate.parse(date, DATE_TIME_FORMATTER_YYYYMMDD));
    }
}