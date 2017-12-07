package net.hehe.excel;

import com.alibaba.fastjson.JSONObject;
import net.hehe.utils.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.Assert;

/**
 * @Author neo·tao
 * @Date 2017/12/1
 * @Desc
 */
public class XlsReader implements ExcelUtils{
    @Override
    public JSONObject getJsonData(HSSFWorkbook excel) {
        Assert.notNull(excel,"excel file can not be null !");

        return null;
    }
}
