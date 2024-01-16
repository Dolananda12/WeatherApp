package com.example.weatherapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class WeatherdataService extends AppCompatActivity {
      public WeatherdataServicelistener listener;
      private String city;
      private final static int CONNECT_TIME_OUT = 30000;
      private final static int READ_OUT_TIME = 50000;
      public interface WeatherdataServicelistener{
            void onRetrieval(String humidity,String Windspeed,String temp,String precip_mm,String city,String country,String lat,String lon,String localtime);
            void onFailure(String error);
      }
      Context context;
      public WeatherdataService(WeatherdataServicelistener listerner,String city_name,Context context){
            this.listener=listerner;
            this.city=city_name;
            this.context=context;
            HTTP_request();
      }
      public void HTTP_request(){
            String base_url="https://api.weatherapi.com/v1/current.json";
            Map<String,String> map=new HashMap<>();
            map.put("key","953f98de1c67473d945170444241601");
            map.put("q",city);
            try{
               post(base_url, (HashMap<String, String>) map,context);
            }catch (Exception e){
                  e.printStackTrace();
                  listener.onFailure("request failed");
            }
      }
      private void post(String url, HashMap<String, String> map, Context context) throws UnsupportedEncodingException {
            RequestQueue volleyQueue = Volley.newRequestQueue(context);
            StringBuilder urlBuilder = new StringBuilder(url);
            urlBuilder.append("?");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                  if (urlBuilder.length() > url.length() + 1) {
                        urlBuilder.append("&");
                  }
                  urlBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                  urlBuilder.append("=");
                  urlBuilder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            String finalUrl = urlBuilder.toString();
            System.out.println(finalUrl);
            JSONObject jsonParams = new JSONObject(map);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, finalUrl, null,
                    response -> {
                          try {
                                JSONObject locationobject=response.getJSONObject("location");
                                JSONObject current=response.getJSONObject("current");
                                String Windspeed = current.getString("wind_kph");
                                String temp = current.getString("temp_c");
                                String precip_mm = current.getString("precip_mm");
                                String city = locationobject.getString("name");
                                String country = locationobject.getString("country");
                                String lat = locationobject.getString("lat");
                                String lon = locationobject.getString("lon");
                                String humidity = current.getString("humidity");
                                String localtime = locationobject.getString("localtime");
                                listener.onRetrieval(humidity, Windspeed, temp, precip_mm, city, country, lat, lon, localtime);
                          } catch (JSONException e) {
                                e.printStackTrace();
                                listener.onFailure("Failed to get JSON response");
                          }
                    }, error -> {
                  error.printStackTrace();
                  listener.onFailure("No response");
            });

            volleyQueue.add(jsonObjectRequest);
      }

}

