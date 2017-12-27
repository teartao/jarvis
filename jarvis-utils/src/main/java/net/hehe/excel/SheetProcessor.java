package net.hehe.excel;

import net.hehe.exceptions.ExcelException;
import net.hehe.utils.ExcelUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author neo·tao
 * @Date 2017/12/18
 * @Desc
 */
public class SheetProcessor{
    private HSSFSheet sheet;

    SheetProcessor(HSSFSheet sheet) {
        this.sheet = sheet;
    }

    /**
     * 往指定行写入数据
     *
     * @param startRowIndex
     * @param rowData
     * @return
     */
    public HSSFSheet insertSimpleRow(int startRowIndex, String[] rowData) {
        if (startRowIndex < 0) {
            throw new ExcelException("start row index must be greater than number '0' ");
        }
        HSSFRow row = this.sheet.createRow(startRowIndex);
        for (int columnIndex = 0; columnIndex < rowData.length; columnIndex++) {
            HSSFCell cellTitle = row.createCell(columnIndex);
            cellTitle.setCellValue(rowData[columnIndex]);
        }
        return this.sheet;
    }

    /**
     * 向指定工作簿中指定行往后批量插入简单格式数据
     *
     * @param startRowIndex 插入数据的起始行，表示数据从该行开始写入，必须大于或等于0
     * @param rowsData      每行对应数据
     * @return 返回编辑过的工作簿对象
     */
    public HSSFSheet insertSimpleRows(int startRowIndex, List<String[]> rowsData) {
        for (String[] currentRow : rowsData) {
            insertSimpleRow(startRowIndex, currentRow);
            startRowIndex++;
        }
        return this.sheet;
    }

    /**
     * @param cellRowIndex    单元格行号 (从0开始计数)
     * @param cellColumnIndex 单元格列号(从0开始计数)
     * @param dropDownData    下拉列表数据
     * @return
     */
    public HSSFSheet insertDropdownCell(String[] dropDownData, int cellRowIndex, int cellColumnIndex) {
        CellRangeAddressList regions = new CellRangeAddressList(cellRowIndex, cellRowIndex, cellColumnIndex, cellColumnIndex);
        //创建下拉列表数据
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(dropDownData);
        //绑定到单元格
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
        this.sheet.addValidationData(dataValidation);
        return this.sheet;
    }

    /**
     * 将sheet工作簿columnIndex指定列设置为下拉
     *
     * @param dropDownData 下拉列表选项数据
     * @param params       参数：
     *                     1、当参数格式为{a,b,c}时，将a行b列指定单元格往后c个单元格设置为下拉，当c>0时设置连续c行下拉，当c<0时设置连续c列为下拉
     *                     2、当参数格式为{1,10,A,D}时，将A1:D10的区域设置为下拉
     *                     [注意]:这里的行号列号均从0计数,且必须大于0
     * @return 操作的工作簿
     */
    public HSSFSheet insertDropdown(String[] dropDownData, int... params) {
        CellRangeAddressList regions;
        if (params[0] < 0 || params[1] < 0) {
            throw new ExcelException("cell index must be grater than 0");
        }
        if (params.length == 4) {
            regions = new CellRangeAddressList(params[0], params[1], params[2], params[3]);
            //创建下拉列表数据
        } else if (params.length == 3) {
            if (params[2] > 0) {
                regions = new CellRangeAddressList(params[0], params[0] += --params[2], params[1], params[1]);
            } else {
                regions = new CellRangeAddressList(params[0], params[0], params[1] - 1, params[1] -= ++params[2]);
            }
        } else {
            return this.sheet;
        }
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(dropDownData);
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
        this.sheet.addValidationData(dataValidation);
        return this.sheet;
    }


    public HSSFSheet insertStyleRows(int startRowIndex, List<String[]> rows, List<Object[]> styles) {
        //todo
        return insertSimpleRows(startRowIndex, rows);
    }

    public List<List<String>> readDataFromSheet() {
        List<List<String>> sheetData = new ArrayList<List<String>>();
        for (int rowIndex = 0; rowIndex <= this.sheet.getLastRowNum(); rowIndex++) {
            Row xlsRow = this.sheet.getRow(rowIndex);
            if (xlsRow != null) {
                List<String> rowData = new ArrayList<String>();
                for (int colIndex = 0; colIndex <= xlsRow.getLastCellNum(); colIndex++) {
                    Cell cell = xlsRow.getCell(colIndex);
                    if (cell != null) {
                        rowData.add(ExcelUtils.getValue(cell));
                    } else {
                        rowData.add("");
                    }
                }
                sheetData.add(rowData);
            } else {
                sheetData.add(new ArrayList<String>());
            }
        }
        return sheetData;

    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String sheetName = "sheet1";
        HSSFWorkbook workbook = ExcelFactory.createWorkbook(new String[]{sheetName});
        SheetProcessor sheetProcessor = ExcelFactory.createSheetProcessor(workbook.getSheetAt(0));
        sheetProcessor.insertSimpleRow(1, new String[]{"111", "222", "333"});
        sheetProcessor.insertDropdown(new String[]{"aa", "vv", "ss"}, 1, 1, 5);
        try {
            File file = new File("E:/xx.xls");
            if (!file.exists()) {
                boolean result = file.createNewFile();
            }
            //将excel写入
            FileOutputStream os = new FileOutputStream(file);
            workbook.write(os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
