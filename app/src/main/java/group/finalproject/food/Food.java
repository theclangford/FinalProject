package group.finalproject.food;

public class Food {

    /**
     * Identification number
     */
    private double id;

    /**
     * Name of food
     */
    private String name;

    /**
     * Calorie amount in grams
     */
    private double calories;

    /**
     * Fat amount in grams
     */
    private double fats;

    /**
     * Brand of food
     */
    private String brand;

    /**
     * Protein amount in grams
     */
    private double protein;

    /**
     * Carbohydrate amount in grams
     */
    private double carbs;

    /**
     * Fiber amount in grams
     */
    private double fiber;

    /**
     * Tag assigned to item
     */
    private String tag;

    /**
     * Default constructor
     */
    Food() {}

    /**
     * Creates a food item without a brand
     * @param name
     * @param calories
     * @param fats
     * @param protein
     * @param carbs
     * @param fiber
     */
    Food(String name, double calories, double fats, double protein, double carbs, double fiber) {
        this.name = name;
        this.calories = calories;
        this.fats = fats;
        this.protein = protein;
        this.carbs = carbs;
        this.fiber = fiber;
        this.brand = "Generic";
    }

    /**
     * Creates a food item with a brand
     * @param name
     * @param calories
     * @param fats
     * @param protein
     * @param carbs
     * @param fiber
     * @param brand
     */
    Food(String name, double calories, double fats, double protein, double carbs, double fiber, String brand) {
        this(name, calories, fats, protein, carbs, fiber);
        setBrand(brand);
    }

    /**
     * Gets the item tag
     * @return the item's tag
     */
    public String getTag() { return tag; }

    /**
     * Sets the items tag
     * @param tag
     */
    public void setTag(String tag) { this.tag = tag; }

    /**
     * Sets the item id
     * @param id
     */
    public void setId(double id) { this.id = id; }

    /**
     * Gets the item id
     * @return id number
     */
    public double getId() { return id; }

    /**
     * Gets the item name
     * @return item name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the item name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets item calories
     * @return calories in grams
     */
    public double getCalories() {
        return calories;
    }

    /**
     * Sets item calories
     * @param calories in grams
     */
    public void setCalories(double calories) {
        this.calories = calories;
    }

    /**
     * Gets item fats
     * @return fats in grams
     */
    public double getFats() {
        return fats;
    }

    /**
     * Sets item fats
     * @param fats in grams
     */
    public void setFats(double fats) {
        this.fats = fats;
    }

    /**
     * Gets item brand
     * @return
     */
    public String getBrand() { return brand; }

    /**
     * Sets item brand
     * @param brand
     */
    public void setBrand(String brand) { this.brand = brand; }

    /**
     * Sets item protein
     * @return protein in grams
     */
    public double getProtein() { return protein; }

    /**
     * Sets item protein
     * @param protein in grams
     */
    public void setProtein(double protein) { this.protein = protein; }

    /**
     * Sets item fiber
     * @return fiber in grams
     */
    public double getFiber() { return fiber; }

    /**
     * Sets item fiber
     * @param fiber in grams
     */
    public void setFiber(double fiber) { this.fiber = fiber; }

    /**
     * Sets item carbs
     * @return carbs in grams
     */
    public double getCarbs() { return carbs; }

    /**
     * Sets item carbs
     * @param carbs in grams
     */
    public void setCarbs(double carbs) { this.carbs = carbs; }

}
