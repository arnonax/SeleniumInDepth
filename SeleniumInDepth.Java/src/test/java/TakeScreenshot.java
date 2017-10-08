import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.io.File;
import java.io.IOException;

public class TakeScreenshot extends FailingTest {
    @After
    public void TestCleanup() {
        File screenshot = ((TakesScreenshot) _driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot, new File("Screenshot.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.TestCleanup();
    }
}

