package io.github.kolacbb.translate.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by Kola on 2016/6/20.
 */
public class StringUtils {

    public static final String INVALID = "invalid";

    // 判断选中的字符是不是纯数字，与isValidTEL方法互不干扰
    public static boolean isOnlyNumber(String input) {
        String regex = "\\d+";
        return input.matches(regex) && input.length() != 11;
    }

    /**
     * 判断字符串中是否包含相应的类型，若包含，仅返回该类型的字符串。若不包含，则返回INVALID
     * */

    public static String isValidEmail(String input) {
        String regex = "\\w+@\\w+.\\w+";
        return matchRegex(input, regex);
    }

    public static String isValidTEL(String input) {
        String regex = "1[358]\\d{9}";
        return matchRegex(input, regex);
    }

    public static String isValidURL(String input) {
        String regex = "http[s]?://\\w+.\\w+[/\\w+]*/?";
        return matchRegex(input, regex);
    }

    private static String matchRegex(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group();
        }
        return INVALID;
    }
}
