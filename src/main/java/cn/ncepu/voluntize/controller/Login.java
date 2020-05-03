package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.config.CategoryInterceptor;
import cn.ncepu.voluntize.service.DDosRedisService;
import cn.ncepu.voluntize.service.LoginService;
import cn.ncepu.voluntize.util.RsaUtils;
import cn.ncepu.voluntize.vo.requestVo.LoginVo;
import cn.ncepu.voluntize.vo.requestVo.StudentUpdateVo;
import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import cn.ncepu.voluntize.vo.responseVo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Date;

@RestController
public class Login extends BaseController {

    @Autowired
    private LoginService service;

    @Autowired
    private HttpSession session;

    /**
     * 登录接口，自动判断用户种类，允许用户多设备同时登录
     *
     * @param loginVo 用户名和密码
     * @return 3种用户信息
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public UserInfoVo login(HttpServletResponse response, @RequestBody LoginVo loginVo) {
        loginVo.decrypt((PrivateKey) session.getAttribute("privateKey"));
        UserInfoVo userInfoVo = service.login(loginVo);
        //在这一回合立即set isVisitor
//        Cookie isVisitor;
//        if (userInfoVo.getUserCategory() == -1) isVisitor = new Cookie("Is-Visitor", "true");
//        else isVisitor = new Cookie("Is-Visitor", "false");
//        isVisitor.setPath("/");
//        response.addCookie(isVisitor);

        session.setAttribute("UserId", loginVo.getId());
        String userCategory = "";
        switch (userInfoVo.userCategory) {
            case -1:
                userCategory = "";
                break;
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
        logger.info(userInfoVo.toString());
        return userInfoVo;
    }

    @RequestMapping(value = "/getLoginVo", method = RequestMethod.POST)
    @ResponseBody
    public UserInfoVo getLoginVo() {
        String userId = (String) session.getAttribute("UserId");
        if (userId != null && !"Visitor".equals(session.getAttribute("UserCategory")))
            return service.login(userId);
        else return new UserInfoVo(-1, null, null);
    }

    /**
     * 登出接口
     *
     * @return 主页未登录页面
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(HttpServletResponse response) {
        session.removeAttribute("UserId");
        session.removeAttribute("UserCategory");
//        Cookie isVisitor = new Cookie("Is-Visitor", "true");
//        isVisitor.setPath("/");
//        response.addCookie(isVisitor);
        logger.info("logout");
        return "index";
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public void updateStudent(@RequestBody StudentUpdateVo studentUpdateVo) {
        logger.info(studentUpdateVo.toString());
    }

    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public String testHttps(String info) {
        logger.info(info);
        return info;
    }

    @RequestMapping(value = "/publicKey", method = RequestMethod.GET)
    public String getPublicKey() {
        if (session.getAttribute("publicKey") != null) {
            return RsaUtils.keyToBase64String((Key) session.getAttribute("publicKey"));
        } else {
            KeyPair keyPair = RsaUtils.genKeyPair(1024);
            session.setAttribute("publicKey", keyPair.getPublic());
            session.setAttribute("privateKey", keyPair.getPrivate());
            logger.info("gen:private :" + keyPair.getPrivate().toString());
            return RsaUtils.keyToBase64String(keyPair.getPublic());
        }
    }

    @Autowired
    DDosRedisService dDosRedisService;

    @RequestMapping(value = "/unlock")
    @ResponseBody
    public HttpResult unlock(HttpServletRequest request,@RequestParam String checkCode) {
        // 获得验证码对象
        Object cko = session.getAttribute("simpleCaptcha");
        if (cko == null) return new HttpResult("unlock:请输入验证码！");
        String captcha = cko.toString();
        // 验证码有效时长为1分钟
        Date now = new Date();
        long codeTime = Long.parseLong(session.getAttribute("codeTime") + "");
        // 判断验证码输入是否正确
        if (StringUtils.isEmpty(checkCode) || !(checkCode.equalsIgnoreCase(captcha)))
            return new HttpResult("unlock:验证码错误，请重新输入！");
        else if ((now.getTime() - codeTime) / 1000 / 60 > 1) return new HttpResult("unlock:验证码已失效，请重新输入！");
        else {
            session.setAttribute("locked", false);
            String ip = CategoryInterceptor.getRemoteIP(request);
            String userAgentAndIP = request.getHeader("User-Agent") + " " + ip;
            session.setAttribute("UserAgentAndIP", userAgentAndIP);
            logger.info("UserAgent&IP reset:" + userAgentAndIP);
            dDosRedisService.remove(ip);
            return new HttpResult("unlock:success");
        }
    }
}
