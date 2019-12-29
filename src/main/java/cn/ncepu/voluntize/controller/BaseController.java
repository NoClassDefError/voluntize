package cn.ncepu.voluntize.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 包含所有服务接口和工具类的初始化
 */
@RestController
public abstract class BaseController {
    @Autowired
    HttpSession session;

    Logger logger;
    public BaseController(){
        logger= LoggerFactory.getLogger(this.getClass());
    }
}
