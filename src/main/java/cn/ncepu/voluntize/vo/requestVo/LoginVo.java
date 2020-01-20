package cn.ncepu.voluntize.vo.requestVo;

import cn.ncepu.voluntize.util.RsaUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;


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

    public void decrypt(PrivateKey privateKey){
        try {
            this.password = new String(RsaUtils.decrypt(this.password.getBytes(),privateKey));
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.warn("The front end did not encrypt the password.");
        }
    }

    private String id;
    private String password;
}
