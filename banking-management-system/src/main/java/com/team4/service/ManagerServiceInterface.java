package com.team4.service;

import java.util.ArrayList;

import com.team4.model.account.Account;

public interface ManagerServiceInterface {
    public void block(Account account);

    public void addAccount(Account account);

    public void removeAccount(Account account);

    public ArrayList<Account> getAllAccounts();

}
