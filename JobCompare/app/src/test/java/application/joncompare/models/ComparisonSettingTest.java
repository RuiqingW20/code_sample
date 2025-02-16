package application.joncompare.models;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.gatech.seclass.jobcompare.models.ComparisonSetting;

public class ComparisonSettingTest {
    ComparisonSetting setting;

    @Before
    public void setUp() throws Exception {
        setting = new ComparisonSetting("yearlyBonus", 1);
    }

    @Test
    public void setValue() {
        assertEquals(1, setting.getValue(), 0);
    }
}
