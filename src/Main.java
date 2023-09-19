import KVServer.KVServer;
import server.HttpTaskServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        KVServer server = new KVServer();
        server.start();
        HttpTaskServer server1 = new  HttpTaskServer();
        server1.start();
    }
}