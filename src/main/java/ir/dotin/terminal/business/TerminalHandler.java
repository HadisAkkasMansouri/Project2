package ir.dotin.terminal.business;

import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
import ir.dotin.server.business.ResponseTransaction;
import ir.dotin.shared.ResponseType;
import ir.dotin.shared.Transaction;
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
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class TerminalHandler implements Runnable {

    Logger logger = Logger.getLogger("Terminal Side");
    Terminal terminal = new Terminal();
    private final static String terminal_Path = "terminal.xml";

    public Terminal readXMLFile() {

        String outLog = null;
        try {
            List<Transaction> transactions = new ArrayList<>();
            File xmlfile = new File(terminal_Path);
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
                if (nodeName.equals("server")) {
                    terminal.setIp(node.getAttributes().getNamedItem("ip").getTextContent());
                    terminal.setPort(node.getAttributes().getNamedItem("port").getTextContent());
                }

                if (nodeName.equals("outLog")) {
                    outLog = String.valueOf((node.getAttributes().getNamedItem("path")));
                } else if (nodeName.equals("transactions")) {
                    NodeList childNode = node.getChildNodes();
                    for (int j = 0; j < childNode.getLength(); j++) {
                        Node nodeChild = childNode.item(j);
                        String nodeChildName = nodeChild.getNodeName();
                        if (nodeChildName.equals("transaction")) {
                            Transaction transaction = new Transaction();
                            transaction.setTransactionId(Integer.parseInt(nodeChild.getAttributes().getNamedItem("id").getTextContent()));
                            transaction.setTransactionType((nodeChild.getAttributes().getNamedItem("type").getTextContent()));
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
        List<ResponseTransaction> responseTransactions = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();

        try {
            logger.info("*************The terminal is ready*************");
            Socket socket = new Socket(ip, Integer.parseInt(port));
            ObjectOutputStream outputStream = null;
            ObjectInputStream inputStream = null;
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            for (Transaction transaction : terminal.getTransactionList()) {
                outputStream.writeObject(transaction);
                logger.info("++++++++++++Request is sent++++++++++++");
                outputStream.flush();
                transactions.add(transaction);

                ResponseTransaction e = (ResponseTransaction) inputStream.readObject();
                responseTransactions.add(e);
                logger.info("++++++++++++Response is received++++++++++++");
            }
            socket.close();
            createResponseXML(transactions, responseTransactions);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createResponseXML(List<Transaction> transactionList, List<ResponseTransaction> responseTransactions) {

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement("transactions");
            document.appendChild(rootElement);

            for (int i = 0; i < transactionList.size(); i++) {
                Element responseForTransaction = writeTransaction(responseTransactions.get(i), document, transactionList.get(i));
                rootElement.appendChild(responseForTransaction);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result_Path = new StreamResult(new File("src\\main\\resources\\response.xml"));
            transformer.transform(source, result_Path);
            logger.info("Response file is generated!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Element writeTransaction(ResponseTransaction responseTransaction, Document document, Transaction transaction) {
        Element element = document.createElement("transaction");

        Attr id = document.createAttribute("id");
        id.setValue(String.valueOf(responseTransaction.getId()));
        element.setAttributeNode(id);

        Element transactionType = document.createElement("transactionType");
        transactionType.appendChild(document.createTextNode(String.valueOf(transaction.getTransactionType())));
        element.appendChild(transactionType);

        Element amountTransaction = document.createElement("transactionAmount");
        amountTransaction.appendChild(document.createTextNode(String.valueOf(transaction.getAmount())));
        element.appendChild(amountTransaction);

        Element responseType = document.createElement("responseType");
        responseType.appendChild(document.createTextNode(String.valueOf(responseTransaction.getResponseType())));
        element.appendChild(responseType);

        Element responseCode = document.createElement("responseCode");
        if (responseTransaction.getResponseType().equals(ResponseType.SUCCESS)) {
            responseCode.appendChild(document.createTextNode("00"));

        } else if (responseTransaction.getResponseType().equals(ResponseType.UPPER_BOUND)) {
            responseCode.appendChild(document.createTextNode("61"));

        } else if (responseTransaction.getResponseType().equals(ResponseType.INADEQUATE_AMOUNT)) {
            responseCode.appendChild(document.createTextNode("51"));

        } else if (responseTransaction.getResponseType().equals(ResponseType.INVALID_TRANSACTION)) {
            responseCode.appendChild(document.createTextNode("12"));

        } else if (responseTransaction.getResponseType().equals(ResponseType.UNDEFINED_DEPOSIT)) {
            responseCode.appendChild(document.createTextNode("79"));
        }
        element.appendChild(responseCode);

        Element description = document.createElement("description");
        description.appendChild(document.createTextNode(responseTransaction.getdescription()));
        element.appendChild(description);
        return element;
    }
}