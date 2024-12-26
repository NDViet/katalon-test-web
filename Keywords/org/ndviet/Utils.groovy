package org.ndviet

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle
import java.util.Iterator
import java.util.Comparators

import org.openqa.selenium.WebElement

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
import com.kms.katalon.core.configuration.RunConfiguration

import internal.GlobalVariable

import java.text.DecimalFormat
import java.math.RoundingMode;
import com.google.common.collect.Ordering
import java.lang.String

public class Utils {

    @Keyword
    static def getRandomNumber() {
        Random generator = new Random()
        return generator.nextDouble().toString()
    }

    @Keyword
    static def getGetDateWithFormat(String format = 'dd-MM-yyyy', int increase = 0) {
        Calendar calendar = Calendar.getInstance()
        Date today = new Date()
        calendar.setTime(today);
        calendar.add(Calendar.MONTH, increase)
        String campaignMonth = calendar.getTime().format(format)
        KeywordUtil.logInfo('Return Date Format: ' + campaignMonth)
        return campaignMonth
    }

    @Keyword
    static def percentageCalculator(double value, double totalValue, String decimal = null, String roundMode = 'up') {
        if (totalValue == 0) {
            return 0
        } else {
            float result = (value / totalValue) * 100
            KeywordUtil.logInfo(String.format('Result of %s / %s = %s', value, totalValue, result))
            if (decimal != null) {
                KeywordUtil.logInfo('Decimal format is enabled')
                RoundingMode rm = RoundingMode.HALF_UP //set as UP by default
                if (roundMode.equalsIgnoreCase('down')) {
                    rm = RoundingMode.HALF_DOWN
                    KeywordUtil.logInfo('Rounding mode is DOWN')
                } else {
                    KeywordUtil.logInfo('Rounding mode is UP')
                }
                DecimalFormat df = new DecimalFormat(decimal)
                df.setRoundingMode(rm)
                String returnString = df.format(result)
                KeywordUtil.logInfo('Decimal format: ' + decimal + ' - Rounded result: ' + returnString)
                return returnString
            } else {
                return result
            }
        }
    }

    @Keyword
    static def averageCalculator(double value, double totalValue, String decimal = null, String roundMode = 'up') {
        if (totalValue == 0) {
            return 0
        } else {
            float result = value / totalValue
            KeywordUtil.logInfo(String.format('Result of %s / %s = %s', value, totalValue, result))
            if (decimal != null) {
                KeywordUtil.logInfo('Decimal format is enabled')
                RoundingMode rm = RoundingMode.HALF_UP //set as UP by default
                if (roundMode.equalsIgnoreCase('down')) {
                    rm = RoundingMode.HALF_DOWN
                    KeywordUtil.logInfo('Rounding mode is DOWN')
                } else {
                    KeywordUtil.logInfo('Rounding mode is UP')
                }
                DecimalFormat df = new DecimalFormat(decimal)
                df.setRoundingMode(rm)
                String returnString = df.format(result)
                KeywordUtil.logInfo('Decimal format: ' + decimal + ' - Rounded result: ' + returnString)
                return returnString
            } else {
                return result
            }
        }
    }

    @Keyword
    static def isDateSorted(List<String> listDateAsString, boolean reverse = false, String datePattern = 'DD-MMM-YYYY') {
        KeywordUtil.logInfo('List Date: ' + listDateAsString)
        List<Date> listDate = new ArrayList<>()
        for (String dateAsString : listDateAsString) {
            listDate.add(Date.parse(datePattern, dateAsString))
        }
        if (reverse) {
            assert (Ordering.natural().reverse().nullsLast().isOrdered(listDate))
            KeywordUtil.logInfo('List Date is sorted as Descending')
        } else {
            assert (Ordering.natural().nullsFirst().isOrdered(listDate))
            KeywordUtil.logInfo('List Date is sorted as Ascending')
        }
    }

    @Keyword
    static def isSorted(List<String> listOfStrings, boolean reverse = false) {
        KeywordUtil.logInfo('List string to be verified: ' + listOfStrings)
        if (listOfStrings.isEmpty() || listOfStrings.size() == 1) {
            return true;
        }
        Iterator<String> iter
        if (reverse) {
            iter = listOfStrings.reverse().iterator()
        } else {
            iter = listOfStrings.iterator();
        }
        String current, previous = iter.next();
        while (iter.hasNext()) {
            current = iter.next();
            if (compareText(previous, current)) {
                return false;
            }
            previous = current;
        }
        return true;
    }

    private static boolean compareText(String previous, String current) {
        previous = previous.toLowerCase()
        current = current.toLowerCase()
        if (previous.indexOf('@') > 0 && current.indexOf('@') > 0) {
            String[] t1Component = previous.split('@')
            String[] t2Component = current.split('@')
            boolean firstCheck = t1Component[0].compareTo(t2Component[0]) > 0
            boolean lastCheck = t1Component[1].compareTo(t2Component[1]) > 0
            return firstCheck && lastCheck
        } else {
            return previous.compareTo(current) > 0
        }
    }

    /**
     * Convert a List WebElement into a List String
     * @param listElement
     * @return list
     */
    @Keyword
    static List<String> convertListWebElementToString(List<WebElement> listElement) {
        List<String> list = []
        for (WebElement element : listElement) {
            list.add(element.getText())
        }
        return list
    }

    /**
     * Create another list from the list params, sort the second list asc (compare the part before and after the @ character),
     * and verify two lists are the same
     * @param list
     * @return
     */
    @Keyword
    static def verifyEmailSortedAsc(List<String> list) {
        List<String> listForSort = []
        listForSort.addAll(list)

        listForSort.sort { a, b -> compareEmail(a, b) }

        KeywordUtil.logInfo('List from UI: \n' + list.toString())
        KeywordUtil.logInfo('List after sorting ascending: \n' + listForSort.toString())

        for (int i = 0; i < list.size(); i++) {
            assert list.getAt(i) == listForSort.getAt(i)
        }
    }

    /**
     * Create another list from the list params, sort the second list desc (compare the part before and after the @ character),
     * and verify two lists are the same
     * @param list
     * @return
     */
    @Keyword
    static def verifyEmailSortedDesc(List<String> list) {
        List<String> listForSort = []
        listForSort.addAll(list)

        listForSort.sort { a, b -> -1 * compareEmail(a, b) }

        KeywordUtil.logInfo('List from UI: \n' + list.toString())
        KeywordUtil.logInfo('List after sorting descending: \n' + listForSort.toString())

        for (int i = 0; i < list.size(); i++) {
            assert list.getAt(i) == listForSort.getAt(i)
        }
    }

    static def compareEmail(String a, String b) {
        def arr = a.split('@')
        def brr = b.split('@')
        if (arr[0].toLowerCase() < brr[0].toLowerCase()) {
            return -1;
        } else if (arr[0].toLowerCase() > brr[0].toLowerCase()) {
            return 1;
        } else {
            if (arr[1].toLowerCase() < brr[1].toLowerCase()) {
                return -1;
            } else if (arr[1].toLowerCase() > brr[1].toLowerCase()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * verify that is the dateStr matched the pattern datetime format?
     * @param pattern
     * @param dateStr
     * @return
     */
    @Keyword
    static def verifyDateFormat(String pattern, String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.STRICT)
        try {
            formatter.parse(dateStr)
            KeywordUtil.markPassed(dateStr + ' matches ' + pattern + ' format')
        } catch (Exception e) {
            KeywordUtil.markFailedAndStop(dateStr + ' not matches ' + pattern + ' format')
        }
    }
}

