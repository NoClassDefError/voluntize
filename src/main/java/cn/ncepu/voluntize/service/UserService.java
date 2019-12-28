package cn.ncepu.voluntize.service;

import cn.ncepu.voluntize.vo.LoginVo;
import cn.ncepu.voluntize.vo.StudentUpdateVo;

/**
 * 用户账户管理，包括学生账户，部门账户<p>
 * 所包含的功能有：
 * <ul>
 *     <li>登录</li>
 *     <li>修改账户信息(不包含密码)</li>
 *     <li>直接修改密码</li>
 *     <li>邮箱密码找回</li>
 * </ul>
 */
public interface UserService {

    /**
     * 登录服务
     * @param user LoginVo
     * @return 登录是否成功
     */
    boolean login(LoginVo user);

    /**
     * 修改学生账户信息（不包括密码）
     * @param student StudentUpdateVo
     * @return 修改是否成功
     */
    boolean updateStudent(StudentUpdateVo student);


}
