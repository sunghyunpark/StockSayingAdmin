package api.response;

import java.util.ArrayList;

import model.AuthorModel;

public class AuthorListResponse {
    private int code;
    private String message;
    private ArrayList<AuthorModel> result;

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

    public ArrayList<AuthorModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<AuthorModel> result) {
        this.result = result;
    }
}
