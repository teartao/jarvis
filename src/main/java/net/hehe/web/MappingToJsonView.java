package net.hehe.web;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import org.slf4j.Logger;

/**
 * author: TaoLei
 * date: 2015/11/21.
 * description: Mapping FastJson to ModelAndView With JSON
 */
public class MappingToJsonView extends AbstractView {
    private static Logger log = LoggerFactory.getLogger(MappingToJsonView.class);

    public static final String DEFAULT_CONTENT_TYPE = "application/json;charset=UTF-8";

    public MappingToJsonView() {
        setContentType(DEFAULT_CONTENT_TYPE);
        setExposePathVariables(false);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        PropertyFilter filter = new PropertyFilter() {
            public boolean apply(Object source, String name, Object value) {
                if (name.indexOf("org.springframework.validation.BindingResult") == 0) {
                    return false;
                }
                return true;
            }
        };
        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.getPropertyFilters().add(filter);
        serializer.write(map);
        log.debug(out.toString());
        response.getWriter().print(out);
    }
}
