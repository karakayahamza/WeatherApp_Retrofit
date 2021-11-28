package com.example.weathertest.view;
//@HK KRKY
//https://www.linkedin.com/in/hamza-karakaya-684a101b6/
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weathertest.R;
import com.example.weathertest.databinding.ActivityMainBinding;
import com.example.weathertest.model.WeatherModel;
import com.example.weathertest.service.WeatherAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    //Manifest Application inside android:usesCleartextTraffic="true" add research it.
    //Date currentTime = Calendar.getInstance().getTime();
    String BASE_URL ="https://api.openweathermap.org/data/2.5/";
    String AppId ="61e8b0259c092b1b9a15474cd800ee25";
    Retrofit retrofit;
    ActivityMainBinding binding;
    ImageView setImageResource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);



            Gson gson = new GsonBuilder().setLenient().create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();



            binding.searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadData(binding.citynametext.getText().toString(),AppId);
                    binding.citynametext.clearFocus();
                }
            });



    }

    public void loadData(String cityname,String Appid) {
        WeatherAPI service = retrofit.create(WeatherAPI.class);

        Call<WeatherModel> call = service.getData(cityname,Appid);
        call.enqueue(new Callback<WeatherModel>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                if (response.isSuccessful()) {
                    WeatherModel weatherModel = response.body();
                    binding.cityName.setText(weatherModel.city.name.toUpperCase());

                    String iconf0 = weatherModel.list.get(0).weather.get(0).icon;
                    Integer tempf0 = (int) ((weatherModel.list.get(0).main.temp)-273.15);
                    String timef0 = weatherModel.list.get(0).dt_txt;
                    setImage(iconf0,0,tempf0,timef0);

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE");
                    LocalDateTime now = LocalDateTime.now();
                    binding.timeroot.setText(dtf.format(now));

                    for (int i = 3;i<8;i++){
                        String icon = weatherModel.list.get(i).weather.get(0).icon;
                        Integer temp = (int) ((weatherModel.list.get(i).main.temp)-273.15);
                        String time = weatherModel.list.get(i).dt_txt;

                        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                        SimpleDateFormat output = new SimpleDateFormat("hh:mm");
                        //SimpleDateFormat output = new SimpleDateFormat("EEEE");
//
                        try {
                            Date t = input.parse(time);
                            time=output.format(t);
                        }
                        catch (Exception e){
                            System.out.println(e);
                        }
                        setImage(icon,i,temp,time);

                    }
                }
            }
            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Invalide city name.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setImage(String iconid,int where,Integer temp,String time){
        switch (where){
            case 0:
                setImageResource = binding.icon0;
                binding.temp0.setText(temp.toString()+"°");
                break;
            case 3:
                setImageResource = binding.icon1;
                binding.temp1.setText(temp.toString()+"°");
                binding.time1.setText(time);
                break;
            case 4:
                setImageResource = binding.icon22;
                binding.temp2.setText(temp.toString()+"°");
                binding.time2.setText(time);
                break;
            case 5:
                setImageResource = binding.icon3;
                binding.temp3.setText(temp.toString()+"°");
                binding.time3.setText(time);
                break;
            case 6:
                setImageResource = binding.icon4;
                binding.temp4.setText(temp.toString()+"°");
                binding.time4.setText(time);
                break;
            case 7:
                setImageResource = binding.icon;
                binding.temp5.setText(temp.toString()+"°");
                binding.time5.setText(time);
                break;

            default:
                break;

        }
        switch (iconid) {
            case "01d":
                setImageResource.setImageResource(R.drawable.sunny);
                break;
            case "01n":
                setImageResource.setImageResource(R.drawable.clear);
                break;
            case "02d":
            case "02n":
                setImageResource.setImageResource(R.drawable.partlycloudyday);
                break;
            case "03d":
            case "03n":
                setImageResource.setImageResource(R.drawable.cloudy);
                break;
            case "04d":
            case "04n":
                setImageResource.setImageResource(R.drawable.overcast);
                break;
            case "09d":
            case "09n":
                setImageResource.setImageResource(R.drawable.freezingrain);
                break;
            case "10d":
            case "10n":
                setImageResource.setImageResource(R.drawable.heavyrainswrsday);
                break;
            case "11d":
            case "11n":
                setImageResource.setImageResource(R.drawable.cloudrainthunder);
                break;
            case "13d":
            case "13n":
                setImageResource.setImageResource(R.drawable.occlightsnow);
                break;
            case "50d":
            case "50n":
                setImageResource.setImageResource(R.drawable.freezingfog);
                break;
            default:
                setImageResource.setImageResource(R.drawable.ic_launcher_background);
        }
    }

}

