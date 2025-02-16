package application.joncompare.models;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.gatech.seclass.jobcompare.models.ComparisonSettings;

public class ComparisonSettingsTest {
    ComparisonSettings settings;

    @Before
    public void setUp() throws Exception {
        settings = ComparisonSettings.getInstance();
        settings.setYearlySalary(1);
        settings.setYearlyBonus(1);
        settings.setGymMembershipAnnual(1);
        settings.setLeaveTimeDays(1);
        settings.setMatch401kPercentage(1);
        settings.setPetInsuranceAnnual(1);
    }

    @Test
    public void testCalculateTotal() {
        assertEquals(settings.getTotal(), 6, 0);
    }

    @Test
    public void testSetYearlySalary() {
        settings.setYearlySalary(2);
        assertEquals(settings.getTotal(), 7, 0);
    }

    @Test
    public void testSetYearlyBonus() {
        settings.setYearlyBonus(2);
        assertEquals(settings.getTotal(), 7, 0);
    }

    @Test
    public void testSetLeaveTimeDays() {
        settings.setLeaveTimeDays(2);
        assertEquals(settings.getTotal(), 7, 0);
    }

    @Test
    public void testSetGymnMembershipAnnual() {
        settings.setGymMembershipAnnual(2);
        assertEquals(settings.getTotal(), 7, 0);
    }

    @Test
    public void testSetMatch401kPercentage() {
        settings.setMatch401kPercentage(2);
        assertEquals(settings.getTotal(), 7, 0);
    }

    @Test
    public void testSetPetInsuranceAnnual() {
        settings.setPetInsuranceAnnual(2);
        assertEquals(settings.getTotal(), 7, 0);
    }
}
