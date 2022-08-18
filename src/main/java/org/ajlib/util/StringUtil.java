package org.ajlib.util;

public class StringUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.equals(empty());
    }

    public static String empty() {
        return "";
    }
}
