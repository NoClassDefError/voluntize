package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.service.LoginService;
import cn.ncepu.voluntize.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class Login extends BaseController {

    @Autowired
    private LoginService service;

    @Autowired
    private HttpSession session;

    /**
     * 登录接口，自动判断用户种类，允许用户多设备同时登录
     * @param loginVo 用户名和密码
     * @return 3种用户页面和错误页面
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(LoginVo loginVo) {
        switch (service.login(loginVo)) {
            case 0:
                session.setAttribute("userId", loginVo.getId());
                return "adminIndex";
            case 1:
                session.setAttribute("userId", loginVo.getId());
                return "studentIndex";
            case 2:
                session.setAttribute("userId", loginVo.getId());
                return "departmentIndex";
            default:
                return "loginFailed";
        }
    }

    /**
     * 登出接口
     * @return 主页未登录页面
     */
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public String logout(){
        session.removeAttribute("userId");
        return "index";
    }
}
