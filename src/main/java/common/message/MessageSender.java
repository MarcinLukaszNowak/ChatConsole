package common.message;

import client.ClientDownloadFile;
import client.ClientSendFile;
import common.configuration.Conf;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import common.command.Command;

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

                if (Command.isCommand(message)) {
                    Pair<String, String> commandAndParam = Command.splitToCommandAndParam(message);
                    String command = commandAndParam.getKey();
                    String param = commandAndParam.getValue();
                    if (Command.SEND_FILE.getCommandString().equals(command)) {
                        new Thread(new ClientSendFile(param)).start();
                    } else if (Command.DOWNLOAD_FILE.getCommandString().equals(command)) {
                        new Thread(new ClientDownloadFile(param)).start();
                    }
                }
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



}
