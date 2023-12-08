package com.example.sem10;

import java.util.concurrent.locks.*;

public class BankAccountCondVar {
    private double balance;

    // sau idk am pierdut
    private final Object mutex = new Object();
    // sau
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition balanceIncreased = lock.newCondition();

    public BankAccountCondVar(double bal) {
        balance = bal;
    }

    public BankAccountCondVar() {
        this(0);
    }

    /*public synchronized double getBalance() {
        return balance;
    }

    public synchronized void deposit(double amt) {
        double temp = balance + amt;

        balance = temp;
        notifyAll();
        System.out.println("after deposit balance = $" + balance);
    }

    public synchronized void withdraw(double amt) throws InterruptedException {
        while (balance < amt) {
            System.out.println("Insufficient funds!");
            wait();
        }

        double temp = balance - amt;
        balance = temp;
        System.out.println("after withdrawal balance = $" + balance);
    }*/

    public double getBalance() {
        synchronized (mutex) { // asa trb pe a doua var..
            return balance;
        }
    }

    public void deposit(double amt) {
        lock.lock();
        double temp = balance + amt;

        balanceIncreased.notifyAll();

        balance = temp;
        notifyAll();
        System.out.println("after deposit balance = $" + balance);
        lock.unlock();
    }

    public synchronized void withdraw(double amt) throws InterruptedException {
        lock.lock();
        while (balance < amt) {
            System.out.println("Insufficient funds!");
            balanceIncreased.await();
        }

        double temp = balance - amt;
        balance = temp;
        System.out.println("after withdrawal balance = $" + balance);
        lock.unlock();
    }
}
