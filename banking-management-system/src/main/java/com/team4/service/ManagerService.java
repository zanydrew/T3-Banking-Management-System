package com.team4.service;

import java.util.ArrayList;

import com.team4.model.account.Account;

public class ManagerService implements ManagerServiceInterface {

    private ArrayList<Account> accounts = new ArrayList<>();

    @Override
    public void block(Account accountToBlock) {

    }

    @Override
    public void addAccount(Account accountToAdd) {
        if (accountToAdd == null) {
            throw new IllegalArgumentException("Cannot add null account!");
        }
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountToAdd.getAccountNumber())) {
                throw new IllegalArgumentException("Account already exists!");
            }
        }
        accounts.add(accountToAdd);
    }

    @Override
    public void removeAccount(Account accountToRemove) {
        boolean found = false;
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountToRemove.getAccountNumber())) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("This account does not exist!");
        }

        accounts.remove(accountToRemove);
    }

    @Override
    public ArrayList<Account> getAllAccounts() {
        return accounts;
    }
}
