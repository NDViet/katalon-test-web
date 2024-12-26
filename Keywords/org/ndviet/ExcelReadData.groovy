package org.ndviet

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject


import java.awt.Point
import java.awt.Robot
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyEvent
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement


import org.openqa.selenium.WebElement
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.DataFormatter


import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import com.kms.katalon.core.util.KeywordUtil


import internal.GlobalVariable
import java.text.SimpleDateFormat
import org.apache.commons.lang.StringEscapeUtils

public class ExcelReadData {

    @Keyword
    public static void assertMapListValues(Map actual, Map expected, boolean allColumns = true) {
        List<String> list_columns_actual = new ArrayList<>(actual.keySet())
        List<String> list_columns_expected = new ArrayList<>(expected.keySet())
        KeywordUtil.logInfo('List of column headers: ' + list_columns_actual)
        if (allColumns) {
            KeywordUtil.logInfo('Verify all columns are present and matched the order: ' + list_columns_expected)
            assert (list_columns_actual.equals(list_columns_expected))
        } else {
            KeywordUtil.logInfo('Verify expected columns are present: ' + list_columns_expected)
            assert (list_columns_actual.containsAll(list_columns_expected))
        }
        for (String column : list_columns_expected) {
            List<String> values_actual = actual[column]
            List<String> values_expected = expected[column]
            KeywordUtil.logInfo('Verify all values are present and matched in column name: ' + column)
            KeywordUtil.logInfo('List of all acutal values: ' + values_actual)
            KeywordUtil.logInfo('List of all expected values: ' + values_expected)
            assert (values_actual.equals(values_expected))
        }
    }

    private static String getFilePath(String fileName, boolean overrideFilePath = false) {
        String filePath = Environment.getDataFilesDirectory() + File.separator + fileName
        if (overrideFilePath) filePath = fileName
        Path absolutePath = Paths.get(filePath)
        return absolutePath.toString()
    }

    @Keyword
    public static List<String> getNameOfSheets(String fileName, boolean overrideFilePath = false) {
        String filePath = getFilePath(fileName, overrideFilePath)
        Workbook workbook = new XSSFWorkbook(new File(filePath))
        def nameOfSheets = []
        def int numberOfSheets = workbook.getNumberOfSheets()
        for (int i = 0; i < numberOfSheets; i++) {
            nameOfSheets.add(workbook.getSheetName(i))
        }
        return nameOfSheets
    }

