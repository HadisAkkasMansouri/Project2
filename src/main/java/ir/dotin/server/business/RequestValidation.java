package ir.dotin.server.business;

import ir.dotin.bean.Deposite;
import ir.dotin.bean.Responsetype;
import ir.dotin.bean.Transaction;
import ir.dotin.bean.TransactionType;
import java.util.List;
import java.util.logging.Logger;

public class RequestValidation {


    ServerHandler serverHandler = new ServerHandler();
    Logger logger = Logger.getLogger("Validation Log");

    public boolean validateTransaction (Transaction transaction) {

        List<Deposite> depositeList = serverHandler.readJSONFile();
        if ((!transaction.getTransactionType().equalsIgnoreCase(TransactionType.DEPOSIT.toString())) && (!transaction.getTransactionType().equalsIgnoreCase(TransactionType.WITHDRAW.toString()))) {
            transaction.setResponseCode(String.valueOf(Responsetype.INVALID_TRANSACTION));
            logger.severe("The transaction is not recognised !");
            return false;
        }
        boolean flag = false;
        for (Deposite deposite : depositeList) {
            if ((transaction.getDepositId()) == (deposite.getId())) {
                if (transaction.getTransactionType().equalsIgnoreCase(TransactionType.DEPOSIT.toString())) {
                    if(validateDeposit(transaction, deposite))
                        serverHandler.doDepositTransaction(deposite,transaction);
                } else {
                    if (validateWithdraw(transaction, deposite)) {
                        serverHandler.doWithdrawTransaction(deposite,transaction);
                    }
                }
                flag = true;
                break;
            }
        }
        if (!flag){
            transaction.setResponseCode(String.valueOf(Responsetype.UNDEFINED_DEPOSIT));
            logger.severe("The deposit Id of : " + transaction.getDepositId() + " is invalid !!!");
        }
        return false;
    }

    public boolean validateDeposit (Transaction transaction,Deposite deposite){

            if((transaction.getAmount() > deposite.getUpperBound()) || ((transaction.getAmount() + deposite.getInitalBalance()) > deposite.getUpperBound())){
                transaction.setResponseCode(String.valueOf(Responsetype.UPPER_BOUND));
                logger.warning("The transaction amount is more than defined UpperBound !!!");
                return false;
            }else{
                return true;
            }
    }


    public boolean validateWithdraw (Transaction transaction, Deposite deposite){

            if (transaction.getAmount() > deposite.getInitalBalance()) {
                transaction.setResponseCode(String.valueOf(Responsetype.INADEQUATE_AMOUNT));
                logger.warning("The balance is not adequate!!!");
                return false;
            }else{
                return true;
            }
    }
}
