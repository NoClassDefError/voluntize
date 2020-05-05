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
public class CategoryInterceptor implements HandlerInterceptor {

    /**
     * 用于拦截部分请求进行权限验证
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        String category;
        HttpSession session = request.getSession();
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Request uri: " + request.getRequestURL() + " Context path" + request.getContextPath());
        logger.info("Request content type: " + request.getContentType());
//        logger.info("Request content" );
        logger.info("Session id = " + session.getId());

        if (session.getAttribute("UserCategory") == null || "".equals(session.getAttribute("UserCategory"))) {
            category = "";
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

    public static String getRemoteIP(HttpServletRequest request) {
        Logger logger = LoggerFactory.getLogger(CategoryInterceptor.class);
        logger.info("Got ips:" + request.getRemoteAddr() + " " + request.getHeader("x-forwarded-for")
                + " " + request.getHeader("Proxy-Client-IP") + " " + request.getHeader("WL-Proxy-Client-IP"));
        String ip = processIP(request.getHeader("x-forwarded-for"));
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = processIP(request.getHeader("Proxy-Client-IP"));
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = processIP(request.getHeader("WL-Proxy-Client-IP"));
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();
        logger.info("Client ip addr:" + ip);
        return ip;
    }

    public static String processIP(String xff) {
        if (xff == null) return null;
        String qudiaokuohao;
        String qudiaodouhao;
        String qudiaomaohao;
        //去掉逗号
        int douhao = xff.indexOf(",");
        if (douhao != -1) qudiaodouhao = xff.substring(0, douhao);
        else qudiaodouhao = xff;
        //去掉中括号
        int begin = qudiaodouhao.indexOf("["), end = qudiaodouhao.indexOf("]");
        if (end == -1) qudiaokuohao = qudiaodouhao;
        else qudiaokuohao = qudiaodouhao.substring(begin + 1, end);
        //去掉冒号
        int maohao = qudiaokuohao.indexOf(":");
        if (maohao != -1) qudiaomaohao = qudiaokuohao.substring(0, maohao);
        else qudiaomaohao = qudiaokuohao;
        return qudiaomaohao;
//        return xff.split("^\\[((:)|(\\d))*\\]")[0];
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
