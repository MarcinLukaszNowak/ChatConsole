package client;

import common.configuration.Conf;
import lombok.AllArgsConstructor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

@AllArgsConstructor
public class ClientDownloadFile implements Runnable {

    private String fileName;

    @Override
    public void run() {
        try {
            Socket socket = new Socket(Conf.HOST, Conf.FILE_SENDER_SERVER_PORT);
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(fileName);

//            fileName = "pobrany" + fileName; // todo
            fileName = "client" + fileName;

            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            byte[] buffer = new byte[2048];
            int read = 0;
            while ((read = dataInputStream.read(buffer, 0, buffer.length)) != -1) {
                fileOutputStream.write(buffer, 0, read);
            }

            dataInputStream.close();
            dataOutputStream.close();
            fileOutputStream.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
