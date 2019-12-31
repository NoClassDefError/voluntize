package cn.ncepu.voluntize.service;

import cn.ncepu.voluntize.entity.Student;

/**
 *
 */
public interface PasswordService {
    /**
     * 判断密码是否正确
     * @param password 密码
     * @return 密码是否正确
     */
    boolean verifyByOrigin(String password);

    /**
     * 将用户身份加密成一段字符串，写在邮件中的超链接的get请求中
     * @return 发送是否成功
     */
    boolean sendEmail(String id);

    /**
     * 要求通过这个唯一的password，确认是谁
     * @param password 来自邮件中的超链接中的get请求参数
     * @return id
     */
    String checkEmail(String password);

    boolean changePassword(String password);
}
