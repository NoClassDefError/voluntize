package cn.ncepu.voluntize.config;

import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class SessionLockInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();
        Logger logger = LoggerFactory.getLogger(this.getClass());

        //经过反向代理，ip地址可能发生改变，于是不再判断ip地址
        //       String userAgentAndIP = request.getHeader("User-Agent") + " " + request.getRemoteAddr();
        String userAgentAndIP = request.getHeader("User-Agent") + " " + CategoryInterceptor.getRemoteIP(request);
        if (session.getAttribute("UserAgentAndIP") == null) {
            session.setAttribute("UserAgentAndIP", userAgentAndIP);
            logger.info("UserAgent&IP set:" + userAgentAndIP);
        } else if (!session.getAttribute("UserAgentAndIP").equals(userAgentAndIP)) {
            logger.info("Danger! UserAgent&IP was " + session.getAttribute("UserAgentAndIP") + "; Yet now " +
                    userAgentAndIP);
            request.getSession().setAttribute("locked", true);
            CategoryInterceptor.sendJsonMessage(response, new HttpResult("intercept:1", "message:你的IP地址或浏览器种类刚刚发生了突变，但会话却没有变，" +
                    "为了防止黑客劫持会话，已将会话锁定。请填写验证码以解锁会话。"));
//            dispatchToError(request, response, "你的IP地址或浏览器种类刚刚发生了突变，但会话却没有变，" +
//                    "为了防止黑客劫持会话，请换个浏览器（重建会话），或者在半小时后重新访问本网站。");
            return false;
        }
        return true;
    }
}
