package com.example.christopher.serverfetchtest;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends ActionBarActivity {

    private final String DEBUGGING  = "DEBUGGING";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream iStream = null;

        // Retrieve data from the server
        try {
            //URL url = new URL("https://data.sparkfun.com/streams/5JrdjYwNOvfGqbQEJqLE");

            URL url = new URL("http://www.google.com/");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            Log.i(DEBUGGING, "setRequestMethod");

            int code = urlConnection.getResponseCode();
            Log.i(DEBUGGING, "getResponsecode");

            Toast.makeText(this, "" + code, Toast.LENGTH_SHORT).show();

            //urlConnection.setRequestProperty("Content-length", "0");
            //urlConnection.setUseCaches(false);
            //urlConnection.setAllowUserInteraction(false);
            //urlConnection.connect();

            //iStream = urlConnection.getInputStream();
            //Toast.makeText(this, convertStreamToString(iStream), Toast.LENGTH_SHORT).show();

            //iStream.close();


        } catch (MalformedURLException e) {
            Toast.makeText(this, "Caught MalformedRLException", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Caught IOException", Toast.LENGTH_SHORT).show();
        }

        catch (Exception e) {
            Toast.makeText(this, "Caught Exception", Toast.LENGTH_SHORT).show();

        }

    }

    private String convertStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Stream Exception", Toast.LENGTH_SHORT).show();
        }
        return total.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
