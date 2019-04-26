package bencode;

import bencode.tokens.DictionaryToken;
import bencode.tokens.EndToken;
import bencode.tokens.ListToken;
import bencode.tokens.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Tokenizer {

    private final byte[] bytes;
    private int current = 0;

    Tokenizer(byte[] bytes) {
        this.bytes = bytes;
    }

    List<Token> scanTokens() {
        var tokens = new ArrayList<Token>();
        while (current < bytes.length)
            tokens.add(scanToken());
        return tokens;
    }

    private Token scanToken() {
        var token = switch ((char) bytes[current]) {
            case 'i' -> new Token<>(number());
            case 'l' -> new ListToken(new ArrayList<>());
            case 'd' -> new DictionaryToken(new HashMap<>());
            case 'e' -> new EndToken();
            default -> new Token<>(string());
        };

        current += 1;
        return token;
    }

    private int number() {
        var sb = new StringBuilder();

        char next;
        while ((next = (char) bytes[++current]) != 'e')
            sb.append(next);

        return Integer.parseInt(sb.toString());
    }

    private byte[] string() {
        var stringLengthSb = new StringBuilder().append((char) bytes[current]);

        char next;
        while ((next = (char) bytes[++current]) != ':')
            stringLengthSb.append(next);

        var stringLength = Integer.parseInt(stringLengthSb.toString());

        var stringBytes = new byte[stringLength];
        for (int i = 0; i < stringLength; i++)
            stringBytes[i] = bytes[++current];

        return stringBytes;
    }
}
