package org.ndviet

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import org.apache.commons.io.FileUtils
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.DataFormatter

import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.configuration.RunConfiguration
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.nio.file.*
import java.nio.file.Path
import java.nio.file.Paths
import java.text.SimpleDateFormat
import internal.GlobalVariable
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream;
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import com.kms.katalon.core.testobject.SelectorMethod as SelectorMethod
import org.openqa.selenium.JavascriptExecutor as JavascriptExecutor
import org.openqa.selenium.interactions.Actions as Actions
import org.openqa.selenium.WebDriver as WebDriver
import org.openqa.selenium.WebElement as WebElement
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import org.openqa.selenium.Keys as Keys
import org.openqa.selenium.By as By
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import groovy.io.FileType
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem
import org.apache.poi.ss.util.CellRangeAddress
import org.testng.Assert

import java.util.Iterator;

import javax.crypto.extObjectInputStream


import org.eclipse.persistence.internal.jpa.parsing.GreaterThanEqualToNode

import org.stringtemplate.v4.compiler.STParser.namedArg_return

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

import java.lang.Integer

import org.apache.poi.ss.usermodel.FormulaEvaluator;

import jxl.*;

import java.time.format.TextStyle

public class DownloadedFile {
	private static final String downloadPath = Environment.getDownloadsDirectory()

	static File getLastDownloadedFile() {
		File downloadDirectory = new File(downloadPath);
		File[] downloadedFiles = downloadDirectory.listFiles();
		if (downloadedFiles == null || downloadedFiles.length == 0) {
			return null;
		}

		File lastModifiedFile = downloadedFiles[0];
		for (int i = 1; i < downloadedFiles.length; i++) {
			if (lastModifiedFile.lastModified() < downloadedFiles[i].lastModified()) {
				lastModifiedFile = downloadedFiles[i];
			}
		}
		return lastModifiedFile;
	}

	@Keyword
	static def GetLatestDownloadedFileName() {
		String filePath = getLastDownloadedFile();
		String fileName = filePath.substring(downloadPath.length(), filePath.length())
		KeywordUtil.logInfo(fileName)
		return fileName
	}

	static File getDownloadedFile(final String fileName) {
		boolean downloaded = isFileDownloaded(fileName);
		if (downloaded) {
			return new File(downloadPath + File.separator + fileName);
		} else {
			return null;
		}
	}

