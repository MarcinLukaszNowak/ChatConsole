package server;

import common.configuration.Conf;
import server.file.FileSendServer;

public class FileSendApp {

    public static void main(String[] args) {
        FileSendServer fileSendServer = new FileSendServer(Conf.FILE_SENDER_SERVER_PORT);
        fileSendServer.listen();
    }

}
