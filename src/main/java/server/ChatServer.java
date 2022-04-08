package server;

import common.configuration.Conf;
import common.message.MessageSender;
import lombok.Getter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatServer {

    @Getter
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter writer;
    private BufferedReader reader;
    private DataInputStream inputStream;
    private List<ClientThread> chatClients = new ArrayList<>();
    @Getter
    private HashMap<Integer, Room> rooms = new HashMap<>();
    private int maxRoomId;


    public ChatServer(int port) {
        try {
            Room mainRoom = new Room(Conf.MAIN_ROOM_ID, Conf.MAIN_ROOM_NAME);
            rooms.put(mainRoom.getId(), mainRoom);
            maxRoomId = 0;
            serverSocket = new ServerSocket(port);
            System.out.println("Chat server connected on port: " + port);
        } catch (IOException e) {
            System.out.println("Chat server connection failed");
            System.out.println(e. getMessage());
        }
    }

    public void listen() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Room mainRoom = rooms.get(Conf.MAIN_ROOM_ID);
                ClientThread clientThread = new ClientThread(this, socket, mainRoom);
                new Thread(clientThread).start();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public synchronized void newRoom(String roomName) {
        maxRoomId++;
        Room newRoom = new Room(maxRoomId, roomName);
        rooms.put(maxRoomId, newRoom);
        rooms.get(Conf.MAIN_ROOM_ID).getParticipants()
                .forEach(p -> MessageSender.serverMessage(p.getSocket(), "there is a new room: " + newRoom));
    }

}
