package net.hehe.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.InputStream;

/**
 * @Author neo·tao
 * @Date 2017/12/1
 * @Desc
 */
public interface ExcelUtils {
    /**
     * get data from excel, and parse data to json
     *
     * @param fileStream
     * @return
     */
    JSONObject getJsonData(HSSFWorkbook fileStream);
}
