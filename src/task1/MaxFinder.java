package task1;

// Класс "Работник" - он будет искать максимум в своей части массива
class MaxFinder implements Runnable {
    private int[] array;      // Весь массив
    private int start;        // Начало его участка
    private int end;          // Конец его участка
    private int result;       // Результат (максимум на его участке)


    public MaxFinder(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        result = array[start];
        for (int i = start + 1; i < end; i++) {
            if (array[i] > result) {
                result = array[i];
            }
        }
        System.out.println("Поток нашел максимум на участке [" + start + ", " + end + "): " + result);
    }


    public int getResult() {
        return result;
    }
}
