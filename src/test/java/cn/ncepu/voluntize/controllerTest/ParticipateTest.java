package cn.ncepu.voluntize.controllerTest;

import cn.ncepu.voluntize.controller.student.Participate;
import cn.ncepu.voluntize.controller.student.StudentQuery;
import cn.ncepu.voluntize.vo.requestVo.LoginVo;
import cn.ncepu.voluntize.vo.requestVo.ParticipateVo;
import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * mockmvc单元测试就是个笨拙的辣鸡，根本不如使用postman手动测试
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
@Deprecated
public class ParticipateTest {
    private MockMvc mock;//定义一个 MockMvc

    @Autowired
    private WebApplicationContext webApplicationContext;

    /**
     * 初始化 MockMvc 通过MockMvcBuilders.standaloneSetup 模拟一个 UserController 测试环境，通过build得到一个MockMvc
     */
    @Before
    public void setUp() throws Exception {
        mock = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        //要先登录学生账号
//        String login = JSONObject.toJSONString(new LoginVo("002", "234567"));
//        RequestBuilder builder = MockMvcRequestBuilders.post("localhost:8888/volunteer/login")
//                .contentType(MediaType.APPLICATION_JSON).content(login);
//        mock.perform(builder).andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
    }

    //    @Test
    public void findIndexActivities() throws Exception {
        RequestBuilder builder = MockMvcRequestBuilders.post("localhost:8888/volunteer/student/query/findIndexActivities")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("page", "0");
        mock.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    //    @Test
    public void participate() throws Exception {
        ParticipateVo participateVo = new ParticipateVo();
        participateVo.setInfo("test1");
        participateVo.setPeriodId("0004");
        String participateVoJson = JSONObject.toJSONString(participateVo);
        RequestBuilder builder = MockMvcRequestBuilders.post("/volunteer/student/service/participate")
                .contentType(MediaType.APPLICATION_JSON).content(participateVoJson);
        mock.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
