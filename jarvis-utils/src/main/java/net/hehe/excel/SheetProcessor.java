package net.hehe.excel;

import net.hehe.exceptions.ExcelException;
import net.hehe.utils.ExcelUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author neo·tao
 * @Date 2017/12/18
 * @Desc
 */
public class SheetProcessor {
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

    public List<Map<String, String>> readDataFromSheet(HSSFFormulaEvaluator formulaEvaluator) {
        List<Map<String, String>> sheetData = new ArrayList<>();
        for (int rowIndex = 0; rowIndex <= this.sheet.getLastRowNum(); rowIndex++) {
            Row xlsRow = this.sheet.getRow(rowIndex);
            if (xlsRow != null) {
                Map<String, String> rowData = new HashMap<>();
                for (int colIndex = 0; colIndex <= xlsRow.getLastCellNum(); colIndex++) {
                    Cell cell = xlsRow.getCell(colIndex);
                    if (cell != null) {
                        rowData.put(String.valueOf(colIndex), ExcelUtils.getCellValue(cell, formulaEvaluator));
                    } else {
                        rowData.put(String.valueOf(colIndex), "");
                    }
                }
                sheetData.add(rowData);
            } else {
                sheetData.add(new HashMap<String, String>());
            }
        }
        return sheetData;

    }


    public static void readFromExcel() throws InvalidFormatException {
        try {
            File file = new File("E:/read.xls");
            if (!file.exists()) {
                boolean result = file.createNewFile();
            }
            HSSFWorkbook workbook = (HSSFWorkbook) WorkbookFactory.create(file);
            SheetProcessor sheetProcessor = new SheetProcessor(workbook.getSheetAt(0));
            HSSFFormulaEvaluator formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
            List<Map<String, String>> data = sheetProcessor.readDataFromSheet(formulaEvaluator);
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToExcel() {
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

    public static void main(String[] args) throws InvalidFormatException {
        readFromExcel();
//        writeToExcel();
        //        formulaOperation();
    }

    private static void formulaOperation() throws IOException {
        String[] countryName = {"张新花", "赵峰", "刘丹", "黄生功", "李春楠", "马艳珍", "张建群", "赵瑞年", "井含英", "郭元维", "王文芳",
                "段国英", "张文婷", "陈鹏英", "常发梅", "孔繁菲", "祁洪香", "韩雅楠", "范明奎", "任顺龙", "丁永乐", "马德录", "吴红英", "严进香", "史芳",
                "林玲", "王有运", "樊有祥", "靳智慧", "马梅", "陈加贤", "李万辉", "马斌花", "李梅林", "李生丰", "刘丹丹", "杨菊清", "贾锡通", "山永信", "陈少渔",
                "卢君", "任永慧", "窦珍香", "张国清", "李美玲", "曹艳慧", "刘永秀", "樊光芳", "侯尚梅", "罗生琳", "张海芬", "梁召贤", "谈明宝", "贾统梅", "王生萍",
                "周泓宇", "江秀兰", "孙小青", "马占富", "吴秀萍", "马宏伟", "德吉卓么", "梁奎先", "宋伯福", "马占刚", "李国锋", "史元", "马佐辉", "李贵福", "乔世红",
                "李晓红", "荘柳红", "戴连梅", "裴延红", "田海兵", "党忠霞", "星玉英", "张廷良", "韩国宝", "韩庆文", "赵玉莹", "马有莲", "郭丽萍", "吴秀春", "贾延章",
                "石维丙", "李成梅", "喇成明", "解生旺", "运占花", "熊成吉", "贾生升", "景源德", "李文君", "马洪淇", "李慧文", "魏学刚", "罗长平", "胡生春", "田种兴",
                "李满存", "石延花", "王生兰", "赵家军", "安生年", "田生花", "赵雪玲", "宋邦元", "张红梅", "刘岩莉", "钟光霖", "汪武祥", "李连发", "张雪莲", "逯进义",
                "马花", "思春梅", "牛永水", "刘万邦", "张小兰", "魏珍", "李永梅", "刘跃峰", "杨有德", "肖正文", "马兰", "郭永清", "蔡晓燕", "孙占德", "陈文娟", "王凌云",
                "卢世宝", "王桂梅", "宋邦宏", "李生花", "张成芳", "赵明花", "张刘贤", "赵仲慧"};
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet realSheet = workbook.createSheet("Sheet xls");
        HSSFSheet hidden = workbook.createSheet("hidden");
        HSSFCell cell = null;
        for (int i = 0, length = countryName.length; i < length; i++) {
            String name = countryName[i];
            HSSFRow row = hidden.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(name);
        }

        CellRangeAddressList regions = new CellRangeAddressList(0, 0, 0, 0);
        //获取单元格的坐标
        System.out.println(regions.getCellRangeAddress(0).formatAsString());

        Name namedCell = workbook.createName();
        namedCell.setNameName("hidden");
        namedCell.setRefersToFormula("hidden!A1:A" + countryName.length);
        //加载数据,将名称为hidden的
        DVConstraint constraint = DVConstraint.createFormulaListConstraint("hidden");

        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList addressList = new CellRangeAddressList(0, 0, 0, 0);
        HSSFDataValidation validation = new HSSFDataValidation(addressList, constraint);

        //将第二个sheet设置为隐藏
        workbook.setSheetHidden(1, true);
        realSheet.addValidationData(validation);
        FileOutputStream stream = new FileOutputStream("E:\\range.xls");
        workbook.write(stream);
        stream.close();
    }

}
