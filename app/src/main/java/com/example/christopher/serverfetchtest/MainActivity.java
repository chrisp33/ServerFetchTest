package com.example.christopher.serverfetchtest;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity {

    private final String DEBUGGING  = "DEBUGGING";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            // Data from Website stream. Contains actual name of current place
            URL fmdServer = new URL("https://data.sparkfun.com/streams/5JrdjYwNOvfGqbQEJqLE.json");
            FetchRecent serverData = new FetchRecent();
            serverData.execute(fmdServer);
            JSONObject stream = serverData.get();
            Log.i(DEBUGGING, "JSON Website: " + stream.toString());

            // Parse the location place name, latitude, and longitude
            JSONObject locationJSONObj = stream.getJSONObject("stream").getJSONObject("_doc").getJSONObject("location");
            String str = locationJSONObj.get("long").toString();
            String latitude = locationJSONObj.get("lat").toString();
            String longitude = locationJSONObj.get("lng").toString();
            final String JSONSTREAM = "JSONSTREAM";

            Log.i(JSONSTREAM,"Place: " + str);
            Log.i(JSONSTREAM,"Lat: " + latitude);
            Log.i(JSONSTREAM,"Long: " + longitude);

            // Launch Google Maps
/*            Intent maps = new Intent(Intent.ACTION_VIEW), chooser = null;
            String label = "FMD";
            String uriString = "http://maps.google.com/maps?q=" + latitude + ',' + longitude + "("+ label +")&z=15";
            maps.setData(Uri.parse(uriString));
            chooser = Intent.createChooser(maps, "Launch Maps");
            startActivity(chooser);*/


            // Get all latitudes and longitudes from the server
            URL fmdLogs = new URL("https://data.sparkfun.com/output/5JrdjYwNOvfGqbQEJqLE.json");
            FetchAll locLog = new FetchAll();
            locLog.execute(fmdLogs);
            JSONArray locArray = locLog.get();
            Log.i(DEBUGGING, "JSON Log: " + locArray.toString());

            // Display latitudes and longitudes in the log
            final String JSONLOGS = "JSONLOG";
            for (int i = 0; i < locArray.length(); i++)
            {
                JSONObject point = (JSONObject) locArray.get(i);
                Log.i(JSONLOGS, "Latitude: " + point.get("latitude"));
                Log.i(JSONLOGS, "Longitude: " + point.get("longitude"));
            }

        } catch (MalformedURLException me) {
            me.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } catch (ExecutionException ee) {
            ee.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class FetchRecent extends AsyncTask<URL, Void, JSONObject>
    {

        @Override
        protected JSONObject doInBackground(URL... params) {

            HttpURLConnection urlConnection;
            try {
                urlConnection = (HttpURLConnection) params[0].openConnection();

                urlConnection.setRequestMethod("GET");
                Log.i(DEBUGGING, "setRequestMethod");

                int code = urlConnection.getResponseCode();
                Log.i(DEBUGGING, "getResponsecode: " + code);

                String message = urlConnection.getResponseMessage();
                Log.i(DEBUGGING, "getResponseMessage: " + message);

                //urlConnection.setRequestProperty("Content-length", "0");
                //urlConnection.setUseCaches(false);
                //urlConnection.setAllowUserInteraction(false);
                urlConnection.connect();

                InputStream iStream = urlConnection.getInputStream();
                String dataString = convertStreamToString(iStream);
                Log.i(DEBUGGING, "website stream: " + dataString);

                iStream.close();

                return new JSONObject(dataString);

            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (JSONException je) {
                je.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }



    private class FetchAll extends AsyncTask<URL, Void, JSONArray>
    {

        @Override
        protected JSONArray doInBackground(URL... params) {

            HttpURLConnection urlConnection;
            try {
                urlConnection = (HttpURLConnection) params[0].openConnection();

                urlConnection.setRequestMethod("GET");
                Log.i(DEBUGGING, "setRequestMethod for all");

                int code = urlConnection.getResponseCode();
                Log.i(DEBUGGING, "getResponsecode for all: " + code);

                String message = urlConnection.getResponseMessage();
                Log.i(DEBUGGING, "getResponseMessage for all: " + message);

                //urlConnection.setRequestProperty("Content-length", "0");
                //urlConnection.setUseCaches(false);
                //urlConnection.setAllowUserInteraction(false);
                urlConnection.connect();

                InputStream iStream = urlConnection.getInputStream();
                String dataString = convertStreamToString(iStream);
                Log.i(DEBUGGING, "log : " + dataString);

                iStream.close();

                return new JSONArray(dataString);

            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (JSONException je) {
                je.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
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
