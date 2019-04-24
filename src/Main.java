import bencode.BEncode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        new Main();
    }

    private Main() throws IOException {
        var bytes = Files.readAllBytes(Paths.get("file.torrent"));
        var token = new BEncode(bytes).decode();
    }
}
