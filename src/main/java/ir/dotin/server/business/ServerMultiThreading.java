package ir.dotin.server.business;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerMultiThreading{

    public static void main(String args[]){

        Logger logger = Logger.getLogger("Log");
        try {
            FileHandler fileHandler = new FileHandler("src\\main\\resources\\outLog");
            logger.addHandler(fileHandler);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            logger.info("*************Server is started*************");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerHandler serverHandler = new ServerHandler();
        serverHandler.readJSONFile();
        new Thread(serverHandler).start();
    }
}
