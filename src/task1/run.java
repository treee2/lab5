package task1;

// Главный класс программы
public class run {
    public static void main(String[] args) {
        // Создаем большой массив
        int[] array = new int[1000];

        // Заполняем массив случайными числами от 0 до 9999
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * 10000);
        }

        // Количество потоков
        int numThreads = 4;

        // Размер участка для каждого потока
        int chunkSize = array.length / numThreads; // 1000 / 4 = 250

        // Создаем массив для хранения наших работников
        MaxFinder[] finders = new MaxFinder[numThreads];
        Thread[] threads = new Thread[numThreads];

        // Создаем и запускаем потоки
        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;              // 0, 250, 500, 750
            int end = (i + 1) * chunkSize;          // 250, 500, 750, 1000

            // Создаем работника для этого участка
            finders[i] = new MaxFinder(array, start, end);

            // Создаем поток с этим работником
            threads[i] = new Thread(finders[i]);

            // ЗАПУСКАЕМ поток
            threads[i].start();
        }

        // ВАЖНО: Ждем завершения всех потоков
        try {
            for (int i = 0; i < numThreads; i++) {
                threads[i].join(); // join() говорит: "жди, пока этот поток закончит"
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Собираем результаты от всех потоков
        int globalMax = finders[0].getResult();
        for (int i = 1; i < numThreads; i++) {
            int threadMax = finders[i].getResult();
            if (threadMax > globalMax) {
                globalMax = threadMax;
            }
        }

        System.out.println("\n=== ИТОГОВЫЙ МАКСИМУМ: " + globalMax + " ===");
    }
}
