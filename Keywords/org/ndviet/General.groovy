package org.ndviet

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import java.nio.file.Path
import java.nio.file.Paths
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW

import java.awt.Robot
import org.openqa.selenium.Keys as Keys

import com.kms.katalon.core.checkpoint.CheckpointFactory

import com.kms.katalon.core.testcase.TestCaseFactory

import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository

import com.kms.katalon.util.CryptoUtil

import groovy.transform.Field

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException


import java.text.SimpleDateFormat as SimpleDateFormat
import javax.crypto.extObjectInputStream
import org.openqa.selenium.JavascriptExecutor

import org.openqa.selenium.WebDriver as WebDriver
import org.openqa.selenium.By as By
import org.openqa.selenium.WebElement
import org.stringtemplate.v4.compiler.STParser.namedArg_return
import org.testng.Assert

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyEvent
import java.awt.MouseInfo
import java.awt.Point
import java.awt.PointerInfo
import java.awt.event.InputEvent
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import java.util.Date as Date
import java.util.Calendar as Calendar
import com.kms.katalon.core.configuration.RunConfiguration

import java.util.regex.Matcher
import java.util.regex.Pattern


class General {
	static Date today = new Date()
	static String todaysDate = today.format('ddMMyyyy')
	static String nowTime = today.time

	@Keyword
	static def setWebDriverPreferencesProperty(String downloadPath = '/Downloads') {
		String projectDir = RunConfiguration.getProjectDir()
		String downloadDir = projectDir + downloadPath
		File directory = new File(downloadDir)
		if (!directory.exists()) {
			directory.mkdirs()
		}
		downloadDir = directory.getAbsolutePath()
		RunConfiguration.setWebDriverPreferencesProperty('args', [
			'--incognito',
			'force-device-scale-factor=0.9',
			'high-dpi-support=0.9'
		])
		RunConfiguration.setWebDriverPreferencesProperty('prefs', ["browser.download.folderList": "2.0", "browser.download.manager.showWhenStarting": "false", "download.default_directory": downloadDir,
			"browser.helperApps.neverAsk.saveToDisk": "*/*", "profile.cookie_controls_mode": "0"])
		GlobalVariable.download_path = downloadDir
		KeywordUtil.logInfo('Global value for download directory: ' + GlobalVariable.download_path)
	}

	@Keyword
	def openbrowserwithurl(String applURL) {
		WebUI.openBrowser('')
		KeywordUtil.logInfo('User navigates to ' + applURL + ' url')
		WebUI.deleteAllCookies()
		WebUI.maximizeWindow()
		WebUI.navigateToUrl(applURL)
		WebUI.waitForPageLoad(10)
	}

	@Keyword
	def loginAuthenticate(String username, String password) {
		Robot robot = new Robot();
		robot.setAutoDelay(350);
		StringSelection ss = new StringSelection(username);
		Toolkit toolkit = Toolkit.getDefaultToolkit()
		Clipboard clipboard = toolkit.getSystemClipboard().setContents(ss, null);
		robot.delay(2000);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		def decryptedpassword = (CryptoUtil.decode(CryptoUtil.getDefault(password)))
		ss = new StringSelection(decryptedpassword);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}

