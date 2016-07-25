package ir.dotin.server.business;

import ir.dotin.shared.ResponseType;
import java.io.Serializable;

public class ResponseTransaction implements Serializable {

    private int id;
    private ResponseType responseType;
    private int newBalance;
    private String description;

    ResponseTransaction(){}

    public ResponseTransaction(int id, ResponseType responseType, int newBalance, String description){

        this.id = id;
        this.responseType = responseType;
        this.newBalance = newBalance;
        this.description = description;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public ResponseType getResponseType(){
        return responseType;
    }

    public void setId(ResponseType responseType){
        this.responseType = responseType;
    }

    public int getnewBalance(){
        return newBalance;
    }

    public void setnewBalance(int newBalance){
        this.newBalance = newBalance;
    }

    public String getdescription(){
        return description;
    }

    public void setdescription(String description){
        this.description = description;
    }

    @Override
    public String toString() {
        return "ResponseTransaction : Id =  " + id
                + "ResponseType = " + responseType
                + "NewBalance = " + newBalance
                + "Description = " + description;
    }
}
