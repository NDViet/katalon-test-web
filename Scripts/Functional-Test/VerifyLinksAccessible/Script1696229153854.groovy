import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.WebElement

WebUI.openBrowser('https://demoqa.com/links')

WebUI.setViewPortSize(3840, 2160)

WebUI.waitForElementClickable(findTestObject('Object Repository/DemoQA/Links/Hyperlinks'), 10)

hyperlinksElement = WebUIAbstractKeyword.findWebElements(findTestObject('Object Repository/DemoQA/Links/Hyperlinks'), 10)

listUrls = []

for(WebElement el : hyperlinksElement) {
	hyperlink = el.getAttribute("href")
	listUrls.add(hyperlink)
}

KeywordUtil.logInfo("List URLs: " + listUrls)

for (String url : listUrls) {
	isAccessible = WebUI.verifyLinksAccessible([url], FailureHandling.CONTINUE_ON_FAILURE)
	KeywordUtil.logInfo("Test accessible of <" + url + ">: " + isAccessible)
}
