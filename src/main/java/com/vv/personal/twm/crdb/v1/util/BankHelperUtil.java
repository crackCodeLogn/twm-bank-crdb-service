package com.vv.personal.twm.crdb.v1.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Vivek
 * @since 10/03/22
 */
@Slf4j
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

    public static boolean writeToFile(String data, String destinationFolder, String destinationFileName) {
        String destinationFile = String.format("%s/%s", destinationFolder, destinationFileName);
        try (FileWriter fileWriter = new FileWriter(destinationFile)) {
            fileWriter.write(data);
            return true;
        } catch (IOException e) {
            log.error("Failed to write content to file: {}. ", destinationFile, e);
        }
        return false;
    }
}