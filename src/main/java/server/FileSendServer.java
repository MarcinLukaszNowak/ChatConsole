package server;

import common.configuration.Conf;
import common.message.FileDownloader;
import common.message.FileSender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileSendServer {

    private ServerSocket serverSocket;

    public FileSendServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("File server connected on port: " + port);
        } catch (IOException e) {
            System.out.println("File server connection failed");
            System.out.println(e. getMessage());
        }
    }

    public void listen() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(new FileSender(socket)).start();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        FileSendServer fileSendServer = new FileSendServer(Conf.FILE_SENDER_SERVER_PORT);
        fileSendServer.listen();
    }

}
