package com.thescienceset.momomessages;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.StringTokenizer;

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

    public static ArrayList<Message> getMessages(ContentResolver resolver){

        ArrayList<Message> messages = new ArrayList<>();

        //set up sms content resolver
        //this provides access to the sms inbox
        //this cursor contains the result set of the query made against the content resolver
        ContentResolver contentResolver = resolver;
        Uri uriSms = Uri.parse("content://sms/inbox");
        Cursor smsInboxCursor = contentResolver.query(uriSms,
                null, null, null, null);

        //pick out info based on columns
        int indexBody = smsInboxCursor.getColumnIndex("body"); //get body's column index
        int indexAddress = smsInboxCursor.getColumnIndex("address"); //get address' column index
        if(indexBody < 0 || !smsInboxCursor.moveToFirst()) return messages; //if there are not sms-es, abort

        int no = 0;

        do{
            no++;
            //get string at cursor's row at the address column
            String smsAddress = smsInboxCursor.getString(indexAddress);
            String smsBody = smsInboxCursor.getString(indexBody);

            //filter for sms from mobilemoney
            if(smsAddress.equals("MobileMoney")){

                String type = parseType(smsBody);
                String name = parseName(smsBody);
                double amount = parseAmount(smsBody);
                String reference = parseReference(smsBody);
                String date = parseDate(smsBody);

                Message message = new Message(type, amount,name + no,reference + no, no + date);

                messages.add(message);
            }

        } while (smsInboxCursor.moveToNext());

        return messages;

    }

    private static String parseType(String body){
        String type ="";

        if(body.startsWith("Payment made for")){
            type = Message.SENT;
        } else if (body.startsWith("Transfer of")){
            type = Message.TRANSFER;
        } else if (body.startsWith("Payment received for")){
            type = Message.IN;
        } else if (body.startsWith("Cash Out made for")){
            type = Message.CASHOUT;
        } else if (body.startsWith("Cash In received for")){
            type = Message.CASHIN;
        } else if (body.startsWith("Payment for")) {
            type = Message.PAYMENTFOR;
        }else if (body.startsWith("Your payment of")){
            type = Message.PAYMENT;
        }else if (body.startsWith("Y'ello. You have Paid")){
            type = Message.MOMOPAYCONFIRMATION;
        }else if (body.startsWith("An amount of")){
            type = Message.INTEREST;
        } else {
            type = Message.SPECIAL;
        }

        return type;
    }

    private static String parseName(String body){
        return "Asomasi Obenteng";
    }

    private static double parseAmount(String body){
        return 100.0;
    }

    //change from String to Date format
    private static String parseDate(String body){
        return "th June 2020";
    }

    private static String parseReference(String body){
        return "Small reference name goes here";
    }

    public Message(String type, Double amount, String name, String reference, String date) {
        this.type = type;
        this.amount = amount;
        this.name = name;
        this.reference = reference;
        this.date = date;
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
