package com.thescienceset.momomessages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private static MainActivity inst;
    ArrayList<String> smsMessagesList = new ArrayList<>();
    ListView smsListView;
    ArrayAdapter arrayAdapter;

    public static MainActivity instance(){
        return inst;
    }

    @Override
    protected void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smsListView = (ListView) findViewById(R.id.smslist);
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, smsMessagesList);
        smsListView.setAdapter(arrayAdapter);

        //add sms read permission at runtime
        //todo: if permission is not granted

        if(ContextCompat.checkSelfPermission(getBaseContext(),
                "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED){
            //todo if permission is granted then show sms
            refreshSMSInbox();
        } else {
            //then set permission
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    public void refreshSMSInbox(){
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"),
                null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if(indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do{
            //filter for sms from mobilemoney
            String smsAddress = smsInboxCursor.getString(indexAddress);
            String smsBody = smsInboxCursor.getString(indexBody);

            String type, name, amount, reference;

            if(smsAddress.equals("MobileMoney")){
                

                if(smsBody.startsWith("Payment made for")){
                    type = Message.SENT;
                } else if (smsBody.startsWith("Transfer of")){
                    type = Message.TRANSFER;
                } else if (smsBody.startsWith("Payment received for")){
                    type = Message.IN;
                } else if (smsBody.startsWith("Cash Out made for")){
                    type = Message.CASHOUT;
                } else if (smsBody.startsWith("Cash In received for")){
                    type = Message.CASHIN;
                } else if (smsBody.startsWith("Payment for")) {
                    type = Message.PAYMENTFOR;
                }else if (smsBody.startsWith("Your payment of")){
                    type = Message.PAYMENT;
                }else if (smsBody.startsWith("Y'ello. You have Paid")){
                    type = Message.MOMOPAYCONFIRMATION;
                }else if (smsBody.startsWith("An amount of")){
                    type = Message.INTEREST;
                } else {
                    type = Message.SPECIAL;
                }

                StringTokenizer tokenizer  = new StringTokenizer(smsBody, ".");
                String token = tokenizer.nextToken().trim();

                Message message = new Message(type, 0.0,"","");
                message.setBody(smsBody);
                message.setAddress(smsAddress);
//
//                String str = type + "\n" + smsBody + "\n";
                arrayAdapter.add(message.toString());
            }

        } while (smsInboxCursor.moveToNext());
    }

    public void updateList(final String smsMessage){
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
    }

    public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
        try{
            String[] smsMessages = smsMessagesList.get(pos).split("\n");
            String address = smsMessages[0];
            String smsMessage = "";
            for(int i=1; i<smsMessages.length; ++i){
                smsMessage += smsMessages[i];
            }
            String smsMessageStr = address + "\n";
            smsMessageStr += smsMessage;
            Toast.makeText(this, smsMessageStr, Toast.LENGTH_SHORT).show();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
