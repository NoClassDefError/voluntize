package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.service.LoginService;
import cn.ncepu.voluntize.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 返回页面的控制器
 */
@Controller
public class Pages extends BaseController {
    @Autowired
    PasswordService passwordService;

    @Autowired
    LoginService loginService;

    @RequestMapping(value = "/errors", method = RequestMethod.GET)
    public String error(Model model, @RequestParam(name = "message") String message) throws UnsupportedEncodingException {
        String m = URLDecoder.decode(message, "utf-8");
        model.addAttribute("message", m);
        return "error";
    }

    @RequestMapping(value = "/")
    public String index() {
        return "../static/index.html";
    }

    /**
     * 邮件验证用户身份，之后跳转至密码修改页面
     *
     * @param code 包含用户信息的加密字符串
     * @return 密码修改页面或错误页面
     */
    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public String verifyByMail(Model model,@RequestParam("code") String code) {
        String id = passwordService.checkEmail(code);
        if (id != null) {
            session.setAttribute("userId", id);
            String userCategory = "";
            switch (loginService.login(id).userCategory) {
                case 0:
                    userCategory = "Admin";
                    break;
                case 1:
                    userCategory = "Student";
                    break;
                case 2:
                    userCategory = "Department";
            }
            session.setAttribute("UserCategory", userCategory);
            session.setAttribute("verified", true);
            return "http://localhost:9528/#/reset?sessionid=asdasf";
        }
        model.addAttribute("message","验证失败了");
        return "error";
    }
}
