package com.gys.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

//public class StringUtil extends org.apache.commons.lang3.StringUtils{ //可以这样写，不用import
public class StringUtil extends StringUtils {

    private static Logger logger = LoggerFactory.getLogger(StringUtil.class);

    public static String ISOtoUTF8(String str) {
        try {
            return new String(str.getBytes("ISO8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("字符串{}转换异常",str);
            throw new RuntimeException("字符串" + str + "转换异常",e);
        }
    }

}
