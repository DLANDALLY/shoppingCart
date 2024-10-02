package com.dlshopping.shoppingcart.service;

import com.dlshopping.shoppingcart.entity.Account;
import com.dlshopping.shoppingcart.repositories.AccountRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private EntityManager entityManager;



    /**
     * Entity Manager will retrieve the class data after authentication
     * Don't use this method for fetch an account prefer user the getAccount() method
     * @param userName
     * @return Account
     */
    public Account findAccount(String userName) {
        return entityManager.find(Account.class, userName);
    }

    /**
     *Get an account by username using Repository
     * @param userName
     * @return
     */
    public Account getAccount(String userName) {
        return accountRepository.findById(userName).get();
    }

    /**
     * Get Username after authentication from SecurityContextHolder
     * @return Account
     */
    public Account getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (username == null) return null;

        return findAccount(username);
    }

}
