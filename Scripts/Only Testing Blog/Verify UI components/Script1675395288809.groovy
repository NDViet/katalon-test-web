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
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.configuration.RunConfiguration

WebUI.openBrowser('http://only-testing-blog.blogspot.com/2013/09/test.html')

WebUI.takeScreenshotAsCheckpoint("Homepage")

WebUI.verifyElementPresent(findTestObject('Object Repository/Only Testing Blog/Choose File'), 5)

WebUI.uploadFile(findTestObject('Object Repository/Only Testing Blog/Choose File'), RunConfiguration.projectDir + '/Data Files/LICENSE')

WebUI.takeScreenshotAsCheckpoint("UploadFile")

i = 0
for (String country : Arrays.asList("USA", "Japan", "Germany")) {
	i += 1
	WebUI.click(findTestObject("Only Testing Blog/Available Country", ["value": country]))
	WebUI.click(findTestObject("Only Testing Blog/Add Country Button"))
	WebUI.verifyElementNotPresent(findTestObject("Only Testing Blog/Available Country", ["value": country]), 5)
	WebUI.verifyElementVisible(findTestObject("Only Testing Blog/Selected Country", ["value": country]))
	WebUI.takeScreenshotAsCheckpoint("AddCountry"+i)
}

WebUI.takeScreenshotAsCheckpoint("SelectedCountry")

i = 0
for (String country : Arrays.asList("USA", "Japan", "Germany")) {
	i += 1
	WebUI.click(findTestObject("Only Testing Blog/Selected Country", ["value": country]))
	WebUI.click(findTestObject("Only Testing Blog/Remove Country Button"))
	WebUI.verifyElementNotPresent(findTestObject("Only Testing Blog/Selected Country", ["value": country]), 5)
	WebUI.verifyElementVisible(findTestObject("Only Testing Blog/Available Country", ["value": country]))
	WebUI.takeScreenshotAsCheckpoint("RemoveCountry"+i)
}

WebUI.takeScreenshotAsCheckpoint("RemovedCountry")

WebUI.closeBrowser()