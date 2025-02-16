package application.joncompare.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import edu.gatech.seclass.jobcompare.models.ComparisonSettings;
import edu.gatech.seclass.jobcompare.models.Job;

public class JobTest {

    protected Job getJob() {
        Job j = new Job();
        j.setID(1);
        j.setTitle("Software Engineer");
        j.setCompanyName("Microsoft");
        j.setLocation("Seattle, WA");
        j.setYearlyBonus(6000);
        j.setYearlySalary(60000);
        j.setCostOfLivingIndex(2);
        j.setLeaveTimeDays(5);
        j.setGymMembershipAnnual(5000);
        j.setPetInsuranceAnnual(5);
        j.setMatch401kPercentage(5);

        return j;
    }

    protected void resetComparisonSettings() {
        ComparisonSettings comparisonSettings = ComparisonSettings.getInstance();
        comparisonSettings.setYearlySalary(1);
        comparisonSettings.setYearlyBonus(1);
        comparisonSettings.setLeaveTimeDays(1);
        comparisonSettings.setGymMembershipAnnual(1);
        comparisonSettings.setMatch401kPercentage(1);
        comparisonSettings.setPetInsuranceAnnual(1);
    }

    @Test
    public void jobConstructorsTest() {
        Job job1;
        job1 = new Job(1, 2, "Software Engineer", "IBM", "Armonk, NY", 102, 80000, 4000, 500, 5, 6, 100, true, false);
        assertTrue(job1.isCurrentJob());
        assertEquals(1, job1.getID(), 0);
        assertNotEquals(0, job1.getScore(), 0);
        assertEquals("Software Engineer", job1.getTitle());
        assertEquals("IBM", job1.getCompanyName());
        assertEquals("Armonk, NY", job1.getLocation());
        assertEquals(102, job1.getCostOfLivingIndex(), 0);
        assertEquals(80000, job1.getYearlySalary(), 0);
        assertEquals(4000, job1.getYearlyBonus(), 0);
        assertEquals(500, job1.getGymMembershipAnnual(), 0);
        assertEquals(5, job1.getLeaveTimeDays(), 0);
        assertEquals(6, job1.getMatch401kPercentage(), 0);
        assertEquals(100, job1.getPetInsuranceAnnual(), 0);

        Job job2;
        job2 = new Job("Software Engineer", "IBM", "Armonk, NY", 102, 80000, 4000, 500, 5, 6, 100, true);
        assertTrue(job2.isCurrentJob());
        assertEquals("Software Engineer", job2.getTitle());
        assertEquals("IBM", job2.getCompanyName());
        assertEquals("Armonk, NY", job2.getLocation());
        assertEquals(102, job2.getCostOfLivingIndex(), 0);
        assertEquals(80000, job2.getYearlySalary(), 0);
        assertEquals(4000, job2.getYearlyBonus(), 0);
        assertEquals(500, job2.getGymMembershipAnnual(), 0);
        assertEquals(5, job2.getLeaveTimeDays(), 0);
        assertEquals(6, job2.getMatch401kPercentage(), 0);
        assertEquals(100, job2.getPetInsuranceAnnual(), 0);
    }

    @Test
    public void setRemoveCurrentJobTest() {
        Job job = getJob();
        job.setAsCurrentJob();
        assertTrue(job.isCurrentJob());

        job.removeAsCurrentJob();
        assertFalse(job.isCurrentJob());
    }

    @Test
    public void calculateAdjustedTest() {
        Job job = getJob();
        assertEquals(30000, job.calculateAdjustedYearlySalary(), 0);
        assertEquals(3000, job.calculateAdjustedYearlyBonus(), 0);
    }

    @Test
    public void setYearlyTest() {
        Job job = getJob();
        job.setYearlySalary(-500);
        assertEquals(0, job.getYearlySalary(), 0);

        job.setYearlyBonus(-500);
        assertEquals(0, job.getYearlyBonus(), 0);
    }

    @Test
    public void setGymMembershipAnnualTest() {
        Job job = getJob();
        job.setGymMembershipAnnual(-1);
        assertEquals(0, job.getGymMembershipAnnual(), 0);

        job.setGymMembershipAnnual(5000);
        assertEquals(500, job.getGymMembershipAnnual(), 0);
    }

    @Test
    public void setLeaveTimeDaysTest() {
        Job job = getJob();
        job.setLeaveTimeDays(-1);
        assertEquals(0, job.getLeaveTimeDays(), 0);

        job.setLeaveTimeDays(5000);
        assertEquals(5000, job.getLeaveTimeDays(), 0);
    }

    @Test
    public void setMatch401kPercentageTest() {
        Job job = getJob();
        job.setMatch401kPercentage(-1);
        assertEquals(0, job.getMatch401kPercentage(), 0);

        job.setMatch401kPercentage(5000);
        assertEquals(20, job.getMatch401kPercentage(), 0);
    }

    @Test
    public void setPetInsuranceAnnualTest() {
        Job job = getJob();
        job.setPetInsuranceAnnual(-1);
        assertEquals(0, job.getPetInsuranceAnnual(), 0);

        job.setPetInsuranceAnnual(50000);
        assertEquals(5000, job.getPetInsuranceAnnual(), 0);
    }

    @Test
    public void calculateScoreTest() {
        Job job = getJob();
        resetComparisonSettings();

        double score = Job.calculateScore(job);

        assertEquals(5942.181, score, 0);
    }

    @Test
    public void getSortedJobsTest() {
        Job job1 = new Job("Software Engineer", "Microsoft", "Seattle, WA", 170, 60000, 6000, 500, 5, 5, 100, true);
        Job job2 = new Job("Software Engineer", "IBM", "Armonk, NY", 220, 80000, 4000, 500, 5, 6, 100, false);
        Job job3 = new Job("Software Engineer III", "Amazon", "SF, CA", 203, 300000, 30000, 500, 5, 6, 1000, true);

        ArrayList<Job> jobsArray = new ArrayList<>();
        jobsArray.add(job1);
        jobsArray.add(job2);
        jobsArray.add(job3);

        Collections.sort(jobsArray);
        assertEquals(jobsArray.get(0), job3);
        assertEquals(jobsArray.get(1), job1);
        assertEquals(jobsArray.get(2), job2);
    }

}
