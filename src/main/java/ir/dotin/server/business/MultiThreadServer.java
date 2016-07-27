package ir.dotin.server.business;

import ir.dotin.exception.InadequateInitialBalanceException;
import ir.dotin.exception.InvalidReceivedDataException;
import ir.dotin.exception.ViolatedUpperBoundException;
import ir.dotin.shared.Transaction;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class MultiThreadServer implements Runnable{

    private Logger logger = Logger.getLogger("Log");
    private ServerHandler serverHandler = new ServerHandler();
    private Socket socket;
    private ServerInfo serverInfo;

    public MultiThreadServer(Socket serverSocket, ServerInfo serverInfo){
        this.socket = serverSocket;
        this.serverInfo = serverInfo;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            while (true) {
                try {
                    Transaction transaction = serverHandler.readNextTransaction(inputStream);
                    ResponseTransaction responseTransaction = transaction.perform();
                    serverHandler.sendToClient(outputStream, responseTransaction);
                } catch (EOFException e) {
                    logger.warning("No more data from client!");
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
}
