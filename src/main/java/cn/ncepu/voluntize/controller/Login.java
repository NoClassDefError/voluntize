package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.requestVo.LoginVo;
import cn.ncepu.voluntize.requestVo.StudentUpdateVo;
import cn.ncepu.voluntize.responseVo.UserInfoVo;
import cn.ncepu.voluntize.service.LoginService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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
    public String login(@RequestBody LoginVo loginVo) {
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
        return JSON.toJSONString(userInfoVo);
    }

    /**
     * 登出接口
     *
     * @return 主页未登录页面
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout() {
        session.removeAttribute("UserId");
        return "index";
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public void updateStudent(@RequestBody StudentUpdateVo studentUpdateVo) {
        System.out.println(studentUpdateVo);
    }
}
