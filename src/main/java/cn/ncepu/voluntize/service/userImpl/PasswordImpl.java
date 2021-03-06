package cn.ncepu.voluntize.service.userImpl;

import cn.ncepu.voluntize.entity.Department;
import cn.ncepu.voluntize.entity.Student;
import cn.ncepu.voluntize.vo.requestVo.LoginVo;
import cn.ncepu.voluntize.service.PasswordService;
import cn.ncepu.voluntize.util.DesUtils;
import cn.ncepu.voluntize.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class PasswordImpl extends BaseUserImpl implements PasswordService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ServletContext context;

    @Value("${spring.mail.username}")
    private String mailHost;

    @Value("${application.verify}")
    private int verifyTime;

    @Value("${frontend.reset}")
    private String reset;

    @Override
    public boolean verifyByOrigin(String password) {
        String id = (String) session.getAttribute("UserId");
        Optional<Student> optional1 = studentRepository.findById(id);
        if (optional1.isPresent()) if (password.equals(optional1.get().getPassword())) return true;
        return departmentRepository.findById(id).filter(department -> password.equals(department.getPassword())).isPresent();
    }

    @Override
    public boolean sendEmail(String id) {
        Optional<Student> optional = studentRepository.findById(id);
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optional.isPresent()) {
            return send(optional.get().getEmail(), encrypt(optional.get().getStudentNum()));
        } else if (optionalDepartment.isPresent()) {
            return send(optionalDepartment.get().getEmail(), encrypt(optionalDepartment.get().getId()));
        }
        return false;
    }

    private boolean send(String emailAddress, String password) {
        if (emailAddress == null) return false;
//        String verifyAddress = context.getAttribute("path") + "/password/verify?code=";
        String verifyAddress = reset + "?code=";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailHost);
            helper.setTo(emailAddress);
            helper.setSubject("华北电力大学公益劳动服务系统：密码找回验证邮件");
            helper.setText("<h3>请在" + verifyTime / 1000 / 60 + "分钟内复制此超链接至浏览器以修改密码，若非本人操作请忽略(๑´ڡ`๑)</h3>" +
                    verifyAddress + password, true);
            Runnable runnable = () -> {
                mailSender.send(message);
                Logger logger = LoggerFactory.getLogger(this.getClass());
                logger.info("Email send!");
            };
            new Thread(runnable).start();
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String checkEmail(String password) {
        try {
            return decrypt(password);
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.info("不能解码，邮件验证超时");
            return null;
        }
    }

    @Override
    public boolean changePassword(String password) {
        String id = (String) session.getAttribute("UserId");
        return changePassword(id, password);
    }

    @Override
    public boolean changePassword(String id, String password) {
        Optional<Student> optional = studentRepository.findById(id);
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optional.isPresent()) {
            Student student = optional.get();
            student.setPassword(password);
            studentRepository.save(student);
            return true;
        } else if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();
            department.setPassword(password);
            departmentRepository.save(department);
            return true;
        }
        return false;
    }

    /**
     * 用户信息的加密方法
     * 密钥在发送邮件时自动生成，五分钟后失效
     */
    private String encrypt(String string) {
        String secretKey = RandomUtil.getRandomString(10);
        session.setAttribute("DesKey", secretKey);
        DesUtils desUtils = new DesUtils(secretKey);
        String result = null;
        try {
            result = desUtils.encrypt(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            //注意这里不能使用lambda表达式，因为schedule方法的参数不是接口类型
            @Override
            public void run() {
                //改变密钥使其失效
                session.setAttribute("DesKey", RandomUtil.getRandomString(10));
                this.cancel();
            }
        }, verifyTime);
        return result;
    }

    /**
     * 解码方法
     */
    private String decrypt(String string) throws Exception {
        if (session.getAttribute("DesKey") != null) {
            DesUtils desUtils = new DesUtils((String) session.getAttribute("DesKey"));
            return desUtils.decrypt(string);
        }
        return null;
    }

}
