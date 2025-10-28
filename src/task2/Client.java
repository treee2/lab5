package task2;

import java.util.List;
import java.util.Random;

class Client extends Thread {
    private final List<BankAccount> accounts;
    private final Random random = new Random();

    // Параметры для прерывания: максимум транзакций за лимит времени
    private static final int MAX_TRANSACTIONS = 10;
    private static final long TIME_LIMIT_MS = 2000; // 5 секунд

    public Client(List<BankAccount> accounts, String name) {
        super(name);
        this.accounts = accounts;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        int transactionCount = 0;

        try {
            while (!isInterrupted()) {
                // Выбор случайных счетов
                int fromIndex = random.nextInt(accounts.size());
                int toIndex = random.nextInt(accounts.size());
                if (fromIndex == toIndex) {
                    continue; // Не переводим на свой счет
                }

                // Случайная сумма от 1 до 100
                double amount = 1 + (random.nextDouble() * 99);

                // Выполнение перевода
                Transfer.transfer(accounts.get(fromIndex), accounts.get(toIndex), amount);
                transactionCount++;

                // Проверка условия прерывания
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (transactionCount > MAX_TRANSACTIONS && elapsedTime < TIME_LIMIT_MS) {
                    System.out.printf("%s: Превышено %d транзакций за %.1f сек - прерывание!\n",
                            getName(), transactionCount, elapsedTime / 1000.0);
                    interrupt();
                    break;
                }

                // Небольшая задержка для реалистичности
                Thread.sleep(random.nextInt(50)); // 0-50 мс
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Восстанавливаем флаг прерывания
        } finally {
            System.out.printf("%s завершен. Выполнено транзакций: %d\n", getName(), transactionCount);
        }
    }
}
