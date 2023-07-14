package com.bankapplication.util;

import java.math.BigDecimal;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final BigDecimal lienAmount= new BigDecimal(500);
    
    public static final Integer range = 10;
    
    public static final String GENERAL_EXCEPTION_MESSAGE = "Request could not be processed due to some issue. Please try again!";
    public static final String ACCOUNT_NOT_FOUND = "No account found with given account number";
    public static final String MIN_BALANCE = "At least Rs 500.00 has to be deposited";
    public static final String INSUFFICIENT_BALANCE = "Insuffcient Balance ..!";
    public static final String NO_TRANSACTIONS_IN_THIS_PAGE= "No Transactions Found !";
   
}
