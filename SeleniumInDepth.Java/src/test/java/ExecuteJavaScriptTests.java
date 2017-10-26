import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

/**
 * Created by Arnon on 26/10/2017.
 */
public class ExecuteJavaScriptTests {
    // ReSharper disable once InconsistentNaming
    WebDriver driver;

    @Before
    public void testInitialize() {
        //noinspection SpellCheckingInspection
        System.setProperty("webdriver.chrome.driver","c:\\temp\\chromeDriver.exe");
        driver = new ChromeDriver();
        driver.navigate().to(BasicSeleniumQuestions.BASE_URL + "JavaScriptExamples.html");
    }

    @After
    public void testCleanup() {
        driver.quit();
    }

    @Test
    public void simpleExample()
    {
        long result = (Long)((JavascriptExecutor)driver).executeScript("return 2+3");
        Assert.assertEquals(5, result);
    }

    @Test
    public void helloWorld()
    {
        ((JavascriptExecutor)driver).executeScript("helloWorld()");
        WebElement span = driver.findElement(By.id("mySpan"));
        Assert.assertEquals("Hello, world", span.getText());
    }

    @Test
    public void passingParameters()
    {
        int x = 3, y = 5;
        long result =
                (Long)((JavascriptExecutor)driver).executeScript("return calculate(arguments[0], arguments[1])", x, y);

        Assert.assertEquals(8, result);
    }

    @Test
    public void elementArgument()
    {
        WebElement span = driver.findElement(By.id("mySpan"));
        ((JavascriptExecutor)driver).executeScript("setText(arguments[0], 'Some text')", span);

        Assert.assertEquals("Some text", span.getText());
    }

    @Test
    public void returnAnElement()
    {
        WebElement newElement0 = (WebElement)((JavascriptExecutor)driver).executeScript("return createNewElement()");
        Assert.assertEquals("I'm a new element: 0", newElement0.getText());

        WebElement newElement1 = (WebElement)((JavascriptExecutor)driver).executeScript("return createNewElement()");
        Assert.assertEquals("I'm a new element: 1", newElement1.getText());
    }

    @Test
    public void startingAsyncOperation()
    {
        final WebElement span = driver.findElement(By.id("mySpan"));
        final String script =
        "var span = arguments[0]; \n" +
        "window.setTimeout(function() { \n" +
        "   setText(span, 'Done'); \n" +
        "}, 3000);";
        ((JavascriptExecutor)driver).executeScript(script, span);

        // Uncomment these lines in order to succeed:
/*        WebDriverWait wait = new WebDriverWait(driver, 10);
        Predicate<WebDriver> spanIsNotEmpty = new Predicate<WebDriver>() {
            public boolean apply(WebDriver webDriver) {
                return span.getText() != null && !span.getText().equals("");
            }
        };

        wait.until(spanIsNotEmpty);
*/        Assert.assertEquals("Done", span.getText());
    }

    @Test
    public void executeAsync()
    {
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);

        WebElement span = driver.findElement(By.id("mySpan"));
        final String script =
        "var span = arguments[0]; \n" +
        "var completedCallback = arguments[1]; \n" +
        "window.setTimeout(function() { \n" +
        "   setText(span, 'Done'); \n" +
        "   completedCallback(); \n" +
        "}, 3000);";
        ((JavascriptExecutor)driver).executeAsyncScript(script, span);

        Assert.assertEquals("Done", span.getText());
    }

}
