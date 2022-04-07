package server;

import common.configuration.Conf;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Room {

    @Getter
    private Integer id;
    @Getter
    private String name;
    @Getter
    private Set<ClientThread> participants = new HashSet<>();
    @Getter
    private File conversationFile;

    public Room(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.conversationFile = new File(name + Conf.CONVERSATION_FILE_EXTENSION);
        try {
            conversationFile.createNewFile();
        } catch (IOException e) {

        }
    }

    @Override
    public String toString() {
        return "room id: " + id + ", name: " + name;
    }


}
