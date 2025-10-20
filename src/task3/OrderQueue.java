package task3;

// Класс очереди заказов с ограничением
class OrderQueue {
    private Order[] queue;
    private int capacity; // Максимальная вместимость
    private int size = 0; // Текущее количество заказов
    private int front = 0; // Индекс начала очереди
    private int rear = 0; // Индекс конца очереди

    public OrderQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new Order[capacity];
    }

    // Добавить заказ в очередь (вызывается официантом)
    public synchronized void addOrder(Order order) throws InterruptedException {
        // Если очередь заполнена — ждем
        while (size == capacity) {
            System.out.println("⚠️  Очередь заполнена! Официант ждет...");
            wait(); // Засыпаем, пока повар не освободит место
        }

        // Добавляем заказ
        queue[rear] = order;
        rear = (rear + 1) % capacity; // Циклический массив
        size++;

        System.out.println("✅ Добавлен в очередь: " + order + " (в очереди: " + size + ")");

        // Уведомляем поваров, что появился новый заказ
        notifyAll();
    }

    // Взять заказ из очереди (вызывается поваром)
    public synchronized Order takeOrder() throws InterruptedException {
        // Если очередь пуста — ждем
        while (size == 0) {
            System.out.println("💤 Очередь пуста. Повар ждет заказов...");
            wait(); // Засыпаем, пока официант не добавит заказ
        }

        // Берем заказ
        Order order = queue[front];
        queue[front] = null;
        front = (front + 1) % capacity; // Циклический массив
        size--;

        System.out.println("👨‍🍳 Повар взял заказ: " + order + " (осталось в очереди: " + size + ")");

        // Уведомляем официантов, что освободилось место
        notifyAll();

        return order;
    }

    public synchronized int getSize() {
        return size;
    }
}
