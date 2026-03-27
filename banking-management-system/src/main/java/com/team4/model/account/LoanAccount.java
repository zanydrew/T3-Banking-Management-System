package com.team4.model.account;

public class LoanAccount extends Account {

    /*
     * Key Components of a Loan Account
     * Principal: The original amount borrowed.
     * Interest Rate: The cost of borrowing, expressed as a percentage.
     * Term: The duration over which the loan will be repaid.
     * Payment Schedule: Frequency of repayments (monthly, biweekly, etc.).
     * Outstanding Balance: The remaining amount owed.
     * Amortization Schedule: A breakdown showing how each payment is applied to
     * principal and interest.
     */

    private double principle;
    private static final double INTEREST_RATE = 0.06; // interest rate per year
    private int term; // Term: The duration over (in year) which the loan will be repaid
    private double rate;

    public LoanAccount(String accountNumber,int userId, String holderName, String email, String accountPin,AccountType accountType,
                       AccountStatus accountStatus, double balance , boolean trusted, double principle, int term) {
        super(accountNumber, userId, holderName, email, accountPin,AccountType.LOAN, accountStatus, balance , trusted);
        this.getAccountType();
        setPrinciple(principle);
        setTerm(term);
        this.rate = calculateInterest(principle, term);
    }

    public LoanAccount(String accountNumber, int userId, String holderName, String email, String accountPin, AccountType accountType,
                       AccountStatus accountStatus, double balance) {
        super(accountNumber, userId, holderName, email, accountPin, AccountType.LOAN, accountStatus, balance);
    }

    // Used for DB (trusted data, no validation)
    public LoanAccount(String accountNumber, int userId, String holderName, String email, String accountPin, AccountType accountType,
                       AccountStatus accountStatus, double balance, boolean trusted) {
        super(accountNumber, userId, holderName, email, accountPin, AccountType.LOAN, accountStatus, balance, trusted);
    }
    // === Getters ===

    public double getPrinciple() {
        return principle;
    }

    public static double getInterestRate() {
        return INTEREST_RATE;
    }

    public int getTerm() {
        return term;
    }

    public void setPrinciple(double principle) {
        if (principle <= 0) {
            throw new IllegalArgumentException("Principle cannot be <= 0");
        }
        this.principle = principle;
    }

    public void setTerm(int term) {
        if (term <= 0) {
            throw new IllegalArgumentException("Duration of loaning money must be from 1 year.");
        }
        this.term = term;
    }

    public double calculateInterest(double principal, int term) {
        this.rate = (principle * INTEREST_RATE * term);
        return this.rate;
    }

    protected double totalLaonPay() {
        return principle + calculateInterest(principle, term);
    }

    @Override
    public String toString() {
        return "User ID: " + getUserId() + "\nAccount Number: " + getAccountNumber() + " \nHolder Name: " + getHolderName() + "\nAccount Type: "
                + getAccountType() + "\nPrinciple: $" + getPrinciple() + "\nTerm: " + getTerm() + " Years"
                + "\nInterest: $"
                + calculateInterest(principle, term) + "\nTotal Loan Pay: $" + totalLaonPay() + "\nBalance: $"
                + getBalance() + "\nEmail: " + getEmail() + "\nAccount status: "
                + getAccountStatus();
    }

}
