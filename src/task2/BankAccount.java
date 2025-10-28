package task2;

class BankAccount {
    private final int id;
    private double balance;

    public BankAccount(int id, double initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public synchronized double getBalance() {
        return balance;
    }

    public synchronized boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public synchronized void deposit(double amount) {
        balance += amount;
    }

    public int getId() {
        return id;
    }
}
