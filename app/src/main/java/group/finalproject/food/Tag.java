package group.finalproject.food;

public class Tag {

    /**
     * Name of tag
     */
    private String name;

    /**
     * Sum of calories for tagged items
     */
    private double sum;

    /**
     * Average calories for tagged items
     */
    private double average;

    /**
     * Max calories for tagged items
     */
    private double max;

    /**
     * Min calories for tagged items
     */
    private double min;

    /**
     * Constructor which takes name, sum, average, max and min
     * @param name
     * @param sum
     * @param average
     * @param max
     * @param min
     */
    public Tag(String name, double sum, double average, double max, double min) {
        this.name = name;
        this.sum = sum;
        this.average = average;
        this.max = max;
        this.min = min;
    }

    /**
     * Gets name of tag
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get sum of calories for tagged items
     * @return sum
     */
    public double getSum() {
        return sum;
    }

    /**
     * Get average calories of tagged items
     * @return average
     */
    public double getAverage() {
        return average;
    }

    /**
     * Get max calories of tagged items
     * @return max
     */
    public double getMax() {
        return max;
    }

    /**
     * Get min calories of tagged items
     * @return min
     */
    public double getMin() {
        return min;
    }

}
