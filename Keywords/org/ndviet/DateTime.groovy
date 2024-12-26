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
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import com.kms.katalon.core.util.KeywordUtil

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.IsoFields
import java.time.temporal.ChronoUnit

import internal.GlobalVariable

public class DateTime {
	static def getQuarterYearNow() {
		LocalDate localDate = LocalDate.now()
		int quarter = localDate.get(IsoFields.QUARTER_OF_YEAR);
		int year = localDate.getYear();
		Map<String, String> returnDate = new LinkedHashMap<>()
		returnDate.put('year', year.toString())
		returnDate.put('quarter', quarter.toString())
		KeywordUtil.logInfo('' + returnDate)
		return returnDate
	}

	static def getQuarterYearLastXYearsFromNow(long years) {
		LocalDate localDate = LocalDate.now()
		localDate = localDate.minus(years, ChronoUnit.YEARS).minus(-3, ChronoUnit.MONTHS)
		int quarter = localDate.get(IsoFields.QUARTER_OF_YEAR);
		int year = localDate.getYear();
		Map<String, String> returnDate = new LinkedHashMap<>()
		returnDate.put('year', year.toString())
		returnDate.put('quarter', quarter.toString())
		KeywordUtil.logInfo('' + returnDate)
		return returnDate
	}

	static def convertDateToQuarterYear(List listDate, String format) {
		List<Map> returnList = new ArrayList<>()
		for (String date : listDate) {
			returnList.add(convertDateToQuarterYear(date, format))
		}
		return returnList
	}

	static def convertDateToQuarterYear(String date, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		LocalDate localDate = LocalDate.parse(date, formatter);
		int quarter = localDate.get(IsoFields.QUARTER_OF_YEAR);
		int year = localDate.getYear();
		Map<String, String> returnDate = new LinkedHashMap<>()
		returnDate.put('year', year.toString())
		returnDate.put('quarter', quarter.toString())
		return returnDate
	}
}
