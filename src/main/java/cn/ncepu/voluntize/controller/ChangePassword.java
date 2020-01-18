package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.service.PasswordService;
import cn.ncepu.voluntize.responseVo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/password")
public class ChangePassword extends BaseController {

    @Autowired
    PasswordService passwordService;

    /**
     * 邮件验证用户身份，之后跳转至密码修改页面
     * @param password 包含用户信息的加密字符串
     * @return 密码修改页面或错误页面
     */
    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public String verifyByMail(String password) {
        String id = passwordService.checkEmail(password);
        if (id != null) {
            session.setAttribute("userId", id);

            //
            return "changePassword";
        }
        return "verifyFailed";
    }

    /**
     * 修改密码接口，根据session中的用户信息判断用户身份
     * @param oldPassword 老密码
     * @param password 新密码
     * @return 成功或失败json字符串
     */
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult changePassword(String oldPassword,String password) {
        if(!passwordService.verifyByOrigin(oldPassword))
            return new HttpResult("changePassword:old password incorrect");
        if (passwordService.changePassword(password))
            return new HttpResult("changePassword:success");
        else return new HttpResult("changePassword:error");
    }

    /**
     * 向特定用户发送修改密码验证邮件
     * @param id 用户id
     * @return 成功或失败json字符串
     */
    @RequestMapping(value = "/sendEmail",method = RequestMethod.POST)
    @ResponseBody
    public HttpResult sendMail(String id){
        if (passwordService.sendEmail(id))
            return new HttpResult("sendEmail:success");
        else return new HttpResult("sendEmail:error");
    }
}
