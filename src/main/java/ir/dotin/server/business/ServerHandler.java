package ir.dotin.server.business;

import ir.dotin.shared.Transaction;
import java.io.*;
import java.util.logging.Logger;

public class ServerHandler{

    ResponseTransaction responseTransaction = null;
    Logger logger = Logger.getLogger("Log");


    public Transaction readNextTransaction(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (Transaction) ois.readObject();
    }

    public void sendToClient(ObjectOutputStream oos, ResponseTransaction responseTransaction) throws IOException {
        oos.writeObject(responseTransaction);
        logger.info("++++++++++++Response is sent++++++++++++");
        oos.flush();
    }
}
