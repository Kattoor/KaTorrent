package bencode;

public class DecodeResult<T> {
    private final T decodedToken;
    private final int byteCount;

    public DecodeResult(T decodedToken, int byteCount) {
        this.decodedToken = decodedToken;
        this.byteCount = byteCount;
    }

    public T getDecodedToken() {
        return decodedToken;
    }

    public int getByteCount() {
        return byteCount;
    }
}
