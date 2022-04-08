package common.message;

import lombok.AllArgsConstructor;

import java.io.*;
import java.net.Socket;

@AllArgsConstructor
public class FileSender implements Runnable {

    private Socket socket;
//    private String filePath;

    public void sendFile() {
//        InputStream fileInputStream = null;
//        DataOutputStream outputStream = null;
//        String fileName = "new" + filePath; //filePath.substring(filePath.lastIndexOf("/"));
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String fileName = dataInputStream.readUTF();

            File file = new File(fileName);
            byte[] fileBytes = new byte[(int) file.length()];
            InputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(fileBytes, 0, fileBytes.length);
            outputStream.write(fileBytes, 0, fileBytes.length);

            dataInputStream.close();
            outputStream.close();
            fileInputStream.close();
            socket.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    @Override
    public void run() {
        sendFile();
    }
}
