package com.infogain.rewardcalculationservice.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CreateCustomerRequest {
    @NonNull
    private String name;
    @NonNull
    private List<Double> amountList;
}
