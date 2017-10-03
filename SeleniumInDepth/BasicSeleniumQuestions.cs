using System;
using System.Collections.Generic;
using System.IO;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using OpenQA.Selenium.Support.PageObjects;
using OpenQA.Selenium.Support.UI;

namespace SeleniumInDepth
{
    [TestClass]
    public class BasicSeleniumQuestions
    {
        #region plumbing code

        private IWebDriver _driver;

        [TestInitialize]
        public void TestInitialize()
        {
            _driver = new ChromeDriver();
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
        public void IsItABugInSelenium()
        {
            _driver.Url = GetUrlForFile("IsItABugInSelenium.html");

            var button = _driver.FindElement(By.Id("myButton"));
            button.Click();
        }

        [TestMethod]
        public void DoWeReallyNeedWait()
        {
            _driver.Url = GetUrlForFile("SlowButton.html");

            var button = _driver.FindElement(By.Id("myButton"));
            var input = _driver.FindElement(By.Id("myInput"));

            // Click and immidiately assert!
            button.Click();
            Assert.AreEqual("Done", input.GetAttribute("value"));
        }

        [TestMethod]
        public void WhatsTheDifference()
        {
            _driver.Url = GetUrlForFile("SlowButton.1.html");

            var button = _driver.FindElement(By.Id("myButton"));
            var input = _driver.FindElement(By.Id("myInput"));

            // Click and immidiately assert!
            button.Click();
            Assert.AreEqual("Done", input.GetAttribute("value"));
        }

        [TestMethod]
        public void WillThisHelp()
        {
            _driver.Manage().Timeouts().ImplicitlyWait(TimeSpan.FromSeconds(10));

            _driver.Url = GetUrlForFile("SlowButton.1.html");

            var button = _driver.FindElement(By.Id("myButton"));
            var input = _driver.FindElement(By.Id("myInput"));

            // Click and immidiately assert!
            button.Click();
            Assert.AreEqual("Done", input.GetAttribute("value"));
        }

        [TestMethod]
        public void ExplicitWait()
        {
            _driver.Url = GetUrlForFile("SlowButton.1.html");

            var button = _driver.FindElement(By.Id("myButton"));
            var input = _driver.FindElement(By.Id("myInput"));

            button.Click();
            var wait = new WebDriverWait(_driver, TimeSpan.FromSeconds(10));     // change to 1 second to see it fail
            wait.Until(drv => input.GetAttribute("value") != "");
            wait.Until(ExpectedConditions.TextToBePresentInElementValue(input, "Done"));
            Assert.AreEqual("Done", input.GetAttribute("value"));
        }

                [TestMethod]
        public void WaitAndIgnoreExceptions()
        {
            const string filename = @"c:\temp\test.txt";
            File.Delete(filename);
            
            var wait = new WebDriverWait(_driver, TimeSpan.FromMinutes(1));
            wait.IgnoreExceptionTypes(typeof(FileNotFoundException));
            var content = wait.Until(drv => File.ReadAllText(filename));

            Assert.AreEqual("Hello", content);
        }

        [TestMethod] // Which line throws the exception?
        public void ObviousNoSuchElementException()
        {
            _driver.Url = GetUrlForFile("DummyPage.html");

            var button = _driver.FindElement(By.Id("NonExistentId"));

            button.Click();
        }

        [TestMethod]
        public void WhenImplicitlyWaitHelps()
        {
            _driver.Manage().Timeouts().ImplicitlyWait(TimeSpan.FromSeconds(10));

            _driver.Url = GetUrlForFile("DynamicElement.html");
            var button = _driver.FindElement(By.Id("myButton"));
            button.Click();

            // This element didn't exist before the click!
            var input = _driver.FindElement(By.Id("myInput"));
            Assert.AreEqual("Done", input.GetAttribute("value"));
        }

        [TestMethod]
        public void StaleReferenceElementDemo()
        {
            _driver.Url = GetUrlForFile("RemoveElement.html");
            // Find both elements first - Should succeed!
            var button = _driver.FindElement(By.Id("myButton"));
            var input = _driver.FindElement(By.Id("myInput"));

            button.Click();

            // Try to refer to input which was removed...
            Assert.AreEqual("Done", input.GetAttribute("value"));
        }

        // Pay attention to where the StaleReferenceElementException is thrown from, 
        // as opposed to where NoSuchElementException is thrown from!

        [TestMethod]
        public void UnjustifiedStaleElement()
        {
            _driver.Url = GetUrlForFile("SeemlessStaleElement.html");
            var button = _driver.FindElement(By.Id("myButton"));
            var input = _driver.FindElement(By.Id("myInput"));

            button.Click();

            Assert.AreEqual("Done", input.GetAttribute("value"));
        }

        // Question: can FindElement also throw StaleElementReferenceException?

        [TestMethod]
        public void WhatHappensWhenFindElementsFindsNothing()
        {
            _driver.Url = "http://www.google.com";
            var elements = _driver.FindElements(By.ClassName("non-exitent-class-name"));
            // What will happen?
            // a. NoSuchElementException will be throw
            // b. elements will be null
            // c. elements will contain 0 elements

            Console.WriteLine(elements.Count);
        }

        [TestMethod]
        public void FindElementsAndExplicitlyWait()
        {
            _driver.Manage().Timeouts().ImplicitlyWait(TimeSpan.FromSeconds(10));
            _driver.Url = "http://www.google.com";
            IReadOnlyCollection<IWebElement> elements;

            // FindElements by id: lst-ib
            Console.WriteLine("FindElements(By.ClassName(\"gb_P\")) started at: {0:T}", DateTime.Now);
            elements = _driver.FindElements(By.ClassName("gb_P"));
            Console.WriteLine("FindElements(By.ClassName(\"gb_P\")) returned at: {0:T}", DateTime.Now);
            Console.WriteLine("Elements found: {0}", elements.Count);
            Console.WriteLine();

            Console.WriteLine("FindElements(By.Id(\"non-exitent-id\")) started at: {0:T}", DateTime.Now);
            elements = _driver.FindElements(By.Id("non-exitent-id"));
            Console.WriteLine("FindElements(By.Id(\"non-exitent-id\")) returned at: {0:T}", DateTime.Now);
            Console.WriteLine("Elements found: {0}", elements.Count);
        }

        [TestMethod]
        public void WasTheButtonReallyClicked()
        {
            _driver.Url = GetUrlForFile("Calculate.html");
            var x = _driver.FindElement(By.Id("X"));
            x.SendKeys("5");
            var y = _driver.FindElement(By.Id("Y"));
            y.SendKeys("3");
            var button = _driver.FindElement(By.TagName("button"));
            button.Click();

            var result = _driver.FindElement(By.Id("calculationResult")).Text;
            Assert.AreEqual("8", result);

            // If this would happen only occasionally, who would you blame?
        }
    }
}
