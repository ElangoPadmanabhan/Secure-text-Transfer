package com.example.main.model.request;

public class EmailRequest {

    private String subject;

    private String text;

    private String receiverEmail;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    @Override
    public String toString() {
        return "EmailRequest{" +
                "subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", receiverEmail='" + receiverEmail + '\'' +
                '}';
    }
}
