package com.bankapplication.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.bankapplication.model.CreateAccountRequest;
import com.bankapplication.model.TransferBalanceRequest;

@Service
public class KafkaMessageProducer {
   
	@Autowired
    KafkaTemplate<String, String> kafkaTemplate;
	@Autowired
	KafkaTemplate<String,TransferBalanceRequest> kafkaTemplateForTransfer;

    @Autowired
    KafkaTemplate<String, CreateAccountRequest> kafkaTemplateForAccount;

    public static final Log LOGGER = LogFactory.getLog(KafkaMessageProducer.class);

    public void sendTransactionMessage(TransferBalanceRequest transferBalanceRequest) {
    	kafkaTemplateForTransfer.send("transaction_message", transferBalanceRequest);
        LOGGER.info("FUNDS TRANSFER SUCCESFULLY ....!");
    }

    public void sendAccountObject(CreateAccountRequest createAccountRequest) {
        kafkaTemplateForAccount.send("account_creation",createAccountRequest);
        LOGGER.info("SUCCESFULLY CREATED ACCOUNT...!");
    }
    public void sendDeleteAccount(String message) {
    	kafkaTemplate.send("delete_account", message);
    	LOGGER.info(" SUCCESFULLY DELETED ACCOUNT...!");
    }
}
