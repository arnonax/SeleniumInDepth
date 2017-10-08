import org.junit.After;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class TakeDocumentSource extends FailingTest {
    @After
    public void TestCleanup() {
        String pageSource = _driver.getPageSource();
        //Console.WriteLine(pageSource);
        PrintWriter file = null;
        try {
            file = new PrintWriter("Source.html");
            file.write(pageSource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            file.close();
        }

        super.TestCleanup();
    }
}
