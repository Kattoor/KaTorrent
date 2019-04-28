package bencode.tokens;

import bencode.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

import static bencode.Utils.concatByteArrays;
import static bencode.Utils.getBytesFor;

public class DictionaryToken extends Token<Map<String, Token<?>>> {
    DictionaryToken(Map<String, Token<?>> value) {
        super(value);
    }

    @Override
    public byte[] encode() {
        var childEntries = getValue();
        var entryList = new ArrayList<>(childEntries.entrySet());

        entryList.sort(Comparator.comparing(Map.Entry::getKey));

        var childBytes = entryList.stream()
                .map(entry ->
                        concatByteArrays(
                                getBytesFor(Integer.toString(entry.getKey().length())),
                                CommonTokens.COLON.getEncodedValue(),
                                getBytesFor(entry.getKey()),
                                entry.getValue().encode()))
                .reduce(new byte[]{}, Utils::concatByteArrays);

        return concatByteArrays(
                CommonTokens.D.getEncodedValue(),
                childBytes,
                CommonTokens.E.getEncodedValue());
    }
}
