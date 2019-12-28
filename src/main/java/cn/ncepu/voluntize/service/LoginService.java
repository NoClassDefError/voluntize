package cn.ncepu.voluntize.service;

import cn.ncepu.voluntize.vo.LoginVo;

public interface LoginService extends UserService {
    /**
     * 登录服务
     *
     * @param user LoginVo
     * @return 登录是否成功
     */
    boolean login(LoginVo user);
}
