package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.vo.requestVo.LoginVo;
import cn.ncepu.voluntize.vo.requestVo.StudentUpdateVo;
import cn.ncepu.voluntize.vo.responseVo.UserInfoVo;
import cn.ncepu.voluntize.service.LoginService;
import cn.ncepu.voluntize.util.RsaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;

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
    public UserInfoVo login(@RequestBody LoginVo loginVo) {
        loginVo.decrypt((PrivateKey) session.getAttribute("privateKey"));
        UserInfoVo userInfoVo = service.login(loginVo);
        session.setAttribute("UserId", loginVo.getId());
        String userCategory = "";
        switch (userInfoVo.userCategory) {
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
        if (userId != null || "Visitor".equals(session.getAttribute("UserCategory")))
            return service.login(userId);
        else return new UserInfoVo(-1, null, null);
    }

    /**
     * 登出接口
     *
     * @return 主页未登录页面
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout() {
        session.removeAttribute("UserId");
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
            return RsaUtils.keyToString((Key) session.getAttribute("publicKey"));
        } else {
            KeyPair keyPair = RsaUtils.genKeyPair(1024);
            session.setAttribute("publicKey", keyPair.getPublic());
            session.setAttribute("privateKey", keyPair.getPrivate());
            return RsaUtils.keyToString(keyPair.getPublic());
        }
    }
}
