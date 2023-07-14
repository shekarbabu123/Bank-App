package com.bankapplication.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import com.bankapplication.exception.BankException;

@Component
@Aspect
public class LoggingAspect {

	public static final Log LOGGER = LogFactory.getLog(LoggingAspect.class);

	@AfterThrowing(pointcut = "execution(* com.bankapplication.service.*Impl.*(..))", throwing = "exception")
	public void afterThrowing(BankException exception)  {
		LOGGER.error(exception.getMessage(),exception);
	}

}