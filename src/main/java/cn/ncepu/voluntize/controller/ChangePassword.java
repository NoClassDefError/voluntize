package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.service.PasswordService;
import cn.ncepu.voluntize.util.CaptchaUtil;
import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/password")
public class ChangePassword extends BaseController {

    @Autowired
    private PasswordService passwordService;

    /**
     * 修改密码接口，根据session中的用户信息判断用户身份
     *
     * @param oldPassword 老密码
     * @param password    新密码
     * @return 成功或失败json字符串
     */
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult changePassword(String oldPassword, String password) {
        if (!passwordService.verifyByOrigin(oldPassword))
            return new HttpResult("changePassword:old password incorrect");
        if (passwordService.changePassword(password))
            return new HttpResult("changePassword:success");
        else return new HttpResult("changePassword:error");
    }

    /**
     * 老版本邮箱修改密码的方法，现已废除
     */
    @Deprecated
//    @RequestMapping(value = "/changePasswordByMail", method = RequestMethod.POST)
//    @ResponseBody
    public ModelAndView changePasswordByMail(String newPassword) {
        //用户直接点击链接访问verify与此接口的session不同，就不能使用session来验证
        ModelAndView modelAndView = new ModelAndView("result");
        if (session.getAttribute("verified") != null && passwordService.changePassword(newPassword))
            modelAndView.addObject("result", "修改成功");
        else modelAndView.addObject("result", "修改失败");
        return modelAndView;
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult changePasswordByMail2(String code, String newPassword) {
        String id = passwordService.checkEmail(code);
        if (id != null) {
            passwordService.changePassword(id, newPassword);
            return new HttpResult("verify:修改成功", "id:" + id);
        } else return new HttpResult("verify:验证超时");
    }

    /**
     * 向特定用户发送修改密码验证邮件
     * 添加了验证码功能 此方法废除
     *
     * @param id 用户id
     * @return 成功或失败json字符串
     */
//    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    @ResponseBody
    @Deprecated
    public HttpResult sendMail(String id) {
        if (passwordService.sendEmail(id))
            return new HttpResult("sendEmail:success");
        else return new HttpResult("sendEmail:error");
    }

    /**
     * 用于生成带四位数字验证码的图片
     */
    @RequestMapping(value = "/captcha")
    @ResponseBody
    public String imageCode(HttpServletResponse response) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        OutputStream os = response.getOutputStream();
        //返回验证码和图片的map
        Map<String, Object> map = CaptchaUtil.getImageCode(86, 37);
        session.setAttribute("simpleCaptcha", map.get("strEnsure").toString().toLowerCase());
        session.setAttribute("codeTime", new Date().getTime());
        try {
            ImageIO.write((BufferedImage) map.get("image"), "jpg", os);
        } catch (IOException e) {
            return "";
        } finally {
            if (os != null) {
                os.flush();
                os.close();
            }
        }
        return null;
    }

    /**
     * @param checkCode 前端用户输入返回的验证码
     * @param id 用户id
     */
    @RequestMapping(value = "/sendEmail")
    @ResponseBody
    public HttpResult checkCode(HttpSession session, String checkCode, String id) {
        // 获得验证码对象
        Object cko = session.getAttribute("simpleCaptcha");
        if (cko == null) return new HttpResult("sendEmail:请输入验证码！");
        String captcha = cko.toString();
        // 验证码有效时长为1分钟
        Date now = new Date();
        long codeTime = Long.parseLong(session.getAttribute("codeTime") + "");
        // 判断验证码输入是否正确
        if (StringUtils.isEmpty(checkCode) || !(checkCode.equalsIgnoreCase(captcha)))
            return new HttpResult("sendEmail:验证码错误，请重新输入！");
        else if ((now.getTime() - codeTime) / 1000 / 60 > 1) return new HttpResult("sendEmail:验证码已失效，请重新输入！");
        else {
            // 在这里可以处理自己需要的事务，比如验证登陆等
            if (passwordService.sendEmail(id))
                return new HttpResult("sendEmail:邮件发送成功");
            else return new HttpResult("sendEmail:邮件发送失败");
        }
    }
}
