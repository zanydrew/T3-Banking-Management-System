package com.team4;

import com.team4.model.account.Account;
import com.team4.model.account.AccountStatus;
import com.team4.model.account.AccountType;
import com.team4.model.account.SavingsAccount;

public class Main {
    public static void main(String[] args) {
        Account account = new SavingsAccount("1244", "idhfnc", "hdwu12@gmail.com",
                "123632", AccountType.MAIN,
                AccountStatus.ACTIVE, 221424);
        System.out.println(account);
    }
}
