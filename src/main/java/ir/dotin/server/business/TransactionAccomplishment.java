package ir.dotin.server.business;

import ir.dotin.shared.ResponseType;
import ir.dotin.shared.Transaction;
import ir.dotin.shared.TransactionType;
import java.util.List;
import java.util.logging.Logger;

public class TransactionAccomplishment {


    Logger logger = Logger.getLogger("Validation Log");
    ServerInfo info = new ServerInfo();
    ResponseTransaction responseTransaction = null;

    public ResponseTransaction readTransaction(Transaction transaction) {

        List<Deposit> depositList = ServerInfo.fromJson().getDeposits();
        if ((!transaction.getTransactionType().equalsIgnoreCase(TransactionType.DEPOSIT.toString())) && (!transaction.getTransactionType().equalsIgnoreCase(TransactionType.WITHDRAW.toString()))) {
            transaction.setResponseType(ResponseType.INVALID_TRANSACTION);
            responseTransaction = new ResponseTransaction(transaction.getTransactionId(), transaction.getResponseType(), transaction.getAmount(), "Invalid Transaction");
            return responseTransaction;
        }
        boolean flag = false;
        for (Deposit deposit : depositList) {
            if ((transaction.getDepositId()) == (deposit.getId())) {
                if (transaction.getTransactionType().equalsIgnoreCase(TransactionType.DEPOSIT.toString())) {
                    if (validateDeposit(transaction, deposit))
                        return doDepositTransaction(deposit, transaction);
                    else
                        responseTransaction = new ResponseTransaction(transaction.getTransactionId(), transaction.getResponseType(), transaction.getAmount(), "UpperBound Transaction Amount");
                    return responseTransaction;
                } else {
                    if (validateWithdraw(transaction, deposit)) {
                        return doWithdrawTransaction(deposit, transaction);
                    }else
                        responseTransaction = new ResponseTransaction(transaction.getTransactionId(),transaction.getResponseType(),transaction.getAmount(),"Inadequate Transaction Amount");
                    return  responseTransaction;
                }
            }
        }
        if (!flag) {
            transaction.setResponseType(ResponseType.UNDEFINED_DEPOSIT);
            responseTransaction = new ResponseTransaction(transaction.getTransactionId(), transaction.getResponseType(), transaction.getAmount(), "Undefined Deposit");
            return responseTransaction;
        }
        return responseTransaction;
    }

    public boolean validateDeposit(Transaction transaction, Deposit deposit) {

        if ((transaction.getAmount() > deposit.getUpperBound()) || ((transaction.getAmount() + deposit.getInitalBalance()) > deposit.getUpperBound())) {
            transaction.setResponseType(ResponseType.UPPER_BOUND);
            return false;
        }
        return true;
    }

    public boolean validateWithdraw (Transaction transaction, Deposit deposit) {

                if (transaction.getAmount() > deposit.getInitalBalance()) {
                    transaction.setResponseType(ResponseType.INADEQUATE_AMOUNT);
                    return false;
                } else {
                    return true;
                }
            }

    public ResponseTransaction doWithdrawTransaction(Deposit deposit, Transaction transaction) {
        int withdrawamount = deposit.getInitalBalance() - transaction.getAmount();
        transaction.setResponseType(ResponseType.SUCCESS);
        deposit.setInitalBalance(withdrawamount);
        responseTransaction = new ResponseTransaction(transaction.getTransactionId(),transaction.getResponseType(),deposit.getInitalBalance(), "Successful Transaction");
        return responseTransaction;
    }

    public ResponseTransaction doDepositTransaction(Deposit deposit, Transaction transaction) {
        int depositAmount = deposit.getInitalBalance() + transaction.getAmount();
        transaction.setResponseType(ResponseType.SUCCESS);
        deposit.setInitalBalance(depositAmount);
        responseTransaction = new ResponseTransaction(transaction.getTransactionId(),transaction.getResponseType(),deposit.getInitalBalance(), "Successful Transaction");
        return responseTransaction;
    }
}
