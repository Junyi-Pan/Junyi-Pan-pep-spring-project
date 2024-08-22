package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Account;
import com.example.exception.DuplicateUsernameException;
import com.example.repository.AccountRepository;

@Service
@Transactional(rollbackFor = DuplicateUsernameException.class)
public class AccountService {

    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Persists new account object into database with validation of duplicate username,
     * and validation of input restrictions.
     * @param account the account object to be persisted.
     * @return The persisted account if successful, null otherwise.
     * @throws DuplicateUsernameException if username already in database.
     */
    public Account register(Account account) throws DuplicateUsernameException{
        Account check = accountRepository.findAccountByUsername(account.getUsername());
        if (check != null) throw new DuplicateUsernameException();
        if (account.getUsername().length()<0) return null;
        if (account.getPassword().length()<4) return null;
        Account res = accountRepository.save(account);
        return res;
    }

    /**
     * Verifies an account object's username exist in database, and the corresponding password is the same.
     * @param account the account object to be verified.
     * @return The verified account object if successful, null otherwise;
     */
    public Account login(Account account) {
        Account check = accountRepository.findAccountByUsername(account.getUsername());
        if (check == null) return null;
        if (!check.getPassword().equals(account.getPassword())) return null;
        return check;
    }
}
