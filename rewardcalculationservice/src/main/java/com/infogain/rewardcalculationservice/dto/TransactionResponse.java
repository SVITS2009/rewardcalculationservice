package com.infogain.rewardcalculationservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TransactionResponse {
    private Double amount;
    private Date transactionDate;
}
