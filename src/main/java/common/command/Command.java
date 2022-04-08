package common.command;

import common.configuration.Conf;
import javafx.util.Pair;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Command {
    HELP( "help", "", "get all commands"),
    NEW_ROOM("new_room", "roomName", "create new room named roomName"),
    ROOM_LIST("room_list", "", "list of rooms"),
    JOIN_ROOM("join_room", "roomId", "join to room with id = roomId"),
    SEND_FILE("send_file", "filePath", "send file which path = filePath"),
    DOWNLOAD_FILE("download_file", "fileName", "download file with name = fileName");

    @Getter
    private final String commandString;
    @Getter
    private final String paramName;
    @Getter
    private String description;

    Command(String commandString, String paramName, String description) {
        this.commandString = Conf.COMMAND_IDENTIFIER + commandString;
        this.paramName = paramName;
        this.description = description;
    }

    public static boolean isCommand(String message) {
        return message.length() >= 2 && Conf.COMMAND_IDENTIFIER.equals(message.substring(0, 2));
    }

    public static Pair<String, String> splitToCommandAndParam(String message) {
        String command = "";
        String param = "";
        if (message.contains(" ")) {
            String[] commandParts = message.split(" ", 2);
            command = commandParts[0];
            param = commandParts[1];
        }
        return new Pair<>(command, param);
    }

    public static String getAllCommands() {
        return Arrays.stream(Command.values())
                .map(v -> v.toString() + "\n")
                .collect(Collectors.joining());
    }

    @Override
    public String toString() {
        return commandString + (paramName.equals("") ? "" : " " + paramName + " ") + " - " + description;
    }

}
