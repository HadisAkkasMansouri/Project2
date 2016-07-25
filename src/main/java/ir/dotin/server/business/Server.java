package ir.dotin.server.business;

import java.util.List;

public class Server {

    private String port;
    private String outLog;
    private List<Deposit> dipositeList;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getOutLog() {
        return outLog;
    }

    public void setOutLog(String outLog) {
        this.outLog = outLog;
    }

    public List<Deposit> getDipositeList(){
        return dipositeList;
    }

    public void setDipositeList(List<Deposit> dipositeList){
        this.dipositeList = dipositeList;
    }
}
