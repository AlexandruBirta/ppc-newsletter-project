package ro.unibuc.fmi.ppcnewsletterproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ApiException;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ExceptionStatus;
import ro.unibuc.fmi.ppcnewsletterproject.model.Account;
import ro.unibuc.fmi.ppcnewsletterproject.model.AccountDTO;
import ro.unibuc.fmi.ppcnewsletterproject.repository.AccountRepository;

import java.util.List;

@Service
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(
                () -> new ApiException(ExceptionStatus.ACCOUNT_NOT_FOUND, String.valueOf(accountId)));
    }

    public Account createAccount(AccountDTO account) {

        List<Account> accountList = accountRepository.findAll();

        for (Account accountEntity : accountList) {
            if (account.getEmail().equals(accountEntity.getEmail())) {
                throw new ApiException(ExceptionStatus.ACCOUNT_ALREADY_EXISTS, String.valueOf(accountEntity.getId()));
            }
        }

        Account accountEntity = Account.builder()
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
                .build();

        accountRepository.save(accountEntity);
        log.info("Created account " + accountEntity);
        return accountEntity;
    }

    public void deleteAccount(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new ApiException(ExceptionStatus.ACCOUNT_NOT_FOUND, String.valueOf(accountId));
        }

        accountRepository.deleteById(accountId);
        log.info("Deleted account with id '" + accountId + "'");
    }

}
