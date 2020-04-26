package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.service.LoginService;
import cn.ncepu.voluntize.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 返回页面的控制器，经过技术攻坚，前后端彻底分离，终于不需要在后台显示任何页面，
 * 只需要将主页重定向。
 */
@Controller
@RefreshScope
public class IndexDispatcher extends BaseController {
    @Autowired
    private PasswordService passwordService;

    @Autowired
    private LoginService loginService;

    @Value("${frontend.login}")
    private String login;

    @RequestMapping(value = "/")
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect(login);
    }

    @Deprecated
//    @RequestMapping(value = "/errors", method = RequestMethod.GET)
    public String error(Model model, @RequestParam(name = "message") String message) throws UnsupportedEncodingException {
        String m = URLDecoder.decode(message, "utf-8");
        model.addAttribute("message", m);
        return "error";
    }

    /**
     * 邮件验证用户身份，之后跳转至密码修改页面
     * 老版本的密码修改方法，修改页面难以与后台分离，现已废除
     *
     * @param code 包含用户信息的加密字符串
     * @return 密码修改页面或错误页面
     */
    @Deprecated
//    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public ModelAndView verifyByMail(@RequestParam("code") String code) {
        String id = passwordService.checkEmail(code);
        logger.info("verifying:" + id);
        if (id != null) {
            session.setAttribute("UserId", id);
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
//            model.addAttribute("message","验证成功，但没能找到修改密码页面");
            //不能使用重定向，否则session改变，由于前后端分离，也没办法请求转发
            //request.getRequestDispatcher(reset).forward(request,response);
            ModelAndView modelAndView = new ModelAndView("reset");
            modelAndView.addObject("id", id);
            modelAndView.addObject("userCategory", userCategory);
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", "验证失败了");
        return modelAndView;
    }
}
