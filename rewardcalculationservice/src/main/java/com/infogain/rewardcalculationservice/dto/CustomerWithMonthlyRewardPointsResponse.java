package com.infogain.rewardcalculationservice.dto;

import lombok.Data;

import java.util.Map;

@Data
public class CustomerWithMonthlyRewardPointsResponse {
    private Long customerId;
    private String name;
    private Map<String, Integer> monthlyListWithRewards;
}
