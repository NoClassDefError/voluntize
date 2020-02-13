package cn.ncepu.voluntize.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class MyInterceptor implements HandlerInterceptor {

    /**
     * 用于拦截部分请求进行权限验证
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURL().substring(request.getRequestURL().lastIndexOf("volunteer"));
        String category;
        HttpSession session = request.getSession();
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Request url: " + request.getRequestURL().toString());
        logger.info("Request content type: " + request.getContentType());
//        logger.info("Request content" );
        logger.info("Session id = " + session.getId());
        if (session.getAttribute("UserCategory") == null) {
            category = "Visitor";
            logger.info("This is a visitor.");
            response.addCookie(new Cookie("Is-Visitor", "true"));
            session.setAttribute("UserCategory", category);
//            if(path.contains("login")||path.contains("verify")){
//                session.setAttribute("UserCategory",category);
//            }else return false;
        } else {
            category = (String) session.getAttribute("UserCategory");
            response.addCookie(new Cookie("Is-Visitor", "false"));
            logger.info("This is a " + category);
        }
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
        logger.info("Response:" + response.getContentType());
    }
}
