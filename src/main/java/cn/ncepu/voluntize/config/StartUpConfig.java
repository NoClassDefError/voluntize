package cn.ncepu.voluntize.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.net.Inet4Address;

@Component
public class StartUpConfig implements CommandLineRunner {

    @Autowired
    private ServletContext context;

    @Autowired
    private Environment environment;

    @Override
    public void run(String... args) throws Exception {
        String url = "http://"+Inet4Address.getLocalHost().getHostAddress() + ":"
                + environment.getProperty("local.server.port") + environment.getProperty("server.servlet.context-path");
        context.setAttribute("path", url);
        context.setAttribute("autoSendActivity", false);
        LoggerFactory.getLogger(this.getClass()).info("Setting server url to context:" + url);
    }
}
