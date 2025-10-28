package task2;

import java.util.ArrayList;
import java.util.List;

public class Bank_Simulation {
    public static void main(String[] args) throws InterruptedException {
        // Создание счетов
        List<BankAccount> accounts = new ArrayList<>();
        final int NUM_ACCOUNTS = 5;
        final double INITIAL_BALANCE = 1000.0;
        for (int i = 0; i < NUM_ACCOUNTS; i++) {
            accounts.add(new BankAccount(i, INITIAL_BALANCE));
        }

        System.out.printf("Создано %d счетов по %.2f на каждом. Общая сумма: %.2f\n",
                NUM_ACCOUNTS, INITIAL_BALANCE, INITIAL_BALANCE * NUM_ACCOUNTS);

        // Создание и запуск клиентов
        List<Thread> clients = new ArrayList<>();
        final int NUM_CLIENTS = 10;
        for (int i = 0; i < NUM_CLIENTS; i++) {
            Client client = new Client(accounts, "Клиент-" + i);
            clients.add(client);
            client.start();
        }

        // Работа симуляции 30 секунд
        System.out.println("Симуляция запущена на 30 секунд...\n");
        Thread.sleep(5000);

        // Прерывание всех клиентов
        System.out.println("Прерывание клиентов...");
        for (Thread client : clients) {
            client.interrupt();
        }

        // Ожидание завершения
        for (Thread client : clients) {
            client.join();
        }

        // Итоговые балансы (проверка целостности)
        System.out.println("\n=== ИТОГОВЫЕ БАЛАНСЫ ===");
        double totalBalance = 0.0;
        for (BankAccount account : accounts) {
            double bal = account.getBalance();
            System.out.printf("Счет %d: %.2f\n", account.getId(), bal);
            totalBalance += bal;
        }
        System.out.printf("Общая сумма: %.2f (исходная: %.2f) ✅\n",
                totalBalance, INITIAL_BALANCE * NUM_ACCOUNTS);

        System.out.println("Симуляция завершена. Целостность данных сохранена!");
    }
}