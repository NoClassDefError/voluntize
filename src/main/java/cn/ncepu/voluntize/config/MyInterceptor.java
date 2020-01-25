package cn.ncepu.voluntize.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class MyInterceptor implements HandlerInterceptor {

    @Autowired
    private HttpSession session;

    /**
     * 用于拦截部分请求进行权限验证
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getContextPath();
        String category;
        if (session == null || session.getAttribute("UserCategory") == null) {
            category = "Visitor";
        } else category = (String) session.getAttribute("UserCategory");
        Logger logger = LoggerFactory.getLogger(this.getClass());
        if ((path.indexOf("student") == 1) && !"Student".equals(category)) {
            logger.info("No authority to access student function!");
            return false;
        } else if ((path.indexOf("department") == 1) && !"Department".equals(category)) {
            logger.info("No authority to access department function!");
            return false;
        } else if ((path.indexOf("admin") == 1) && !"Admin".equals(category)) {
            logger.info("No authority to access admin function!");
            return false;
        }

        return true;
    }

    /**
     * 用于输出请求信息
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Logger logger = LoggerFactory.getLogger(handler.getClass());
        logger.info("Request: " + request.getRequestURL().toString());
        logger.info("Response:" + response.getContentType());
    }
}
