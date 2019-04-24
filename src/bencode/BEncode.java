package bencode;

import bencode.tokens.DictionaryToken;
import bencode.tokens.EndToken;
import bencode.tokens.ListToken;
import bencode.tokens.Token;

import java.util.Stack;

public class BEncode {

    private final byte[] bytes;

    public BEncode(byte[] bytes) {
        this.bytes = bytes;
    }

    public Token decode() {
        var tokens = new Tokenizer(bytes).scanTokens();

        var containerStack = new Stack<Token>();
        containerStack.push(tokens.get(0));

        for (int i = 1; i < tokens.size(); i++) {
            var token = tokens.get(i);
            var container = containerStack.peek();

            if (token instanceof EndToken) {
                containerStack.pop();
            } else if (token.isContainer()) { // only occurs when list in list
                if (container instanceof ListToken) {
                    ((ListToken) container).getValue().add(token);
                    containerStack.push(token);
                }
            } else {
                if (container instanceof ListToken) {
                    ((ListToken) container).getValue().add(token);
                } else if (container instanceof DictionaryToken) {
                    var nextToken = tokens.get(++i);
                    ((DictionaryToken) container).getValue().put(token, nextToken);
                    if (nextToken.isContainer())
                        containerStack.push(nextToken);
                }
            }
        }

        return tokens.get(0);
    }
}
