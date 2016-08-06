package com.example.myqrscanapplication.util;

/**
 * Created by yatra on 7/3/16.
 */
public class StringUtils {

    public static boolean isEmptyOrNull(String str) {
        if(str == null || str.isEmpty())
            return true;

        return false;
    }

}
