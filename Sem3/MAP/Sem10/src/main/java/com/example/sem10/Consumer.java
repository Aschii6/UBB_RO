package com.example.sem10;

public class Consumer implements Runnable {
    private BankAccountCondVar bankAccount;
    private double amount;

    public Consumer(BankAccountCondVar bankAccount, double amount) {
        this.bankAccount = bankAccount;
        this.amount = amount;
    }

    @Override
    public void run() {
        try {
            bankAccount.withdraw(amount);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
