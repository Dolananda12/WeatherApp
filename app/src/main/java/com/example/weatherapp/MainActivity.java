package com.example.weatherapp;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.*;
import org.json.JSONException;
import org.json.JSONObject;
public class MainActivity extends AppCompatActivity implements WeatherdataService.WeatherdataServicelistener {

    private EditText city_name;
    private Button Submit;
    private TextView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city_name=findViewById(R.id.city_name);
        Submit=findViewById(R.id.submit);
        view=findViewById(R.id.conditions);
        Submit.setOnClickListener(v -> {
            WeatherdataService weatherdataService=new WeatherdataService(MainActivity.this,city_name.getText().toString(),MainActivity.this);
        });
    }

    @Override
    public void onRetrieval(String humidity, String Windspeed, String temp, String precip_mm, String city, String country, String lat, String lon, String localtime) {
        city_name.setText("");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("humidity:" + humidity + "\n");
        stringBuilder.append("wind speed(kph):" + Windspeed + "\n");
        stringBuilder.append("temp:" + temp + "\n");
        stringBuilder.append("precipitation(mm):" + precip_mm + "\n");
        stringBuilder.append("city:" + city + "\n");
        stringBuilder.append("country:" + country + "\n");
        stringBuilder.append("latitude:" + lat + "\n");
        stringBuilder.append("longitude:" + lon + "\n");
        view.setText(stringBuilder.toString());
    }


    @Override
    public void onFailure(String error) {
        System.out.println(error);
    }
}