package group.finalproject.food;

public class Food {

    private double id;
    private String name;
    private double calories;
    private double fats;
    private String brand;
    private double protein;
    private double carbs;
    private double fiber;
    private String tag;

    Food() {}

    Food(String name, double calories, double fats, double protein, double carbs, double fiber) {
        this.name = name;
        this.calories = calories;
        this.fats = fats;
        this.protein = protein;
        this.carbs = carbs;
        this.fiber = fiber;
        this.brand = "Generic";
    }

    Food(String name, double calories, double fats, double protein, double carbs, double fiber, String brand) {
        this(name, calories, fats, protein, carbs, fiber);
        setBrand(brand);
    }

    public String getTag() { return tag; }

    public void setTag(String tag) { this.tag = tag; }

    public void setId(double id) { this.id = id; }

    public double getId() { return id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public String getBrand() { return brand; }

    public void setBrand(String brand) { this.brand = brand; }

    public double getProtein() { return protein; }

    public void setProtein(double protein) { this.protein = protein; }

    public double getFiber() { return fiber; }

    public void setFiber(double fiber) { this.fiber = fiber; }

    public double getCarbs() { return carbs; }

    public void setCarbs(double carbs) { this.carbs = carbs; }

}
