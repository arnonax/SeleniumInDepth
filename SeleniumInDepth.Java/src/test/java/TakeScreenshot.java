import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;

public class TakeScreenshot extends FailingTest {
    @After
    public void testCleanup() {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot, new File("Screenshot.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.testCleanup();
    }
}

