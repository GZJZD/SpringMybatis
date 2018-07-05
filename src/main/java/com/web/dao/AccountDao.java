package com.web.dao;

import com.web.pojo.Account;

import java.util.List;

public interface  AccountDao {
    void save (Account account);
    void updateAccount(Account account);
    Account getAccountById(Long accountId);
    List<Account> getListAccount();
}
