package bencode.tokens;

import static bencode.Utils.*;
import static bencode.Utils.getBytesFor;

public class StringToken extends Token<byte[]> {
    StringToken(byte[] value) {
        super(value);
    }

    @Override
    public byte[] encode() {
        var bytesValue = getValue();
        return concatByteArrays(
                getBytesFor(Integer.toString((getStringFor(bytesValue)).length())),
                CommonTokens.COLON.getEncodedValue(),
                bytesValue);
    }
}
