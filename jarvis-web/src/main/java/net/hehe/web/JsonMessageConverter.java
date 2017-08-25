package net.hehe.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * author: taolei
 * date: 15/11/22.
 * description: Converter
 */

public class JsonMessageConverter extends AbstractHttpMessageConverter<Object> {
    private static Logger log = LoggerFactory.getLogger(JsonMessageConverter.class);

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private SerializerFeature[] serializerFeature;

    private Feature[] feature;

    public SerializerFeature[] getSerializerFeature() {
        return serializerFeature;
    }

    public Feature[] getFeature() {
        return feature;
    }

    public void setFeature(Feature[] feature) {
        this.feature = feature;
    }

    public void setSerializerFeature(SerializerFeature[] serializerFeature) {
        this.serializerFeature = serializerFeature;
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        /**
         * @todo 是否需要根据supportedMediaTypes来进行判断
         */

//        return canRead(mediaType);
        return true;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        // return this.objectMapper.canSerialize(clazz) && canWrite(mediaType);
        return true;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        // should not be called, since we override canRead/Write instead
        throw new UnsupportedOperationException();
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        log.debug(clazz.getName());

        String raw = extract(inputMessage);

        //json 2 JSONObject
        if (raw == null || raw.length() == 0) {
            try {
                return clazz.newInstance();
            } catch (InstantiationException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        }

        if (raw.startsWith("[")) {

            return JSON.parseArray(raw, clazz);
        } else {
            return JSON.parseObject(raw, clazz, feature);
        }
    }

    /**
     * Controller的方法如果直接返回JSONObject，会经过此方法转换。
     * 如果JSONObject中只有业务数据，则包装结果数据。
     *
     * @param data
     * @param outputMessage
     * @throws IOException
     * @throws HttpMessageNotWritableException
     */
    @Override
    protected void writeInternal(Object data, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        String response = JSON.toJSONString(data, serializerFeature);
        log.debug("response:" + response);
        OutputStream out = outputMessage.getBody();
        out.write(response.getBytes(DEFAULT_CHARSET));
        out.flush();
    }

    private String extract(HttpInputMessage inputMessage) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int i;
        InputStream is = inputMessage.getBody();
        while ((i = is.read(buffer)) != -1) {
            baos.write(buffer, 0, i);
        }

        String raw = baos.toString(DEFAULT_CHARSET.name());
        if (raw == null) {
            throw new RuntimeException("http message is empty");
        }
        log.debug("request:" + raw);
        return raw;
    }
}
