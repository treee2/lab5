package task3;


// Главный класс
public class Restaurant_Simulation {
    public static void main(String[] args) {
        System.out.println("=== ОТКРЫТИЕ РЕСТОРАНА ===\n");

        // Создаем меню
        Dish[] menu = {
                new Dish("Пицца", 2000),
                new Dish("Паста", 1500),
                new Dish("Салат", 800),
                new Dish("Суп", 1200),
                new Dish("Стейк", 2500)
        };

        // Создаем очередь заказов (максимум 3 заказа в очереди)
        OrderQueue orderQueue = new OrderQueue(3);

        // Создаем официантов (каждый примет 4 заказа)
        int numWaiters = 2;
        int ordersPerWaiter = 4;
        Thread[] waiterThreads = new Thread[numWaiters];

        for (int i = 0; i < numWaiters; i++) {
            Waiter waiter = new Waiter("Официант-" + (i + 1), orderQueue, menu, ordersPerWaiter);
            waiterThreads[i] = new Thread(waiter);
            waiterThreads[i].start();
        }

        // Создаем поваров (каждый может готовить 1 блюдо одновременно)
        int numChefs = 2;
        Thread[] chefThreads = new Thread[numChefs];
        Chief[] chefs = new Chief[numChefs];

        for (int i = 0; i < numChefs; i++) {
            chefs[i] = new Chief("Повар-" + (i + 1), orderQueue, 1);
            chefThreads[i] = new Thread(chefs[i]);
            chefThreads[i].start();
        }

        // Ждем завершения работы всех официантов
        try {
            for (Thread thread : waiterThreads) {
                thread.join();
            }

            System.out.println("\n=== ВСЕ ОФИЦИАНТЫ ЗАКОНЧИЛИ РАБОТУ ===");

            // Даем поварам время завершить оставшиеся заказы
            Thread.sleep(5000);

            // Останавливаем поваров
            for (Chief chef : chefs) {
                chef.stopWorking();
            }

            // Прерываем потоки поваров (они могут ждать в wait())
            for (Thread thread : chefThreads) {
                thread.interrupt();
            }

            // Ждем завершения поваров
            for (Thread thread : chefThreads) {
                thread.join();
            }

            System.out.println("\n=== ЗАКРЫТИЕ РЕСТОРАНА ===");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
