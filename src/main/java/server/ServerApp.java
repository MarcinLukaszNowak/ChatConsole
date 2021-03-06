package server;

import common.configuration.Conf;
import server.file.FileDownloadServer;

public class ServerApp {
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(Conf.CHAT_SERVER_PORT);
        chatServer.listen();
        FileDownloadServer fileDownloadServer = new FileDownloadServer(Conf.FILE_DOWNLOAD_SERVER_PORT);
        fileDownloadServer.listen();
    }

}
