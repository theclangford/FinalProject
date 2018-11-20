package group.finalproject;

public class Food {
    private String name;
    private double calories;
    private double fats;
    private String brand;

    Food(String name, double calories, double fats) {
        this.name = name;
        this.calories = calories;
        this.fats = fats;
    }

    Food(String name, double calories, double fats, String brand) {
        this(name, calories, fats);
        setBrand(brand);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(float fats) {
        this.fats = fats;
    }

    public String getBrand() { return brand; }

    public void setBrand(String brand) { this.brand = brand; }

}
