package ir.dotin.server.business;

import ir.dotin.bean.Deposite;
import ir.dotin.bean.ResponseType;
import ir.dotin.bean.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class ServerHandler implements Runnable {

    Logger logger = Logger.getLogger("Log");
    Server server = new Server();
    List<Deposite> depositeList = new ArrayList<>();


    public List<Deposite> readJSONFile() {

        JSONParser jsonParser = new JSONParser();
        try {
            Object object = jsonParser.parse(new FileReader("core.json"));
            JSONObject jsonObject = (JSONObject) object;
            server.setPort(String.valueOf(jsonObject.get("port")));
            JSONArray jsonArray = (JSONArray) jsonObject.get("deposits");
            Iterator<Object> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                Deposite deposite = new Deposite();
                JSONObject pars = (JSONObject) iterator.next();
                deposite.setId(Integer.valueOf(pars.get("id").toString()));
                deposite.setName((String) pars.get("customer"));
                deposite.setInitalBalance((Integer.valueOf((String) pars.get("initialBalance"))));
                deposite.setUpperBound((Integer.valueOf((String) pars.get("upperBound"))));
                depositeList.add(deposite);
            }
            return depositeList;
        } catch (FileNotFoundException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (ParseException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void run() {
        boolean listening = true;
        RequestValidation validation = new RequestValidation();
        String port = server.getPort();
        ServerSocket serverSocket;
        Transaction transaction;
        try {
            serverSocket = new ServerSocket(Integer.valueOf(port));
            logger.info("Server is Listening on the port : " + port );
            while (true){
                try {
                    Socket socket = serverSocket.accept();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    while ((transaction = (Transaction) ois.readObject()) != null) {
                        validation.validateTransaction((Transaction) transaction);

                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//                        for (Deposite deposite : depositeList){
                            oos.writeObject(transaction);
//                        }
                    }
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
    }

    public void doWithdrawTransaction(Deposite deposite, Transaction transaction) {
        int withdrawamount;
        withdrawamount = deposite.getInitalBalance() - transaction.getAmount();
        transaction.setResponseCode(String.valueOf(ResponseType.SUCCESS));
        deposite.setInitalBalance(withdrawamount);
        logger.info("The transaction of " + transaction.getTransactionType() + " with amount of " + transaction.getAmount() + " is done successfully!");
    }

    public void doDepositTransaction(Deposite deposite, Transaction transaction) {
        int depositAmount;
        depositAmount = deposite.getInitalBalance() + transaction.getAmount();
        transaction.setResponseCode(String.valueOf(ResponseType.SUCCESS));
        deposite.setInitalBalance(depositAmount);
        logger.info("The transaction of " + transaction.getTransactionType() + " with amount of " + transaction.getAmount() + " is done successfully!");
    }
}
