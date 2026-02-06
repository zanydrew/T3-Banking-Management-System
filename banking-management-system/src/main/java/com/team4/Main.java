package com.team4;

import com.team4.model.Account;

public class Main {
    public static void main(String[] args) {
        Account acc1 = new Account(01, "So Mana", 50.0);
        Account acc2 = new Account(02, "San Soklin", 40.0);

        System.out.println(acc1);
        System.out.println(acc2);
    }
}
