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
    protected WebDriver _driver;

    @Before
    public void TestInitialize() {
        System.setProperty("webdriver.chrome.driver","c:\\temp\\chromeDriver.exe");
        _driver = new ChromeDriver();
    }

    @After
    public void TestCleanup() {
        _driver.quit();
    }

    @Test
    public void WasTheButtonReallyClicked()
    {
        //_driver.navigate().to(BasicSeleniumQuestions.BASE_URL + "Calculate.passes.html");
        _driver.navigate().to(BasicSeleniumQuestions.BASE_URL + "Calculate.fails.html");

        WebElement x = _driver.findElement(By.id("X"));
        x.sendKeys("5");
        WebElement y = _driver.findElement(By.id("Y"));
        y.sendKeys("3");
        WebElement button = _driver.findElement(By.tagName("button"));
        button.click();

        String result = _driver.findElement(By.id("calculationResult")).getText();
        Assert.assertEquals("8", result);
    }
}
