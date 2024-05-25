package com.example.mychat.model;

public class CallModel {
    private String target;
    private String sender;
    private String data;
    private CallTypeModel type;

    public CallModel(String target, String sender, String data, CallTypeModel type) {
        this.target = target;
        this.sender = sender;
        this.data = data;
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public CallTypeModel getType() {
        return type;
    }

    public void setType(CallTypeModel type) {
        this.type = type;
    }
}
