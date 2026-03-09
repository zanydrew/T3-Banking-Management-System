package com.team4;

import java.util.Scanner;

import com.team4.model.account.Account;
import com.team4.model.account.AccountStatus;
import com.team4.model.account.AccountType;
import com.team4.model.account.LoanAccount;
import com.team4.model.account.SavingsAccount;
import com.team4.model.account.MainAccount;
import com.team4.service.AccountService;

public class Main {
    public static void main(String[] args) {
        Account mainAccount1 = new MainAccount("0001", "Channy Sreyoun", "sreyoun12@gmail.com", "111111",
                AccountStatus.ACTIVE, 50.12);
        Account mainAccount2 = new MainAccount("0002", "La riya", "riya13@gmail.com", "222222", AccountStatus.ACTIVE,
                14.32);
        Account loanAccount1 = new LoanAccount("0001", "Channy Sreyoun", "Sreyoun12@gmail.com", "111111",
                AccountStatus.ACTIVE, 52.12, 230.00, 1);
        AccountService accountService1 = new AccountService();

        Scanner input = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n============User===========");
            System.out.println("1. Customer");
            System.out.println("2. Manager ");
            System.out.println("3. Exit");
            System.out.print("Please enter your choice: ");
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    boolean inCustomer = true;
                    while (inCustomer) {
                        System.out.println("============Customer============");
                        System.out.println("1. View Account Information");
                        System.out.println("2. Withraw money");
                        System.out.println("3. Tranfer money");
                        System.out.println("4. Deposit money");
                        System.out.println("5. Saving Account");
                        System.out.println("6. Loan Account");
                        System.out.println("7. Exit");
                        System.out.print("please enter your choice: ");
                        int choiceCustomer = input.nextInt();
                        switch (choiceCustomer) {
                            case 1:
                                System.out.println("=========Account Information==========");
                                System.out.println(mainAccount1.toString());
                                System.out.println("Exit(E): ");
                                break;
                            case 2:

                                System.out.println("==========Withdraw money==========");
                                System.out.println("Balance: " + mainAccount1.getBalance());
                                System.out.println("How Much do you want to withdraw?: ");
                                double amountTowithdraw = input.nextDouble();
                                accountService1.withdraw("0001", amountTowithdraw);
                                System.out.println("You withdraw successfully.");
                                System.out.println("Balance: " + amountTowithdraw);
                                break;
                            case 3:
                                System.out.println("==========Tranfer Money==========");
                                System.out.println("Sender: ");
                                String sender = input.nextLine();
                                System.out.println("To: ");
                                String reciever = input.nextLine();
                                System.out.println("Please enter tranfer amount: ");
                                double amountTotranfer = input.nextDouble();
                                accountService1.transfer(sender, mainAccount2, amountTotranfer);
                                System.out.println("Tranfer successfully.");
                                System.out.println("Remain Balance: " + amountTotranfer);
                                break;

                            case 4:
                                System.out.println("==========Deposit==========");

                                break;
                            case 5:
                                System.out.println("==========Saving Account==========");
                                break;
                            case 6:
                                System.out.println("==========Loan Account==========");
                                break;
                            case 7:
                                inCustomer = true;
                                System.out.println("System Exiting....");
                            default:
                                System.out.println("Invalid choice.");
                                break;

                        }
                        break;
                    }

                case 2:
                    boolean inManager = true;
                    while (inManager) {
                        System.out.println("============Manager============");
                        System.out.println("1. Manage Customer Accounts");
                        System.out.println("2. View Customer Information");
                        System.out.println("3. Manage Transactions");
                        System.out.println("4. Transaction records");
                        System.out.println("5. Exit");
                        System.out.println("Enter your number choice");
                        int managerChoice = input.nextInt();
                        switch (managerChoice) {
                            case 1:
                                System.out.println("==========Manage Customer Account===========");

                                break;

                            default:
                                break;
                        }
                    }

                default:

                    break;
            }
        }

    }
}
