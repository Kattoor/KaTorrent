package bencode.tokens;

import java.util.Map;

public class DictionaryToken extends Token<Map<Token, Token>> {
    public DictionaryToken(Map<Token, Token> value) {
        super(value);
    }
}
