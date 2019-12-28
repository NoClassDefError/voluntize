package cn.ncepu.voluntize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "cn.ncepu.voluntize.entity")
@EnableJpaRepositories(basePackages = "cn.ncepu.voluntize.repository")
public class VoluntizeApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoluntizeApplication.class, args);
    }

}