	@Keyword
	static def pressTab() {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_TAB)
		robot.keyRelease(KeyEvent.VK_TAB)
	}

	@Keyword
	static def pressEsc() {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_ESCAPE)
		robot.keyRelease(KeyEvent.VK_ESCAPE)
	}

	@Keyword
	def zoom_pagesize_75() {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_SUBTRACT);
		robot.keyRelease(KeyEvent.VK_SUBTRACT)
		robot.keyRelease(KeyEvent.VK_CONTROL)
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_SUBTRACT);
		robot.keyRelease(KeyEvent.VK_SUBTRACT)
		robot.keyRelease(KeyEvent.VK_CONTROL)
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_SUBTRACT);
		robot.keyRelease(KeyEvent.VK_SUBTRACT)
		robot.keyRelease(KeyEvent.VK_CONTROL)
	}

	@Keyword
	def zoom_pagesize(int times, boolean isZoomIn = false) {
		Robot robot = new Robot();
		for (int i = 0; i < times; i++) {
			// 1 = 90%, 2 = 80%, 3 = 75%
			if (!isZoomIn) {
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_SUBTRACT);
				robot.keyRelease(KeyEvent.VK_SUBTRACT)
				robot.keyRelease(KeyEvent.VK_CONTROL)
			} else {
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_ADD);
				robot.keyRelease(KeyEvent.VK_ADD)
				robot.keyRelease(KeyEvent.VK_CONTROL)
			}
		}
	}

	@Keyword
	def boolean horizontalScrollBarVisible(String elementClassName) {
		String scrollWidth = WebUI.executeJavaScript('return document.getElementsByClassName("' + elementClassName + '")[0].scrollWidth.toString();', null)
		String clientWidth = WebUI.executeJavaScript('return document.getElementsByClassName("' + elementClassName + '")[0].clientWidth.toString();', null)
		KeywordUtil.logInfo('scrollWidth: ' + scrollWidth)
		KeywordUtil.logInfo('clientWidth: ' + clientWidth)
		return (scrollWidth > clientWidth)
	}

	@Keyword
	def takeaScreenshot(String Screenshot_name) {
		WebUI.takeScreenshot((((RunConfiguration.getProjectDir() + '\\Screenshots\\' + GlobalVariable.currentTestSuiteId + '\\' + GlobalVariable.currentTestCaseId + '\\' + Screenshot_name + '_' + todaysDate + '_' + nowTime))) + '.png')
	}

	@Keyword
	public static TakeScreenshot() {
		String dateTime = getDateWithFormat("yyyyMMddHHmmss")
		String reportFolderPath = RunConfiguration.getReportFolder() + "\\Screenshots\\"
		RunConfiguration.collectedTestDataProperties
		System.out.println("Final Report folder: " + reportFolderPath)
		new File(reportFolderPath).mkdirs();
		GlobalVariable.screenshotIndex += 1
		String screenshotFile = GlobalVariable.currentTestCaseId + '_SS' + GlobalVariable.screenshotIndex.toString() + "_" + dateTime + '.png'
		System.out.println("Screenshot Name: " + screenshotFile)
		KeywordUtil.logInfo("Screenshot Name: " + screenshotFile)
		WebUI.takeScreenshot(reportFolderPath + "\\" + screenshotFile)
	}

	@Keyword
	public static TakefullpageScreenshot() {
		String dateTime = getDateWithFormat("yyyyMMddHHmmss")
		String reportFolderPath = RunConfiguration.getReportFolder() + "\\Screenshots\\"
		RunConfiguration.collectedTestDataProperties
		System.out.println("Final Report folder: " + reportFolderPath)
		new File(reportFolderPath).mkdirs();
		GlobalVariable.screenshotIndex += 1
		String screenshotFile = GlobalVariable.currentTestCaseId + '_SS' + GlobalVariable.screenshotIndex.toString() + "_" + dateTime + '.png'
		System.out.println("Screenshot Name: " + screenshotFile)
		KeywordUtil.logInfo("Screenshot Name: " + screenshotFile)
		WebUI.takeFullPageScreenshot(reportFolderPath + "\\" + screenshotFile)
	}

	@Keyword
	public static String getDateWithFormat(String dateFormat, int plusDate = 0) {
		Date date = (new Date()) + plusDate
		System.out.println("Date: " + new SimpleDateFormat(dateFormat).format(date))
		return new SimpleDateFormat(dateFormat).format(date)
	}

	@Keyword
	def TestObject createTestObject(String locator) {
		TestObject tb = new TestObject()
		tb.addProperty("xpath", ConditionType.EQUALS, locator)
		return tb
	}

	@Keyword
	def checkListNotContains(List<WebElement> list, String blacklistedvalue) {
		String temp;
		KeywordUtil.logInfo(list.size() + "")
		for (WebElement element in list) {
			temp = element.getText()
			KeywordUtil.logInfo(temp)
			if (temp.equals(blacklistedvalue)) {
				return false;
			}
		}
		return true;
	}


	@Keyword
	static boolean verifyObjectPresent(TestObject objectReference) {
		try {
			WebUiCommonHelper.findWebElement(objectReference, 10)
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Keyword
	static def verifyElementTextContains(TestObject testObject, String expectedValue) {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement element = WebUI.findWebElement(testObject, 5)
		String text = element.getText()
		boolean isContain = text.contains(expectedValue)
		KeywordUtil.logInfo('Verify element text of test object ' + testObject.getObjectId())
		if (isContain) {
			KeywordUtil.logInfo('"' + text + '" contains "' + expectedValue + '"')
		} else {
			KeywordUtil.markFailedAndStop('"' + text + '" does not contain "' + expectedValue + '"')
		}
	}

	@Keyword
	static def verifyElementTextEquals(TestObject testObject, String expectedValue) {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement element = WebUI.findWebElement(testObject, 5)
		String text = element.getText().trim()
		boolean isContain = text.equals(expectedValue)
		KeywordUtil.logInfo('Verify element text of test object ' + testObject.getObjectId())
		if (isContain) {
			KeywordUtil.logInfo('"' + text + '" equals "' + expectedValue + '"')
		} else {
			KeywordUtil.markFailedAndStop('"' + text + '" does not equal "' + expectedValue + '"')
		}
	}

	@Keyword
	static def verifyElementTextMatchesRegex(TestObject testObject, String regex) {
		String text = WebUI.getText(testObject).trim()
		KeywordUtil.logInfo('Verify element text of test object ' + testObject.getObjectId())
		Pattern pattern = Pattern.compile(regex)
		Matcher matcher = pattern.matcher(text)
		if (matcher.matches()) {
			KeywordUtil.logInfo('"' + text + '" matches the regex "' + regex + '"')
		} else {
			KeywordUtil.markFailedAndStop('"' + text + '" does not match the regex "' + regex + '"')
		}
	}

	@Keyword
	static def ElementPresent(TestObject testObject, String menuitemname) {
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement element = WebUI.findWebElement(testObject, 5)
		String name = element.getText()
		if (element.isDisplayed()) {
			KeywordUtil.logInfo(menuitemname + ' is displayed')
			WebUI.delay(2)
		} else {
			KeywordUtil.logInfo(menuitemname + ' is Not displayed')
			WebUI.delay(2)
		}
	}

	@Keyword
	static def uploadFile(TestObject to, String filename) {
		WebUI.click(to)
		WebUI.delay(15)
		String filePath = RunConfiguration.getProjectDir() + '/Data Files/' + filename
		Path absolutePath = Paths.get(filePath)
		String absolutePathStr = absolutePath.toString()
		StringSelection ss = new StringSelection(absolutePathStr);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}

	@Keyword
	static def openNewTab(String url = null) {
		int currentTab = WebUI.getWindowIndex()
		WebDriver driver = DriverFactory.getWebDriver()
		JavascriptExecutor js = ((driver) as JavascriptExecutor)
		js.executeScript('window.open();')
		WebUI.delay(1)
		WebUI.switchToWindowIndex(currentTab + 1)
		if (url != null) {
			WebUI.navigateToUrl(url)
		}
	}

	@Keyword
	static def VerifyElementNonEditable(TestObject testObject) {
		WebDriver driver = DriverFactory.getWebDriver()
		WebUI.verifyElementAttributeValue(testObject, 'isContentEditable', 'false', 10)
	}

	@Keyword
	static def returnCurrentDatetimeInUTC() {
		Calendar c = Calendar.getInstance();
		TimeZone tz = TimeZone.getTimeZone("UTC");
		SimpleDateFormat sdf = new SimpleDateFormat('dd-MMM-yyyy HH:mm');
		sdf.setTimeZone(tz)
		String curdatetime = sdf.format(c.getTime())
		return curdatetime
	}
}