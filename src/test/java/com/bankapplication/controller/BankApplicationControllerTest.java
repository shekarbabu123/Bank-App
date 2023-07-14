package com.bankapplication.controller;

import com.bankapplication.entity.Account;
import com.bankapplication.entity.Transaction;
import com.bankapplication.exception.BankException;
import com.bankapplication.model.Response;
import com.bankapplication.model.TransferBalanceRequest;
import com.bankapplication.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class BankApplicationControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private BankApplicationController bankApplicationController;


    @Test
    @DisplayName("Should throw an exception when the account number is not found")
    void deleteAccountWhenAccountNumberNotFoundThenThrowException() {
        Integer accountNumber = 123456;
        when(accountService.deleteAccount(accountNumber)).thenThrow(new BankException("Account not found"));

        assertThrows(BankException.class, () -> {
            bankApplicationController.deleteAccount(accountNumber);
        });

        verify(accountService, times(1)).deleteAccount(accountNumber);
    }

    @Test
    @DisplayName("Should delete the account when the account number is valid")
    void deleteAccountWhenAccountNumberIsValid() {
        Integer accountNumber = 123456;
        Account account = new Account();
        account.setAccountId(accountNumber);
        account.setBalanceAmount(BigDecimal.ZERO);
        account.setLimitAmount(BigDecimal.ZERO);
        account.setLienAmount(BigDecimal.ZERO);

        when(accountService.deleteAccount(accountNumber)).thenReturn(account);

        ResponseEntity<Account> response = bankApplicationController.deleteAccount(accountNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());

        verify(accountService, times(1)).deleteAccount(accountNumber);
    }

    @Test
    @DisplayName("Should throw an exception when the account number is not found")
    void getStatementWhenAccountNumberNotFoundThenThrowException() {
        int accountNumber = 123456;
        int page = 0;

        when(accountService.getStatement(accountNumber, page)).thenThrow(BankException.class);

        assertThrows(BankException.class, () -> {
            bankApplicationController.getStatement(accountNumber, page);
        });

        verify(accountService, times(1)).getStatement(accountNumber, page);
    }

    @Test
    @DisplayName("Should return the account statement for the given account number and page")
    void getStatementForGivenAccountNumberAndPage() {
        Integer accountNumber = 123456;
        int page = 0;
        Page<Transaction> expectedStatement = new PageImpl<>(Collections.emptyList());

        when(accountService.getStatement(accountNumber, page)).thenReturn(expectedStatement);

        ResponseEntity<Page<Transaction>> response = bankApplicationController.getStatement(accountNumber, page);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStatement, response.getBody());

        verify(accountService, times(1)).getStatement(accountNumber, page);
    }

    @Test
    @DisplayName("Should throw an exception when the source account has insufficient balance")
    void sendMoneyWhenInsufficientBalanceThenThrowException() {
        TransferBalanceRequest transferBalanceRequest = TransferBalanceRequest.builder()
                .fromAccountNumber(123456)
                .toAccountNumber(789012)
                .amount(BigDecimal.valueOf(1000))
                .currency("USD")
                .build();

        when(accountService.sendMoney(transferBalanceRequest)).thenThrow(new BankException("Insufficient balance"));

        BankException exception = assertThrows(BankException.class, () -> {
            bankApplicationController.sendMoney(transferBalanceRequest);
        });

        assertEquals("Insufficient balance", exception.getMessage());

        verify(accountService, times(1)).sendMoney(transferBalanceRequest);
    }

    @Test
    @DisplayName("Should throw an exception when the source account number is invalid")
    void sendMoneyWhenInvalidSourceAccountNumberThenThrowException() {
        TransferBalanceRequest transferBalanceRequest = TransferBalanceRequest.builder()
                .fromAccountNumber(123456)
                .toAccountNumber(789012)
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .build();

        when(accountService.sendMoney(transferBalanceRequest)).thenThrow(new BankException("Invalid source account number"));

        assertThrows(BankException.class, () -> {
            bankApplicationController.sendMoney(transferBalanceRequest);
        });

        verify(accountService, times(1)).sendMoney(transferBalanceRequest);
    }

    @Test
    @DisplayName("Should throw an exception when the destination account number is invalid")
    void sendMoneyWhenInvalidDestinationAccountNumberThenThrowException() {
        TransferBalanceRequest transferBalanceRequest = TransferBalanceRequest.builder()
                .fromAccountNumber(123456)
                .toAccountNumber(987654)
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .build();

        when(accountService.sendMoney(transferBalanceRequest)).thenThrow(new BankException("Invalid destination account number"));

        assertThrows(BankException.class, () -> {
            bankApplicationController.sendMoney(transferBalanceRequest);
        });

        verify(accountService, times(1)).sendMoney(transferBalanceRequest);
    }

    @Test
    @DisplayName("Should transfer funds successfully between two accounts")
    void sendMoneySuccessfully() {
        TransferBalanceRequest transferBalanceRequest = TransferBalanceRequest.builder()
                .fromAccountNumber(123456)
                .toAccountNumber(789012)
                .amount(BigDecimal.valueOf(1000))
                .currency("USD")
                .build();

        Transaction transaction = Transaction.builder()
                .transactionRefId("1234567890")
                .fromAccountId(transferBalanceRequest.getFromAccountNumber())
                .toAccountId(transferBalanceRequest.getToAccountNumber())
                .transactionAmount(transferBalanceRequest.getAmount())
                .transactionCurrency(transferBalanceRequest.getCurrency())
                .build();

        when(accountService.sendMoney(transferBalanceRequest)).thenReturn(transaction);

        ResponseEntity<Response> responseEntity = bankApplicationController.sendMoney(transferBalanceRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("SUCCESS", responseEntity.getBody().getMessage());
        assertEquals(transaction, responseEntity.getBody().getTransaction());

        verify(accountService, times(1)).sendMoney(transferBalanceRequest);
    }

    @Test
    @DisplayName("Should throw BankException when account number is not found")
    void viewAccountBalanceWhenAccountNumberNotFoundThenThrowBankException() {
        Integer accountNumber = 123456;
        when(accountService.findByAccountNumber(accountNumber)).thenThrow(new BankException("Account not found"));

        // Act and Assert
        assertThrows(BankException.class, () -> {
            bankApplicationController.viewAccountBalance(accountNumber);
        });

        // Verify
        verify(accountService, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    @DisplayName("Should return account balance when account number is valid")
    void viewAccountBalanceWhenAccountNumberIsValid() {
        Integer accountNumber = 123456;
        Account account = new Account();
        account.setAccountId(accountNumber);
        account.setBalanceAmount(BigDecimal.valueOf(1000));
        when(accountService.findByAccountNumber(accountNumber)).thenReturn(account);

        ResponseEntity<Account> response = bankApplicationController.viewAccountBalance(accountNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
        verify(accountService, times(1)).findByAccountNumber(accountNumber);
    }
}