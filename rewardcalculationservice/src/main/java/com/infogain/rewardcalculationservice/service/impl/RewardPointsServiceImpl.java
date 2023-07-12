package com.infogain.rewardcalculationservice.service.impl;


import com.infogain.rewardcalculationservice.dto.*;
import com.infogain.rewardcalculationservice.entity.Customer;
import com.infogain.rewardcalculationservice.entity.Transaction;
import com.infogain.rewardcalculationservice.exceptions.CustomerExistException;
import com.infogain.rewardcalculationservice.exceptions.CustomerNotFoundException;
import com.infogain.rewardcalculationservice.repository.CustomerRepository;
import com.infogain.rewardcalculationservice.repository.TransactionRepository;
import com.infogain.rewardcalculationservice.service.RewardPointsService;
import com.infogain.rewardcalculationservice.utils.RewardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RewardPointsServiceImpl implements RewardPointsService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public String createCustomer(CreateCustomerRequest request) throws CustomerExistException {
        if (customerRepository.findByName(request.getName()) != null) {
            throw new CustomerExistException("Customer: " + request.getName() + " already Exist.");
        }
        Customer customer = new Customer();
        customer.setName(request.getName());
        List<Transaction> transactionList = new ArrayList<>();

        for (Double amount : request.getAmountList()) {
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setTransactionDate(new Date());
            transaction.setCustomer(customer);
            transactionList.add(transaction);
        }
        customer.setTransactionList(transactionList);
        Customer savedCustomer = customerRepository.save(customer);
        return "Customer id : " + savedCustomer.getId() + " saved successfully.";
    }

    @Override
    public String updateCustomer(Long customerId, UpdateCustomerRequest request) throws CustomerNotFoundException {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            for (Double amount : request.getAmountList()) {
                Transaction transaction = new Transaction();
                transaction.setAmount(amount);
                transaction.setTransactionDate(new Date());
                transaction.setCustomer(customer);
                transactionRepository.save(transaction);
            }

            return "Transactions are updated for Customer id : " + customerId;
        } else {
            throw new CustomerNotFoundException("Customer: " + customerId + " doesn't Exist.");
        }
    }

    @Override
    public CustomerResponse getCustomerById(Long customerId) throws CustomerNotFoundException {
        CustomerResponse customerResponse = new CustomerResponse();

        Optional<Customer> customerById = customerRepository.findById(customerId);
        if (customerById.isPresent()) {
            Customer customer = customerById.get();
            customerResponse.setCustomerId(customer.getId());
            customerResponse.setName(customer.getName());
            List<TransactionResponse> transactionResponseList = new ArrayList<>();
            for (Transaction transaction : customer.getTransactionList()) {
                TransactionResponse transactionResponse = new TransactionResponse();
                transactionResponse.setAmount(transaction.getAmount());
                transactionResponse.setTransactionDate(transaction.getTransactionDate());
                transactionResponseList.add(transactionResponse);
            }

            customerResponse.setTransactionList(transactionResponseList);
        } else {
            throw new CustomerNotFoundException("Customer id: " + customerId + " doesn't exist.");
        }
        return customerResponse;
    }

    @Override
    public List<CustomerWithMonthlyRewardPointsResponse> findAllTransactionOfLast3MonthsForEachCustomer() {
        Date last3MonthDate = RewardUtils.getLast3MonthDate();
        List<Object[]> allTransactionOfLast3MonthsForEachCustomer = customerRepository.findAllTransactionOfLast3MonthsForEachCustomer(last3MonthDate);
        List<TransactionWithCustomerResponse> transactionWithCustomerResponseList = new ArrayList<>();
        for (Object[] obj : allTransactionOfLast3MonthsForEachCustomer) {
            TransactionWithCustomerResponse transactionWithCustomerResponse = new TransactionWithCustomerResponse();
            transactionWithCustomerResponse.setCustomerName((String) obj[0]);
            Date date = (Date) obj[1];
            transactionWithCustomerResponse.setMonth(RewardUtils.getMonthFromDate(date.toString()));
            BigDecimal amount = (BigDecimal) obj[2];
            transactionWithCustomerResponse.setRewardPoints(RewardUtils.calculateRewardPointsForTransaction(amount.doubleValue()));
            transactionWithCustomerResponse.setCustomerId((Long) obj[3]);
            transactionWithCustomerResponseList.add(transactionWithCustomerResponse);
        }
        return RewardUtils.CalculateRewardsPointForEachMonthsAndTotal(transactionWithCustomerResponseList);
    }

    @Override
    public String deleteCustomer(Long customerId) throws CustomerNotFoundException {
        Optional<Customer> savedCustomer = customerRepository.findById(customerId);
        if (savedCustomer.isPresent()) {
            customerRepository.deleteById(customerId);
            return "Customer id : " + customerId + " deleted successfully";
        } else {
            throw new CustomerNotFoundException("Customer id: " + customerId + " doesn't exist.");
        }

    }
}
