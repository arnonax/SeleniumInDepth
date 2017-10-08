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
    // region plumbing code

    private WebDriver _driver;

    @Before
    public void TestInitialize()
    {
        System.setProperty("webdriver.chrome.driver","c:\\temp\\chromeDriver.exe");
        _driver = new ChromeDriver();
    }

    @After
    public void TestCleanup()
    {
        _driver.quit();
    }

    private static String getUrlForFile(String filename)
    {
        //return $"file:///{Directory.GetCurrentDirectory()}/{filename}";
        return "https://SeleniumInDepthDemos.AzureWebSites.net/" + filename;
    }

    // endregion

    @Test
    public void IsItABugInSelenium()
    {
        _driver.navigate().to(getUrlForFile("IsItABugInSelenium.html"));

        WebElement button = _driver.findElement(By.id("myButton"));
        button.click();
    }

    @Test
    public void DoWeReallyNeedWait()
    {
        _driver.navigate().to(getUrlForFile("SlowButton.html"));

        WebElement button = _driver.findElement(By.id("myButton"));
        WebElement input = _driver.findElement(By.id("myInput"));

        // Click and immidiately assert!
        button.click();
        Assert.assertEquals("Done", input.getAttribute("value"));
    }

    @Test
    public void WhatsTheDifference()
    {
        _driver.navigate().to(getUrlForFile("SlowButton.1.html"));

        WebElement button = _driver.findElement(By.id("myButton"));
        WebElement input = _driver.findElement(By.id("myInput"));

        // Click and immidiately assert!
        button.click();
        Assert.assertEquals("Done", input.getAttribute("value"));
    }

    @Test
    public void WillThisHelp()
    {
        _driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        _driver.navigate().to(getUrlForFile("SlowButton.1.html"));

        WebElement button = _driver.findElement(By.id("myButton"));
        WebElement input = _driver.findElement(By.id("myInput"));

        // Click and immidiately assert!
        button.click();
        Assert.assertEquals("Done", input.getAttribute("value"));
    }

    @Test
    public void ExplicitWait()
    {
        _driver.navigate().to(getUrlForFile("SlowButton.1.html"));

        WebElement button = _driver.findElement(By.id("myButton"));
        final WebElement input = _driver.findElement(By.id("myInput"));

        button.click();
        WebDriverWait wait = new WebDriverWait(_driver, 10);     // change to 1 second to see it fail
        wait.until(ExpectedConditions.textToBePresentInElementValue(input, "Done"));
        Assert.assertEquals("Done", input.getAttribute("value"));
    }

    @Test
    public void WaitAndIgnoreExceptions()
    {
        final String filename = "c:\\temp\\test.txt";
        final File file = new File(filename);
        //noinspection ResultOfMethodCallIgnored
        file.delete();

        WebDriverWait wait = new WebDriverWait(_driver, 60);
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
        _driver.navigate().to(getUrlForFile("DummyPage.html"));

        WebElement button = _driver.findElement(By.id("NonExistentId")); // (a)

        button.click(); // (b)
    }

    @Test
    public void WhenImplicitlyWaitHelps()
    {
        _driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        _driver.navigate().to(getUrlForFile("DynamicElement.html"));
        WebElement button = _driver.findElement(By.id("myButton"));
        button.click();

        // This element didn't exist before the click!
        WebElement input = _driver.findElement(By.id("myInput"));
        Assert.assertEquals("Done", input.getAttribute("value"));
    }

    @Test
    public void StaleReferenceElementDemo()
    {
        _driver.navigate().to(getUrlForFile("RemoveElement.html"));
        // Find both elements first - Should succeed!
        WebElement button = _driver.findElement(By.id("myButton"));
        WebElement input = _driver.findElement(By.id("myInput"));

        button.click();

        // Try to refer to input which was removed...
        Assert.assertEquals("Done", input.getAttribute("value"));
    }

    // Pay attention to where the StaleReferenceElementException is thrown from,
    // as opposed to where NoSuchElementException is thrown from!

    @Test
    public void UnjustifiedStaleElement()
    {
        _driver.navigate().to(getUrlForFile("SeemlessStaleElement.html"));
        WebElement button = _driver.findElement(By.id("myButton"));
        WebElement input = _driver.findElement(By.id("myInput"));

        button.click();

        Assert.assertEquals("Done", input.getAttribute("value"));
    }

    // Question: can FindElement also throw StaleElementReferenceException?

    @Test
    public void WhatHappensWhenfindElementsFindsNothing()
    {
        _driver.navigate().to("http://www.google.com");
        List<WebElement> elements = _driver.findElements(By.className("non-exitent-class-name"));
        // What will happen?
        // a. NoSuchElementException will be throw
        // b. elements will be null
        // c. elements will contain 0 elements

        System.out.println(elements.size());
    }

    @Test
    public void findElementsAndExplicitlyWait()
    {
        _driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        _driver.navigate().to("http://www.google.com");
        List<WebElement> elements;

        System.out.println("findElements(By.className(\"gb_P\")) started at: " + new Date());
        elements = _driver.findElements(By.className("gb_P"));
        System.out.println("findElements(By.className(\"gb_P\")) returned at: " + new Date());
        System.out.println("Elements found: " + elements.size());
        System.out.println();

        System.out.println("findElements(By.id(\"non-exitent-id\")) started at: " + new Date());
        elements = _driver.findElements(By.id("non-exitent-id"));
        System.out.println("findElements(By.id(\"non-exitent-id\")) returned at: " + new Date());
        System.out.println("Elements found: " + elements.size());
    }

    @Test
    public void CalculateTest()
    {
        _driver.navigate().to(getUrlForFile("Calculate.html"));
        WebElement x = _driver.findElement(By.id("X"));
        x.sendKeys("5");
        WebElement y = _driver.findElement(By.id("Y"));
        y.sendKeys("3");
        WebElement button = _driver.findElement(By.tagName("button"));
        button.click();

        String result = _driver.findElement(By.id("calculationResult")).getText();
        Assert.assertEquals("8", result);

        // If this would happen only occasionally, who would you blame?
    }

}
