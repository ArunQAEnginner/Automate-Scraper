package demo;

import org.checkerframework.checker.units.qual.Luminance;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {
    public static ChromeDriver driver;

    @Test
    public void testCase01() throws InterruptedException, IOException{
        Wrappers test1 = new Wrappers();
        test1.ScrapingHomePage();
        test1.ClickHockeyTeams();
        List<Map<String,Object>> TeamsDetails=test1.GetTeamNameYearAndWinPercent(0.40f, 4);
        test1.CreateJsonFile(TeamsDetails, "hockey-team-data.json");
        File outputFile = new File("Output/hockey-team-data.json");
        System.out.println("The JSON file should be exist in testCase1 : "+outputFile.exists());
        Assert.assertTrue(outputFile.exists(), "The JSON file should not exist.");
        System.out.println("The JSON file should not be empty in testCase1 : "+(outputFile.length() > 0));
        Assert.assertTrue(outputFile.length() > 0, "The JSON file should be empty.");
    }

    @Test
    public void testCase02() throws InterruptedException, StreamWriteException, DatabindException, IOException{
        Wrappers test2 = new Wrappers();
        test2.ScrapingHomePage();
        test2.ClickOscarWinningFilms();
        List<Map<String,Object>> MovieDetails = test2.GetTopMoviesDetailsInEachYear(5);
        test2.CreateJsonFile(MovieDetails, "oscar-winner-data.json");
        File outputFile = new File("Output/oscar-winner-data.json");
        System.out.println("The JSON file should be exist in testCase2 : "+outputFile.exists());
        Assert.assertTrue(outputFile.exists(), "The JSON file should not exist.");
        System.out.println("The JSON file should not be empty in testCase2 : "+(outputFile.length() > 0));
        Assert.assertTrue(outputFile.length() > 0, "The JSON file should be empty.");
    }
     

    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    @AfterTest
    public void endTest()
    {
        driver.close();
        driver.quit();

    }
}