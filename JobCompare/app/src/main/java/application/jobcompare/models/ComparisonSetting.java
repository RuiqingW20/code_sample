package application.jobcompare.models;

public final class ComparisonSetting {
    private final String name;
    private int value = 1;

    public ComparisonSetting(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
