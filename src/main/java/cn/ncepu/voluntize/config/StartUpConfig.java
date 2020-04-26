package cn.ncepu.voluntize.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
@RefreshScope
public class StartUpConfig implements CommandLineRunner {

    @Autowired
    private ServletContext context;

    @Autowired
    private Environment environment;

    @Bean
    public ScheduledExecutorService getScheduledExecutorService() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void run(String... args) {
        String uri = environment.getProperty("server.servlet.context-path");
        context.setAttribute("autoSendActivity", false);
        LoggerFactory.getLogger(this.getClass()).info("Setting server uri to context:" + uri);
    }
}
