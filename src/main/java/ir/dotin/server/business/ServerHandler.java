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
    Server server = new Server();
    List<Deposit> depositList = new ArrayList<>();
    private final static String core_path = "core.json";
    JSONParser jsonParser = new JSONParser();

    public List<Deposit> readJSONFile() {

        try {
            Object object = jsonParser.parse(new FileReader(core_path));
            JSONObject jsonObject = (JSONObject) object;
            JSONArray jsonArray = (JSONArray) jsonObject.get("deposits");
            Iterator<Object> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                Deposit deposit = new Deposit();
                JSONObject pars = (JSONObject) iterator.next();
                deposit.setId(Integer.valueOf(pars.get("id").toString()));
                deposit.setName((String) pars.get("customer"));
                deposit.setInitalBalance((Integer.valueOf((String) pars.get("initialBalance"))));
                deposit.setUpperBound((Integer.valueOf((String) pars.get("upperBound"))));
                depositList.add(deposit);
            }
            return depositList;
        } catch (Exception e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String readPortFromFile(){
        Object object = null;
        String port = null;
        try {
            object = jsonParser.parse(new FileReader(core_path));
            JSONObject jsonObject = (JSONObject) object;
            server.setPort(String.valueOf(jsonObject.get("port")));
            port = server.getPort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return port;
    }

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
