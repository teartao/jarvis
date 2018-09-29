package net.hehe.web;

import com.alibaba.fastjson.JSON;
import net.hehe.dto.RestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @Author neotao
 * @Date 2018/9/29
 * @Version V0.0.1
 * @Desc
 */
public class RestMessageConverter extends AbstractHttpMessageConverter<Object> {
    private static final Logger log = LoggerFactory.getLogger(RestMessageConverter.class);
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    @Override
    protected boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    protected Object readInternal(Class<?> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }


    @Override
    protected void writeInternal(Object data, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        String response;
        if (data instanceof RestResult) {
            response = JSON.toJSONString(data);
        } else {
            RestResult<Object> result = new RestResult<>();
            result.setData(data);
            response = JSON.toJSONString(result);
        }
        OutputStream out = outputMessage.getBody();
        out.write(response.getBytes(DEFAULT_CHARSET));
        out.flush();
    }

}
