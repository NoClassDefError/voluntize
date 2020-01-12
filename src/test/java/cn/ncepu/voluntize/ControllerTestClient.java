package cn.ncepu.voluntize;

import cn.ncepu.voluntize.requestVo.ImageVo;
import cn.ncepu.voluntize.requestVo.LoginVo;
import cn.ncepu.voluntize.requestVo.StudentUpdateVo;
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ControllerTestClient {

        private static String url = "http://39.106.92.15:8888/volunteer/";
//    private static String url = "http://localhost:8888/volunteer/";

    public static void main(String[] args) throws IOException {
        System.out.println("Press enter to send an request.");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String request;
            switch (scanner.nextInt()) {
                case 0:
                    StudentUpdateVo vo = getStudentUpdateVo();
                    request = JSON.toJSONString(vo);
                    System.out.println(request);
                    sendRequest(request, "test");
                    break;
                case 1:
                    LoginVo loginVo = getLoginVo();
                    request = JSON.toJSONString(loginVo);
                    System.out.println(request);
                    sendRequest(request, "login");
                    break;
            }
        }
    }

    private static LoginVo getLoginVo() {
        LoginVo loginVo = new LoginVo();
        loginVo.setId("120171020201");
        loginVo.setPassword("123456");
        return loginVo;
    }

    private static StudentUpdateVo getStudentUpdateVo() {
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
        return vo;
    }

    private static void sendRequest(String request, String interf) throws IOException {
        //编码格式
        String charset = "UTF-8";
        //请求内容
        CloseableHttpClient client = HttpClients.createDefault();
        //HTTP请求类型创建HttpPost实例
        HttpPost post = new HttpPost(url + interf);
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
        System.out.println("response:");
        IOUtils.copy(response.getEntity().getContent(), System.out);
        System.out.println();
    }
}
