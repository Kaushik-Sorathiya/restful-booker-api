package base;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ApiUtil;

/**
 * Base test class for setting up and tearing down common test configuration,
 * including ExtentReports and RestAssured base URI.
 * All test classes should extend this class.
 */
public class BaseTest {
    protected ExtentReports extent;
    protected ExtentTest test;

    /**
     * Initializes the ExtentReports and sets the RestAssured base URI.
     * This method runs once before all test suites.
     */
    @BeforeSuite
    public void setUpSuite() {
        ExtentSparkReporter reporter = new ExtentSparkReporter("test-output/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
        ApiUtil.init(); // Ensures baseURI is set before tests
    }

    /**
     * Logs the test result (pass/fail) to the Extent Report after each test method execution.
     *
     * @param result the ITestResult object containing the result of the test method
     */
    @AfterMethod
    public void tearDownMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test passed");
        }
    }

    /**
     * Flushes the Extent Report. This ensures that all information is written to the report.
     * This method runs once after all test suites.
     */
    @AfterSuite
    public void tearDownSuite() {
        extent.flush();
    }
}
