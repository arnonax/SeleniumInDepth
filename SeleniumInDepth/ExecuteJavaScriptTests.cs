using System;
using System.IO;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using OpenQA.Selenium.Support.Extensions;
using OpenQA.Selenium.Support.UI;

namespace SeleniumInDepth
{
    [TestClass]
    public class ExecuteJavaScriptTests
    {
        #region plumbing code

        private IWebDriver _driver;

        [TestInitialize]
        public void TestInitialize()
        {
            _driver = new ChromeDriver
            {
                Url = GetUrlForFile("JavaScriptExamples.html")
            };
        }

        [TestCleanup]
        public void TestCleanup()
        {
            _driver.Dispose();
        }

        private static string GetUrlForFile(string filename)
        {
            return $"file:///{Directory.GetCurrentDirectory()}/{filename}";
        }

        #endregion

        [TestMethod]
        public void SimpleExample()
        {
            _driver.Url = GetUrlForFile("JavaScriptExamples.html");
            var result = _driver.ExecuteJavaScript<long>("return 2+3");
            Assert.AreEqual(5, result);
        }

        [TestMethod]
        public void HelloWorld()
        {
            _driver.ExecuteJavaScript("helloWorld()");
            var span = _driver.FindElement(By.Id("mySpan"));
            Assert.AreEqual("Hello, world", span.Text);
        }

        [TestMethod]
        public void PassingParameters()
        {
            int x = 3, y = 5;
            var result = 
                _driver.ExecuteJavaScript<long>("return calculate(arguments[0], arguments[1])", x, y);

            Assert.AreEqual(8, result);
        }

        [TestMethod]
        public void ElementArgument()
        {
            var span = _driver.FindElement(By.Id("mySpan"));
            _driver.ExecuteJavaScript("setText(arguments[0], 'Some text')", span);

            Assert.AreEqual("Some text", span.Text);
        }

        [TestMethod]
        public void ReturnAnElement()
        {
            IWebElement newElement0 = _driver.ExecuteJavaScript<IWebElement>("return createNewElement()");
            Assert.AreEqual("I'm a new element: 0", newElement0.Text);

            IWebElement newElement1 = _driver.ExecuteJavaScript<IWebElement>("return createNewElement()");
            Assert.AreEqual("I'm a new element: 1", newElement1.Text);
        }

        [TestMethod]
        public void StartingAsyncOperation()
        {
            var span = _driver.FindElement(By.Id("mySpan"));
            const string script = @"
var span = arguments[0];
window.setTimeout(function() {
    setText(span, 'Done');
}, 3000);";
            _driver.ExecuteJavaScript(script, span);

            // Uncomment these lines in order to succeed:
            //var wait = new WebDriverWait(_driver, TimeSpan.FromSeconds(10));
            //wait.Until(drv => !string.IsNullOrEmpty(span.Text));
            Assert.AreEqual("Done", span.Text);
        }

        [TestMethod]
        public void ExecuteAsync()
        {
            _driver.Manage().Timeouts().SetScriptTimeout(TimeSpan.FromSeconds(10));

            var span = _driver.FindElement(By.Id("mySpan"));
            const string script = @"
var span = arguments[0];
var completedCallback = arguments[1];
window.setTimeout(function() {
    setText(span, 'Done');
    completedCallback();
}, 3000);";
            ((IJavaScriptExecutor)_driver).ExecuteAsyncScript(script, span);

            Assert.AreEqual("Done", span.Text);
        }
    }
}
