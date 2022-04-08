package common.message;

import lombok.AllArgsConstructor;

import java.io.*;
import java.net.Socket;

@AllArgsConstructor
public class FileDownloader implements Runnable {

//    private String fileName;
    private Socket socket;

    public void receiveFile() throws Exception {
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        String fileName = dataInputStream.readUTF();

        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        byte[] buffer = new byte[2048];
        int read = 0;
        while ((read = dataInputStream.read(buffer, 0, buffer.length)) != -1) {
            fileOutputStream.write(buffer, 0, read);
        }

        dataInputStream.close();
        fileOutputStream.close();
        socket.close();

    }

    @Override
    public void run() {
        try {
            receiveFile();
        } catch (Exception e) {

        }
    }
}
