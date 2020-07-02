package com.thescienceset.momomessages;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Message {
    String type;
    double amount;
    String name;
    String reference;
    Date date;

    String body;
    String address;

    final static String SENT = "Sent";
    final static String TRANSFER = "Transfer";
    final static String IN = "In";
    final static String CASHOUT = "Cash out";
    final static String CASHIN = "Cash In";
    final static String PAYMENT = "Payment";
    final static String PAYMENTFOR = "Payment For";
    final static String MOMOPAY = "MomoPay";
    final static String MOMOPAYCONFIRMATION = "Momo Pay Confirmation";
    final static String INTEREST = "Interest";
    final static String SPECIAL = "Special";

    public Message(String type, double amount, String name, String reference, Date date) {
        this.type = type;
        this.amount = amount;
        this.name = name;
        this.reference = reference;
        this.date = date;
    }

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
        int indexDate = smsInboxCursor.getColumnIndex("date");
        if(indexBody < 0 || !smsInboxCursor.moveToFirst()) return messages; //if there are not sms-es, abort

        int no = 0;

        do{
            no++;
            //get string at cursor's row at the address column
            String smsAddress = smsInboxCursor.getString(indexAddress);
            String smsBody = smsInboxCursor.getString(indexBody);
            long smsDate = smsInboxCursor.getLong(indexDate);


            //filter for sms from mobilemoney
            if(smsAddress.equals("MobileMoney")){

                String type = parseType(smsBody);
                String name = parseName(smsBody);
                double amount = parseAmount(smsBody);
                String reference = parseReference(smsBody);
                Date date = new Date(smsDate);

                Message message = new Message(type, amount,name,reference, date);

                messages.add(message);
            }

        } while (smsInboxCursor.moveToNext());

        return messages;

    }

    private static String parseType(String body){
        String type ="";

        if(body.startsWith("Payment made for")){
            type = SENT;
        } else if (body.startsWith("Transfer of")){
            type = TRANSFER;
        } else if (body.startsWith("Payment received for")){
            type = IN;
        } else if (body.startsWith("Cash Out made for")){
            type = CASHOUT;
        } else if (body.startsWith("Cash In received for")){
            type = CASHIN;
        } else if (body.startsWith("Payment for")) {
            type = PAYMENTFOR;
        }else if (body.startsWith("Your payment of")){
            type = PAYMENT;
        }else if (body.startsWith("Y'ello. You have Paid")){
            type = MOMOPAYCONFIRMATION;
        }else if (body.startsWith("An amount of")){
            type = INTEREST;
        } else {
            type = SPECIAL;
        }

        return type;
    }

    private static String parseName(String body){
        String name="";

        if(parseType(body).equals(SENT)){
            int beginIndex = body.indexOf("to") + 3;
            int endIndex = body.indexOf(")")+1;
            name = body.substring(beginIndex, endIndex);

        } else if (parseType(body).equals(CASHOUT)){
            int beginIndex = body.indexOf("to") + 3;
            int endIndex = body.indexOf("Current") - 2;
            name = body.substring(beginIndex, endIndex);
        } else if (parseType(body).equals(IN)){
            int beginIndex = body.indexOf("from") + 5;
            int endIndex = body.indexOf("Current") - 1;
            name = body.substring(beginIndex, endIndex);
        } else if (parseType(body).equals(CASHIN)){
            int beginIndex = body.indexOf("from") + 5;
            int endIndex = body.indexOf("Current") - 2;
            name = body.substring(beginIndex, endIndex);
        }

        return name;
    }

    private static double parseAmount(String body){
        String[] tokens = body.split(" ");
        String strAmount = "";
        double amount = 0.0;

        for (int i=0; i<tokens.length; i++){
            String token = tokens[i];
            if(token.equals("GHS")){
                strAmount = tokens[i+1];
                break;
            }
            else if (token.startsWith("GHS")){
                strAmount = token.substring(3);
                break;
            }
        }

        try{
            amount = Double.valueOf(strAmount);
        } catch (Exception ex){
            //handle error

        }

        return amount;
    }

    private static String parseReference(String body){
        String[] tokens = body.split("\\.");
        String reference = "";

        for(int i=0; i<tokens.length; i++){
            String token = tokens[i];
            if(token.startsWith(" Reference")){
                reference = token.substring(11);
                break;
            }
        }

        return reference;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
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

    public void setDate(Date date){
        this.date = date;
    }

    public Date getDate(){
        return date;
    }

    public String getDateString(){
        DateFormat dateFormat = new SimpleDateFormat("E, dd-M-Y hh:mm");
        return(dateFormat.format(getDate()));
    }

    public String toString(){
        return type + "\n" + body + "\n";
    }
}
