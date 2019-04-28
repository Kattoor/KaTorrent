import bencode.tokens.DictionaryToken;
import bencode.tokens.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import static bencode.Utils.*;

public class Main {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        var file = Files.readAllBytes(Paths.get("ubuntu.torrent"));

        var root = (DictionaryToken) Token.decode(file).getDecodedToken();
        var info = (DictionaryToken) root.getValue().get("info");
        var announce = getStringFor((byte[]) root.getValue().get("announce").getValue());

        if (!prepareUrl(announce, info.encode()).equals("http://torrent.ubuntu.com:6969/file?info_hash=%B7%B0%FB%ABt%A8%5DJ%C1pf%2CdY%82%A8b%82dU"))
            throw new RuntimeException("Mistakes were made");
    }

    private static String prepareUrl(String announce, byte[] encodedInfo) throws NoSuchAlgorithmException {
        return String.format("%s?info_hash=%s",
                announce.replace("announce", "file"),
                urlEncode(getStringFor(hash(encodedInfo))));
    }
}
