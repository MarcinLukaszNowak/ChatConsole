package client;

import common.configuration.Conf;

import java.util.Scanner;

public class ClientApp {

    public static void main(String[] args) {
//        Scanner
        ChatClient chatClient = new ChatClient(Conf.HOST, Conf.MAIN_PORT);
        chatClient.joinChat();
    }

}