    @Keyword
    public static Map<String, List<String>> getValuesAsMap(String fileName, String sheetName = null, boolean overrideFilePath = false) {
        String filePath = getFilePath(fileName, overrideFilePath)
        Workbook workbook = new XSSFWorkbook(new File(filePath));
        if (sheetName == null) {
            List<String> sheets = getNameOfSheets(fileName, overrideFilePath)
            sheetName = sheets[0]
        }
        Sheet sheet = workbook.getSheet(sheetName)
        DataFormatter formatter = new DataFormatter();
        Map<String, List<String>> sheet_map = new LinkedHashMap<>()
        Row headers = sheet.getRow(0)
        int numberOfColumns = headers.getPhysicalNumberOfCells()
        for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
            String headerName = null
            List<String> listValues = new ArrayList<>()
            for (int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                Row row = sheet.getRow(rowIndex)
                Cell cell = row.getCell(columnIndex)
                String cellValue = null
                if (cell != null) {
                    cellValue = formatter.formatCellValue(cell)
                    cellValue = StringEscapeUtils.unescapeHtml(cellValue.trim())
                    if (rowIndex > 0) {
                        listValues.add(cellValue)
                    } else {
                        headerName = cellValue
                    }
                }
            }
            sheet_map.put(headerName, listValues)
        }
        KeywordUtil.logInfo('Sheet details: ' + sheet_map.toString())
        return sheet_map
    }

    @Keyword
    public static List<HashMap<String, String>> readDataFromExcel(String fileName) {
        List<HashMap<String, String>> mapDatasList = new ArrayList()
        try {
            String filePath = Environment.getDataFilesDirectory() + File.separator + fileName
            Path absolutePath = Paths.get(filePath)
            String absolutePathStr = absolutePath.toString()
            FileInputStream file = new FileInputStream(new File(absolutePathStr))
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheet('Sheet1')
            int sourceNoOfRows = sheet.getPhysicalNumberOfRows()
            Environment.addGlobalVariable('globalVar_sourceNoOfRows', sourceNoOfRows)
            Row headerRow = sheet.getRow(0)
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row currentRow = sheet.getRow(i)
                HashMap<String, String> mapDatas = new HashMap<String, String>()
                for (int j = 0; j < headerRow.getPhysicalNumberOfCells(); j++) {
                    Cell currentCell = currentRow.getCell(j)
                    if (currentCell == null)
                        continue
                    def type = currentCell.getCellType()
                    if (type == CellType.STRING) {
                        mapDatas.put(headerRow.getCell(j).getStringCellValue(), currentCell.getStringCellValue())
                        println(currentCell.getStringCellValue())
                    } else if (type == CellType.NUMERIC) {
                        if (DateUtil.isCellDateFormatted(currentCell)) {
                            Date getDate = currentCell.getDateCellValue()
                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy")
                            String date = sdf.format(getDate)
                            mapDatas.put(headerRow.getCell(j).getStringCellValue(), date)
                        } else {
                            double getnum = currentCell.getNumericCellValue()
                            long l = (long) getnum
                            String number = String.valueOf(l)
                            mapDatas.put(headerRow.getCell(j).getStringCellValue(), number)
                        }
                    }
                }
                mapDatasList.add(mapDatas)
                Set<List<String>> mapDatasSet = new LinkedHashSet(mapDatasList)
                mapDatasList.clear()
                mapDatasList.addAll(mapDatasSet)
            }
        } catch (Throwable e) {
            e.printStackTrace()
        }
        return mapDatasList
    }

    @Keyword
    public static List<HashMap<String, String>> readDataFromErrorLog(String fileName) {
        List<HashMap<String, String>> mapDatasList = new ArrayList()
        try {
            String filePath = Environment.getDownloadsDirectory() + File.separator + fileName
            FileInputStream file = new FileInputStream(new File(filePath))
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheet('RefContactErrorValidate')
            int sourceNoOfRows = sheet.getPhysicalNumberOfRows()
            Environment.addGlobalVariable('globalVar_sourceNoOfRows', sourceNoOfRows)
            Row headerRow = sheet.getRow(0)
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row currentRow = sheet.getRow(i)
                HashMap<String, String> mapDatas = new HashMap<String, String>()
                for (int j = 0; j < headerRow.getPhysicalNumberOfCells(); j++) {
                    Cell currentCell = currentRow.getCell(j)
                    int type = currentCell.getCellType()
                    if (type == CellType.STRING) {
                        mapDatas.put(headerRow.getCell(j).getStringCellValue(), currentCell.getStringCellValue())
                    } else if (type == CellType.NUMERIC) {
                        if (DateUtil.isCellDateFormatted(currentCell)) {
                            Date getDate = currentCell.getDateCellValue()
                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy")
                            String date = sdf.format(getDate)
                            mapDatas.put(headerRow.getCell(j).getStringCellValue(), date)
                        } else {
                            double getnum = currentCell.getNumericCellValue()
                            long l = (long) getnum
                            String number = String.valueOf(l)
                            mapDatas.put(headerRow.getCell(j).getStringCellValue(), number)
                        }
                    }
                }
                mapDatasList.add(mapDatas)
                Set<List<String>> mapDatasSet = new LinkedHashSet(mapDatasList)
                mapDatasList.clear()
                mapDatasList.addAll(mapDatasSet)
            }
        } catch (Throwable e) {
            e.printStackTrace()
        }
        return mapDatasList
    }


    @Keyword
    public static List<HashMap<String, String>> readDataFromrefContactErrorLog(String fileName) {
        List<HashMap<String, String>> mapDatasList = new ArrayList()
        try {
            String filePath = Environment.getDownloadsDirectory() + File.separator + fileName
            FileInputStream file = new FileInputStream(new File(filePath))
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheet('RefContactError')
            int sourceNoOfRows = sheet.getPhysicalNumberOfRows()
            Environment.addGlobalVariable('globalVar_sourceNoOfRows', sourceNoOfRows)
            Row headerRow = sheet.getRow(0)
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row currentRow = sheet.getRow(i)
                HashMap<String, String> mapDatas = new HashMap<String, String>()
                for (int j = 0; j < headerRow.getPhysicalNumberOfCells(); j++) {
                    Cell currentCell = currentRow.getCell(j)
                    int type = currentCell.getCellType()
                    if (type == 1) {
                        mapDatas.put(headerRow.getCell(j).getStringCellValue(), currentCell.getStringCellValue())
                    } else if (type == 0) {
                        if (DateUtil.isCellDateFormatted(currentCell)) {
                            Date getDate = currentCell.getDateCellValue()
                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy")
                            String date = sdf.format(getDate)
                            mapDatas.put(headerRow.getCell(j).getStringCellValue(), date)
                        } else {
                            double getnum = currentCell.getNumericCellValue()
                            long l = (long) getnum
                            String number = String.valueOf(l)
                            mapDatas.put(headerRow.getCell(j).getStringCellValue(), number)
                        }
                    }
                }
                mapDatasList.add(mapDatas)
                Set<List<String>> mapDatasSet = new LinkedHashSet(mapDatasList)
                mapDatasList.clear()
                mapDatasList.addAll(mapDatasSet)
            }
        } catch (Throwable e) {
            e.printStackTrace()
        }
        return mapDatasList
    }
}

