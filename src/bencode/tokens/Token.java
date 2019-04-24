package bencode.tokens;

public class Token<T> {
    private final T value;

    public Token(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public boolean isContainer() {
        return this instanceof ListToken || this instanceof DictionaryToken;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
