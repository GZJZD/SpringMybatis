package com.web.service.imp;

import com.web.dao.AccountDao;
import com.web.pojo.Account;
import com.web.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    @Override
    public void save(Account account) {
        accountDao.save(account);
    }

    @Override
    public void updateAccount(Account account) {
        accountDao.updateAccount(account);
    }

    @Override
    public Account getAccountById(Long accountId) {
        return accountDao.getAccountById(accountId);
    }

    @Override
    public List<Account> getListAccount() {
        return accountDao.getListAccount();
    }
}
