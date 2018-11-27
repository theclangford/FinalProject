package group.finalproject.food;

public class Tag {

    private String name;
    private double sum;
    private double average;
    private double max;
    private double min;

    public Tag(String name, double sum, double average, double max, double min) {
        this.name = name;
        this.sum = sum;
        this.average = average;
        this.max = max;
        this.min = min;
    }

    public String getName() {
        return name;
    }

    public double getSum() {
        return sum;
    }

    public double getAverage() {
        return average;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

}
