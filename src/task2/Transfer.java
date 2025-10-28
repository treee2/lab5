package task2;

class Transfer {
    /**
     * Безопасный перевод средств между счетами.
     * Использует порядок блокировки по ID аккаунта для избежания deadlock.
     */
    public static void transfer(BankAccount from, BankAccount to, double amount) {
        BankAccount first = from.getId() < to.getId() ? from : to;
        BankAccount second = from.getId() < to.getId() ? to : from;

        synchronized (first) {
            synchronized (second) {
                if (from.withdraw(amount)) {
                    to.deposit(amount);
                    System.out.printf("[%s] Перевод %.2f с %d на %d (баланс %d: %.2f, %d: %.2f)\n",
                            Thread.currentThread().getName(), amount, from.getId(), to.getId(),
                            from.getId(), from.getBalance(), to.getId(), to.getBalance());
                } else {
                    System.out.printf("[%s] Недостаточно средств на %d для перевода %.2f\n",
                            Thread.currentThread().getName(), from.getId(), amount);
                }
            }
        }
    }
}
