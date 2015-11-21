package net.hehe.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * author: TaoLei
 * date: 2015/11/21.
 * description:
 */
public class ExceptionHandler extends AbstractHandlerExceptionResolver {
    private static Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    //数据库相关异常
    private String foreignErr = "a foreign key constraint fails";
    private String foreignMsg = "该资源被其它业务占用,无法删除";
    private MappingToJsonView viewName;

    public void setViewName(MappingToJsonView viewName) {
        this.viewName = viewName;
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,
                                              Object handler, Exception e) {
        log.error("Catch Exception: ", e);
        ModelAndView mav = new ModelAndView(viewName);
        mav.addObject(ResponseStatus.SUCCESS, false);
        String msg = e.getLocalizedMessage();
        if (msg.indexOf(foreignErr) > 0) {
            msg = foreignMsg;
        }
        mav.addObject(ResponseStatus.ERROR_MSG, msg);
        return mav;
    }
}
