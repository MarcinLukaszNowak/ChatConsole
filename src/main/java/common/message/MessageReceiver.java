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

    public static void receiveFile(Socket socket) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = new FileOutputStream("pobieranyPlik.txt");
            byte[] fileBytes = new byte[1024];
            inputStream.read(fileBytes, 0, fileBytes.length);
            outputStream.write(fileBytes, 0, fileBytes.length); // to chyba w while władować?
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
