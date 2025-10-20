package task2;

// Класс банковского счета
class BankAccount {
    private int balance; // Баланс счета
    private String accountId; // ID счета (для удобства)

    public BankAccount(String accountId, int initialBalance) {
        this.accountId = accountId;
        this.balance = initialBalance;
    }

    // Метод для снятия денег
    // ВАЖНО: synchronized убран, так как синхронизация теперь на уровне транзакции
    public boolean withdraw(int amount) {
        // Проверяем: хватает ли денег?
        if (balance >= amount) {
            System.out.println("[" + accountId + "] Снятие " + amount +
                    " (баланс до: " + balance + ")");

            // Имитируем задержку обработки (чтобы увидеть проблемы без synchronized)
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            balance -= amount; // Уменьшаем баланс
            System.out.println("[" + accountId + "] Снятие успешно (баланс после: " + balance + ")");
            return true; // Успех
        } else {
            System.out.println("[" + accountId + "] Недостаточно средств для снятия " +
                    amount + " (баланс: " + balance + ")");
            return false; // Недостаточно средств
        }
    }

    // Метод для пополнения счета
    // ВАЖНО: synchronized убран, так как синхронизация теперь на уровне транзакции
    public void deposit(int amount) {
        System.out.println("[" + accountId + "] Пополнение " + amount +
                " (баланс до: " + balance + ")");
        balance += amount;
        System.out.println("[" + accountId + "] Пополнение успешно (баланс после: " + balance + ")");
    }

    // Получить текущий баланс
    public synchronized int getBalance() {
        return balance;
    }

    public String getAccountId() {
        return accountId;
    }
}

// Класс клиента, который делает переводы
class Client implements Runnable {
    private String clientName;
    private BankAccount[] accounts; // Все счета в банке
    private int maxTransactions; // Максимум транзакций
    private int transactionCount = 0; // Счетчик транзакций

    // Для отслеживания частоты транзакций
    private long lastTransactionTime = 0; // Время последней транзакции
    private int rapidTransactionCount = 0; // Счетчик быстрых транзакций подряд
    private static final long MIN_TRANSACTION_INTERVAL = 500; // Минимум 500 мс между транзакциями
    private static final int MAX_RAPID_TRANSACTIONS = 2; // Максимум 2 быстрых транзакции подряд

    public Client(String clientName, BankAccount[] accounts, int maxTransactions) {
        this.clientName = clientName;
        this.accounts = accounts;
        this.maxTransactions = maxTransactions;
    }

    @Override
    public void run() {
        try {
            while (transactionCount < maxTransactions) {
                // Проверяем частоту транзакций
                long currentTime = System.currentTimeMillis();

                if (lastTransactionTime != 0) {
                    long timeSinceLastTransaction = currentTime - lastTransactionTime;

                    // Если транзакция произошла слишком быстро (меньше 500 мс)
                    if (timeSinceLastTransaction < MIN_TRANSACTION_INTERVAL) {
                        rapidTransactionCount++;
                        System.out.println("⚠️  " + clientName + " делает транзакции слишком быстро! " +
                                "Интервал: " + timeSinceLastTransaction + " мс " +
                                "(быстрых транзакций подряд: " + rapidTransactionCount + ")");

                        // Если слишком много быстрых транзакций подряд - прерываем
                        if (rapidTransactionCount >= MAX_RAPID_TRANSACTIONS) {
                            System.out.println("🛑 " + clientName + " превысил лимит частоты транзакций! " +
                                    "ПРЕРЫВАНИЕ РАБОТЫ.");
                            throw new InterruptedException("Превышен лимит частоты транзакций");
                        }
                    } else {
                        // Если пауза нормальная - сбрасываем счетчик
                        rapidTransactionCount = 0;
                    }
                }

                // Выбираем случайные счета для перевода
                int fromIndex = (int) (Math.random() * accounts.length);
                int toIndex = (int) (Math.random() * accounts.length);

                // Проверяем, что это разные счета
                if (fromIndex == toIndex) {
                    continue; // Пропускаем, если тот же счет
                }

                BankAccount fromAccount = accounts[fromIndex];
                BankAccount toAccount = accounts[toIndex];

                // Случайная сумма от 10 до 50
                int amount = 10 + (int) (Math.random() * 41);

                // Выполняем перевод
                System.out.println("\n>>> " + clientName + " переводит " + amount +
                        " с " + fromAccount.getAccountId() +
                        " на " + toAccount.getAccountId());

                // РЕШЕНИЕ DEADLOCK: Захватываем счета в фиксированном порядке
                // Сначала захватываем счет с меньшим индексом, потом с большим
                BankAccount firstLock = fromIndex < toIndex ? fromAccount : toAccount;
                BankAccount secondLock = fromIndex < toIndex ? toAccount : fromAccount;

                // Синхронизируемся на обоих счетах в правильном порядке
                synchronized (firstLock) {
                    synchronized (secondLock) {
                        // Теперь оба счета заблокированы в одном и том же порядке!
                        // Снимаем с одного счета
                        if (fromAccount.withdraw(amount)) {
                            // Если снятие успешно, пополняем другой счет
                            toAccount.deposit(amount);
                            System.out.println(">>> " + clientName + ": перевод успешен!\n");
                            transactionCount++;

                            // Обновляем время последней транзакции
                            lastTransactionTime = System.currentTimeMillis();
                        } else {
                            System.out.println(">>> " + clientName + ": перевод отменен (недостаточно средств)\n");
                        }
                    }
                }

                // Небольшая пауза между транзакциями
                Thread.sleep(100);
            }

            System.out.println("*** " + clientName + " завершил работу (выполнено транзакций: " +
                    transactionCount + ") ***");

        } catch (InterruptedException e) {
            // Прерывание потока (для пункта 4 задания)
            System.out.println("!!! " + clientName + " был прерван (выполнено транзакций: " +
                    transactionCount + ") !!!");
        }
    }
}

