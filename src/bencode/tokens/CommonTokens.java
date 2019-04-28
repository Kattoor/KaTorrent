package bencode.tokens;

import static bencode.Utils.getBytesFor;

public enum CommonTokens {
    I(getBytesFor("i")),
    D(getBytesFor("d")),
    L(getBytesFor("l")),
    E(getBytesFor("e")),
    COLON(getBytesFor(":"));

    private final byte[] encodedValue;

    CommonTokens(byte[] encodedValue) {
        this.encodedValue = encodedValue;
    }

    public byte[] getEncodedValue() {
        return encodedValue;
    }
}
