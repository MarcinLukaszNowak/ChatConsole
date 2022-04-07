package server.command;

import common.configuration.Conf;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Command {
    HELP( "help", "get all commands"),
    NEW_ROOM("new_room roomName", "create new room named roomName"),
    ROOM_LIST("room_list", "list of rooms"),
    JOIN_ROOM("join_room roomId", "join to room with id = roomId"),
    SEND_FILE("send_file filePath", "send file which path = filePath"),
    DOWNLOAD_FILE("download_file fileName", "download file with name = fileName");

    @Getter
    private final String commandString;
    @Getter
    private String description;

    Command(String commandString, String description) {
        this.commandString = Conf.COMMAND_IDENTIFIER + commandString;
        this.description = description;
    }

    public String getCommandWithoutParam() {
        if (commandString.contains(" ")) {
            return commandString.substring(0, commandString.indexOf(" "));
        } else {
            return commandString;
        }
    }

    public static String getAllCommands() {
        return Arrays.stream(Command.values())
                .map(v -> v.toString() + "\n")
                .collect(Collectors.joining());
    }

    @Override
    public String toString() {
        return commandString + " - " + description;
    }

}
