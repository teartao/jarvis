package net.hehe.utils;

import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelUtils {
    public static String getValue(Cell hssfCell) {
        String result = "";
        HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
        if (hssfCell == null) {
            return result;
        }
        switch (hssfCell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                result = replaceBlank(String.valueOf(hssfCell.getBooleanCellValue()));
                break;
            case Cell.CELL_TYPE_NUMERIC:
                result = replaceBlank(dataFormatter.formatCellValue(hssfCell));
                break;
            case Cell.CELL_TYPE_STRING:
                result = replaceBlank(String.valueOf(hssfCell.getStringCellValue()));
                break;
            case Cell.CELL_TYPE_ERROR:
                break;
        }
        return result;
    }

    static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
