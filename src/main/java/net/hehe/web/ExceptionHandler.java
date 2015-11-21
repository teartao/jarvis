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

    //���ݿ�����쳣
    private String foreignErr = "a foreign key constraint fails";
    private String foreignMsg = "����Դ������ҵ��ռ��,�޷�ɾ��";

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,
                                              Object handler, Exception e) {
        log.error("Catch Exception: ", e);
        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject(ResponseStatus.SUCCESS, false);
        String msg = e.getLocalizedMessage();
        if (msg.indexOf(foreignErr) > 0) {
            msg = foreignMsg;
        }
        mav.addObject(ResponseStatus.ERROR_MSG, msg);
        return mav;
    }
}
