package cn.ncepu.voluntize.config;

import cn.ncepu.voluntize.service.RedisService;
import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DDosInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private RedisService redisService;

    //一秒内请求5次
    @Value("${fangshua.maxCount}")
    private int maxCount;

    //统计访问量时限
    @Value("${fangshua.timeout}")
    private int timeout;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //判断请求是否属于方法的请求
        if (handler instanceof HandlerMethod) {
            if(request.getSession().getAttribute("locked")!=null)
            if ((boolean) request.getSession().getAttribute("locked")) {
                MyInterceptor.sendJsonMessage(response, new HttpResult("intercept:6",
                        "message:会话被锁定，请填写验证码解锁会话"));
                return false;
            }
            String key = MyInterceptor.getRemoteIP(request);
            //从redis中获取用户访问的次数
//            System.out.println("service" + redisService);
            Integer count = redisService.get(key);
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.info("key=" + key + "count=" + count);
            if (count == null) {
                redisService.set(key, timeout);
            } else if (count < maxCount) redisService.incur(key, timeout);
            else {
                request.getSession().setAttribute("locked", true);
                MyInterceptor.sendJsonMessage(response, new HttpResult("intercept:5",
                        "message:登录次数过于频繁，锁定会话，请填写验证码解锁会话"));
                return false;
            }
        }
        return true;
    }
}
