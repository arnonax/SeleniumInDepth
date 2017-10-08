import org.junit.After;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class TakeDocumentSource extends FailingTest {
    @After
    public void TestCleanup() {
        String pageSource = _driver.getPageSource();
        //Console.WriteLine(pageSource);
        try {
            PrintWriter file = new PrintWriter("Source.html");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        super.TestCleanup();
    }
}
