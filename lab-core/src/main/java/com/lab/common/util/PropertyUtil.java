package com.lab.common.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

    public static Properties load(String path) {
        try {
            // 使用InPutStream流读取properties文件
            InputStream in = new BufferedInputStream(new FileInputStream(path));
            Properties properties = new Properties();
            properties.load(in);
            // 获取key对应的 value 值
            return properties;
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        Properties properties = PropertyUtil.load("C:\\data\\lab\\ali-email.properties");
        properties.forEach((k, v) -> System.out.println(k + ", " + v));
        System.out.println(properties.getProperty("accessKeyId"));
    }


}
