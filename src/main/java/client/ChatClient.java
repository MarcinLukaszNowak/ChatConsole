package client;

import common.message.MessageReceiver;
import common.message.MessageSender;
import lombok.AllArgsConstructor;

import java.io.*;
import java.net.Socket;

@AllArgsConstructor
public class ChatClient {

    private String host;
    private int port;

    public void joinChat() {
        try {
            Socket socket = new Socket(host, port);
            MessageReceiver messageReceiver = new MessageReceiver(socket);
            new Thread(messageReceiver).start();

            MessageSender messageSender = new MessageSender(socket);
            new Thread(messageSender).start();


        } catch (IOException e) {
            System.out.println("nie udało się dołączyć");
        }
    }

}
