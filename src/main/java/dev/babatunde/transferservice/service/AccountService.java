package dev.babatunde.transferservice.service;

import dev.babatunde.transferservice.models.db.Account;

public interface AccountService {

    Account performAccountEnquiry(String accountNumber);

    void updateAccounts(Account ... account);
}
