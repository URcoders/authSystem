package com.qgailab.authsystem.utils;

import java.text.SimpleDateFormat;

/**
 * @author linxu
 * @version 1.0
 * date time tool...
 */
public class DateUtil {
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        return sdf.format(System.currentTimeMillis());
    }

}
