package ir.dotin.shared;

import ir.dotin.server.business.Deposit;
import ir.dotin.server.business.ResponseTransaction;
import ir.dotin.server.business.ServerInfo;
import java.io.Serializable;
import java.util.List;

public class Transaction implements Serializable {

    private static ServerInfo serverInfo;

    public static void initialize(ServerInfo serverInfo) {
        Transaction.serverInfo = serverInfo;
    }

    private int transactionId;
    private String transactionType;
    private int amount;
    private int depositId;

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

    @Override
    public String toString() {
        return "{transactionId:" + transactionId +
                ",transactionType:" + transactionType +
                ",amount:" + amount +
                ",depositId:" + depositId +
                "}";
    }

    public ResponseTransaction perform() {

        synchronized (this) {
            List<Deposit> depositList = serverInfo.getDeposits();
            if ((!getTransactionType().equalsIgnoreCase(TransactionType.DEPOSIT.toString())) && (!getTransactionType().equalsIgnoreCase(TransactionType.WITHDRAW.toString()))) {
//                    logger.warning("The Transaction of" + this + "is invalid..");
//                    throw new InvalidReceivedDataException("Invalid transaction type!");
                return new ResponseTransaction(getTransactionId(), ResponseType.INVALID_TRANSACTION);
            }
            for (Deposit deposit : depositList) {
                if (getDepositId() == deposit.getId()) {
                    if (getTransactionType().equalsIgnoreCase(TransactionType.DEPOSIT.toString())) {
                        if (validateDeposit(deposit)) {
                            return deposit.doDepositTransaction(this);
                        } else {
                            return new ResponseTransaction(getTransactionId(), ResponseType.UPPER_BOUND);
//                          throw new ViolatedUpperBoundException("The transaction is upper bound balance restriction violated!");
                        }
                    } else {
                        if (validateWithdraw(deposit)) {
                            return deposit.doWithdrawTransaction(this);
                        } else {
                            //                      throw InadequateInitialBalanceException("Not enough balance!");
                            return new ResponseTransaction(getTransactionId(), ResponseType.INADEQUATE_AMOUNT);
                        }
                    }
                }
            }

            return new ResponseTransaction(getTransactionId(), ResponseType.UNDEFINED_DEPOSIT);
        }
    }

    public boolean validateDeposit(Deposit deposit) {
        synchronized (this) {
            return getAmount() + deposit.getInitalBalance() <= deposit.getUpperBound();
        }
    }

    public boolean validateWithdraw(Deposit deposit) {
        synchronized (this) {
            return getAmount() <= deposit.getInitalBalance();
        }
    }

}
