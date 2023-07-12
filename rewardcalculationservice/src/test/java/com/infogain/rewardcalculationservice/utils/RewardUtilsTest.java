package com.infogain.rewardcalculationservice.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RewardUtilsTest {

    @Test
    void testCalculateRewardPointsForTransactionWith100$() {
        Assertions.assertEquals(50, RewardUtils.calculateRewardPointsForTransaction(100d));
    }

    @Test
    void testCalculateRewardPointsForTransactionWith200$() {
        Assertions.assertEquals(250, RewardUtils.calculateRewardPointsForTransaction(200d));
    }

    @Test
    void testCalculateRewardPointsForTransactionWith40$() {
        Assertions.assertEquals(0, RewardUtils.calculateRewardPointsForTransaction(50d));
    }
}
