package ir.dotin.server.business;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class ServerInfo {
    private int port;
    private String outLog;

    private List<Deposit> deposits;

    private final static String core_path = "core.json";

    public static ServerInfo fromJson() {

        ServerInfo result = new ServerInfo();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(core_path));
            result.port = (int) (long) jsonObject.get("port");
            result.outLog = (String) jsonObject.get("outLog");
            result.deposits = readDepositsFromJson(jsonObject);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Deposit> readDepositsFromJson(JSONObject jsonObject) {

        Logger logger = Logger.getLogger("ServerInfoLog");
        List<Deposit> deposits = new ArrayList<>();

        try {

            JSONArray jsonArray = (JSONArray) jsonObject.get("deposits");
            Iterator<Object> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                Deposit deposit = new Deposit();
                JSONObject pars = (JSONObject) iterator.next();
                deposit.setId(Integer.valueOf(pars.get("id").toString()));
                deposit.setName((String) pars.get("customer"));
                deposit.setInitalBalance((Integer.valueOf((String) pars.get("initialBalance"))));
                deposit.setUpperBound((Integer.valueOf((String) pars.get("upperBound"))));
                deposits.add(deposit);
            }
            return deposits;
        } catch (Exception e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public int getPort() {
        return port;
    }

    public String getOutLog() {
        return outLog;
    }

    public List<Deposit> getDeposits() {
        return deposits;
    }
}
