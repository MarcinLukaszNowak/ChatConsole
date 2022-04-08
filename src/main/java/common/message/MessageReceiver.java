package common.message;

import lombok.AllArgsConstructor;

import java.io.*;
import java.net.Socket;

@AllArgsConstructor
public class MessageReceiver implements Runnable {

    private final Socket socket;

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            while (true) {
                System.out.println(inputStream.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
