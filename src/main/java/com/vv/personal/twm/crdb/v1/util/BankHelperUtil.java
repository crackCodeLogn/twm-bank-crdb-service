package com.vv.personal.twm.crdb.v1.util;

import com.google.protobuf.Timestamp;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Vivek
 * @since 10/03/22
 */
@Slf4j
public final class BankHelperUtil {
  private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYYMMDD =
      DateTimeFormatter.ofPattern("yyyyMMdd");

  private BankHelperUtil() {}

  public static String getDateInYyyyMmDd(Date date) {
    return date.toLocalDate().format(DATE_TIME_FORMATTER_YYYYMMDD);
  }

  public static Date getDateFromYyyyMmDd(String date) {
    return Date.valueOf(LocalDate.parse(date, DATE_TIME_FORMATTER_YYYYMMDD));
  }

  public static String getZonedDateTimeForFileName() {
    String zonedDateTime = ZonedDateTime.now(ZoneId.of("IST", ZoneId.SHORT_IDS)).toString();
    zonedDateTime = zonedDateTime.substring(0, zonedDateTime.indexOf('.')).replace(":", "");
    return zonedDateTime;
  }

  public static boolean writeToFile(
      String data, String destinationFolder, String destinationFileName) {
    String destinationFile = String.format("%s/%s", destinationFolder, destinationFileName);
    try (FileWriter fileWriter = new FileWriter(destinationFile)) {
      fileWriter.write(data);
      return true;
    } catch (IOException e) {
      log.error("Failed to write content to file: {}. ", destinationFile, e);
    }
    return false;
  }

  public static Instant fromTimestamp(Timestamp timestamp) {
    return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
  }

  public static Timestamp fromInstant(Instant instant) {
    return Timestamp.newBuilder()
        .setSeconds(instant.getEpochSecond())
        .setNanos(instant.getNano())
        .build();
  }

  public static UUID getUUID(String id) {
    return UUID.fromString(id);
  }

  public static String getUUIDString(UUID uuid) {
    return uuid.toString();
  }
}
