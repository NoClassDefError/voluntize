package cn.ncepu.voluntize.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.net.Inet4Address;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Component
public class StartUpConfig implements CommandLineRunner {

    @Autowired
    private ServletContext context;

    @Autowired
    private Environment environment;

    @Value("${application.ip}")
    private String ip;

    @Bean
    public ScheduledExecutorService getScheduledExecutorService(){
        return Executors.newSingleThreadScheduledExecutor();
    }


    @Override
    public void run(String... args) {
        String url = "http://"+ ip + ":"
                + environment.getProperty("local.server.port") + environment.getProperty("server.servlet.context-path");
        context.setAttribute("path", url);
        context.setAttribute("autoSendActivity", false);
        LoggerFactory.getLogger(this.getClass()).info("Setting server url to context:" + url);
    }
}
