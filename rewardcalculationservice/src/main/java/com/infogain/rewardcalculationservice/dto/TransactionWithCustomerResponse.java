package com.infogain.rewardcalculationservice.dto;

import lombok.Data;

@Data
public class TransactionWithCustomerResponse {
    private String customerName;
    private String month;
    private Integer rewardPoints;
    private Long customerId;
}
