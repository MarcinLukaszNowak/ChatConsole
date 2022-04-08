package client;

import common.configuration.Conf;
import lombok.AllArgsConstructor;

import java.io.*;
import java.net.Socket;

@AllArgsConstructor
public class ClientSendFile implements Runnable {

    String filePath;

    private String getFileName() {
        return "new" + filePath; // todo
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(Conf.HOST, Conf.FILE_DOWNLOAD_SERVER_PORT);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(getFileName());
            File file = new File(filePath);
            byte[] fileBytes = new byte[(int) file.length()];
            InputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(fileBytes, 0, fileBytes.length);
            outputStream.write(fileBytes, 0, fileBytes.length);

            outputStream.close();
            fileInputStream.close();
            socket.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

}
