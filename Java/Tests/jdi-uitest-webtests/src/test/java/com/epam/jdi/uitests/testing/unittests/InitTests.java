package com.epam.jdi.uitests.testing.unittests;

import com.epam.jdi.uitests.core.logger.LogLevels;
import com.epam.jdi.uitests.testing.unittests.entities.User;
import com.epam.jdi.uitests.testing.unittests.pageobjects.EpamJDISite;
import com.epam.jdi.uitests.web.selenium.elements.composite.WebSite;
import com.epam.jdi.uitests.web.testng.testRunner.TestNGBase;
import com.epam.web.matcher.verify.Verify;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import static com.epam.jdi.uitests.core.logger.LogLevels.OFF;
import static com.epam.jdi.uitests.testing.unittests.pageobjects.EpamJDISite.login;
import static com.epam.jdi.uitests.web.selenium.driver.WebDriverUtils.killAllRunWebBrowsers;
import static com.epam.jdi.uitests.web.selenium.elements.composite.WebSite.open;
import static com.epam.jdi.uitests.web.settings.WebSettings.logger;


/**
 * Created by Roman_Iovlev on 7/13/2015.
 */
public class InitTests extends TestNGBase {

    @BeforeSuite(alwaysRun = true)
    public static void setUp() {
        logger.setLogLevel(OFF);
        WebSite.init(EpamJDISite.class);
        open();
        login.submit(User.DEFAULT);
        Verify.getFails();
        logger.info("Run Tests");
    }

    @AfterMethod
    public void tearDown() {
        Verify.getFails();
    }

    @AfterSuite(alwaysRun = true)
    public static void shutDown() {
        killAllRunWebBrowsers();
    }
}
