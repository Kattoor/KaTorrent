package bencode;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Utils {

    public static final Charset CHARSET = StandardCharsets.ISO_8859_1;

    static byte[] getBytesFor(String s) {
        return s.getBytes(CHARSET);
    }

    public static String getStringFor(byte[] b) {
        return new String(b, CHARSET);
    }
}
