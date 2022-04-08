package common.message;

import lombok.AllArgsConstructor;

import java.io.*;
import java.net.Socket;

@AllArgsConstructor
public class FileExchanger implements Runnable {

    private Socket socket;

    public void receiveFile(String fileName, InputStream inputStream) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        byte[] buffer = new byte[2048];
        int read = 0;
        while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
            fileOutputStream.write(buffer, 0, read);
        }
    }

    public void sendFile(String filePath) {
        InputStream fileInputStream = null;
        OutputStream outputStream = null;
        try {
            File file = new File(filePath);
            byte[] fileBytes = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(fileBytes, 0, fileBytes.length);
            outputStream = socket.getOutputStream();
            outputStream.write(fileBytes, 0, fileBytes.length);
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

    }
}
