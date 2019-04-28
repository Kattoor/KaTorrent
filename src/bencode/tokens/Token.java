package bencode.tokens;

import bencode.DecodeResult;

import java.util.*;

import static bencode.Utils.getStringFor;

public abstract class Token<T> implements Encodable {
    private T value;

    Token(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public static DecodeResult<Token<?>> decode(byte[] bytes) {
        var subArray = Arrays.copyOfRange(bytes, 1, bytes.length);

        return switch ((char) bytes[0]) {
            case 'i' -> {
                var pair = decodeInteger(subArray);
                var token = new IntegerToken(pair.getDecodedToken());
                break new DecodeResult<>(token, pair.getByteCount() + 1);
            }
            case 'l' -> {
                var pair = decodeList(subArray);
                var token = new ListToken(pair.getDecodedToken());
                break new DecodeResult<>(token, pair.getByteCount() + 1);
            }
            case 'd' -> {
                var pair = decodeDictionary(subArray);
                var token = new DictionaryToken(pair.getDecodedToken());
                break new DecodeResult<>(token, pair.getByteCount() + 1);
            }
            default -> {
                var pair = decodeString(bytes);
                var token = new StringToken(pair.getDecodedToken());
                break new DecodeResult<>(token, pair.getByteCount());
            }
        };
    }

    private static DecodeResult<Integer> decodeInteger(byte[] bytes) {
        int offset = 0;

        var sb = new StringBuilder();

        char next;
        while ((next = (char) bytes[offset++]) != 'e')
            sb.append(next);

        return new DecodeResult<>(Integer.parseInt(sb.toString()), offset);
    }

    private static DecodeResult<List<Token<?>>> decodeList(byte[] bytes) {
        int offset = 0;
        var list = new ArrayList<Token<?>>();

        char peek = (char) bytes[offset];

        while (peek != 'e') {
            var recordPair = decode(Arrays.copyOfRange(bytes, offset, bytes.length));
            var record = recordPair.getDecodedToken();
            offset += recordPair.getByteCount();
            list.add(record);
            peek = (char) bytes[offset];
        }

        offset += 1;

        return new DecodeResult<>(list, offset);
    }

    private static DecodeResult<Map<String, Token<?>>> decodeDictionary(byte[] bytes) {
        int offset = 0;
        var dictionary = new HashMap<String, Token<?>>();

        char peek = (char) bytes[offset];

        while (peek != 'e') {
            var keyPair = decodeString(Arrays.copyOfRange(bytes, offset, bytes.length));
            var key = keyPair.getDecodedToken();
            offset += keyPair.getByteCount();

            var valuePair = decode(Arrays.copyOfRange(bytes, offset, bytes.length));
            var value = valuePair.getDecodedToken();
            offset += valuePair.getByteCount();

            dictionary.put(getStringFor(key), value);

            peek = (char) bytes[offset];
        }

        offset += 1;

        return new DecodeResult<>(dictionary, offset);
    }

    private static DecodeResult<byte[]> decodeString(byte[] bytes) {
        int offset = 0;

        var stringLengthSb = new StringBuilder().append((char) bytes[offset++]);

        char next;
        while ((next = (char) bytes[offset++]) != ':')
            stringLengthSb.append(next);

        var stringLength = Integer.parseInt(stringLengthSb.toString());

        var stringBytes = new byte[stringLength];
        for (int i = 0; i < stringLength; i++)
            stringBytes[i] = bytes[offset++];

        return new DecodeResult<>(stringBytes, offset);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
