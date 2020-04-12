package cn.ncepu.voluntize.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor()).excludePathPatterns("/errors");
        registry.addInterceptor(fangshuaInterceptor()).addPathPatterns("/login");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedOrigins("*")
                .allowCredentials(true).maxAge(3600);
    }

    @Bean
    public FangshuaInterceptor fangshuaInterceptor(){
        return new FangshuaInterceptor();
    }

    @Bean
    public RedisTemplate<String, Integer> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Integer> redis = new RedisTemplate<>();
        redis.setConnectionFactory(redisConnectionFactory);
        redis.afterPropertiesSet();
        return redis;
    }

}
