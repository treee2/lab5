package task3;

// Класс повара (Consumer)
class Chief implements Runnable {
    private String name;
    private OrderQueue orderQueue;
    private int maxConcurrentDishes; // Максимум блюд одновременно
    private volatile boolean working = true; // Флаг работы

    public Chief(String name, OrderQueue orderQueue, int maxConcurrentDishes) {
        this.name = name;
        this.orderQueue = orderQueue;
        this.maxConcurrentDishes = maxConcurrentDishes;
    }

    @Override
    public void run() {
        try {
            while (working) {
                // Берем заказ из очереди
                Order order = orderQueue.takeOrder();

                System.out.println("\n👨‍🍳 " + name + " начал готовить: " + order);

                // Имитируем приготовление
                Thread.sleep(order.getDish().getPreparationTime());

                System.out.println("✨ " + name + " завершил приготовление: " + order);
            }
        } catch (InterruptedException e) {
            System.out.println("\n!!! " + name + " завершил работу");
        }
    }

    public void stopWorking() {
        working = false;
    }
}
