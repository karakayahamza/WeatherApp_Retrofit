package com.example.weathertest.view;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.weathertest.R;
import com.example.weathertest.database.PlaceNamesDataBase;
import com.example.weathertest.database.PlacesDao;
import com.example.weathertest.databinding.FragmentMainBinding;
import com.example.weathertest.model.WeatherModel;
import com.example.weathertest.service.WeatherAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends Fragment {
    //Manifest Application inside android:usesCleartextTraffic="true" add research it.
    //Date currentTime = Calendar.getInstance().getTime();
    FragmentMainBinding binding;
    ImageView setImageResource;
    String time;
    CompositeDisposable compositeDisposable;
    WeatherAPI weatherAPI;
    private PlaceNamesDataBase dataBase;
    private PlacesDao placesDao;
    static WeatherModel weatherModel2;

    public static MainFragment newInstance(String cityName) {
        return newInstance(MainActivity.FRAGMENT_TAG_ARG,cityName);
    }

    public static MainFragment newInstance(String cityName,WeatherModel weatherModel) {
        weatherModel2 = weatherModel;
        return newInstance(MainActivity.FRAGMENT_TAG_ARG,cityName);
    }

    public static MainFragment newInstance(String tag,String cityName) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("cityname",cityName);
        args.putString(MainActivity.FRAGMENT_TAG_ARG,tag + "_" + fragment.hashCode());
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        binding = FragmentMainBinding.inflate(inflater,container,false);

        Gson gson = new GsonBuilder().setLenient().create();

        weatherAPI = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(WeatherAPI.class);

        dataBase = Room.databaseBuilder(getContext(),PlaceNamesDataBase.class,"Places")
                .allowMainThreadQueries()
                .build();
        placesDao = dataBase.placesDao();
        compositeDisposable = new CompositeDisposable();

        loadData(getArguments().getString("cityname"),MainActivity.AppId);

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadData(String cityname, String AppId){
       // WeatherAPI service = retrofit.create(WeatherAPI.class);
        handleresponse(cityname);
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(weatherAPI.getData(cityname,AppId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<WeatherModel>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onNext(@NonNull WeatherModel weatherModel) {
                        setAllData(weatherModel);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handleresponse(cityname);
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleresponse(String cityName){
        // Deleting 'PROVINCE' word from json data

        compositeDisposable.add(placesDao.getByCityName(cityName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<WeatherModel>() {
                    @Override
                    public void onSuccess(@NonNull WeatherModel weatherModels) {
                        setAllData(weatherModels);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                }));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setAllData(WeatherModel weatherModels){
        String cityName = weatherModels.city.name.toUpperCase();
        if (cityName.contains("PROVINCE")){
            String target= String.copyValueOf("PROVINCE".toCharArray());
            cityName=cityName.replace(target, "");
        }
        binding.cityName.setText(cityName);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE");
        LocalDateTime now = LocalDateTime.now();
        binding.timeroot.setText(dtf.format(now));

        for (int i = 0;i<6;i++){
            String icon = weatherModels.list.get(i).weather.get(0).icon;
            Integer temp = (int) ((weatherModels.list.get(i).main.temp)-273.15);
            time = weatherModels.list.get(i).dt_txt;

            @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("HH:mm");

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

    public void setImage(String iconid,int where,Integer temp,String time){
        switch (where){
            case 0:
                setImageResource = binding.icon0;
                binding.temp0.setText(temp.toString()+"°");
                break;
            case 1:
                setImageResource = binding.icon1;
                binding.temp1.setText(temp.toString()+"°");
                binding.time1.setText(time);
                break;
            case 2:
                setImageResource = binding.icon22;
                binding.temp2.setText(temp.toString()+"°");
                binding.time2.setText(time);
                break;
            case 3:
                setImageResource = binding.icon3;
                binding.temp3.setText(temp.toString()+"°");
                binding.time3.setText(time);
                break;
            case 4:
                setImageResource = binding.icon4;
                binding.temp4.setText(temp.toString()+"°");
                binding.time4.setText(time);
                break;
            case 5:
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
                setImageResource.setImageResource(R.drawable.night);
                break;
            case "02d":
                setImageResource.setImageResource(R.drawable.partlycloudyday);
                break;
            case "02n":
                setImageResource.setImageResource(R.drawable.partlycloudydaynight);
                break;
            case "03d":
            case "03n":
                setImageResource.setImageResource(R.drawable.cloud);
            case "04d":
            case "04n":
                setImageResource.setImageResource(R.drawable.cloudy);
                break;
            case "09d":
            case "09n":
                setImageResource.setImageResource(R.drawable.freezingrain);
                break;
            case "10d":
                setImageResource.setImageResource(R.drawable.heavyrainswrsday);
                break;
            case "10n":
                setImageResource.setImageResource(R.drawable.heavyrainswrsdaynight);
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