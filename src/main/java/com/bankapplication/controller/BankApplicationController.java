package com.bankapplication.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bankapplication.entity.Account;
import com.bankapplication.entity.Transaction;
import com.bankapplication.exception.BankException;
import com.bankapplication.model.CreateAccountRequest;
import com.bankapplication.model.Response;
import com.bankapplication.model.TransferBalanceRequest;
import com.bankapplication.service.AccountService;
import com.bankapplication.service.KafkaMessageProducer;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1")
@Validated
public class BankApplicationController {

    @Autowired
    private AccountService accountService;
    
    @Autowired
    KafkaMessageProducer kafkaMessageProducer;

    @Value("${kafka.enable}")
    private boolean kafkaEnable;
    @Operation(summary = "Create Account", description = "This endpoint is responsible for creating an bank account ")
    @PostMapping(value = "/create-account", consumes = "application/json")
    public ResponseEntity<Response> createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest) throws Exception {
        Response response = new Response();
        if (!kafkaEnable) {
            Account account = accountService.saveUser(createAccountRequest);
            response.setMessage("SUCCESS");
            response.setAccount(account);
        } else {
            kafkaMessageProducer.sendAccountObject(createAccountRequest);
            response.setMessage("SuccessFully Posted Request ..!");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    

    @Operation(summary = "Check Account Balance",
            description = "This endpoint is responsible for verifying account balance")
    @GetMapping(value = "/account-balance/{accountNumber}")
    public ResponseEntity<Account> viewAccountBalance(@NotNull @PathVariable Integer accountNumber) throws BankException {
        return new ResponseEntity<>(accountService.findByAccountNumber(accountNumber), HttpStatus.OK);
    }
    

    @Operation(summary = "Transfer Funds", description = "This endpoint is responsible for transferring funds from one account to another")
    @PostMapping(value = "/transfer-funds", consumes = "application/json")
    public ResponseEntity<Response> sendMoney(@RequestBody TransferBalanceRequest transferBalanceRequest) throws BankException {
        Transaction transaction = accountService.sendMoney(transferBalanceRequest);
        Response response = new Response();
        kafkaMessageProducer.sendTransactionMessage(transferBalanceRequest);
        response.setMessage("SUCCESSFULLY TRANFER THE AMOUNT");
        response.setTransaction(transaction);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "Get Account Statement", description = "This endpoint is responsible for fecting the account transactions ")
    @GetMapping(value = "/account-statement/{accountNumber}")
    public ResponseEntity<Page<Transaction>> getStatement(@NotNull @PathVariable Integer accountNumber, @RequestParam(value="page", required = false, defaultValue = "0") int page) throws BankException {
        return new ResponseEntity<>(accountService.getStatement(accountNumber,page), HttpStatus.OK);
    }
    
    
    @Operation(summary = "Delete Account", description = "This endpoint is responsible for Deleting the account ")
    @DeleteMapping(value="/delete-account/{accountNumber}")
    public ResponseEntity<Account> deleteAccount(@PathVariable Integer accountNumber){
    	return new ResponseEntity<>(accountService.deleteAccount(accountNumber), HttpStatus.OK);
    }
    
    
    @Operation(summary = "Get All Account", description = "This endpoint is responsible for showing all accounts ")
    @GetMapping(value="/getAll-account/{accountNumber}")
    public ResponseEntity<List<Account>> getAllAccounts(){
    	return new ResponseEntity<>(accountService.getAllAccounts(),HttpStatus.OK);
    }



}
