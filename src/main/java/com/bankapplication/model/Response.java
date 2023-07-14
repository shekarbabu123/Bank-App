package com.bankapplication.model;

import com.bankapplication.entity.Account;
import com.bankapplication.entity.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private String message;

    private Transaction transaction;

    private Account account;
}
