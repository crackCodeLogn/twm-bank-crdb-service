package com.vv.personal.twm.crdb.v1.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.hash.Hashing;
import com.google.protobuf.Timestamp;
import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Vivek
 * @since 10/03/22
 */
@Slf4j
public final class BankHelperUtil {
  private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYYMMDD =
      DateTimeFormatter.ofPattern("yyyyMMdd");

  private BankHelperUtil() {}

  private static final BiMap<String, String> ccyCountryMap =
      ImmutableBiMap.<String, String>builder().put("CAD", "CA").put("INR", "IN").build();

  public static String getCountryForCcy(String ccy) {
    return ccyCountryMap.get(ccy);
  }

  public static String getCcyForCountry(String country) {
    return ccyCountryMap.inverse().get(country);
  }

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

  public static List<BankProto.BankAccountType> getListBankAccountTypes(String bankAccountTypes) {
    return Arrays.stream(bankAccountTypes.split("\\|"))
        .map(BankProto.BankAccountType::valueOf)
        .collect(Collectors.toList());
  }

  public static String getStringBankAccountTypes(
      List<BankProto.BankAccountType> bankAccountTypesList) {
    return StringUtils.join(
        bankAccountTypesList.stream().map(BankProto.BankAccountType::name).toList(), "|");
  }

  public static String generateSha512Hash(String data) {
    return Hashing.sha512().hashString(data, StandardCharsets.UTF_8).toString();
  }
}
