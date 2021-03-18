package com.example.homework2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    int CAMERA_REQUEST_CODE = 50;
    int PHONE_PERMISSION_REQUEST_CODE = 40;
    String NAME_CODE = "NAME";
    String ADDRESS_CODE = "ADRESS";
    String DATE_CODE = "DATE";
    String BLOODGROUP_CODE = "BLOODGROUP";
    String QUALIFICATION_CODE = "QUALIFICATION";


    MediaPlayer mediaPlayer;


    Bitmap bitmap;
    Button button_location;
    Button profileButton;
    TextView textViewLocation;
    ImageView imageView;
    Button buttonC;
    Button submit;
    DatePickerDialog picker;
    EditText dateEditText;
    EditText nameEditText;
    EditText bloodGroupEditText;
    EditText addressEditText;
    ArrayList qualificationList;
    ArrayAdapter arrayAdapter;
    Spinner spinner;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this,R.raw.ting);

        submit = findViewById(R.id.submit);
        checkBox = findViewById(R.id.checkBox);
        nameEditText = findViewById(R.id.et_name);
        bloodGroupEditText = findViewById(R.id.et_bloodGroup);
        imageView = findViewById(R.id.imageView);
        addressEditText = findViewById(R.id.et_address);
        profileButton = findViewById(R.id.profile_button);
        button_location = findViewById(R.id.button_location);
        textViewLocation = findViewById(R.id.tv_location);


        dateEditText = findViewById(R.id.et_date);
        spinner = findViewById(R.id.spinner);
        qualificationList = new ArrayList();
        qualificationList.add("Business");
        qualificationList.add("IT");
        qualificationList.add("Finance");
        qualificationList.add("Management");
        qualificationList.add("Marketing");

        arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, qualificationList);
        spinner.setAdapter(arrayAdapter);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra(NAME_CODE, nameEditText.getText().toString());
                intent.putExtra(DATE_CODE, dateEditText.getText().toString());
                intent.putExtra(BLOODGROUP_CODE, bloodGroupEditText.getText().toString());
                intent.putExtra(ADDRESS_CODE, addressEditText.getText().toString());
                intent.putExtra(QUALIFICATION_CODE, spinner.getSelectedItem().toString());

                if (checkBox.isChecked()) {
                    startActivity(intent);
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Tick Terms and Agreements for submission");
                    alert.setNeutralButton("Cancel", null);

                    alert.show();
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void showAboutDialog(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    public void callIn112(MenuItem item) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:123"));
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, PHONE_PERMISSION_REQUEST_CODE);
        } else {
            startActivity(callIntent);
        }
    }

    public void showContactDialog(MenuItem item) {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.contact_alert_dialog);
        dialog.show();

        buttonC = findViewById(R.id.buttonC);
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void showDatePicker(View view) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        picker = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();
    }


    public void openCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
                profileButton.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getLocation(View view) {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        textViewLocation.setText(longitude + ", " + latitude);
        button_location.setVisibility(View.INVISIBLE);
        textViewLocation.setVisibility(View.VISIBLE);


    }

    public void makeSound(View view) {
        mediaPlayer.start();


    }
}


