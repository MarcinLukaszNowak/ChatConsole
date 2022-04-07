package server;

import common.configuration.Conf;
import common.message.MessageReceiver;
import common.message.MessageSender;
import lombok.Getter;
import lombok.Setter;
import server.command.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
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
                if (message.length() > 2 && Conf.COMMAND_IDENTIFIER.equals(message.substring(0,2))) {
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

        System.out.println("gdzie ja jestem?");

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

    private void handleCommand(String command) {
        String param = "";
        if (command.contains(" ")) {
            String[] commandParts = command.split(" ", 2);
            command = commandParts[0];
            param = commandParts[1];
        }
        if (Command.HELP.getCommandWithoutParam().equals(command)) {
            MessageSender.directMessage(this.socket, Command.getAllCommands());
        } else if (Command.NEW_ROOM.getCommandWithoutParam().equals(command)) {
            this.server.newRoom(param);
        } else if (Command.JOIN_ROOM.getCommandWithoutParam().equals(command)) {
            joinRoom(param);
        } else if (Command.ROOM_LIST.getCommandWithoutParam().equals(command)) {
            getRoomList();
        } else if (Command.SEND_FILE.getCommandWithoutParam().equals(command)) {
            sendFile();
        } else if (Command.DOWNLOAD_FILE.getCommandWithoutParam().equals(command)) {

        } else {
            MessageSender.serverMessage(this.socket, "command: '" + command + "' doesn't exist. Type '" + Command.HELP.getCommandString() + "' to get list of commands");
        }
    }

    private void sendFile() {
        MessageSender.sendFile(this.socket, "ex.txt");
        MessageReceiver.receiveFile(this.socket);
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
