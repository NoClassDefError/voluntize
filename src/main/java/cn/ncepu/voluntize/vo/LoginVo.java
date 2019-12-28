package cn.ncepu.voluntize.vo;

import lombok.Data;

/**
 * 学生账号，管理员账号，部门账号同称为UserVo
 */
@Data
public class LoginVo {
    private String id;
    private String password;
}
