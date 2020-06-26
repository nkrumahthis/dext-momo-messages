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
//    ArrayList<String> smsMessagesList = new ArrayList<>();
//    ListView smsListView;
//    ArrayAdapter arrayAdapter;

    ArrayList<Message> messages;
    ListView listView;
    private static MessageAdapter adapter;

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
        listView = findViewById(R.id.smslist);

        messages = new ArrayList<>();

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
        adapter.clear();
        do{
            //filter for sms from mobilemoney
            String smsAddress = smsInboxCursor.getString(indexAddress);
            String smsBody = smsInboxCursor.getString(indexBody);

            String type, name, amount, reference;

            if(smsAddress.equals("MobileMoney")){
                messages.add(new Message(smsBody));
            }

        } while (smsInboxCursor.moveToNext());

        adapter = new MessageAdapter(messages, getApplicationContext());
        listView.setAdapter(adapter);

    }

    public void updateList(final Message message){
        adapter.insert(message, 0);
        adapter.notifyDataSetChanged();
    }

}
