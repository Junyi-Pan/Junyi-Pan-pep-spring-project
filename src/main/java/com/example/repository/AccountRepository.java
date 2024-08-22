package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
    //Query for account based on username
    @Query("SELECT a FROM Account a WHERE a.username = ?1")
    Account findAccountByUsername(String username);
    //Query for account based on accountId
    @Query("SELECT a FROM Account a WHERE a.accountId = ?1")
    Account findAccountByAccountId(Integer accountId);


}
