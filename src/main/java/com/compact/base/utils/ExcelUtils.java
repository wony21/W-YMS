package com.compact.base.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compact.base.HomeController;
import com.sun.javafx.binding.StringFormatter;

public class ExcelUtils {

	private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

	public FileInputStream fileInputStream;

	public XSSFWorkbook WorkBook;
	public XSSFSheet Sheet;

	public void OpenExcel(String fileName) throws IOException {
		fileInputStream = new FileInputStream(fileName);
		WorkBook = new XSSFWorkbook(fileInputStream);
	}

	public void OpenSheetLike(String sheetName) {
		int sheetCount = WorkBook.getNumberOfSheets();
		for (int i = 0; i < sheetCount; i++) {
			String xlsSheetName = WorkBook.getSheetName(i);
			if (xlsSheetName.replace(" ", "").contains("성적서")) {
				Sheet = WorkBook.getSheetAt(i);
			}
		}
	}

	public String GetCellString(int rowIndex, int colIndex) {
		XSSFRow row = Sheet.getRow(rowIndex);
		XSSFCell cell = row.getCell(colIndex);
		CellType cellType = cell.getCellTypeEnum();
		String cellValue = cell.getRawValue();
		if (cellType == CellType.STRING) {
			cellValue = cell.getStringCellValue();
		} else if (cellType == CellType.NUMERIC) {
			cellValue = String.valueOf(cell.getNumericCellValue());
		} else if (cellType == CellType.FORMULA) {
			try {
				cellValue = cell.getRawValue();
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
		}
		return cellValue;
	}
	
	public String GetCellString2(int rowIndex, int colIndex) {
		XSSFRow row = Sheet.getRow(rowIndex);
		XSSFCell cell = row.getCell(colIndex);
		CellType cellType = cell.getCellTypeEnum();
		String cellValue = cell.getRawValue();
		if (cellType == CellType.STRING) {
			cellValue = cell.getStringCellValue();
		} else if (cellType == CellType.NUMERIC) {
			double cellDoubleValue = cell.getNumericCellValue();
			cellValue = String.valueOf((int)cellDoubleValue);
		} else if (cellType == CellType.FORMULA) {
			try {
				cellValue = cell.getRawValue();
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
		}
		return cellValue;
	}

	public String GetCellNumberFormat(int rowIndex, int colIndex, String format) {
		try {

			XSSFRow row = Sheet.getRow(rowIndex);
			XSSFCell cell = row.getCell(colIndex);
			CellType cellType = cell.getCellTypeEnum();
			String cellValue = cell.getRawValue();
			logger.info("CellType : " + cellType.toString());
			logger.info("RawValue : " + cellValue);

			if (cellType == CellType.STRING) {
				cellValue = cell.getStringCellValue();
			} else if (cellType == CellType.NUMERIC) {
				Double cellDouble = cell.getNumericCellValue();
				logger.info("double value : {}", cellDouble);
				DecimalFormat decimalFormat = new DecimalFormat(format);
				cellValue = decimalFormat.format(cellDouble);
			} else if (cellType == CellType.FORMULA) {
				try {
					cellValue = cell.getRawValue();

					try {
						BigDecimal bigDecimal = new BigDecimal(cellValue);
						DecimalFormat decimalFormat = new DecimalFormat(format);
						cellValue = decimalFormat.format(bigDecimal.doubleValue());
						logger.info("Big Decimal : " + cellValue);
					} catch (Exception e) {
						logger.error("Big Decimal Error " + e.getMessage());
					}

				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
			return cellValue;

		} catch (Exception e) {
			logger.error(e.getMessage());
			return "";
		}
	}

	public String GetCellStringFromDate(int rowIndex, int colIndex) {
		XSSFRow row = Sheet.getRow(rowIndex);
		XSSFCell cell = row.getCell(colIndex);
		CellType cellType = cell.getCellTypeEnum();
		String cellValue = cell.getRawValue();
		if (cellType == CellType.STRING) {
			cellValue = cell.getStringCellValue();
		} else if (cellType == CellType.NUMERIC) {
			cellValue = String.valueOf(cell.getNumericCellValue());
		} else if (cellType == CellType.FORMULA) {
			try {
				cellValue = cell.getRawValue();
				if (StringUtils.isNumeric(cellValue)) {
					cellValue = DateFormatUtils.format(cell.getDateCellValue(), "yyyy-MM-dd");
				}
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
		}
		return cellValue;
	}

	public void Close() throws IOException {
		WorkBook.close();
		fileInputStream.close();
	}

}
