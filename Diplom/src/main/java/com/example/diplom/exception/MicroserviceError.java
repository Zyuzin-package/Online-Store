package com.example.diplom.exception;

public class MicroserviceError extends Exception {

    private int num;
    private String message;

    public MicroserviceError(String str, int num) {
        this.message = str;
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
