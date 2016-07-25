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
    private List<Deposit> depositList;
    Socket serverSocket;

    public MultiThreadServer(Socket serverSocket, List<Deposit> deposits){
        this.serverSocket = serverSocket;
        this.depositList = deposits;
    }

    @Override
    public void run() {

        String port = serverHandler.readPortFromFile();
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.valueOf(port));
            logger.info("Server is Listening on the port : " + port);
            Socket socket = serverSocket.accept();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
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
        List<Deposit> depositList = serverHandler.readJSONFile();
//        MultiThreadServer multiThreadServer = new MultiThreadServer();
//        new Thread(multiThreadServer).start();

        String port = serverHandler.readPortFromFile();
        ServerSocket serverSocket = new ServerSocket(Integer.valueOf(port));
        while (true){
            Socket socket = serverSocket.accept();
            logger.info(":D Hello++++++++++++server is connected++++++++++++");
            new Thread(new MultiThreadServer(socket, depositList)).start();
        }
    }
}
