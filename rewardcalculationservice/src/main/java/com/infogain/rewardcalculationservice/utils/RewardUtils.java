package com.infogain.rewardcalculationservice.utils;

import com.infogain.rewardcalculationservice.dto.CustomerWithMonthlyRewardPointsResponse;
import com.infogain.rewardcalculationservice.dto.TransactionWithCustomerResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implements RewardUtils class to implement reward calculate logic
 * Independent is from service layer
 * which can be use in all classes.
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RewardUtils {
    public static int calculateRewardPointsForTransaction(double amount) {
        int rewardPoints = 0;

        if (amount > 100) {
            amount -= 100;
            rewardPoints = (int) amount * 2 + 50;
        } else if (amount > 50) {
            amount -= 50;
            rewardPoints = (int) amount;
        }

        return rewardPoints;
    }

    public static List<CustomerWithMonthlyRewardPointsResponse> CalculateRewardsPointForEachMonthsAndTotal(List<TransactionWithCustomerResponse> transactionWithCustomerResponseList) {
        Map<Long, String> mapNameWithID = new HashMap<>();
        List<CustomerWithMonthlyRewardPointsResponse> customerWithMonthlyRewardPointsResponseArrayList = new ArrayList<>();

        for (TransactionWithCustomerResponse transactionWithCustomerResponse : transactionWithCustomerResponseList) {
            mapNameWithID.put(transactionWithCustomerResponse.getCustomerId(), transactionWithCustomerResponse.getCustomerName());
        }
        for (Map.Entry<Long, String> entry : mapNameWithID.entrySet()) {
            CustomerWithMonthlyRewardPointsResponse customerWithMonthlyRewardPointsResponse = new CustomerWithMonthlyRewardPointsResponse();
            customerWithMonthlyRewardPointsResponse.setCustomerId(entry.getKey());
            customerWithMonthlyRewardPointsResponse.setName(entry.getValue());

            Map<String, Integer> mapWithMonthAndAmount = transactionWithCustomerResponseList
                    .stream()
                    .filter(t -> (Objects.equals(t.getCustomerId(), entry.getKey())))
                    .collect(Collectors.toMap(TransactionWithCustomerResponse::getMonth, TransactionWithCustomerResponse::getRewardPoints));

            int totalRewards = mapWithMonthAndAmount.values().stream().mapToInt(integer -> integer).sum();
            mapWithMonthAndAmount.put("Total Rewards", totalRewards);
            customerWithMonthlyRewardPointsResponse.setMonthlyListWithRewards(mapWithMonthAndAmount);
            customerWithMonthlyRewardPointsResponseArrayList.add(customerWithMonthlyRewardPointsResponse);
        }
        return customerWithMonthlyRewardPointsResponseArrayList;
    }

    public static Date getLast3MonthDate() {
        LocalDate currentDate = LocalDate.now();
        LocalDate threeMonthsAgo = currentDate.minusMonths(3);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String threeMonthsAgoStr = threeMonthsAgo.format(formatter);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(threeMonthsAgoStr);
        } catch (Exception ignored) {
        }
        return date;
    }

    public static String getMonthFromDate(String date) {
        LocalDate currentDate
                = LocalDate.parse(date);
        return currentDate.getMonth().toString();
    }

}
