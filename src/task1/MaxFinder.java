package task1;

// Класс "Работник" - он будет искать максимум в своей части массива
class MaxFinder implements Runnable {
    private int[] array;      // Весь массив
    private int start;        // Начало его участка
    private int end;          // Конец его участка
    private int result;       // Результат (максимум на его участке)

    // Конструктор - задаем работнику его участок работы
    public MaxFinder(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    // Метод run() - это то, что будет делать поток
    @Override
    public void run() {
        // Находим максимум на нашем участке (от start до end)
        result = array[start];
        for (int i = start + 1; i < end; i++) {
            if (array[i] > result) {
                result = array[i];
            }
        }
        System.out.println("Поток нашел максимум на участке [" + start + ", " + end + "): " + result);
    }

    // Метод для получения результата
    public int getResult() {
        return result;
    }
}
