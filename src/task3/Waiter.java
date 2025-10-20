package task3;

// Класс официанта (Producer)
class Waiter implements Runnable {
    private String name;
    private OrderQueue orderQueue;
    private Dish[] menu; // Меню ресторана
    private int ordersToCreate; // Сколько заказов создать

    public Waiter(String name, OrderQueue orderQueue, Dish[] menu, int ordersToCreate) {
        this.name = name;
        this.orderQueue = orderQueue;
        this.menu = menu;
        this.ordersToCreate = ordersToCreate;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < ordersToCreate; i++) {
                // Генерируем случайный заказ
                Dish randomDish = menu[(int) (Math.random() * menu.length)];
                String customerName = "Клиент-" + (i + 1);
                Order order = new Order(customerName, randomDish);

                System.out.println("\n🧑‍💼 " + name + " принял заказ: " + order);

                // Добавляем в очередь
                orderQueue.addOrder(order);

                // Пауза между приемом заказов
                Thread.sleep(300);
            }

            System.out.println("\n*** " + name + " закончил смену ***");

        } catch (InterruptedException e) {
            System.out.println("!!! " + name + " был прерван");
        }
    }
}
