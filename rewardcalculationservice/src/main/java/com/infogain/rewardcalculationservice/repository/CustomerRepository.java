package com.infogain.rewardcalculationservice.repository;

import com.infogain.rewardcalculationservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByName(String name);

    @Query(
            value = "SELECT customer.name AS customer_name,  transaction.transaction_date, " +
                    "sum(transaction.amount), transaction.customer_id " +
                    "FROM customer JOIN transaction ON customer.id=transaction.customer_id " +
                    "group by customer_id, transaction_date having transaction.transaction_date >= ?1",
            nativeQuery = true)
    List<Object[]> findAllTransactionOfLast3MonthsForEachCustomer(Date date);
}