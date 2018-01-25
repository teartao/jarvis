package net.hehe.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelUtils {

    static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    //未处理公式
    public static String getCellValue(Cell cell, FormulaEvaluator formulaEvaluator) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//非线程安全
                    return sdf.format(cell.getDateCellValue());
                } else {
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    return String.valueOf(cell.getStringCellValue());
                }
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString().trim();
            case Cell.CELL_TYPE_FORMULA:
                return formulaEvaluator.evaluate(cell).getStringValue();
            case Cell.CELL_TYPE_ERROR:
                return "";
            default:
                return "";
        }
    }
}
