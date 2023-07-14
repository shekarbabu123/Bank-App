package com.bankapplication.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.bankapplication.entity.Account;
import com.bankapplication.entity.Transaction;
import com.bankapplication.exception.BankException;
import com.bankapplication.model.CreateAccountRequest;
import com.bankapplication.model.TransferBalanceRequest;

public interface AccountService {

    Account findByAccountNumber(Integer accountNumber) throws BankException;
    Account saveUser(CreateAccountRequest createAccountRequest) throws Exception;
    Transaction sendMoney(TransferBalanceRequest transferBalanceRequest) throws BankException;
    Page<Transaction> getStatement(Integer accountNumber,Integer limit) throws BankException;
    Account deleteAccount(Integer accountNumber);
    List<Account> getAllAccounts();
}
