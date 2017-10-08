import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class FailingTest
{
    // ReSharper disable once InconsistentNaming
    WebDriver driver;

    @Before
    public void testInitialize() {
        //noinspection SpellCheckingInspection
        System.setProperty("webdriver.chrome.driver","c:\\temp\\chromeDriver.exe");
        driver = new ChromeDriver();
    }

    @After
    public void testCleanup() {
        driver.quit();
    }

    @Test
    public void wasTheButtonReallyClicked()
    {
        //driver.navigate().to(BasicSeleniumQuestions.BASE_URL + "Calculate.passes.html");
        driver.navigate().to(BasicSeleniumQuestions.BASE_URL + "Calculate.fails.html");

        WebElement x = driver.findElement(By.id("X"));
        x.sendKeys("5");
        WebElement y = driver.findElement(By.id("Y"));
        y.sendKeys("3");
        WebElement button = driver.findElement(By.tagName("button"));
        button.click();

        String result = driver.findElement(By.id("calculationResult")).getText();
        Assert.assertEquals("8", result);
    }
}
