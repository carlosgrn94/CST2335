package com.example.chuck_csp.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.Result;

public class WeatherForecast extends Activity {

    private final String ACTIVITY_NAME = "WeatherForecast";
    //private final ProgressBar pBar = (ProgressBar)findViewById(R.id.weatherPBar);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        Log.i(ACTIVITY_NAME, "in onCreate()");

        //Create ForecastQuery object to run doInBackground
        ForecastQuery fq = new ForecastQuery();
        fq.execute();

    }

    private class ForecastQuery extends AsyncTask<String, Integer, String>{

        protected String min, max, current, iconName;
        protected Bitmap weatherPic;

        private final ProgressBar progressBar = (ProgressBar)findViewById(R.id.weatherPBar);


        @Override
        protected String doInBackground(String... strings) {
            Log.i(ACTIVITY_NAME, "in doInBackground()");

            String urlString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
            XmlPullParser parser = null;

            try {
                parser = getXMLParser(downloadURL(urlString));
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                readFeed(parser);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Executed";
        }

        //Read feed from xml
        protected void readFeed(XmlPullParser parser) throws XmlPullParserException, IOException{
            Log.i("READFEED", "onReadFeed");
            while(parser.next() != XmlPullParser.END_DOCUMENT){
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                if(parser.getEventType() == XmlPullParser.END_TAG){
                    parser.nextTag();
                }

                if(parser.getName().equals("temperature")) {
                    this.min = parser.getAttributeValue(null, "min");
                    publishProgress(25,50,75);
                    this.max = parser.getAttributeValue(null, "max");
                    publishProgress(25,50,75);
                    this.current = parser.getAttributeValue(null, "value");
                    publishProgress(25,50,75);
                    Log.i("READFEED", "min: " + min);
                    Log.i("READFEED", "max: " + max);
                    Log.i("READFEED", "current: " + current);
                }
                if(parser.getName().equals("weather")){
                    this.iconName = parser.getAttributeValue(null, "icon");
                    Log.i("READFEED", "iconName: " + iconName);
                }
            }
        }

        //Instantiate the parser from input stream
        private XmlPullParser getXMLParser(InputStream in) throws XmlPullParserException, IOException{
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();
                Log.i("XPParser: ", parser.getName());
                return parser;
            } finally {
                in.close();
            }
        }

        //Download url and return input stream
        private InputStream downloadURL(String urlString) throws IOException{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            return conn.getInputStream();
        }

        //Get bitmap image
        private Bitmap getBitmapImage(){
            String urlString = "http://openweathermap.org/img/w/" + iconName + ".png";
            String fileName = iconName + ".png";

            Log.i(ACTIVITY_NAME, "file name: "+ fileName);

            if(fileExistance(fileName)){
                Log.i(ACTIVITY_NAME, "Image gotten from file " + fileName);
                return getDownloadedImage(fileName);
            } else {
                Log.i(ACTIVITY_NAME, "Image downloaded from url " + urlString);
                try {
                    saveBitmapToLocal();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return getImage(urlString);
            }
        }

        //Save Bitmap to Local Storage
        private void saveBitmapToLocal() throws IOException {
            String urlString = "http://openweathermap.org/img/w/" + iconName + ".png";
            weatherPic = getImage(urlString);
            Bitmap image  = getImage(urlString);
            FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
            outputStream.flush();
            outputStream.close();
        }

        //Check if image is already stored
        private boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            Log.i("fileExistance: ", file.exists()? fname + " file already downloaded": fname + "file not found, or not downloaded yet");
            return file.exists();
        }

        //Use image if already downloaded
        private Bitmap getDownloadedImage(String imagefile){
            FileInputStream fis = null;
            try {    fis = openFileInput(imagefile);   }
            catch (FileNotFoundException e) {    e.printStackTrace();  }
            Bitmap bm = BitmapFactory.decodeStream(fis);
            return bm;
        }


        //Download image   -code from: http://www.java2s.com/Code/Android/2D-Graphics/GetBitmapfromUrlwithHttpURLConnection.htm
        private Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    publishProgress(100);
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        private Bitmap getImage(String urlString) {
            try {
                URL url = new URL(urlString);
                return getImage(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }

        //Progress update
        @Override
        protected void onProgressUpdate(Integer ...value){

            Log.i(ACTIVITY_NAME, "in onProgressUpdate()");

            progressBar.setProgress(value[0]);
            progressBar.setVisibility(View.VISIBLE);
        }

        //Post Execute
        @Override
        protected void onPostExecute(String s){

            Log.i(ACTIVITY_NAME, "in onPostExecute()");

            progressBar.setProgress(0);
            progressBar.setVisibility(View.INVISIBLE);

            TextView minView = (TextView)findViewById(R.id.minTemperature);
            TextView maxView = (TextView)findViewById(R.id.maxTemperature);
            TextView currentView = (TextView)findViewById(R.id.currentTemperature);
            //ProgressBar progressBar = (ProgressBar)findViewById(R.id.weatherPBar);
            ImageView imageView = (ImageView)findViewById(R.id.weatherImage);

            imageView.setImageBitmap(getBitmapImage());

            minView.setText("Min temperature: " + min);
            maxView.setText("Max temperature: " + max);
            currentView.setText("Current temperature: " + current);



        }

    }
}
