package hexlet.code.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;


public class Utilities {
    public static Timestamp getDateFormat(Timestamp timestamp, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        return Timestamp.valueOf(formatter.format(timestamp));
    }
}
