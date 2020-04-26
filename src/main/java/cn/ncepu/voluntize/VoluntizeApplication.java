package cn.ncepu.voluntize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@SpringBootApplication
@EntityScan(basePackages = "cn.ncepu.voluntize.entity")
@EnableJpaRepositories(basePackages = "cn.ncepu.voluntize.repository")
public class VoluntizeApplication extends SpringBootServletInitializer {

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(VoluntizeApplication.class);
//    }

    public static void main(String[] args) {
        SpringApplication.run(VoluntizeApplication.class, args);
    }
}
