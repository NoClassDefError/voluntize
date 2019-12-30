package cn.ncepu.voluntize;

import cn.ncepu.voluntize.requestVo.ImageVo;
import cn.ncepu.voluntize.requestVo.StudentUpdateVo;
import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerTestClient {
    public static void main(String[] args) throws IOException {
        StudentUpdateVo vo = new StudentUpdateVo();
        vo.setEmail("12345");
        vo.setName("12");
        vo.setPhoneNum("1234");
        ImageVo imageVo = new ImageVo();
        imageVo.setName("1234");
        imageVo.setUrl("123456");
        ArrayList<ImageVo> imageVos = new ArrayList<>();
        imageVos.add(imageVo);
        vo.setProfiles(imageVos);
        String request = JSON.toJSONString(vo);
        System.out.println(request);
        while (System.in.read() != -1) {
            //发送请求的URL
            String url = "http://localhost:8888/volunteer/test";
            //编码格式
            String charset = "UTF-8";
            //请求内容
            CloseableHttpClient client = HttpClients.createDefault();
            //HTTP请求类型创建HttpPost实例
            HttpPost post = new HttpPost(url);
            //使用addHeader方法添加请求头部,诸如User-Agent, Accept-Encoding等参数.
            post.setHeader("Content-Type", "application/json;charset=UTF-8");
            //组织数据
            StringEntity se = new StringEntity(request);
            //设置编码格式
            se.setContentEncoding(charset);
            //设置数据类型
            se.setContentType("application/json");
            //对于POST请求,把请求体填充进HttpPost实体.
            post.setEntity(se);
            //通过执行HttpPost请求获取CloseableHttpResponse实例 ,从此CloseableHttpResponse实例中获取状态码,错误信息,以及响应页面等等.
            CloseableHttpResponse response = client.execute(post);
        }
    }
}
