package bencode;

import bencode.tokens.DictionaryToken;
import bencode.tokens.EndToken;
import bencode.tokens.ListToken;
import bencode.tokens.Token;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Stack;

import static bencode.Utils.getBytesFor;
import static bencode.Utils.getStringFor;

public class BEncode {

    private void writeBytesTo(ByteArrayOutputStream out, byte[]... bytesFlow) {
        for (byte[] bytes : bytesFlow) {
            out.writeBytes(bytes);
        }
    }

    private byte[] concatByteArrays(byte[]... bytesArrays) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (byte[] bytes : bytesArrays)
            out.writeBytes(bytes);
        return out.toByteArray();
    }

    public Token decode(byte[] bytes) {
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
                    ((DictionaryToken) container).getValue().put(getStringFor((byte[]) token.getValue()), nextToken);
                    if (nextToken.isContainer())
                        containerStack.push(nextToken);
                }
            }
        }

        return tokens.get(0);
    }

    public byte[] encode(Token token) {
        var out = new ByteArrayOutputStream();

        if (token.isContainer()) {
            if (token instanceof ListToken) {
                var childTokens = ((ListToken) token).getValue();

                var childBytes = childTokens.stream()
                        .map(this::encode)
                        .reduce(new byte[]{}, this::concatByteArrays);

                writeBytesTo(out,
                        getBytesFor("l"),
                        childBytes,
                        getBytesFor("e"));
            } else if (token instanceof DictionaryToken) {
                var childEntries = ((DictionaryToken) token).getValue();
                var entryList = new ArrayList<>(childEntries.entrySet());

                entryList.sort(Comparator.comparing(Map.Entry::getKey));

                var childBytes = entryList.stream()
                        .map(entry ->
                                concatByteArrays(
                                        getBytesFor(Integer.toString(entry.getKey().length())),
                                        getBytesFor(":"),
                                        getBytesFor(entry.getKey()),
                                        encode(entry.getValue())))
                        .reduce(new byte[]{}, this::concatByteArrays);

                writeBytesTo(out,
                        getBytesFor("d"),
                        childBytes,
                        getBytesFor("e"));
            }
        } else {
            Object value = token.getValue();
            if (value instanceof byte[]) {
                var bytesValue = (byte[]) value;
                writeBytesTo(out,
                        getBytesFor(Integer.toString((getStringFor(bytesValue)).length())),
                        getBytesFor(":"),
                        bytesValue);
            } else if (value instanceof Integer) {
                writeBytesTo(out,
                        getBytesFor("i"),
                        getBytesFor(Integer.toString((Integer) value)),
                        getBytesFor("e"));
            }
        }

        return out.toByteArray();
    }
}
