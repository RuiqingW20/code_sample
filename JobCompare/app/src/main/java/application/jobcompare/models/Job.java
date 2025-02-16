package application.jobcompare.models;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class Job implements Comparable<Job> {
    /**
     * Job ID
     */
    private long id;

    /**
     * Job Title
     */
    private String title;

    /**
     * Company Name
     */
    private String companyName;

    /**
     * Location
     * Entered as City, State
     */
    private String location;

    /**
     * Cost of Living Index
     * See https://www.expatistan.com/cost-of-living/index/north-america
     */
    private int costOfLivingIndex;

    /**
     * Yearly Salary
     */
    private int yearlySalary;

    /**
     * Yearly bonus
     */
    private int yearlyBonus;

    /**
     * Gym Membership
     * Constrained to 0-500.
     */
    private int gymMembershipAnnual;

    /**
     * Leave time in days
     * Constrained to 0-20.
     */
    private int leaveTimeDays;

    /**
     * 401k Match
     * 0-20 inclusive, expressed as a whole percent
     */
    private int match401kPercentage;

    /**
     * Pet Insurance
     * Constrained to 0-5000.
     */
    private int petInsuranceAnnual;

    /**
     * Whether this job is the current job or not.
     */
    private boolean currentJob = false;

    /**
     * Whether this job is selected for comparison.
     */
    private boolean selected = false;

    /**
     * The job score.
     */
    private double score;

    public Job() {
    }

    public Job(long id, double score, String title,
               String companyName, String location,
               int costOfLivingIndex, int yearlySalary,
               int yearlyBonus, int gymMembershipAnnual,
               int leaveTimeDays, int match401kPercentage,
               int petInsuranceAnnual, boolean isCurrentJob,
               boolean isSelected) {
        this.id = id;
        this.score = score;
        this.title = title;
        this.companyName = companyName;
        this.location = location;
        this.costOfLivingIndex = costOfLivingIndex;
        this.yearlySalary = yearlySalary;
        this.yearlyBonus = yearlyBonus;
        this.gymMembershipAnnual = gymMembershipAnnual;
        this.leaveTimeDays = leaveTimeDays;
        this.match401kPercentage = match401kPercentage;
        this.petInsuranceAnnual = petInsuranceAnnual;
        this.currentJob = isCurrentJob;
        this.selected = isSelected;
        this.score = Job.calculateScore(this);
    }

    public Job(String title, String companyName,
               String location, int costOfLivingIndex,
               int yearlySalary, int yearlyBonus,
               int gymMembershipAnnual, int leaveTimeDays,
               int match401kPercentage, int petInsuranceAnnual,
               boolean isCurrentJob) {
        this.title = title;
        this.companyName = companyName;
        this.location = location;
        this.costOfLivingIndex = costOfLivingIndex;
        this.yearlySalary = yearlySalary;
        this.yearlyBonus = yearlyBonus;
        this.gymMembershipAnnual = gymMembershipAnnual;
        this.leaveTimeDays = leaveTimeDays;
        this.match401kPercentage = match401kPercentage;
        this.petInsuranceAnnual = petInsuranceAnnual;
        this.currentJob = isCurrentJob;
        this.score = Job.calculateScore(this);
    }

    public static String format(int number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public long getID() {
        return id;
    }

    public void setID(long id) {
        this.id = id;
    }

    public double getScore() {
        if (score == 0) {
            score = Job.calculateScore(this);
        }
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCostOfLivingIndex() {
        return costOfLivingIndex;
    }

    public void setCostOfLivingIndex(int costOfLivingIndex) {
        this.costOfLivingIndex = getValidValue(costOfLivingIndex, 0);
    }

    public int getYearlySalary() {
        return yearlySalary;
    }

    public void setYearlySalary(int yearlySalary) {
        this.yearlySalary = getValidValue(yearlySalary, 0);
    }

    public int getYearlyBonus() {
        return yearlyBonus;
    }

    public void setYearlyBonus(int yearlyBonus) {
        this.yearlyBonus = getValidValue(yearlyBonus, 0);
    }

    public int getGymMembershipAnnual() {
        return gymMembershipAnnual;
    }

    public void setGymMembershipAnnual(int gymMembershipAnnual) {
        this.gymMembershipAnnual = getValidValue(gymMembershipAnnual, 0, 500);
    }

    public int getLeaveTimeDays() {
        return leaveTimeDays;
    }

    public void setLeaveTimeDays(int leaveTimeDays) {
        this.leaveTimeDays = getValidValue(leaveTimeDays, 0);
    }

    public int getMatch401kPercentage() {
        return match401kPercentage;
    }

    public void setMatch401kPercentage(int match401kPercentage) {
        this.match401kPercentage = getValidValue(match401kPercentage, 0, 20);
    }

    public int getPetInsuranceAnnual() {
        return petInsuranceAnnual;
    }

    public void setPetInsuranceAnnual(int petInsuranceAnnual) {
        this.petInsuranceAnnual = getValidValue(petInsuranceAnnual, 0, 5000);
    }

    public boolean isCurrentJob() {
        return currentJob;
    }

    public void setAsCurrentJob() {
        this.currentJob = true;
    }

    public void removeAsCurrentJob() {
        this.currentJob = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public double calculateAdjustedYearlySalary() {
        return Utils.round((double) yearlySalary / (double) costOfLivingIndex);
    }

    public double calculateAdjustedYearlyBonus() {
        return Utils.round((double) yearlyBonus / (double) costOfLivingIndex);
    }

    public static double calculateScore(Job job) {
        double adjustedYearlySalary = Utils.round(job.calculateAdjustedYearlySalary());
        double adjustedYearlyBonus = Utils.round(job.calculateAdjustedYearlyBonus());
        ComparisonSettings comparisonSettings = ComparisonSettings.getInstance();

        double score = (adjustedYearlySalary * comparisonSettings.getWeightedYearlySalary())
                + (adjustedYearlyBonus * comparisonSettings.getWeightedYearlyBonus())
                + (job.getGymMembershipAnnual() * comparisonSettings.getWeightedGymMembershipAnnual())
                + ((job.getLeaveTimeDays() * adjustedYearlySalary / 260) * comparisonSettings.getWeightedLeaveTimeDays())
                + ((adjustedYearlySalary * job.getMatch401kPercentage() / 100) * comparisonSettings.getWeightedMatch401kPercentage())
                + (job.getPetInsuranceAnnual() * comparisonSettings.getWeightedPetInsuranceAnnual());

        return Utils.round(score);
    }

    @Override
    public String toString() {
        return title + " - " + companyName;
    }

    @Override
    public int compareTo(Job otherJob) {
        // Compare jobs based on their scores
        double thisScore = calculateScore(this);
        double otherScore = calculateScore(otherJob); // Assuming you have access to factors

        if (thisScore > otherScore) {
            return -1;
        } else if (thisScore < otherScore) {
            return 1;
        } else {
            return 0;
        }
    }

    private int getValidValue(int value, int minValue, int maxValue) {
        if (value < minValue) {
            return minValue;
        }

        if (maxValue > 0 && value > maxValue) {
            return maxValue;
        }

        return value;
    }

    private int getValidValue(int value, int minValue) {
        if (value < minValue) {
            return minValue;
        }

        return value;
    }

    /**
     * Override the equals method to compare Job objects based on their attributes.
     *
     * @param obj Job to be compared.
     * @return Whether equal or not.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Job otherJob = (Job) obj;
        return id == otherJob.id &&
                Double.compare(otherJob.score, score) == 0 &&
                costOfLivingIndex == otherJob.costOfLivingIndex &&
                yearlySalary == otherJob.yearlySalary &&
                yearlyBonus == otherJob.yearlyBonus &&
                gymMembershipAnnual == otherJob.gymMembershipAnnual &&
                leaveTimeDays == otherJob.leaveTimeDays &&
                match401kPercentage == otherJob.match401kPercentage &&
                petInsuranceAnnual == otherJob.petInsuranceAnnual &&
                currentJob == otherJob.currentJob &&
                Objects.equals(title, otherJob.title) &&
                Objects.equals(companyName, otherJob.companyName) &&
                Objects.equals(location, otherJob.location);
    }

    /**
     * Override hashcode to generate hash code based on proper object attributes.
     *
     * @return Hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, score, title, companyName, location, costOfLivingIndex,
                yearlySalary, yearlyBonus, gymMembershipAnnual, leaveTimeDays,
                match401kPercentage, petInsuranceAnnual, currentJob);
    }

}