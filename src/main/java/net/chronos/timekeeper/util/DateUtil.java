package net.chronos.timekeeper.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {
    private static final String DATETIME_FORMAT ="yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private static final BigDecimal SECONDS_PER_HOUR = new BigDecimal(3600);

    public static Date from(String string) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
            return sdf.parse(string);
        } catch (ParseException pe) {
            return null;
        }

    }

    public static String from(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        return sdf.format(date);
    }

    public static BigDecimal hoursBetween(Date start, Date end) {
        long dateDiff= end.getTime() - start.getTime();
        BigDecimal diffSeconds = new BigDecimal(TimeUnit.MILLISECONDS.toSeconds(dateDiff));
        return diffSeconds.divide(SECONDS_PER_HOUR);
    }
}
