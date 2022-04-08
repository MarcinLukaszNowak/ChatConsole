package common.message;

import lombok.AllArgsConstructor;

import java.io.*;
import java.net.Socket;

@AllArgsConstructor
public class FileSender implements Runnable {

    private Socket socket;
    private String filePath;

    public void sendFile() {
        InputStream fileInputStream = null;
        DataOutputStream outputStream = null;
        String fileName = "new" + filePath; //filePath.substring(filePath.lastIndexOf("/"));
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(fileName);
            File file = new File(filePath);
            byte[] fileBytes = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(fileBytes, 0, fileBytes.length);
            outputStream.write(fileBytes, 0, fileBytes.length);

            outputStream.close();
            fileInputStream.close();
            socket.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        sendFile();
    }
}
