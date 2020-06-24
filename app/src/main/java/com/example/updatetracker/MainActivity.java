package com.example.updatetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AsyncResponse {

    RecyclerView recyclerView;
    Button checkUpdatesButton;
    SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkUpdatesButton = findViewById(R.id.checkUpdates);

        final ArrayList<String> versionName = new ArrayList<>();
        final ArrayList<String> versionLink = new ArrayList<>();
        mydatabase = openOrCreateDatabase("VERSIONS_DATABASE",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Version(version_name VARCHAR, version_code VARCHAR, version_link VARCHAR, notes VARCHAR);");

        checkUpdatesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckUpdates checkUpdates = new CheckUpdates();
                checkUpdates.delegate = MainActivity.this;
                checkUpdates.execute("https://raw.githubusercontent.com/Panchal-Parth/UpdateTracker2/master/version_log.xml");

//                AppUpdater appUpdater = new AppUpdater(MainActivity.this);
//
//                appUpdater.setUpdateFrom(UpdateFrom.XML)
//                        .setUpdateXML("https://raw.githubusercontent.com/Panchal-Parth/UpdateTracker2/master/version_log.xml")
//                        .setDisplay(Display.DIALOG)
//                        .showAppUpdated(true)
//                        .start();

                Cursor resultSet = mydatabase.rawQuery("Select * from Version",null);
                try{
                    while (resultSet.moveToNext()){
                        versionName.add(resultSet.getString(0));
                        versionLink.add(resultSet.getString(1));
                    }
                }
                finally {
                    resultSet.close();
                }
                recyclerView = findViewById(R.id.recyclerView);
                MyAdapter myAdapter = new MyAdapter(MainActivity.this,versionName,versionLink);
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }

        });


    }

    @Override
    public void processFinish(String[] output) {
        Cursor res =  mydatabase.rawQuery( "select * from Version where version_name="+output[0]+"", null );
        if(res.getCount() == 0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("version_name", output[0]);
            contentValues.put("version_code",output[1]);
            contentValues.put("version_link", output[2]);
            contentValues.put("notes", output[3]);
            System.out.println("This is where i fucked up");
            mydatabase.insert("Version", null, contentValues);
        }
        if(android.os.Build.VERSION.SDK_INT <  Integer.parseInt(output[1])){
            //Dialogue
        }else{

        }

    }
}