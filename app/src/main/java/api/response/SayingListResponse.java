package api.response;

import java.util.ArrayList;

import model.SayingModel;

public class SayingListResponse {
    private int code;
    private String message;
    private ArrayList<SayingModel> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<SayingModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<SayingModel> result) {
        this.result = result;
    }
}
