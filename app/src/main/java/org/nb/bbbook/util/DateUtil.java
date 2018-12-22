package org.nb.bbbook.util;

import java.time.LocalDate;

public class DateUtil {
    public static String getMonthLeadingZero(LocalDate d) {
        final int monthOfYear = d.getMonthValue();
        if (String.valueOf(monthOfYear).length() == 1)
        {
            return String.format("0%s", monthOfYear);
        }
        return String.valueOf(monthOfYear);
    }

    public static String getDayOfMonthLeadingZero(LocalDate d) {
        final int dayOfMonth = d.getDayOfMonth();
        if (String.valueOf(dayOfMonth).length() == 1)
        {
            return String.format("0%s", dayOfMonth);
        }
        return String.valueOf(dayOfMonth);
    }
}
