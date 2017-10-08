import org.junit.After;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

public class ReadBrowserLogs extends FailingTest {
    @After
    public void testCleanup() {
        LogEntries browserLog = driver.manage().logs().get(LogType.BROWSER); // There are additional log types...
        System.out.println("Browser log:");
        for (LogEntry entry : browserLog) {
            System.out.println(entry.toString());
        }

        super.testCleanup();
    }
}
