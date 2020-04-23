package cn.ncepu.voluntize.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
@EnableCaching
public class ApplicationConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor()).excludePathPatterns("/errors");
        registry.addInterceptor(dDosInterceptor()).addPathPatterns("/login");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedOrigins("*")
                .allowCredentials(true).maxAge(3600);
    }

    @Bean
    public DDosInterceptor dDosInterceptor() {
        return new DDosInterceptor();
    }

    @Bean
    public RedisTemplate<String, Integer> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Integer> redis = new RedisTemplate<>();
        redis.setConnectionFactory(redisConnectionFactory);
        redis.afterPropertiesSet();
        return redis;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory)
    {
        // 更改值的序列化方式，否则在Redis可视化软件中会显示乱码。默认为JdkSerializationRedisSerializer，
        // 要序列化的对象必须实现Serializable接口，用jackson序列化则不用
        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair
            .fromSerializer(new GenericJackson2JsonRedisSerializer());
//        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair
//                .fromSerializer(RedisSerializer.java());
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeValuesWith(pair)      // 设置序列化方式
                .entryTtl(Duration.ofHours(1)); // 设置过期时间

        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(factory))
                .cacheDefaults(defaultCacheConfig).build();
    }

    @Bean
    public KeyGenerator wiselyKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }
}
