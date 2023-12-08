/*
package com.example.sem10;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private double balance;

    private final Object mutex = new Object();
    private final Lock lock = new ReentrantLock();

    public BankAccount(double bal) {
        balance = bal;
    }

    public BankAccount() {
        this(0);
    }

    public double getBalance() {
        */
/*synchronized (mutex) { // sa fie la toate la fel
            synchronized (mutex) { // merge
                return balance;
            }
        }*//*


        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " acquired thread");
            Thread.sleep(4000);
            return balance;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + " freed lock");
        }
    }

    // cateva variante
    */
/*public synchronized void deposit(double amt) {
        double temp = balance + amt;
        try {
            Thread.sleep(300);
        } catch (InterruptedException ie) {
            System.err.println(ie.getMessage());
        }
        balance = temp;
        System.out.println("after deposit balance = $" + balance);
    }

    public void withdraw(double amt) {
        synchronized (this) {
            if (balance < amt) {
                System.out.println("Insufficient funds!");
                return;
            }
            double temp = balance - amt;
            try {
                Thread.sleep(200);
            } catch (InterruptedException ie) {
                System.err.println(ie.getMessage());
            }
            balance = temp;
            System.out.println("after withdrawal balance = $" + balance);
        }
    }*//*


    public void deposit(double amt) {
        synchronized (mutex) {
            double temp = balance + amt;
            try {
                Thread.sleep(300);
            } catch (InterruptedException ie) {
                System.err.println(ie.getMessage());
            }
            balance = temp;
            System.out.println("after deposit balance = $" + balance);
        }
    }

    public void withdraw(double amt) {
        // sau in loc de synchronized
        // la inceput lock.lock()
        // la final lock.unlock()
        synchronized (mutex) {
            if (balance < amt) {
                System.out.println("Insufficient funds!");
                return;
            }
            double temp = balance - amt;
            try {
                Thread.sleep(200);
            } catch (InterruptedException ie) {
                System.err.println(ie.getMessage());
            }
            balance = temp;
            System.out.println("after withdrawal balance = $" + balance);
        }
    }
}
*/

package com.example.sem10;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BankAccount {
    private double balance;
    private final ReadWriteLock mutex = new ReentrantReadWriteLock();

    private final Lock readLock = mutex.readLock();

    private final Lock writeLock = mutex.writeLock();

    public BankAccount(double bal) {
        balance = bal;
    }

    public BankAccount() {
        this(0);
    }

    public double getBalance() {
        try {
            readLock.lock();
            System.out.println(Thread.currentThread().getName() + " acquired thread");
            Thread.sleep(4000);
            return balance;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            readLock.unlock();
            System.out.println(Thread.currentThread().getName() + " freed lock");
        }
    }

    public void deposit(double amt) {
        writeLock.lock();
        double temp = balance + amt;
        try {
            Thread.sleep(300);
        } catch (InterruptedException ie) {
            System.err.println(ie.getMessage());
        }
        balance = temp;
        System.out.println("after deposit balance = $" + balance);
        writeLock.unlock();
    }

    public void withdraw(double amt) {
        writeLock.lock();
        if (balance < amt) {
            System.out.println("Insufficient funds!");
            return;
        }
        double temp = balance - amt;
        try {
            Thread.sleep(200);
        } catch (InterruptedException ie) {
            System.err.println(ie.getMessage());
        }
        balance = temp;
        System.out.println("after withdrawal balance = $" + balance);
        writeLock.unlock();
    }
}
