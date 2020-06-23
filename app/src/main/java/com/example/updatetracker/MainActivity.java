package com.example.updatetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> versionName = new ArrayList<>();
        ArrayList<String> versionLink = new ArrayList<>();
        SQLiteDatabase mydatabase = openOrCreateDatabase("VERSIONS_DATABASE",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Version(version_name VARCHAR,version_link VARCHAR);");

        mydatabase.execSQL("INSERT INTO Version VALUES('3.2','admin');");
        mydatabase.execSQL("INSERT INTO Version VALUES('3.2','admin');");
        mydatabase.execSQL("INSERT INTO Version VALUES('3.3','admin');");
        mydatabase.execSQL("INSERT INTO Version VALUES('3.4','admin');");
        mydatabase.execSQL("INSERT INTO Version VALUES('3.5','admin');");

        Cursor resultSet = mydatabase.rawQuery("Select * from Version",null);
        //String vsn = resultSet.getString(0);
        //String link = resultSet.getString(1);

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
        //versionName = new String[]{"1.2","2.2"};
        //versionLink = new String[]{"faergferg","fwraferg"};

        MyAdapter myAdapter = new MyAdapter(this,versionName,versionLink);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}