import com.google.common.io.Files;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Arnon on 03/10/2017.
 */
public class BasicSeleniumQuestions {
    public static final String BASE_URL = "https://SeleniumInDepthDemos.AzureWebSites.net/";
    // region plumbing code

    private WebDriver driver;

    @Before
    public void TestInitialize()
    {
        System.setProperty("webdriver.chrome.driver","c:\\temp\\chromeDriver.exe");
        driver = new ChromeDriver();
    }

    @After
    public void TestCleanup()
    {
        driver.quit();
    }

    // endregion

    @Test
    public void SimpleDemo()
    {
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        String page = "SimpleDemo.html";
        //String page = "SimpleDemo.1.html";
        //String page = "SimpleDemo.2.html";
        //String page = "SimpleDemo.3.html";
        driver.navigate().to(BASE_URL + page);

        WebElement button = driver.findElement(By.id("myButton"));
        WebElement input = driver.findElement(By.id("myInput"));

        button.click();
        Assert.assertEquals("Done", input.getAttribute("value"));
    }

    @Test
    public void ExplicitWait()
    {
        driver.navigate().to(BASE_URL + "SimpleDemo.2.html");

        WebElement button = driver.findElement(By.id("myButton"));
        final WebElement input = driver.findElement(By.id("myInput"));

        button.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);     // change to 1 second to see it fail
        Boolean done = wait.until(ExpectedConditions.textToBePresentInElementValue(input, "Done"));
        Assert.assertTrue(done);
    }

    @Test
    public void WaitAndIgnoreExceptions()
    {
        final String filename = "c:\\temp\\test.txt";
        final File file = new File(filename);
        //noinspection ResultOfMethodCallIgnored
        file.delete();

        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.ignoring(IOException.class);
        String content = wait.until(new ExpectedCondition<String>() {
            public String apply(WebDriver drv) {
                try {
                    return Files.toString(file, Charset.defaultCharset());
                }
                catch (IOException ex) {
                    return null;
                }
            }
        });

        Assert.assertEquals("Hello", content);
    }

    @Test // Which line throws the exception?
    public void ObviousNoSuchElementException()
    {
        driver.navigate().to(BASE_URL + "DummyPage.html");

        WebElement button = driver.findElement(By.id("NonExistentId")); // (a)

        button.click(); // (b)
    }

    @Test
    public void WhenImplicitlyWaitHelps()
    {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.navigate().to(BASE_URL + "DynamicElement.html");
        WebElement button = driver.findElement(By.id("myButton"));
        button.click();

        // This element didn't exist before the click!
        WebElement input = driver.findElement(By.id("myInput"));
        Assert.assertEquals("Done", input.getAttribute("value"));
    }

    @Test
    public void StaleReferenceElementDemo()
    {
        driver.navigate().to(BASE_URL + "RemoveElement.html");
        // Find both elements first - Should succeed!
        WebElement button = driver.findElement(By.id("myButton"));
        WebElement input = driver.findElement(By.id("myInput"));

        button.click();

        // Try to refer to input which was removed...
        Assert.assertEquals("Done", input.getAttribute("value"));
    }

    // Pay attention to where the StaleReferenceElementException is thrown from,
    // as opposed to where NoSuchElementException is thrown from!

    @Test
    public void UnjustifiedStaleElement()
    {
        driver.navigate().to(BASE_URL + "SeemlessStaleElement.html");
        WebElement button = driver.findElement(By.id("myButton"));
        WebElement input = driver.findElement(By.id("myInput"));

        button.click();

        Assert.assertEquals("Done", input.getAttribute("value"));
    }

    // Question: can FindElement also throw StaleElementReferenceException?

    @Test
    public void WhatHappensWhenfindElementsFindsNothing()
    {
        driver.navigate().to("http://www.google.com");
        List<WebElement> elements = driver.findElements(By.className("non-exitent-class-name"));
        // What will happen?
        // a. NoSuchElementException will be throw
        // b. elements will be null
        // c. elements will contain 0 elements

        System.out.println(elements.size());
    }

    @Test
    public void findElementsAndExplicitlyWait()
    {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.navigate().to("http://www.google.com");
        List<WebElement> elements;

        System.out.println("findElements(By.className(\"gb_P\")) started at: " + new Date());
        elements = driver.findElements(By.className("gb_P"));
        System.out.println("findElements(By.className(\"gb_P\")) returned at: " + new Date());
        System.out.println("Elements found: " + elements.size());
        System.out.println();

        System.out.println("findElements(By.id(\"non-exitent-id\")) started at: " + new Date());
        elements = driver.findElements(By.id("non-exitent-id"));
        System.out.println("findElements(By.id(\"non-exitent-id\")) returned at: " + new Date());
        System.out.println("Elements found: " + elements.size());
    }

    @Test
    public void CalculateTest()
    {
        driver.navigate().to(BASE_URL + "Calculate.html");
        WebElement x = driver.findElement(By.id("X"));
        x.sendKeys("5");
        WebElement y = driver.findElement(By.id("Y"));
        y.sendKeys("3");
        WebElement button = driver.findElement(By.tagName("button"));
        button.click();

        String result = driver.findElement(By.id("calculationResult")).getText();
        Assert.assertEquals("8", result);

        // If this would happen only occasionally, who would you blame?
    }

}
