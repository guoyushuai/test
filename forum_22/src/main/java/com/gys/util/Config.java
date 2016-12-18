package com.gys.util;

import com.gys.exception.DataAccessException;

import java.io.IOException;
import java.util.Properties;

public class Config {

    //放在static外部
    private static Properties prop = new Properties();

    static {
        try {
            //加载并读取config.properties文件
            prop.load(ConnectionManager.class.getClassLoader().getResourceAsStream("config.properties"));

        } catch (IOException e) {
            throw new DataAccessException("读取config.properties配置文件异常", e);
        }
    }

    public static String get(String key) {
        return prop.getProperty(key);
    }

}
