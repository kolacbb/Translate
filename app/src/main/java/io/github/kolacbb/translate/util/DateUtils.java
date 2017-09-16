package io.github.kolacbb.translate.util;

import java.util.Calendar;

/**
 * Created by zhangd on 2017/9/15.
 */

public class DateUtils {
    private static Calendar sToadyCalendar = Calendar.getInstance();
    private static Calendar sTempCalendar = Calendar.getInstance();
    public static String getDateString(long time) {
        sTempCalendar.setTimeInMillis(time);
        return getDateString(sTempCalendar);
    }

    public static String getDateString(Calendar c) {
        StringBuilder builder = new StringBuilder();
        if (c.get(Calendar.YEAR) != sToadyCalendar.get(Calendar.YEAR)) {
            builder.append(c.get(Calendar.YEAR));
        }
        builder.append((c.get(Calendar.MONTH) + 1))
                .append("月")
                .append(c.get(Calendar.DAY_OF_MONTH))
                .append("日");
        return builder.toString();
    }
}
