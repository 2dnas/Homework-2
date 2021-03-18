package com.example.homework2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    int SMS_REQUEST_CODE = 12;
    String NAME_CODE = "NAME";
    String ADDRESS_CODE = "ADRESS";
    String DATE_CODE = "DATE";
    String BLOODGROUP_CODE = "BLOODGROUP";
    String QUALIFICATION_CODE = "QUALIFICATION";

    Button buttonC;

    ListView listView;
    ArrayList infoList;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        listView = findViewById(R.id.info_listview);
        buttonC = findViewById(R.id.buttonC);

        infoList = new ArrayList();
        arrayAdapter = new ArrayAdapter(SecondActivity.this,android.R.layout.simple_list_item_1,infoList);

        infoList.add(getIntent().getStringExtra(NAME_CODE));
        infoList.add(getIntent().getStringExtra(ADDRESS_CODE));
        infoList.add(getIntent().getStringExtra(DATE_CODE));
        infoList.add(getIntent().getStringExtra(BLOODGROUP_CODE));
        infoList.add(getIntent().getStringExtra(QUALIFICATION_CODE));

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                notificationAndSMS(view);

                ActivityCompat.requestPermissions(SecondActivity.this,
                        new String[]{Manifest.permission.SEND_SMS}, SMS_REQUEST_CODE);

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("123321212", null, "Hello How Are You?", null, null);
            }
        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void showAboutDialog(MenuItem item) {
        Dialog dialog = new Dialog(SecondActivity.this);
        dialog.setContentView(R.layout.about_custom_dialog);
        dialog.show();
    }

    public void callIn112(MenuItem item) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:123"));
    }

    public void showContactDialog(MenuItem item) {
        Dialog dialog = new Dialog(SecondActivity.this);
        dialog.setContentView(R.layout.contact_alert_dialog);
        dialog.show();

        buttonC = findViewById(R.id.buttonC);
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }


    public void notificationAndSMS(View view) {
        NotificationManager notificationManager = (NotificationManager) SecondActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(SecondActivity.this, channelId)
                .setSmallIcon(android.R.drawable.ic_notification_clear_all)
                .setContentTitle("Hey New Notification")
                .setContentText("It's already 3AM :(");

        notificationManager.notify(notificationId, mBuilder.build());
    }
}