package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;

@RestController
@RefreshScope
public class UploadImage extends BaseController {
    @Value("${application.uploadDir}")
    private String uploadDir;

    @Autowired
    ServletContext context;

    /**
     * 图片上传接口
     *
     * @param file spring mvc中的文件上传格式：MultipartFile
     * @return "isEmpty" "uploadFailed" "success"
     */
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult uploadImage(@RequestParam(value = "file") MultipartFile file) {
        if (file.isEmpty()) return new HttpResult("uploadImage:isEmpty");
        // 获取文件名
        String fileName = file.getOriginalFilename();
        logger.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        logger.info("上传的后缀名为：" + suffixName);
        // 文件上传后的路径
        String filePath = uploadDir;
        // 解决中文问题，linux下中文路径，图片显示问题
        // fileName = UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) dest.getParentFile().mkdirs();
        try {
            file.transferTo(dest);
            logger.info("上传成功后的文件路径是：" + filePath + fileName);
            return new HttpResult("uploadImage:success", "url:" + "/image/" + fileName);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return new HttpResult("uploadImage:error");
    }
}