	static boolean isFileDownloaded(final String fileName) {
		File downloadDirectory = new File(downloadPath);
		File[] downloadedFiles = downloadDirectory.listFiles(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						KeywordUtil.logInfo('Directory: ' + dir + " - File name: " + name)
						return name.equals(fileName);
					}
				});
	}

	@Keyword
	static def VerifyExcelValueIs(String fileName, int rowIndex, int columnIndex, String expectedValue) {
		File file = new File(downloadPath + File.separator + fileName)
		Workbook workbook = WorkbookFactory.create(file);
		Sheet currentSheet = workbook.getSheetAt(0);
		String actualValue = getCellText(rowIndex, columnIndex).trim()
		workbook.close()
		boolean isEqual = actualValue.equals(expectedValue)
		if (isEqual) {
			KeywordUtil.markPassed('Actual value: ' + actualValue + ' equals expected value: ' + expectedValue)
		} else {
			KeywordUtil.markFailedAndStop('Actual value: ' + actualValue + ' does not equal expected value: ' + expectedValue)
		}
	}

	@Keyword
	static def EditExcelValue(String fileName, int rowIndex, int columnIndex, String expectedValue, String newFileName = null, boolean overridePath = false, String sheetName = null) {
		String filePath = downloadPath + File.separator + fileName
		if (overridePath) {
			filePath = fileName
		}
		Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath))
		Sheet currentSheet
		if (sheetName == null) {
			currentSheet = workbook.getSheetAt(0)
		} else {
			currentSheet = workbook.getSheet(sheetName)
		}
		if (currentSheet.getRow(rowIndex - 1) == null) {
			currentSheet.createRow(rowIndex - 1)
		}
		currentSheet.getRow(rowIndex - 1).createCell(columnIndex - 1).setCellValue(expectedValue)
		KeywordUtil.logInfo('Input value: ' + expectedValue + ' to cell address row: ' + rowIndex + ' col: ' + columnIndex)
		String outputFilePath = ''
		if (newFileName != null) {
			outputFilePath = downloadPath + File.separator + newFileName
		} else {
			outputFilePath = filePath
		}
		File outputFile = new File(outputFilePath)
		outputFile.createNewFile()
		FileOutputStream output = new FileOutputStream(outputFilePath)
		workbook.write(output)
		output.close()
		workbook.close()
		return outputFilePath
	}

	@Keyword
	static def String copyFileToDirectory(String source, String destination) {
		File fileSource = new File(source);
		FileUtils.copyFileToDirectory(fileSource, new File(destination))
		KeywordUtil.logInfo('Copied file ' + source + ' to directory ' + destination)
		File fileDestination = new File(destination + File.separator + fileSource.getName())
		KeywordUtil.logInfo('' + fileDestination.getAbsolutePath())
		return fileDestination.getAbsolutePath()
	}

	@Keyword
	static def PrintExcelFileContent(String filePath) {
		DataFormatter formatter = new DataFormatter();
		Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath))
		Sheet currentSheet = workbook.getSheetAt(0)
		String content = ''
		for (Row row : currentSheet) {
			for (Cell cell : row) {
				content = content + String.format("%-40s", formatter.formatCellValue(cell))
			}
			content = content + '\n'
		}
		workbook.close()
		KeywordUtil.logInfo('Print contents of file ' + filePath)
		KeywordUtil.logInfo(content)
	}

	@Keyword
	static def VerifyFileIsDownloaded(String fileName) {
		boolean downloaded = getDownloadedFile(fileName)
		if (downloaded) {
			KeywordUtil.logInfo("File " + fileName + " is downloaded successfully")
		} else
			throw StepFailedException
	}

	@Keyword
	// cell start from 0 0
	static def getCellText(final int rowIndex, final int columnIndex, Sheet currentSheet = null) {
		Row row = currentSheet.getRow(rowIndex - 1);
		Cell cell = row.getCell(columnIndex - 1);
		String text = cell.getStringCellValue();
		KeywordUtil.logInfo('Get value in Cell adress row: ' + rowIndex + ' col: ' + columnIndex)
		return text;
	}

	static def ExcelDocument(String fileName) {
		File file = new File(downloadPath + File.separator + fileName)
		Workbook workbook = WorkbookFactory.create(file);
		Sheet currentSheet = workbook.getSheetAt(0);
		return currentSheet
	}

	@Keyword
	static def VerifyStudyListReportContentest(String fileName) {
		File file = new File(downloadPath + File.separator + fileName)
		Workbook workbook = WorkbookFactory.create(file);
		Sheet currentSheet = workbook.getSheetAt(0);
		for (int i = 1; i <= currentSheet.getPhysicalNumberOfRows(); i++) {
			assert getCellText(i, 1).equals(findTestData('Download_Study_List_Report').getValue(1, i))
			KeywordUtil.logInfo(getCellText(i, 1))
			assert getCellText(i, 2).equals(findTestData('Download_Study_List_Report').getValue(2, i))
			KeywordUtil.logInfo(getCellText(i, 2))
			assert getCellText(i, 3).equals(findTestData('Download_Study_List_Report').getValue(3, i))
			KeywordUtil.logInfo(getCellText(i, 3))
			assert getCellText(i, 4).equals(findTestData('Download_Study_List_Report').getValue(4, i))
			KeywordUtil.logInfo(getCellText(i, 4))
			assert getCellText(i, 5).equals(findTestData('Download_Study_List_Report').getValue(5, i))
			KeywordUtil.logInfo(getCellText(i, 5))
			assert getCellText(i, 6).equals(findTestData('Download_Study_List_Report').getValue(6, i))
			KeywordUtil.logInfo(getCellText(i, 6))
		}
		workbook.close()
	}

	@Keyword
	static def DeleteDownloadedFile(String fileName) {
		if (fileName.equals('*')) {
			FileUtils.cleanDirectory(new File(downloadPath + File.separator));
			return;
		}

		int deletedownlodedlogfile
		File dir = new File(downloadPath);
		File[] dirContents = dir.listFiles();
		KeywordUtil.logInfo("list " + dirContents)
		boolean flag = false
		String lastAttempt = '';
		if (dirContents.length > 0) {
			for (int i = 0; i < dirContents.length; i++) {
				String d = dirContents[i].getName()
				if (dirContents[i].getName().equals(fileName)) {
					// File has been found
					lastAttempt = dirContents[i].getName();
					deletedownlodedlogfile = i
					KeywordUtil.markPassed(fileName + ' exist in ' + downloadPath)
					flag = true
					break
				}
				dirContents[deletedownlodedlogfile].delete();
			}
		}
	}
}
