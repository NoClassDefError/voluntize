package cn.ncepu.voluntize.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

@Component
public class MyInterceptor implements HandlerInterceptor {

    /**
     * 用于拦截部分请求进行权限验证
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        String path = request.getRequestURI();
        String category;
        HttpSession session = request.getSession();
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Request uri: " + request.getRequestURL() + " Context path" + request.getContextPath());
        logger.info("Request content type: " + request.getContentType());
//        logger.info("Request content" );
        logger.info("Session id = " + session.getId());
        String userAgentAndIP = request.getHeader("User-Agent") + " " + request.getRemoteAddr();
        if (session.getAttribute("UserAgentAndIP") == null) {
            session.setAttribute("UserAgentAndIP", userAgentAndIP);
            logger.info("UserAgent&IP set:" + userAgentAndIP);
        } else if (!session.getAttribute("UserAgentAndIP").equals(userAgentAndIP)) {
            logger.info("Danger! UserAgent&IP was " + session.getAttribute("UserAgentAndIP") + "; Yet now " +
                    userAgentAndIP);
            dispatchToError(request, response, "你的IP地址或浏览器种类刚刚发生了突变，但会话却没有变，" +
                    "为了防止黑客劫持会话，请换个浏览器（重建会话），或者在半小时后重新访问本网站。");
            return false;
        }
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
        if ((path.contains("student")) && !"Student".equals(category)) {
            logger.info("No authority to access student interfaces!");
            dispatchToError(request, response, "你的角色是" + category + "，没有访问学生接口的权限");
            return false;
        } else if ((path.contains("department")) && !"Department".equals(category)) {
            logger.info("No authority to access department interfaces!");
            dispatchToError(request, response, "你的角色是" + category + "，没有访问部门接口的权限");
            return false;
        } else if ((path.contains("admin")) && !"Admin".equals(category)) {
            logger.info("No authority to access admin interfaces!");
            dispatchToError(request, response, "你的角色是" + category + "，没有访问管理员接口的权限");
            return false;
        }
        return true;
    }

    private void dispatchToError(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
        String s = URLEncoder.encode(message, "utf-8");
        response.sendRedirect(request.getContextPath() + "/errors?message=" + s);
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
