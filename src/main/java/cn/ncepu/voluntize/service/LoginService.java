package cn.ncepu.voluntize.service;

import cn.ncepu.voluntize.requestVo.LoginVo;
import cn.ncepu.voluntize.responseVo.UserInfoVo;

public interface LoginService {
    /**
     * 登录服务
     *
     * @param user LoginVo
     * @return -1-登录失败 0-管理员 1-学生 2-部门
     */
    UserInfoVo login(LoginVo user);
}
