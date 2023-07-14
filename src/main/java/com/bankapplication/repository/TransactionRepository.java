package com.bankapplication.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankapplication.entity.Transaction;



@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    Page<Transaction> findAllByFromAccountIdOrToAccountId(Integer fromAccountId, Integer toAccountId, Pageable lastTenRecords);
}
