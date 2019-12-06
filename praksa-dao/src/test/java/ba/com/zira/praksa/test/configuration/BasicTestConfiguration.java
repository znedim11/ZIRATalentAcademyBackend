package ba.com.zira.praksa.test.configuration;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

@ContextConfiguration(classes = ApplicationTestConfiguration.class)
public abstract class BasicTestConfiguration extends AbstractTransactionalTestNGSpringContextTests {

    @BeforeSuite
    public void beforeSuiteMethod() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("TEST_USER", "TEST_USER"));
        // super.springTestContextPrepareTestInstance();
        // drop db objects
        // executeSqlScript("dbscripts/cleanup.sql", true);
        // create db objects
        // executeSqlScript("dbscripts/postgres/init/setupDb.sql", false);
    }

    @AfterSuite
    public void afterSuiteMethod() {
        // executeSqlScript("dbscripts/cleanup.sql", true);
    }

}
