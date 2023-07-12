package com.infogain.rewardcalculationservice.controller;

import com.infogain.rewardcalculationservice.dto.CreateCustomerRequest;
import com.infogain.rewardcalculationservice.dto.CustomerResponse;
import com.infogain.rewardcalculationservice.dto.CustomerWithMonthlyRewardPointsResponse;
import com.infogain.rewardcalculationservice.dto.UpdateCustomerRequest;
import com.infogain.rewardcalculationservice.exceptions.CustomerExistException;
import com.infogain.rewardcalculationservice.exceptions.CustomerNotFoundException;
import com.infogain.rewardcalculationservice.service.RewardPointsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Implements RewardPointsController to add APIs
 */
@Tag(name = "Reward Points", description = "Endpoints for Reward points operations")
@RestController
@RequestMapping("/api/v1/reward-points")
@Slf4j
public class RewardPointsController {
    @Autowired
    private RewardPointsService rewardPointsService;

    @Operation(summary = "Insert Customer,", description = "Insert a new Customer")
    @PostMapping("/customers")
    public ResponseEntity<String> addCustomer(@RequestBody CreateCustomerRequest createCustomerRequest) throws CustomerExistException {
        return new ResponseEntity<>(rewardPointsService.createCustomer(createCustomerRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Update Customer transaction,", description = "Update a existing Customer transaction")
    @PutMapping("/customers/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable Long id, @RequestBody UpdateCustomerRequest request) throws CustomerNotFoundException {
        return new ResponseEntity<>(rewardPointsService.updateCustomer(id, request), HttpStatus.OK);
    }

    @Operation(summary = "Fetch a specific Customer with All transaction", description = "Fetch the item using customerId")
    @GetMapping("/customer/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable(value = "id") Long customerId)
            throws CustomerNotFoundException {
        return ResponseEntity.ok().body(rewardPointsService.getCustomerById(customerId));
    }

    @Operation(summary = "Fetch a all customer with last 3 months transaction group by month and calculate the reward point monthly and total ", description = "fetch all the customoer Customer")
    @GetMapping("/monthly-rewards-point")
    public ResponseEntity<List<CustomerWithMonthlyRewardPointsResponse>> findAllTransactionOfLast3MonthsForEachCustomer() {
        return ResponseEntity.ok().body(rewardPointsService.findAllTransactionOfLast3MonthsForEachCustomer());
    }

    @Operation(summary = "Delete Customer", description = "Delete a existing Customer")
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) throws CustomerNotFoundException {
        return ResponseEntity.ok().body(rewardPointsService.deleteCustomer(id));
    }
}
