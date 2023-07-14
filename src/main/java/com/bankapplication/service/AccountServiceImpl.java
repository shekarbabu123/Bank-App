package com.bankapplication.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bankapplication.entity.Account;
import com.bankapplication.entity.Transaction;
import com.bankapplication.entity.User;
import com.bankapplication.exception.BankException;
import com.bankapplication.exception.NotFoundException;
import com.bankapplication.model.CreateAccountRequest;
import com.bankapplication.model.TransferBalanceRequest;
import com.bankapplication.repository.AccountRepository;
import com.bankapplication.repository.TransactionRepository;
import com.bankapplication.repository.UserRepository;
import com.bankapplication.util.Constants;
import com.bankapplication.util.EncryptionDecryptionAES;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    public static final Log LOGGER = LogFactory.getLog(AccountServiceImpl.class);

    @Override
    public Account saveUser(CreateAccountRequest createAccountRequest) throws Exception {

        // SAVE ACCOUNT DETAILS IN THE ACCOUNT_TABLE.
        Account account = new Account();
        account.setBalanceAmount(createAccountRequest.getBalanceAmount());
        if(createAccountRequest.getBalanceAmount().compareTo(Constants.lienAmount)<0){
            throw new BankException(Constants.MIN_BALANCE);
        }
        account.setLienAmount(Constants.lienAmount);
        account.setLimitAmount(createAccountRequest.getBalanceAmount().subtract(account.getLienAmount()));
        accountRepository.save(account);

        // SAVE USER DETAILS IN THE USER_TABLE
        User user = new User();
        user.setUserName(createAccountRequest.getUserName());
        user.setAccountId(account.getAccountId());
        String encryptedPassword = EncryptionDecryptionAES.encryptDecryptPassword(createAccountRequest.getPassword());
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        LOGGER.info("USER ACCOUNT SUCCESSFULLY CREATED");
        return account;

    }

    @Override
    @Cacheable(value = "account",key = "#accountNumber")
    public Account findByAccountNumber(Integer accountNumber) throws BankException {
        Optional<Account> optional = accountRepository.findById(accountNumber);
        if(optional.isEmpty()){
            LOGGER.info("USER ACCOUNT NOT FOUND" + accountNumber);
        }
        return optional.orElseThrow(() -> new BankException(Constants.ACCOUNT_NOT_FOUND));
    }

    @Override
    @Cacheable(value = "transactions",key = "#accountNumber")
    public Transaction sendMoney(TransferBalanceRequest transferBalanceRequest
    ) throws BankException {
        Integer fromAccountNumber = transferBalanceRequest.getFromAccountNumber();
        Integer toAccountNumber = transferBalanceRequest.getToAccountNumber();
        BigDecimal amount = transferBalanceRequest.getAmount();
        Account fromAccount = findByAccountNumber(fromAccountNumber);
        Account toAccount = findByAccountNumber(toAccountNumber);
        if(fromAccount.getLimitAmount().compareTo(BigDecimal.ONE) == 1
                && fromAccount.getLimitAmount().compareTo(amount) >= 0){

            fromAccount.setLimitAmount(fromAccount.getLimitAmount().subtract(amount));
            fromAccount.setBalanceAmount(fromAccount.getBalanceAmount().subtract(amount));
            accountRepository.save(fromAccount);
            toAccount.setLimitAmount(toAccount.getLimitAmount().add(amount));
            toAccount.setBalanceAmount(toAccount.getBalanceAmount().add(amount));
            accountRepository.save(toAccount);
            LOGGER.info("AMOUNT TRANSFERRED SUCCESSFULLY");
            return transactionRepository.save(new Transaction(UUID.randomUUID().toString()
                    ,fromAccountNumber,toAccountNumber,amount, LocalDateTime.now(),"INR"));
        } else {
        	LOGGER.info("INSUFFICIENT BALANCE");
            throw new BankException(Constants.INSUFFICIENT_BALANCE);
        }
    }
       
    @Override
    public Page<Transaction> getStatement(Integer accountNumber, Integer page) throws NotFoundException {
        LOGGER.info("FECTCHING TRANSACTION DETAILS FROM DATABASE");
        Sort sort = Sort.by("transactionDate").descending();
        Pageable pageble = PageRequest.of(page,Constants.range,sort);
        Page<Transaction> fromPage = transactionRepository.findAllByFromAccountIdOrToAccountId(accountNumber,accountNumber,pageble);
        if (fromPage.isEmpty()) {
            LOGGER.info("TRANSACTIONS NOT FOUND FOR ACCOUNT ID " + accountNumber);
            throw new NotFoundException(Constants.NO_TRANSACTIONS_IN_THIS_PAGE);
        } else {
            return fromPage;
        }
    }

	@Override
	public Account deleteAccount(Integer accountNumber) {
		Optional<Account> acc=accountRepository.findById(accountNumber);
		if(acc==null) {
			LOGGER.info("THIS ACCOUNT_NUMBER IS DOES NOT EXIST " + accountNumber);
		}else {
			accountRepository.deleteById(accountNumber);
		}
		return null;
	}

	@Override
	public List<Account> getAllAccounts() {
		 List<Account> list=accountRepository.findAll();
		 LOGGER.info("FETCHING ALL ACCOUNTS");
		return list;
	}

}
