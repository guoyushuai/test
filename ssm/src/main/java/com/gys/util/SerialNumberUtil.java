package com.gys.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;

public class SerialNumberUtil {

    public static String getSerialNumber() {
        DateTime now = new DateTime();
        String result = now.toString("YYYYMMDDHHmmss");
        //最后四位随机
        result += RandomStringUtils.randomNumeric(4);
        return result;
    }

}
