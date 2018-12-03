package group.finalproject.cbc;

public class Statistic {
    private int sum;
    private int avg;
    private int min;
    private int max;

    public Statistic(int sum, int avg, int min, int max) {
        this.sum = sum;
        this.avg = avg;
        this.min = min;
        this.max = max;
    }

    public int getSum() {
        return sum;
    }

    public int getAvg() {
        return avg;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
