package cn.ncepu.voluntize.config;

import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
import java.io.PrintWriter;
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
        //经过反向代理，ip地址可能发生改变，于是不再判断ip地址
//        String userAgentAndIP = request.getHeader("User-Agent") + " " + request.getRemoteAddr();
        String userAgentAndIP = request.getHeader("User-Agent") + " " + getRemoteIP(request);
        if (session.getAttribute("UserAgentAndIP") == null) {
            session.setAttribute("UserAgentAndIP", userAgentAndIP);
            logger.info("UserAgent&IP set:" + userAgentAndIP);
        } else if (!session.getAttribute("UserAgentAndIP").equals(userAgentAndIP)) {
            logger.info("Danger! UserAgent&IP was " + session.getAttribute("UserAgentAndIP") + "; Yet now " +
                    userAgentAndIP);
            sendJsonMessage(response, new HttpResult("intercept:1", "message:你的IP地址或浏览器种类刚刚发生了突变，但会话却没有变，" +
                    "为了防止黑客劫持会话，已将会话锁定。请填写验证码以解锁会话。"));
//            dispatchToError(request, response, "你的IP地址或浏览器种类刚刚发生了突变，但会话却没有变，" +
//                    "为了防止黑客劫持会话，请换个浏览器（重建会话），或者在半小时后重新访问本网站。");
            return false;
        }
        if (session.getAttribute("UserCategory") == null || "".equals(session.getAttribute("UserCategory"))) {
            category = "Visitor";
            logger.info("This is a visitor.");
            Cookie isVisitor = new Cookie("Is-Visitor", "true");
            isVisitor.setPath("/");
            response.addCookie(isVisitor);
            session.setAttribute("UserCategory", category);
//            if(path.contains("login")||path.contains("verify")){
//                session.setAttribute("UserCategory",category);
//            }else return false;
        } else {
            category = (String) session.getAttribute("UserCategory");
            Cookie isVisitor = new Cookie("Is-Visitor", "false");
            isVisitor.setPath("/");
            response.addCookie(isVisitor);
            logger.info("This is a " + category);
        }
        if ((path.contains("student")) && !"Student".equals(category)) {
            logger.info("No authority to access student interfaces!");
            sendJsonMessage(response, new HttpResult("intercept:2", "message:你的角色是" + category + "，没有访问学生接口的权限"));
//            dispatchToError(request, response, "你的角色是" + category + "，没有访问学生接口的权限");
            return false;
        } else if ((path.contains("department")) && !"Department".equals(category)) {
            logger.info("No authority to access department interfaces!");
            sendJsonMessage(response, new HttpResult("intercept:3", "message:你的角色是" + category + "，没有访问部门接口的权限"));
//            dispatchToError(request, response, "你的角色是" + category + "，没有访问部门接口的权限");
            return false;
        } else if ((path.contains("admin")) && !"Admin".equals(category)) {
            logger.info("No authority to access admin interfaces!");
            sendJsonMessage(response, new HttpResult("intercept:4", "message:你的角色是" + category + "，没有访问管理员接口的权限"));
//            dispatchToError(request, response, "你的角色是" + category + "，没有访问管理员接口的权限");
            return false;
        }
        return true;
    }

    static void sendJsonMessage(HttpServletResponse response, Object obj) {
        try {
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.print(JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat));
            writer.close();
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    private void dispatchToError(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
        String s = URLEncoder.encode(message, "utf-8");
        response.sendRedirect(request.getContextPath() + "/errors?message=" + s);
    }

    static String getRemoteIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) return request.getRemoteAddr();
        return request.getHeader("x-forwarded-for").split(",")[0].split(":")[0];
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
