package net.hehe.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * neo·tao@2017/9/8
 */
public class ExcelFactory extends WorkbookFactory {

    /**
     * 根据指定的工作簿名称创建excel
     *
     * @param sheetNames
     * @return
     */
    public static HSSFWorkbook createWorkbook(String[] sheetNames) {
        HSSFWorkbook wb = new HSSFWorkbook();
        for (String sheetName : sheetNames) {
            wb.createSheet(sheetName);
        }
        return wb;
    }

    public static SheetProcessor createSheetProcessor(HSSFSheet sheet) {
        return new SheetProcessor(sheet);
    }

}
