package cn.ncepu.voluntize.vo.requestVo;

import cn.ncepu.voluntize.util.RsaUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.util.Base64;


/**
 * 学生账号，管理员账号，部门账号同称为UserVo
 */
@Data
public class LoginVo {
    public LoginVo() {

    }

    /**
     * 前端加密后要用Base64编码发回来，才能解码
     */
    public void decrypt(PrivateKey privateKey) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info(encrypted);
        try {
            if (!"false".equals(encrypted)) {
                this.password = new String(RsaUtils.decrypt(Base64.getDecoder().decode(this.password), privateKey));
                logger.info("decrypt: " + RsaUtils.keyToBase64String(privateKey) + " " + this.password);
            } else logger.warn("The front end did not encrypt the password.");
        } catch (Exception e) {
            logger.error("Encrypt failed");
        }
    }

    private String id;
    private String password;
    private String encrypted="";
}
