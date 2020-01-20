package cn.ncepu.voluntize.util;

import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.service.ActivityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 无论是FastJson还是Jackson在对象转json时都有缺陷，这里使用自编的JsonUtils彻底解决问题
 *
 * @author Gehanchen
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonUtils {

    public static Map<String, Object> objectToMap(Object obj) {
        if (obj == null) return null;

        Map<String, Object> map = new HashMap<>();

        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(obj.getClass());

            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) continue;
                Method getter = property.getReadMethod();
                Object value = getter != null ? getter.invoke(obj) : null;
                map.put(key, value);
            }
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }

    private static ArrayList<Integer> objList = new ArrayList<>();

    /**
     * <p>
     * 对象转json的过程中，会出现以下几种情况：
     * 对象为空：返回 "null"
     * 对象已经输出过：返回 "对象的类名"
     * 对象为List：返回 {对象类名:[内容1,内容2...]}
     * 对象正常：
     * Field为JsonIgnore注解过的：返回 {Field名:递归,...}
     * Field为非注释过的: 返回 {Field名:内容,...}
     * <p>
     * 如果要转化hibernate代理的持久类对象，要取消懒加载模式
     *
     * @param mode 为级联模式，防止对象里面套对象造成数据过长，1为全输出，2为省略输出
     */
    public static String objectToJson(Object object, int mode) {
        try {
            if (object == null) return "\"null\"";//探空
            else if (objList.contains(object.hashCode())) {
                //防止一对一关系中相互转化
                objList.clear();
                return "\"" + object.toString() + "\"";
            } else objList.add(object.hashCode());//注意在对象比较中hashCode散列值的运用
            StringBuilder sb = new StringBuilder();
            if (object instanceof List) {
                sb.append("{\"").append(object.getClass().getName()).append("\":[");
                if (((List) object).isEmpty()) sb.append("\"empty\",");
                else for (Object o : (List) object)
                    sb.append(objectToJson(o, mode)).append(",");
                sb.deleteCharAt(sb.length() - 1).append("]}");
                objList.clear();
                return new String(sb);
            } else {
                sb.append("{");
                return pureObjToJson(object, mode, sb);//此处可选实现
            }
        } catch (Exception e) {
            e.printStackTrace();
            objList.clear();
            return null;
        }
    }

    /**
     *  直接通过反射加载所有私有属性
     */
    private static String pureObjToJson(Object object, int mode, StringBuilder sb) throws IllegalAccessException {
        Class cls = object.getClass();

        Field[] fields = cls.getDeclaredFields();//通过反射是可以调用私有属性的值的
        for (Field f : fields) {
            f.setAccessible(true);
            Object field = f.get(object);
            //通过注解判断是否为可输出对象
            if (!f.isAnnotationPresent(JsonIgnore.class) && mode == 1)
                sb.append("\"").append(f.getName()).append("\":")
                        .append(objectToJson(field, mode)).append(",");
            else sb.append("\"").append(f.getName()).append("\":").append("\"")
                    .append(field).append("\",");
        }
        objList.clear();
        return String.format("%s}", sb.substring(0, sb.length() - 1));
    }

    /**
     * 通过内省加载属性
     */
    private static String pureObjToJson2(Object object, int mode, StringBuilder sb) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            Object field = descriptor.getReadMethod().invoke(object);
            if (!descriptor.getReadMethod().isAnnotationPresent(JsonIgnore.class) && mode == 1)
                sb.append("\"").append(descriptor.getName()).append("\":")
                        .append(objectToJson(field, mode)).append(",");
            else sb.append("\"").append(descriptor.getName()).append("\":").append("\"")
                    .append(field).append("\",");
        }
        objList.clear();
        return String.format("%s}", sb.substring(0, sb.length() - 1));
    }


    @Autowired
    private ActivityService activityService;

    @Test
    public void jsonTest() throws JsonProcessingException {
        List<Activity> activities = activityService.findStatus(Activity.ActivityStatus.CONFIRMING);
        activities.addAll(activityService.findStatus(Activity.ActivityStatus.STARTED));
        activities.addAll(activityService.findStatus(Activity.ActivityStatus.FINISHED));
        System.out.println(new ObjectMapper().writeValueAsString(activities));
//        System.out.println(objectToJson(activities,1));
    }
}
