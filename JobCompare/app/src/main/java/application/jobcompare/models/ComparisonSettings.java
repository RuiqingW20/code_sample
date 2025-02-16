package application.jobcompare.models;

import java.util.ArrayList;

import application.jobcompare.db.DbHelper;

public class ComparisonSettings {
    private static ComparisonSettings instance;
    private int yearlySalary = 1;
    private int yearlyBonus = 1;
    private int gymMembershipAnnual = 1;
    private int leaveTimeDays = 1;
    private int match401kPercentage = 1;
    private int petInsuranceAnnual = 1;

    private int total = 6;

    // Singleton.
    private ComparisonSettings() {
    }

    public static ComparisonSettings getInstance() {
        if (instance == null) {
            instance = new ComparisonSettings();
        }
        return instance;
    }

    public ComparisonSettings setFromDB(ArrayList<ComparisonSetting> settings) {
        for(ComparisonSetting setting: settings) {
            switch (setting.getName()) {
                case DbHelper.YEARLY_SALARY:
                    this.setYearlySalary(setting.getValue());
                    break;
                case DbHelper.YEARLY_BONUS:
                    this.setYearlyBonus(setting.getValue());
                    break;
                case DbHelper.GYM_MEMBERSHIP_ANNUAL:
                    this.setGymMembershipAnnual(setting.getValue());
                    break;
                case DbHelper.LEAVE_TIME_DAYS:
                    this.setLeaveTimeDays(setting.getValue());
                    break;
                case DbHelper.MATCH_401_PERCENTAGE:
                    this.setMatch401kPercentage(setting.getValue());
                    break;
                case DbHelper.PET_INSURANCE_ANNUAL:
                    this.setPetInsuranceAnnual(setting.getValue());
                    break;
            }
        }
        return this;
    }

    private void calculateTotal() {
        total = yearlySalary + yearlyBonus + gymMembershipAnnual + leaveTimeDays + match401kPercentage + petInsuranceAnnual;
    }

    public int getTotal() {
        return total;
    }

    public int getYearlySalary() {
        return yearlySalary;
    }

    public double getWeightedYearlySalary() {
        return Utils.round((double)yearlySalary / (double)total);
    }

    public void setYearlySalary(int yearlySalary) {
        this.yearlySalary = yearlySalary;
        calculateTotal();
    }

    public int getYearlyBonus() {
        return yearlyBonus;
    }

    public double getWeightedYearlyBonus() {
        return Utils.round((double)yearlyBonus / (double)total);
    }

    public void setYearlyBonus(int yearlyBonus) {
        this.yearlyBonus = yearlyBonus;
        calculateTotal();
    }

    public int getGymMembershipAnnual() {
        return gymMembershipAnnual;
    }

    public double getWeightedGymMembershipAnnual() {
        return Utils.round((double)gymMembershipAnnual / (double)total);
    }

    public void setGymMembershipAnnual(int gymMembershipAnnual) {
        this.gymMembershipAnnual = gymMembershipAnnual;
        calculateTotal();
    }

    public int getLeaveTimeDays() {
        return leaveTimeDays;
    }

    public double getWeightedLeaveTimeDays() {
        return Utils.round((double)leaveTimeDays / (double)total);
    }

    public void setLeaveTimeDays(int leaveTimeDays) {
        this.leaveTimeDays = leaveTimeDays;
        calculateTotal();
    }

    public int getMatch401kPercentage() {
        return match401kPercentage;
    }

    public double getWeightedMatch401kPercentage() {
        return Utils.round((double)match401kPercentage / (double)total);
    }

    public void setMatch401kPercentage(int match401kPercentage) {
        this.match401kPercentage = match401kPercentage;
        calculateTotal();
    }

    // PETs

    public int getPetInsuranceAnnual() {
        return petInsuranceAnnual;
    }

    public double getWeightedPetInsuranceAnnual() {
        return Utils.round((double)petInsuranceAnnual / (double)total);
    }

    public void setPetInsuranceAnnual(int petInsuranceAnnual) {
        this.petInsuranceAnnual = petInsuranceAnnual;
        calculateTotal();
    }
}
