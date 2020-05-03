package cn.ncepu.voluntize.service;

import cn.ncepu.voluntize.entity.Department;
import cn.ncepu.voluntize.vo.requestVo.LoginVo;
import cn.ncepu.voluntize.vo.responseVo.*;

import java.util.List;

public interface LoginService {
    /**
     * 登录服务
     *
     * @param user LoginVo
     * @return -1-登录失败 0-管理员 1-学生 2-部门
     */
    UserInfoVo login(LoginVo user);

    UserInfoVo login(String userId);

    UserInfoVoAdmin findUser(String userId);

    List<StudentExcelVo> findStuExcel(Integer grade);

    List<DepartmentExcelVo> findDepExcel();
}
