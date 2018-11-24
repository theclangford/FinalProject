package group.finalproject.food;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {

    private double id;
    private String name;
    private double calories;
    private double fats;
    private String brand;
    private double protein;
    private double carbs;
    private double fiber;

    Food() {

    }

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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(calories);
        dest.writeDouble(fats);
        dest.writeString(brand);
        dest.writeDouble(protein);
        dest.writeDouble(carbs);
        dest.writeDouble(fiber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Food> CREATOR
            = new Parcelable.Creator<Food>() {
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    private Food(Parcel in) {
        name = in.readString();
        calories = in.readDouble();
        fats = in.readDouble();
        brand = in.readString();
        protein = in.readDouble();
        carbs = in.readDouble();
        fiber = in.readDouble();
    }

    public void setId(double id) { this.id = id; }

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
