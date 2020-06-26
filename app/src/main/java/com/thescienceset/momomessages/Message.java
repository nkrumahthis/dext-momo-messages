package com.thescienceset.momomessages;

public class Message {
    String type;
    Double amount;
    String name;
    String reference;
    String date;

    String body;
    String address;

    public static final String SENT = "Sent";
    public static final String TRANSFER = "Transfer";
    public static final String IN = "In";
    public static final String CASHOUT = "Cash out";
    public static final String CASHIN = "Cash In";
    public static final String PAYMENT = "Payment";
    public static final String PAYMENTFOR = "Payment For";
    public static final String MOMOPAY = "MomoPay";
    public static final String MOMOPAYCONFIRMATION = "Momo Pay Confirmation";
    public static final String INTEREST = "Interest";
    public static final String SPECIAL = "Special";

    public Message(String type, Double amount, String name, String reference) {
        this.type = type;
        this.amount = amount;
        this.name = name;
        this.reference = reference;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setBody(String body){
        this.body = body;
    }

    public String getBody(){
        return body;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress(){
        return address;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return date;
    }

    public String toString(){
        return type + "\n" + body + "\n";
    }
}
