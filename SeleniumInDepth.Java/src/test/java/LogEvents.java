import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class LogEvents extends FailingTest {
    @Before
    public void testInitialize() {
        super.testInitialize();
        EventFiringWebDriver eventFiringDriver = new EventFiringWebDriver(driver);

        eventFiringDriver.register(new AbstractWebDriverEventListener() {
            @Override
            public void beforeClickOn(WebElement element, WebDriver driver) {
                System.out.println("Clicking element" + element.getText());
            }

            @Override
            public void afterClickOn(WebElement element, WebDriver driver) {
                System.out.println("Element " + element.getText() + " clicked!");
            }
        });

        driver = eventFiringDriver;
    }
}
