package cn.ncepu.voluntize.vo.responseVo;

import java.util.HashMap;

/**
 * 用于后端向前端发送结果信息，转成json字符串
 */
public class HttpResult {
    public HashMap<String, String> result = new HashMap<>();

    /**
     * 单个字符串中包含一个键值对，之间用冒号隔开，例如：</br>
     * ResultFactory("result:success","this:error")
     *
     * @param strings 多个键值对
     */
    public HttpResult(String... strings) {
        for (String s : strings) {
            String[] kv = s.split(":");
            result.put(kv[0], kv[1]);
        }
    }
}
