package com.taikang.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: HuYi.Zhang
 * @create: 2018-04-24 17:20
 **/
public class JsonUtils {

    public static final ObjectMapper mapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("json序列化出错：" + obj, e);
            return null;
        }
    }

    public static <T> T toBean(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    public static <E> List<E> toList(String json, Class<E> eClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    public static <T> T nativeRead(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }


    //测试代码
//    static class User{
//        String name;
//        Integer age;
//
//        public User() {
//        }
//
//        public User(String name, Integer age) {
//            this.name = name;
//            this.age = age;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public Integer getAge() {
//            return age;
//        }
//
//        public void setAge(Integer age) {
//            this.age = age;
//        }
//
//        @Override
//        public String toString() {
//            return "User{" +
//                    "name='" + name + '\'' +
//                    ", age=" + age +
//                    '}';
//        }
//    }

//    public static void main(String[] args) {
//        User user = new User("曹锐群", 25);
        //toString---toString
//        String json = toString(user);
//        System.out.println("json = "+json);   //json = {"name":"曹锐群","age":25} json字符串数组

        //toBean---toBean 反序列化
//        User user1 = toBean(json, User.class);
//        System.out.println("user1 = " + user1); //user1 = User{name='曹锐群', age=25}

        //toList---toList
//        String json2 = "[20,-10,5,15]";
//        List<Integer> integerList = toList(json2, Integer.class);
//        System.out.println("integerList = " + integerList); //integerList = [20, -10, 5, 15]

        //toMap---toMap
        //language=JSON
//        String json3 = "{\"name\": \"小美人\",\"age\": \"20\"}";
//        Map<String, String> toMap = toMap(json3, String.class, String.class);
//        System.out.println("toMap = " + toMap);   //toMap = {name=小美人, age=20}

        //nativeRead list里面装map 复杂类型
//        String json4 = "[{\"name\": \"小美人\",\"age\": \"20\"},{\"name\": \"大美人\",\"age\": \"30\"}]";
//        List<Map<String, String>> maps = nativeRead(json4, new TypeReference<List<Map<String, String>>>() {
//        });
//        for (Map<String, String> map : maps) {
//            System.out.println("map = " + map);
//        }
//        map = {name=小美人, age=20}
//        map = {name=大美人, age=30}

//        List<User> users = nativeRead(json5, new TypeReference<List<User>>() {
//        });
//    }

}
