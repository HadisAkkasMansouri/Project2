package ir.dotin.server.business;

import ir.dotin.shared.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class ServerHandler{

    ResponseTransaction responseTransaction = null;
    Logger logger = Logger.getLogger("Log");


    public ResponseTransaction receiveFromClient(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        TransactionAccomplishment transactionAccomplishment = new TransactionAccomplishment();
        Transaction transaction = (Transaction) ois.readObject();
        logger.info("++++++++++++Request is received++++++++++++");
        responseTransaction = transactionAccomplishment.readTransaction((Transaction) transaction);
        return responseTransaction;
    }

    public void sendToClient(ObjectOutputStream oos, ResponseTransaction responseTransaction) throws IOException {
        oos.writeObject(responseTransaction);
        logger.info("++++++++++++Response is sent++++++++++++");
        oos.flush();
    }
}
