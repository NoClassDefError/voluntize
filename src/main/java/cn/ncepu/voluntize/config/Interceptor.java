package cn.ncepu.voluntize.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class Interceptor implements HandlerInterceptor {
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        Logger logger = LoggerFactory.getLogger(requestMappingHandlerMapping.getHandler(request).getHandler().getClass());
        logger.info(request.getRequestURL().toString());
    }
}