// Главный класс
public class BankTransferSimulation {
    public static void main(String[] args) {
        // Создаем 5 банковских счетов с начальным балансом 1000
        BankAccount[] accounts = new BankAccount[5];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new BankAccount("Счет-" + (i + 1), 1000);
        }

        // Вычисляем общую сумму денег в системе (должна остаться неизменной!)
        int totalBefore = 0;
        for (BankAccount acc : accounts) {
            totalBefore += acc.getBalance();
        }
        System.out.println("=== НАЧАЛО СИМУЛЯЦИИ ===");
        System.out.println("Общая сумма в системе: " + totalBefore + "\n");

        // Создаем 3 обычных клиента и 1 быстрого (нарушителя)
        int numClients = 4;
        int maxTransactions = 5;
        Thread[] clientThreads = new Thread[numClients];

        for (int i = 0; i < numClients; i++) {
            Client client = new Client("Клиент-" + (i + 1), accounts, maxTransactions);
            clientThreads[i] = new Thread(client);
            clientThreads[i].start();
        }

        // Создаем "быстрого" клиента, который нарушает правила
        // Он будет делать транзакции без паузы
        Thread fastClient = new Thread(() -> {
            String clientName = "Быстрый-Клиент";
            int count = 0;
            try {
                while (count < 10) {
                    int fromIndex = (int) (Math.random() * accounts.length);
                    int toIndex = (int) (Math.random() * accounts.length);
                    if (fromIndex == toIndex) continue; // бессмысленность перевода на свой счет с него же

                    BankAccount fromAccount = accounts[fromIndex];
                    BankAccount toAccount = accounts[toIndex];
                    int amount = 10 + (int) (Math.random() * 41);

                    System.out.println("\n>>> " + clientName + " (БЕЗ ПАУЗЫ) переводит " + amount);

                    BankAccount firstLock = fromIndex < toIndex ? fromAccount : toAccount;
                    BankAccount secondLock = fromIndex < toIndex ? toAccount : fromAccount;

                    synchronized (firstLock) {
                        synchronized (secondLock) {
                            if (fromAccount.withdraw(amount)) {
                                toAccount.deposit(amount);
                                count++;
                            }
                        }
                    }
                    // НЕТ Thread.sleep() - делает транзакции слишком быстро!
                }
            } catch (Exception e) {
                System.out.println("!!! " + clientName + " был остановлен: " + e.getMessage());
            }
        });

        // Запускаем быстрого клиента через 2 секунды
        try {
            Thread.sleep(2000);
            System.out.println("\n🚀 Запуск быстрого клиента (нарушителя)...\n");
            fastClient.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ждем завершения всех клиентов (включая быстрого)
        try {
            for (Thread thread : clientThreads) {
                thread.join();
            }
            fastClient.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Проверяем итоговые балансы
        System.out.println("\n=== КОНЕЦ СИМУЛЯЦИИ ===");
        int totalAfter = 0;
        for (BankAccount acc : accounts) {
            int balance = acc.getBalance();
            System.out.println(acc.getAccountId() + ": " + balance + " руб.");
            totalAfter += balance;
        }
        System.out.println("\nОбщая сумма в системе: " + totalAfter);
        System.out.println("Разница: " + (totalAfter - totalBefore) + " (должна быть 0!)");
    }
}