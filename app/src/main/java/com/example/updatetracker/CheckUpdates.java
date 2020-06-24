package com.example.updatetracker;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.FileHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class CheckUpdates extends AsyncTask<String, Void, String[]> {
    protected String version_name,version_code,version_url,notes;
    private String[] answer;
    public AsyncResponse delegate = null;

    @Override
    protected String[] doInBackground(String... file_url) {
        try {
            URL url = new URL(file_url[0]);
            URLConnection urlConnection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(in);
            doc.getDocumentElement().normalize();
            NodeList nList  = doc.getElementsByTagName("update");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    this.version_name= eElement.getElementsByTagName("latestVersion").item(0).getTextContent();
                    this.version_code= eElement.getElementsByTagName("latestVersionCode").item(0).getTextContent();
                    this.version_url= eElement.getElementsByTagName("url").item(0).getTextContent();
                    this.notes= eElement.getElementsByTagName("releaseNotes").item(0).getTextContent();
                }
            }

        }
        catch (Exception e){
            System.out.println(e);
        }
        this.answer = new String[]{version_name,version_code,version_url,notes};
        return answer;
    }

    @Override
    protected void onPostExecute(String[] values) {
        delegate.processFinish(values);
    }

}
