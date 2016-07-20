package ir.dotin.terminal.business;

import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
import ir.dotin.bean.ResponseType;
import ir.dotin.bean.Transaction;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.transform.Transformer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class TerminalHandler implements Runnable{

    Logger logger = Logger.getLogger("Terminal Side");
    Terminal terminal = new Terminal();

    public Terminal readXMLFile() {

        String outLog = null;
        try {
            List<Transaction> transactions = new ArrayList<>();
            File xmlfile = new File("terminal.xml");
            DocumentBuilderFactory dbfactory = DocumentBuilderFactoryImpl.newInstance();
            DocumentBuilder dBuilder = dbfactory.newDocumentBuilder();
            Document document = dBuilder.parse(xmlfile);
            document.getDocumentElement().normalize();
            Element element = document.getDocumentElement();
            terminal.setId(Integer.parseInt(element.getAttribute("id")));
            terminal.setType(element.getAttribute("type"));
            NodeList nodeList = element.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String nodeName = node.getNodeName();
                if (nodeName.equals("server")){
                    terminal.setIp(node.getAttributes().getNamedItem("ip").getTextContent());
                    terminal.setPort(node.getAttributes().getNamedItem("port").getTextContent());
                }

                if (nodeName.equals("outLog")){
                    outLog = String.valueOf((node.getAttributes().getNamedItem("path")));
                }
                else if (nodeName.equals("transactions")){
                        NodeList childNode = node.getChildNodes();
                        for (int j = 0; j < childNode.getLength(); j++) {
                            Node nodeChild = childNode.item(j);
                            String nodeChildName = nodeChild.getNodeName();
                            if (nodeChildName.equals("transaction")) {
                                Transaction transaction = new Transaction();
                                transaction.setTransactionId(Integer.parseInt(nodeChild.getAttributes().getNamedItem("id").getTextContent()));
                                transaction.setTransactionType(String.valueOf(nodeChild.getAttributes().getNamedItem("type").getTextContent()));
                                transaction.setAmount(Integer.parseInt(nodeChild.getAttributes().getNamedItem("amount").getTextContent()));
                                transaction.setDepositId(Integer.parseInt(nodeChild.getAttributes().getNamedItem("deposit").getTextContent()));
                                transactions.add(transaction);
                            }
                        }
                }
            }
            terminal.setTransactionList(transactions);
            return terminal;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        String ip = terminal.getIp();
        String port = terminal.getPort();
        try{
            logger.info("*************Sending to Server*************");
            Socket socket = new Socket(ip, Integer.parseInt(port));
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            for (Transaction transaction : terminal.getTransactionList()) {
                outputStream.writeObject(transaction);
            }

            logger.info("*************Received from Server*************");
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            createResponseXML((Transaction) inputStream.readObject());
            boolean stop = true;
            String temp;
            while (stop){
                for (Transaction transaction : terminal.getTransactionList()) {
                    System.out.println(inputStream.readUTF());
                }
               temp = String.valueOf(inputStream);
                if(temp.compareToIgnoreCase("END") == 0){
                    stop = false;
                }
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createResponseXML(Transaction transaction) {

        try {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();
        Element rootElement = document.createElement("transactions");
        document.appendChild(rootElement);

        Element element = document.createElement("transaction");
        rootElement.appendChild(element);

        Attr id = document.createAttribute("id");
        id.setValue(String.valueOf(transaction.getTransactionId()));
        element.setAttributeNode(id);

        Element transactionType = document.createElement("transactionType");
        transactionType.appendChild(document.createTextNode(transaction.getTransactionType()));
        element.appendChild(transactionType);

        Element amountTransaction = document.createElement("transactionAmount");
        amountTransaction.appendChild(document.createTextNode(String.valueOf(transaction.getAmount())));
        element.appendChild(amountTransaction);


        Element responseCode = document.createElement("responseCode");
        Element description = document.createElement("description");
            if(transaction.getResponseCode().equalsIgnoreCase(ResponseType.SUCCESS.toString())){
                responseCode.appendChild(document.createTextNode("00"));
                description.appendChild(document.createTextNode("Successful Transaction"));
            }
            else if(transaction.getResponseCode().equalsIgnoreCase(ResponseType.UPPER_BOUND.toString())){
                responseCode.appendChild(document.createTextNode("61"));
                description.appendChild(document.createTextNode("UpperBound Transaction Amount"));
            }
            else if(transaction.getResponseCode().equalsIgnoreCase(ResponseType.INADEQUATE_AMOUNT.toString())) {
                responseCode.appendChild(document.createTextNode("51"));
                description.appendChild(document.createTextNode("Inadequate Transaction Amount"));
            }
            else if(transaction.getResponseCode().equalsIgnoreCase(ResponseType.INVALID_TRANSACTION.toString())) {
                responseCode.appendChild(document.createTextNode("12"));
                description.appendChild(document.createTextNode("Invalid Transaction"));
            }
            else if(transaction.getResponseCode().equalsIgnoreCase(ResponseType.UNDEFINED_DEPOSIT.toString())) {
                responseCode.appendChild(document.createTextNode("79"));
                description.appendChild(document.createTextNode("Undefined transaction"));
            }else {
                responseCode.appendChild(document.createTextNode("**"));
                description.appendChild(document.createTextNode("**"));
            }
        element.appendChild(responseCode);
        element.appendChild(description);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File("src\\main\\resources\\response.xml"));
            transformer.transform(source, result);
            logger.info("Response file is saved!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}