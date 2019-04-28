package bencode;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    private static final Charset CHARSET = StandardCharsets.ISO_8859_1;
    private static final String HASHING_ALGORITHM = "SHA-1";

    public static byte[] getBytesFor(String s) {
        return s.getBytes(CHARSET);
    }

    public static String getStringFor(byte[] b) {
        return new String(b, CHARSET);
    }

    public static byte[] concatByteArrays(byte[]... bytesArrays) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (byte[] bytes : bytesArrays)
            out.writeBytes(bytes);
        return out.toByteArray();
    }

    public static String urlEncode(String s) {
        return URLEncoder.encode(s, CHARSET);
    }

    public static byte[] hash(byte[] b) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(HASHING_ALGORITHM).digest(b);
    }
}
