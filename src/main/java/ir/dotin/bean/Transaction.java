package ir.dotin.bean;

import java.io.Serializable;
import java.util.List;

public class Transaction implements Serializable{

    private int transactionId;
    private String transactionType;
    private int amount;
    private int depositId;
    private String responseCode;


    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDepositId() {
        return depositId;
    }

    public void setDepositId(int depositId) {
        this.depositId = depositId;
    }

    public String getResponseCode(){
        return responseCode;
    }

    public void setResponseCode(String responseCode){
        this.responseCode = responseCode;
    }

    @Override
    public String toString() {
        return "{transactionId:" +transactionId+
                ",transactionType:" +transactionType+
                ",amount:" +amount+
                ",depositId:" +depositId+
                "}";
    }
}
