package server;

import common.configuration.Conf;
import common.message.FileDownloader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileDownloadServer {

    private ServerSocket fileSocket;

    public FileDownloadServer(int port) {
        try {
            fileSocket = new ServerSocket(port);
            System.out.println("File server connected on port: " + port);
        } catch (IOException e) {
            System.out.println("File server connection failed");
            System.out.println(e. getMessage());
        }
    }

    public void listen() {
        while (true) {
            try {
                Socket socket = fileSocket.accept();
                new Thread(new FileDownloader(socket)).start();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        FileDownloadServer fileDownloadServer = new FileDownloadServer(Conf.FILE_DOWNLOAD_SERVER_PORT);
        fileDownloadServer.listen();
    }

}
