package ba.com.zira.praksa.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import ba.com.zira.praksa.dao.SampleDAO;
import ba.com.zira.praksa.test.configuration.BasicTestConfiguration;
import ba.com.zira.praksa.test.configuration.ApplicationTestConfiguration;

@ContextConfiguration(classes = ApplicationTestConfiguration.class)
public class SampleDaoTest extends BasicTestConfiguration {

    @Autowired
    private SampleDAO sampleDAO;

    @Test(enabled = false, description = "find samples")
    public void findSamplesDaoTest1() {
        
    }

}
