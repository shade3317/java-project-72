package hexlet.code.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.net.URL;


public class Utilities {
    public static Timestamp getDateFormat(Timestamp timestamp, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        return Timestamp.valueOf(formatter.format(timestamp));
    }

    public static String formatURL(URL url) {
        var port      = url.getPort() == -1 ? "" : ":" + String.valueOf(url.getPort());
        var protocol  = url.getProtocol();
        var authority = url.getAuthority();

        return protocol.concat("://").concat(authority).concat(port);
    }
}
