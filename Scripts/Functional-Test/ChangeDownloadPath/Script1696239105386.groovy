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
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import org.ndviet.Environment as Environment
import org.ndviet.DownloadedFile as DownloadedFile
import com.kms.katalon.core.webui.driver.DriverFactory
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver


WebUI.openBrowser('https://demoqa.com/upload-download')

WebUI.waitForElementClickable(findTestObject('Object Repository/DemoQA/UploadDownload/btnDownload'), 10)

WebUI.click(findTestObject('Object Repository/DemoQA/UploadDownload/btnDownload'))

WebUI.delay(2)

File downloadFile = new File((Environment.getDownloadsDirectory() + File.separator) + 'sampleFile.jpeg')

assert downloadFile.exists()

@com.kms.katalon.core.annotation.SetUp
def setup() {
    customArgs = ['--incognito', 'disable-features=DownloadBubble,DownloadBubbleV2', '--no-sandbox']

    customPrefs = [('download.default_directory') : Environment.getDownloadsDirectory(), ('download.prompt_for_download') : false]

    localState = [('browser.enabled_labs_experiments') : ['open-download-dialog@2']]

    RunConfiguration.setWebDriverPreferencesProperty('args', customArgs)

    RunConfiguration.setWebDriverPreferencesProperty('prefs', customPrefs)

    RunConfiguration.setWebDriverPreferencesProperty('localState', localState)
	
	ChromeOptions options = new ChromeOptions();
	options.setEnableDownloads(true);
	options.addArguments(customArgs);
	WebDriver driver = new ChromeDriver(options);
	DriverFactory.changeWebDriver(driver)
}

@com.kms.katalon.core.annotation.TearDown
def teardown() {
	DownloadedFile.DeleteDownloadedFile(Environment.getDownloadsDirectory())
}

