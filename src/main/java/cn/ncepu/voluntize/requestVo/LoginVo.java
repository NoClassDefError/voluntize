package cn.ncepu.voluntize.requestVo;

import lombok.Data;

/**
 * 学生账号，管理员账号，部门账号同称为UserVo
 */
@Data
public class LoginVo {
    public LoginVo() {

    }

    public LoginVo(String id, String password) {
        this.id = id;
        this.password = password;
    }

    private String id;
    private String password;
}
