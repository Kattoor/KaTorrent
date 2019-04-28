package bencode.tokens;

import bencode.Utils;

import java.util.List;

import static bencode.Utils.concatByteArrays;

public class ListToken extends Token<List<Token<?>>> {
    ListToken(List<Token<?>> value) {
        super(value);
    }

    @Override
    public byte[] encode() {
        var childTokens = getValue();

        var childBytes = childTokens.stream()
                .map(Token::encode)
                .reduce(new byte[]{}, Utils::concatByteArrays);

        return concatByteArrays(
                CommonTokens.L.getEncodedValue(),
                childBytes,
                CommonTokens.E.getEncodedValue());
    }
}
