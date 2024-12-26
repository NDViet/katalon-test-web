package org.ndviet

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

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

import internal.GlobalVariable

public class FilterAndSortHelper {
    /**
     * get the list value after sort in UI,
     * Create another list from the list value in UI, sort the second list
     * And verify two lists are the same
     * @param listElement
     * @return
     */
    @Keyword
    static def verifyListElementOrderByAlphabetASC(List<WebElement> listElement) {
        List<String> listFromUI = Utils.convertListWebElementToString(listElement)

        List<String> listForSort = []
        listForSort.addAll(listFromUI)
        listForSort.sort { a, b -> a.toLowerCase() == b.toLowerCase() ? 0 : a.toLowerCase() < b.toLowerCase() ? -1 : 1 }

        KeywordUtil.logInfo('List from UI: \n' + listFromUI.toString())
        KeywordUtil.logInfo('List after sorting ascending: \n' + listForSort.toString())

        for (int i = 0; i < listFromUI.size(); i++) {
            assert listFromUI.getAt(i) == listForSort.getAt(i)
        }
    }

    /**
     * get the list value after sort in UI,
     * Create another list from the list value in UI, sort the second list desc
     * And verify two lists are the same
     * @param listElement
     * @return
     */
    @Keyword
    static def verifyListElementOrderByAlphabetDESC(List<WebElement> listElement) {
        List<String> listFromUI = Utils.convertListWebElementToString(listElement)

        List<String> listForSort = []
        listForSort.addAll(listFromUI)

        listForSort.sort { a, b -> a.toLowerCase() == b.toLowerCase() ? 0 : a.toLowerCase() < b.toLowerCase() ? 1 : -1 }

        KeywordUtil.logInfo('List from UI: \n' + listFromUI.toString())
        KeywordUtil.logInfo('List after sorting descending: \n' + listForSort.toString())


        for (int i = 0; i < listFromUI.size(); i++) {
            assert listFromUI.getAt(i) == listForSort.getAt(i)
        }
    }

    /**
     * Get the list value after sort in UI,
     * Create another list from the list value in UI, sort the second list asc (compare the part before and after the @ character)
     * And verify two lists are the same
     * @param listElement
     * @return
     */
    @Keyword
    static def verifyListEmailElementOrderByAlphabetAsc(List<WebElement> listElement) {
        List<String> listFromUI = Utils.convertListWebElementToString(listElement)

        Utils.verifyEmailSortedAsc(listFromUI)
    }

    /**
     * Get the list value after sort in UI,
     * Create another list from the list value in UI, sort the second list desc (compare the part before and after the @ character)
     * And verify two lists are the same
     * @param listElement
     * @return
     */
    @Keyword
    static def verifyListEmailElementOrderByAlphabetDesc(List<WebElement> listElement) {
        List<String> listFromUI = Utils.convertListWebElementToString(listElement)

        Utils.verifyEmailSortedDesc(listFromUI)
    }
}

