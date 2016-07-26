package ir.dotin.server.business;

import ir.dotin.shared.ResponseType;
import ir.dotin.shared.Transaction;
import java.io.Serializable;

public class Deposit implements Serializable {

    private String name;
    private int id;
    private int initialBalance;
    private int upperBound;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInitalBalance() {
        return initialBalance;
    }

    public void setInitalBalance(int initalBalance) {
        this.initialBalance = initalBalance;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    public ResponseTransaction doWithdrawTransaction(Transaction transaction) {
        int withdrawamount = getInitalBalance() - transaction.getAmount();
        setInitalBalance(withdrawamount);
        ResponseTransaction responseTransaction = new ResponseTransaction(transaction.getTransactionId(), ResponseType.SUCCESS);
        return responseTransaction;
    }

    public ResponseTransaction doDepositTransaction(Transaction transaction) {
        int depositAmount = getInitalBalance() + transaction.getAmount();
        setInitalBalance(depositAmount);
        ResponseTransaction responseTransaction = new ResponseTransaction(transaction.getTransactionId(),ResponseType.SUCCESS);
        return responseTransaction;
    }
}
