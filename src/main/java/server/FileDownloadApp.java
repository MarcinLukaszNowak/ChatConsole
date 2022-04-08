package server;

import common.configuration.Conf;
import server.file.FileDownloadServer;

public class FileDownloadApp {

    public static void main(String[] args) {
        FileDownloadServer fileDownloadServer = new FileDownloadServer(Conf.FILE_DOWNLOAD_SERVER_PORT);
        fileDownloadServer.listen();
    }

}
