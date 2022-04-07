package server;

import common.configuration.Conf;

public class ServerApp {
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(Conf.MAIN_PORT);
        chatServer.listen();
    }
}
