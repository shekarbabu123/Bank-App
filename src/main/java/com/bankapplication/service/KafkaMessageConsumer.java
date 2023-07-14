package com.bankapplication.service;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bankapplication.model.CreateAccountRequest;
import com.bankapplication.model.TransferBalanceRequest;

@Service
public class KafkaMessageConsumer {
    public static final Log LOGGER = LogFactory.getLog(KafkaMessageConsumer.class);

    @Autowired
    private AccountService accountService;

    @KafkaListener(topics = "transaction_message", groupId = "transaction_group")
    public void listenTransactionMessage(TransferBalanceRequest transferBalanceRequest) {
        LOGGER.info("MESSAGE RECEIVED FROM : " + transferBalanceRequest);
        accountService.sendMoney(transferBalanceRequest);
    }

    @KafkaListener(topics = "account_creation", groupId = "account_group")
    public void listenAccountMessage(CreateAccountRequest createAccountRequest) throws Exception {
        LOGGER.info("MESSAGE RECEIVED FROM : " + createAccountRequest.toString());
        accountService.saveUser(createAccountRequest);
    }
    @KafkaListener(topics="delete_account", groupId="delete_group")
    public void listenDeleteAccount(Integer accountNumber) {
        LOGGER.info("MESSAGE RECEIVED FROM :"+ accountNumber);
        accountService.deleteAccount(accountNumber);
    }
}
