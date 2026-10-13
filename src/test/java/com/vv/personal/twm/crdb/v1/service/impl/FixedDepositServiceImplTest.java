package com.vv.personal.twm.crdb.v1.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import com.vv.personal.twm.crdb.v1.data.dao.BankFixedDepositDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Vivek
 * @since 2026-10-12
 */
@ExtendWith(MockitoExtension.class)
class FixedDepositServiceImplTest {

  @Mock BankFixedDepositDao fixedDepositDao;

  @InjectMocks FixedDepositServiceImpl fixedDepositService;

  @Test
  void getNewFdNumberForExpiringNrFd_Fail_IllegalArg1() {
    try {
      fixedDepositService.getNewFdNumberForExpiringNrFd(null);
      fail();
    } catch (RuntimeException e) {
      assertEquals("fd number to expire cannot be empty", e.getMessage());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  void getNewFdNumberForExpiringNrFd_Fail_IllegalArg2() {
    try {
      fixedDepositService.getNewFdNumberForExpiringNrFd("");
      fail();
    } catch (RuntimeException e) {
      assertEquals("fd number to expire cannot be empty", e.getMessage());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  void getNewFdNumberForExpiringNrFd_Fail_IllegalArg3() {
    try {
      fixedDepositService.getNewFdNumberForExpiringNrFd("  ");
      fail();
    } catch (RuntimeException e) {
      assertEquals("fd number to expire cannot be empty", e.getMessage());
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  void getNewFdNumberForExpiringNrFd_Pass1() {
    when(fixedDepositDao.getFixedDeposit("12343fqfer343-343rf3_exp"))
        .thenReturn(FixedDepositProto.FixedDeposit.newBuilder().build());

    assertEquals(
        "12343fqfer343-343rf3_exp",
        fixedDepositService.getNewFdNumberForExpiringNrFd("12343fqfer343-343rf3"));
  }

  @Test
  void getNewFdNumberForExpiringNrFd_Pass2() {
    when(fixedDepositDao.getFixedDeposit("12343fqfer343-343rf3_exp2"))
        .thenReturn(FixedDepositProto.FixedDeposit.newBuilder().build());

    assertEquals(
        "12343fqfer343-343rf3_exp2",
        fixedDepositService.getNewFdNumberForExpiringNrFd("12343fqfer343-343rf3_exp"));
  }

  @Test
  void getNewFdNumberForExpiringNrFd_Pass3() {
    when(fixedDepositDao.getFixedDeposit("12343fqfer343-343rf3_exp5"))
        .thenReturn(FixedDepositProto.FixedDeposit.newBuilder().build());

    assertEquals(
        "12343fqfer343-343rf3_exp5",
        fixedDepositService.getNewFdNumberForExpiringNrFd("12343fqfer343-343rf3_exp4"));
  }

  @Test
  void getNewFdNumberForExpiringNrFd_Pass4() {
    when(fixedDepositDao.getFixedDeposit("12343fqfer343-343rf3_exp21"))
        .thenReturn(FixedDepositProto.FixedDeposit.newBuilder().build());

    assertEquals(
        "12343fqfer343-343rf3_exp21",
        fixedDepositService.getNewFdNumberForExpiringNrFd("12343fqfer343-343rf3_exp20"));
  }

  @Test
  void getNewFdNumberForExpiringNrFd_Pass5() {
    when(fixedDepositDao.getFixedDeposit("12343fqfer343-343rf3_exp"))
        .thenReturn(
            FixedDepositProto.FixedDeposit.newBuilder()
                .setFdNumber("12343fqfer343-343rf3_exp")
                .build());
    when(fixedDepositDao.getFixedDeposit("12343fqfer343-343rf3_exp2"))
        .thenReturn(
            FixedDepositProto.FixedDeposit.newBuilder()
                .setFdNumber("12343fqfer343-343rf3_exp2")
                .build());
    when(fixedDepositDao.getFixedDeposit("12343fqfer343-343rf3_exp3"))
        .thenReturn(FixedDepositProto.FixedDeposit.newBuilder().build());

    assertEquals(
        "12343fqfer343-343rf3_exp3",
        fixedDepositService.getNewFdNumberForExpiringNrFd("12343fqfer343-343rf3"));
  }
}
