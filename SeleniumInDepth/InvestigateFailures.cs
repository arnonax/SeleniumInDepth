using System;
using System.Drawing.Imaging;
using System.IO;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using OpenQA.Selenium.Support.Events;
using OpenQA.Selenium.Support.Extensions;

namespace SeleniumInDepth
{
    [TestClass]
    public class FailingTest
    {
        // ReSharper disable once InconsistentNaming
        protected IWebDriver _driver;

        [TestInitialize]
        public virtual void TestInitialize()
        {
            _driver = new ChromeDriver();
        }

        [TestCleanup]
        public virtual void TestCleanup()
        {
            _driver.Dispose();
        }

        public TestContext TestContext { get; set; }

        [TestMethod]
        public void WasTheButtonReallyClicked()
        {
            _driver.Url = BasicSeleniumQuestions.BaseUrl + "Calculate.passes.html";
            //_driver.Url = BasicSeleniumQuestions.BaseUrl + "Calculate.fails.html";

            var x = _driver.FindElement(By.Id("X"));
            x.SendKeys("5");
            var y = _driver.FindElement(By.Id("Y"));
            y.SendKeys("3");
            var button = _driver.FindElement(By.TagName("button"));
            button.Click();

            var result = _driver.FindElement(By.Id("calculationResult")).Text;
            Assert.AreEqual("8", result);
        }
    }

    [TestClass]
    public class TakeScreenshot : FailingTest
    {
        [TestCleanup]
        public override void TestCleanup()
        {
            var screenshot = _driver.TakeScreenshot();
            screenshot.SaveAsFile("Screenshot.jpg", ScreenshotImageFormat.Jpeg);
            TestContext.AddResultFile("Screenshot.jpg");

            base.TestCleanup();
        }
    }

    [TestClass]
    public class TakeDocumentSource : FailingTest
    {
        [TestCleanup]
        public override void TestCleanup()
        {
            var pageSource = _driver.PageSource;
            //Console.WriteLine(pageSource);
            File.WriteAllText("Source.html", pageSource);
            TestContext.AddResultFile("Source.html");

            base.TestCleanup();
        }
    }

    [TestClass]
    public class LogEvents : FailingTest
    {
        [TestInitialize]
        public override void TestInitialize()
        {
            base.TestInitialize();
            var eventFiringDriver = new EventFiringWebDriver(_driver);

            eventFiringDriver.ElementClicking += EventFiringDriver_ElementClicking;
            eventFiringDriver.ElementClicked += EventFiringDriver_ElementClicked;

            _driver = eventFiringDriver;
        }

        private void EventFiringDriver_ElementClicked(object sender, WebElementEventArgs e)
        {
            Console.WriteLine($"Element '{e.Element.Text}' clicked!");
        }

        private void EventFiringDriver_ElementClicking(object sender, WebElementEventArgs e)
        {
            Console.WriteLine($"Clicking element '{e.Element.Text}'");
        }
    }

    [TestClass]
    public class ReadBrowserLogs : FailingTest
    {
        [TestCleanup]
        public override void TestCleanup()
        {
            var browserLog = _driver.Manage().Logs.GetLog(LogType.Browser); // There are additional log types...
            Console.WriteLine("Browser log:");
            Console.WriteLine(string.Join(Environment.NewLine, browserLog));

            base.TestCleanup();
        }
    }
}
