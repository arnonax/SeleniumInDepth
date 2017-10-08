import org.junit.After;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class TakeDocumentSource extends FailingTest {
    @After
    public void testCleanup() {
        String pageSource = driver.getPageSource();
        //Console.WriteLine(pageSource);
        PrintWriter file = null;
        try {
            file = new PrintWriter("Source.html");
            file.write(pageSource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if (file != null)
                file.close();
        }

        super.testCleanup();
    }
}
