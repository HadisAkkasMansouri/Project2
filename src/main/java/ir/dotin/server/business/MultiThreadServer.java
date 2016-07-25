package ir.dotin.server.business;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

public class MultiThreadServer implements Runnable{

    Logger logger = Logger.getLogger("Log");
    ServerHandler serverHandler = new ServerHandler();
    Server server = new Server();

    Socket serverSocket;
    MultiThreadServer(Socket serverSocket, List<Deposit> deposits){
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {

        String port = serverHandler.readPortFromFile();
        ServerSocket serverSocket;
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(Integer.valueOf(port));
            logger.info("Server is Listening on the port : " + port);
            socket = serverSocket.accept();
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            while (true) {
                try {
                    ResponseTransaction responseTransaction = serverHandler.receiveFromClient(inputStream);
                    serverHandler.sendToClient(outputStream, responseTransaction);
                } catch (EOFException e) {
                    logger.info("connection closed !");
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {

        Logger logger = Logger.getLogger("ServerLog");
        ServerHandler serverHandler = new ServerHandler();
        MultiThreadServer multiThreadServer = new MultiThreadServer();
        new Thread(multiThreadServer).start();

        String port = serverHandler.readPortFromFile();
        ServerSocket serverSocket = new ServerSocket(Integer.valueOf(port));
        while (true){
            Socket socket = serverSocket.accept();
            logger.info(":D Hello++++++++++++server is connected++++++++++++");
//            new Thread(new MultiThreadServer(socket,)).start();
        }
    }
}
