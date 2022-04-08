package client.message;

import client.ChatClient;
import client.file.ClientDownloadFile;
import client.file.ClientSendFile;
import common.configuration.Conf;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import common.command.Command;

import java.io.*;
import java.net.Socket;

@AllArgsConstructor
public class MessageSender implements Runnable {

    private ChatClient chatClient;

    @Override
    public void run() {
        try {
            DataOutputStream outputStream = new DataOutputStream(chatClient.getSocket().getOutputStream());
            BufferedReader input;
            String message;
            while (true) {
                input = new BufferedReader(new InputStreamReader(System.in));
                message = input.readLine();

                if (Command.isCommand(message)) {
                    Pair<String, String> commandAndParam = Command.splitToCommandAndParam(message);
                    String command = commandAndParam.getKey();
                    String param = commandAndParam.getValue();
                    if (Command.SEND_FILE.equalsCommand(command)) {
                        new Thread(new ClientSendFile(param, chatClient.getRoomName())).start();
                    } else if (Command.DOWNLOAD_FILE.equalsCommand(command)) {
                        new Thread(new ClientDownloadFile(param, chatClient.getRoomName())).start();
                    } else {
                        outputStream.writeUTF(message);
                    }
                } else {
                    outputStream.writeUTF(message);
                }
                if (Command.LEAVE_CHAT.equalsCommand(message)) {
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
