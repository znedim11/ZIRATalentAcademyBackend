package ba.com.zira.praksa.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import ba.com.zira.praksa.dao.GameDAO;
import ba.com.zira.praksa.test.configuration.ApplicationTestConfiguration;
import ba.com.zira.praksa.test.configuration.BasicTestConfiguration;

@ContextConfiguration(classes = ApplicationTestConfiguration.class)
public class GameDaoTest extends BasicTestConfiguration {

    @Autowired
    private GameDAO gameDAO;

    @Test(enabled = false, description = "find samples")
    public void findSamplesDaoTest1() {

    }

}
