package ir.dotin.terminal.business;

import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
import ir.dotin.shared.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TerminalInfo {

    private int id;
    private String type;
    private String ip;
    private String port;
    private String outLogPath;
    private List<Transaction> transactionList;
    private final static String terminal_Path = "terminal.xml";

    public static TerminalInfo fromXML() {

        TerminalInfo terminalInfo = new TerminalInfo();
        String outLog = null;
        List<Transaction> transactions = new ArrayList<>();
        try {
            File xmlfile = new File(terminal_Path);
            DocumentBuilderFactory dbfactory = DocumentBuilderFactoryImpl.newInstance();
            DocumentBuilder dBuilder = dbfactory.newDocumentBuilder();
            Document document = dBuilder.parse(xmlfile);
            document.getDocumentElement().normalize();
            Element element = document.getDocumentElement();
            terminalInfo.setId(Integer.parseInt(element.getAttribute("id")));
            terminalInfo.setType(element.getAttribute("type"));
            NodeList nodeList = element.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String nodeName = node.getNodeName();
                if (nodeName.equals("server")) {
                    terminalInfo.setIp(node.getAttributes().getNamedItem("ip").getTextContent());
                    terminalInfo.setPort(node.getAttributes().getNamedItem("port").getTextContent());
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
            terminalInfo.setTransactionList(transactions);
            return terminalInfo;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       return terminalInfo;
    }

    public int getId(){
        return id;
    }

    public void setId (int id){
        this.id = id;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getOutLogPath() {
        return outLogPath;
    }

    public void setOutLogPath(String outLogPath) {
        this.outLogPath = outLogPath;
    }

    public List<Transaction> getTransactionList(){
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList){
        this.transactionList = transactionList;
    }
}
