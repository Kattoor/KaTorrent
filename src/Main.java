import bencode.BEncode;
import bencode.Utils;
import bencode.tokens.DictionaryToken;
import bencode.tokens.Token;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static bencode.Utils.getStringFor;

public class Main {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        new Main();
    }

    private Main() throws IOException, NoSuchAlgorithmException {
        var bEncode = new BEncode();

        var rootDict = bEncode.decode(Files.readAllBytes(Paths.get("ubuntu.torrent")));

        var infoDict = (DictionaryToken) ((DictionaryToken) rootDict).getValue().get("info");

        var encodedInfoDict = bEncode.encode(infoDict);

        var announce = new String((byte[]) ((Token) ((HashMap) rootDict.getValue()).get("announce")).getValue());
        var infoHash = URLEncoder.encode(getStringFor(MessageDigest.getInstance("SHA-1").digest(encodedInfoDict)), Utils.CHARSET);

        System.out.printf("%s?info_hash=%s", announce.replace("announce", "file"), infoHash);
    }
}
