package task3;

// Класс для представления заказа
class Order {
    private static int orderCounter = 0; // Счетчик для уникальных ID
    private int orderId;
    private Dish dish;
    private String customerName;

    public Order(String customerName, Dish dish) {
        this.orderId = ++orderCounter;
        this.customerName = customerName;
        this.dish = dish;
    }

    public int getOrderId() {
        return orderId;
    }

    public Dish getDish() {
        return dish;
    }

    public String getCustomerName() {
        return customerName;
    }

    @Override
    public String toString() {
        return "Заказ №" + orderId + " (" + customerName + "): " + dish.getName();
    }
}
