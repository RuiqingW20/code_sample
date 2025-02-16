package application.jobcompare.models;

public class Utils {
    public static double round(double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }
}
