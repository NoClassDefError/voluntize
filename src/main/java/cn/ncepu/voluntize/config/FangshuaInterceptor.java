package cn.ncepu.voluntize.config;

import cn.ncepu.voluntize.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FangshuaInterceptor extends HandlerInterceptorAdapter {
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
            String key = request.getRequestURI();
            //从redis中获取用户访问的次数
//            System.out.println("service" + redisService);
            Integer count = redisService.get(key);
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.info("key=" + key + "count=" + count);
            if (count == null) {
                redisService.set(key, timeout);
            } else if (count < maxCount) redisService.incur(key,timeout);
            else return false;
        }
        return true;
    }
}
