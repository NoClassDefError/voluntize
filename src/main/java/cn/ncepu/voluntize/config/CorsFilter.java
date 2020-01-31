package cn.ncepu.voluntize.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//解决跨域问题，CORS：Cross-Origin Resource Sharing，跨域资源共享
@WebFilter(urlPatterns = "/**", filterName = "CorsFilter")
@Component
public class CorsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        response.setHeader("Access-Control-Allow-Origin", "http://2o8w878415.qicp.vip");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET,PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, content-Type, Accept, Authorization");
        response.setHeader("Access-Control-Request-Headers","Origin, X-Requested-With, content-Type, Accept, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("CorsFilter working...");
        try {
            chain.doFilter(req, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }
}
