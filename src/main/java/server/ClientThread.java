package server;

import common.configuration.Conf;
import common.message.MessageSender;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import common.command.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ClientThread implements Runnable {

    @Getter
    @Setter
    private String name;
    @Getter
    private Socket socket;
    private List<ClientThread> chatClients;
    private ChatServer server;
    private Room room;

    public ClientThread(ChatServer server, Socket socket, Room room) {
        this.server = server;
        this.socket = socket;
        this.room = room;
    }

    private void handleMessages(DataInputStream inputStream) {
        while (true) {
            try {
                String message = inputStream.readUTF();
                if (Command.isCommand(message)) {
                    handleCommand(message);
                } else {
                    broadcast(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcast(String message) {
        String messageDate = new SimpleDateFormat(Conf.MESSAGE_DATETIME_FORMAT).format(new Date());
        String outputMessage = messageDate + " [" + name + "]: " + message;
        try {
            Files.write(Paths.get(room.getConversationFile().getPath()), (outputMessage + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        room.getParticipants()
                .forEach(p -> {
                    try {
                        DataOutputStream outputStream = new DataOutputStream(p.getSocket().getOutputStream());
                        if (p.equals(this)) {
                            outputStream.writeUTF( messageDate + " {me}: " + message);
                        } else {
                            outputStream.writeUTF(outputMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void handleCommand(String message) {
        Pair<String, String> commandAndParam = Command.splitToCommandAndParam(message);
        String command = commandAndParam.getKey();
        String param = commandAndParam.getValue();
        if (Command.HELP.getCommandString().equals(command)) {
            MessageSender.directMessage(this.socket, Command.getAllCommands());
        } else if (Command.NEW_ROOM.getCommandString().equals(command)) {
            this.server.newRoom(param);
        } else if (Command.JOIN_ROOM.getCommandString().equals(command)) {
            joinRoom(param);
        } else if (Command.ROOM_LIST.getCommandString().equals(command)) {
            getRoomList();
        } else if (Command.SEND_FILE.getCommandString().equals(command)) {
            downloadFileByServer();
        } else if (Command.DOWNLOAD_FILE.getCommandString().equals(command)) {

        } else {
            MessageSender.serverMessage(this.socket, "command: '" + commandAndParam.getKey() + "' doesn't exist. Type '" + Command.HELP.getCommandString() + "' to get list of commands");
        }
    }

    private void downloadFileByServer() {
//        MessageReceiver.receiveFile(socket);
    }

    private void sendFile() {
//        MessageSender.sendFile(this.socket, "ex.txt");
//        MessageReceiver.receiveFile(this.socket);
    }

    private void getRoomList() {
        StringBuilder sb = new StringBuilder();
        server.getRooms()
                .forEach((key, value) -> sb.append("id: ").append(key).append(", name: ").append(value.getName()).append("\n"));
        MessageSender.directMessage(this.socket, sb.toString());
    }

    private void joinRoom(String roomId) {
        try {
            room.getParticipants().remove(this);
            Room joiningRoom = server.getRooms().get(Integer.valueOf(roomId));
            this.room = joiningRoom;
            joiningRoom.getParticipants().add(this);
            MessageSender.serverMessage(this.socket, "joined " + room.toString());
        } catch (NullPointerException e) {
            MessageSender.serverMessage(this.socket, "room doesn't exist");
        } catch (NumberFormatException e) {
            MessageSender.serverMessage(this.socket, "wrong room id");
        }
    }

    @Override
    public void run() {
        try {
            new DataOutputStream(socket.getOutputStream()).writeUTF("Witaj użytkowniku! Podaj swoje imię i zatwierdź. Po zalogowaniu zostaniesz dołączony do czatu ogólnego.");
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            this.name =  inputStream.readUTF();
            this.room.getParticipants().add(this);
            MessageSender.serverMessage(socket, "Welcome " + name);
            handleMessages(inputStream);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
