package bencode.tokens;

import static bencode.Utils.concatByteArrays;
import static bencode.Utils.getBytesFor;

public class IntegerToken extends Token<Integer> {
    IntegerToken(Integer value) {
        super(value);
    }

    @Override
    public byte[] encode() {
        var intValue = getValue();
        return concatByteArrays(
                CommonTokens.I.getEncodedValue(),
                getBytesFor(Integer.toString(intValue)),
                CommonTokens.E.getEncodedValue());
    }
}
