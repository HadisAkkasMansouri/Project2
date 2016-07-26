package ir.dotin.server.business;

import ir.dotin.shared.Transaction;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class ServerMain {
    public static void main(String args[]) throws IOException {
        FileHandler fileHandler;
        Logger logger = Logger.getLogger("ServerLog");

        fileHandler = new FileHandler("src\\main\\resources\\outLog");
        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);

        ServerInfo serverInfo = ServerInfo.fromJson();
        Transaction.initialize(serverInfo);

        ServerSocket serverSocket = new ServerSocket(serverInfo.getPort());
        while (true){
            Socket socket = serverSocket.accept();
            logger.info(":D Hello++++++++++++server is connected++++++++++++");
            new Thread(new MultiThreadServer(socket, serverInfo)).start();
        }
    }
}
