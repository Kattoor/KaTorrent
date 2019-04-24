package bencode.tokens;

import java.util.List;

public class ListToken extends Token<List<Token<?>>> {
    public ListToken(List<Token<?>> value) {
        super(value);
    }
}
