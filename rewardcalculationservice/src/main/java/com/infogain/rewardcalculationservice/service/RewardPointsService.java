package com.infogain.rewardcalculationservice.service;

import com.infogain.rewardcalculationservice.dto.*;
import com.infogain.rewardcalculationservice.exceptions.CustomerExistException;
import com.infogain.rewardcalculationservice.exceptions.CustomerNotFoundException;

import java.util.List;

/**
 * Implements RewardPointsService to declare
 * service API which interacts with repository
 */
public interface RewardPointsService {
    String createCustomer(CreateCustomerRequest request) throws CustomerExistException;
    String updateCustomer(Long customerId, UpdateCustomerRequest request) throws CustomerNotFoundException;
    CustomerResponse getCustomerById(Long customerId) throws CustomerNotFoundException;
    List<CustomerWithMonthlyRewardPointsResponse> findAllTransactionOfLast3MonthsForEachCustomer();

    String deleteCustomer(Long customerId) throws CustomerNotFoundException;
}
