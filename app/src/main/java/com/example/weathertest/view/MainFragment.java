package com.example.weathertest.view;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weathertest.R;


import com.example.weathertest.databinding.FragmentMainBinding;
import com.example.weathertest.model.WeatherModel;
import com.example.weathertest.service.WeatherAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends Fragment {
    private static String cityname;
    //Manifest Application inside android:usesCleartextTraffic="true" add research it.
    //Date currentTime = Calendar.getInstance().getTime();
    FragmentMainBinding binding;
    String BASE_URL ="https://api.openweathermap.org/data/2.5/";
    String AppId ="61e8b0259c092b1b9a15474cd800ee25";
    Retrofit retrofit;
    ImageView setImageResource;
    WeatherModel weatherModel;
    String time;


    public static MainFragment newInstance(String cityName) {
        return newInstance(MainActivity.FRAGMENT_TAG_ARG,cityName);
    }

    public static MainFragment newInstance(String tag,String cityName) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("cityname",cityName);
        args.putString(MainActivity.FRAGMENT_TAG_ARG, tag + "_" + fragment.hashCode());
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        binding = FragmentMainBinding.inflate(inflater,container,false);

        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        loadData(getArguments().getString("cityname"),AppId);

        return binding.getRoot();
    }

    public void loadData(String cityname,String Appid) {
        WeatherAPI service = retrofit.create(WeatherAPI.class);

        Call<WeatherModel> call = service.getData(cityname,Appid);
        call.enqueue(new Callback<WeatherModel>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                if (response.isSuccessful()) {
                    weatherModel = response.body();

                    // delete 'PROVINCE' in json
                    String cityName = weatherModel.city.name.toUpperCase();
                    if (cityName.contains("PROVINCE")){
                        String target=cityName.copyValueOf("PROVINCE".toCharArray());
                        cityName=cityName.replace(target, "");
                    }
                    binding.cityName.setText(cityName);

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
                        time = weatherModel.list.get(i).dt_txt;

                        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        SimpleDateFormat output = new SimpleDateFormat("HH:mm");

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
                Toast.makeText(getContext(), "Invalid city name.", Toast.LENGTH_LONG).show();
                System.out.println("Eroor");
            }
        });

        //SharedPreferences.Editor editor = sharedPref.edit();
        //editor.putString("cityName",cityname);
        //editor.commit();
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
            case "01n":
                setImageResource.setImageResource(R.drawable.sunny);
                break;
            case "02d":
            case "02n":
                setImageResource.setImageResource(R.drawable.partlycloudyday);
                break;
            case "03d":
            case "03n":
            case "04d":
            case "04n":
                setImageResource.setImageResource(R.drawable.cloudy);
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