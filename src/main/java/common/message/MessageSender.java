package common.message;

import lombok.AllArgsConstructor;

import java.io.*;
import java.net.Socket;

@AllArgsConstructor
public class MessageSender implements Runnable {

    Socket socket;

    @Override
    public void run() {
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            BufferedReader input;
            String message;

            while (true) {
                input = new BufferedReader(new InputStreamReader(System.in));
                message = input.readLine();
                outputStream.writeUTF(message);
            }
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }

    public static void directMessage(Socket socket, String message) {
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(message);
        } catch (IOException e) {

        }
    }

    public static void serverMessage(Socket socket, String message) {
        directMessage(socket, "[server]: " + message);
    }

    public static void sendFile(Socket socket, String fullPath) {
        InputStream fileInputStream = null;
        OutputStream outputStream = null;
        try {
            fullPath = "ex.txt"; // todo
            File file = new File(fullPath);
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

}
