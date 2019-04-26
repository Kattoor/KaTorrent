package bencode.tokens;

import java.util.Map;

public class DictionaryToken extends Token<Map<String, Token>> {
    public DictionaryToken(Map<String, Token> value) {
        super(value);
    }
}
