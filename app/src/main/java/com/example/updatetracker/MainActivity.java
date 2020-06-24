package com.example.updatetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AsyncResponse {

    RecyclerView recyclerView;
    Button checkUpdatesButton;
    SQLiteDatabase mydatabase;
    TextView currVersion;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String version = "version_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkUpdatesButton = findViewById(R.id.checkUpdates);
        recyclerView = findViewById(R.id.recyclerView);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        currVersion = findViewById(R.id.currentVersion);
        currVersion.setText(sharedpreferences.getString(version,"1.0"));
        mydatabase = openOrCreateDatabase("VERSIONS_DATABASE",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Version(version_name VARCHAR, version_code VARCHAR, version_link VARCHAR, notes VARCHAR);");

        processRecyclerView();

        checkUpdatesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckUpdates checkUpdates = new CheckUpdates();
                checkUpdates.delegate = MainActivity.this;
                checkUpdates.execute("https://raw.githubusercontent.com/Panchal-Parth/UpdateTracker2/master/version_log.xml");
            }

        });
    }

    @Override
    public void processFinish(String[] output) {
        Cursor res =  mydatabase.rawQuery( "select * from Version where version_name="+output[0]+"", null );
        String prev_version = sharedpreferences.getString(version,null);
        if( prev_version == null){
            SharedPreferences.Editor edit = sharedpreferences.edit();
            edit.putString(version,output[0]);
            edit.commit();
        }
        if(android.os.Build.VERSION.SDK_INT <  Integer.parseInt(output[1])){
            //Dialogue cannot update
        }else if(res.getCount() <= 0){
            //install dialogue box
            currVersion.setText(output[0]);
            ContentValues contentValues = new ContentValues();
            contentValues.put("version_name", output[0]);
            contentValues.put("version_code",output[1]);
            contentValues.put("version_link", output[2]);
            contentValues.put("notes", output[3]);
            System.out.println("This is where i fucked up");
            mydatabase.insert("Version", null, contentValues);
        }
        processRecyclerView();
    }

    public void processRecyclerView(){
        ArrayList<String> versionName = new ArrayList<>();
        ArrayList<String> versionCode = new ArrayList<>();
        ArrayList<String> versionLink = new ArrayList<>();
        Cursor resultSet = mydatabase.rawQuery("Select * from Version",null);
        try{
            while (resultSet.moveToNext()){
                versionName.add(resultSet.getString(0));
                versionCode.add(resultSet.getString(1));
                versionLink.add(resultSet.getString(2));
            }
        }
        finally {
            resultSet.close();
        }

        MyAdapter myAdapter = new MyAdapter(MainActivity.this,versionName,versionCode,versionLink);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }
}