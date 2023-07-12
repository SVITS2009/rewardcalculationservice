package com.infogain.rewardcalculationservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomerResponse {
    private Long customerId;
    private String name;
    private List<TransactionResponse> transactionList;
}
