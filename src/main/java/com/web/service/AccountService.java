package com.web.service;

import com.web.pojo.Account;

import java.util.List;

public interface AccountService {
    void save(Account account);

    void updateAccount(Account account);

    Account getAccountById(Long accountId);

    List<Account> getListAccount();
}
