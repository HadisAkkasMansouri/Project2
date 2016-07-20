package ir.dotin.terminal.business;

import ir.dotin.bean.Transaction;

import java.util.List;

public class Terminal {

    private int id;
    private String type;
    private String ip;
    private String port;
    private String outLogPath;
    private List<Transaction> transactionList;

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
