package com.example.prm392_v1.data.model.gemini;

public class Message {
    public String text;
    public boolean isFromUser;

    public Message(String text, boolean isFromUser) {
        this.text = text;
        this.isFromUser = isFromUser;
    }
}