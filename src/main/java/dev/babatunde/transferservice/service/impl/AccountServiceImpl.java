package dev.babatunde.transferservice.service.impl;

import dev.babatunde.transferservice.exceptions.AccountNumberNotExistException;
import dev.babatunde.transferservice.models.db.Account;
import dev.babatunde.transferservice.repository.AccountRepository;
import dev.babatunde.transferservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account performAccountEnquiry(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNumberNotExistException(String.format("%s does not exist", accountNumber)));
    }

    @Override
    public void updateAccounts(Account ... accounts) {
        accountRepository.saveAll(List.of(accounts));
    }
}
