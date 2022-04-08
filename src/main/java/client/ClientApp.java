package client;

import common.configuration.Conf;

public class ClientApp {

    public static void main(String[] args) {
//        Scanner
        ChatClient chatClient = new ChatClient(Conf.HOST, Conf.CHAT_SERVER_PORT);
        chatClient.joinChat();
    }

}
