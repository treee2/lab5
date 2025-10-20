package task3;

// Класс для представления блюда
class Dish {
    private String name;
    private int preparationTime; // Время приготовления в миллисекундах

    public Dish(String name, int preparationTime) {
        this.name = name;
        this.preparationTime = preparationTime;
    }

    public String getName() {
        return name;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    @Override
    public String toString() {
        return name;
    }
}
