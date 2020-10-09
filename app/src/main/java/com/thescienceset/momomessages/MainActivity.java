package com.thescienceset.momomessages;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView smsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smsListView = findViewById(R.id.smslist);

        //add sms read permission at runtime
        //todo: if permission is not granted

        if(ContextCompat.checkSelfPermission(getBaseContext(),
                "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED){
            //todo if permission is granted then show sms
            ArrayList<Message> messageList = Message.getMessages(getContentResolver());
            MessageAdapter messageAdapter = new MessageAdapter(messageList, this);
            smsListView.setAdapter(messageAdapter);
        } else {
            //then set permission
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

    }

}
