package com.example.sem10;

public class Producer implements Runnable{
    private BankAccountCondVar bankAccount;

    private double amount;
    private long startAfterMillis;

    public Producer(BankAccountCondVar bankAccount, double amount, long startAfterMillis) {
        this.bankAccount = bankAccount;
        this.amount = amount;
        this.startAfterMillis = startAfterMillis;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(startAfterMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        bankAccount.deposit(amount);
    }
}
