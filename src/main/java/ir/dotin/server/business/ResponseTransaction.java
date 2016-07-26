package ir.dotin.server.business;

import ir.dotin.shared.ResponseType;
import java.io.Serializable;

public class ResponseTransaction implements Serializable {

    private int id;
    private ResponseType responseType;

    public ResponseTransaction(int id, ResponseType responseType){
        this.id = id;
        this.responseType = responseType;
    }

    public int getId(){
        return id;
    }

    public ResponseType getResponseType(){
        return responseType;
    }

    @Override
    public String toString() {
        return "ResponseTransaction : Id =  " + id
                + "ResponseType = " + responseType
                + "Description = " + responseType.getDescription();
    }
}
