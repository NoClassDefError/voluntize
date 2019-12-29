package cn.ncepu.voluntize.service;

import cn.ncepu.voluntize.vo.LoginVo;

public interface LoginService extends UserService {
    /**
     * 登录服务
     *
     * @param user LoginVo
     * @return -1-登录失败 0-管理员 1-学生 2-部门
     */
    int login(LoginVo user);
}
