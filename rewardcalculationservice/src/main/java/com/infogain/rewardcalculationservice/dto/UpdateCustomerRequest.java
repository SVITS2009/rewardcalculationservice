package com.infogain.rewardcalculationservice.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class UpdateCustomerRequest {
    @NonNull
    private List<Double> amountList;
}
