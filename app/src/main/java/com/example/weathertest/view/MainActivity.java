package com.example.weathertest.view;
//@HK KRKY
//https://www.linkedin.com/in/hamza-karakaya-684a101b6/

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.weathertest.R;
import com.example.weathertest.adapter.CustomPagerAdapter;
import com.example.weathertest.database.PlaceNamesDataBase;
import com.example.weathertest.database.PlacesDao;
import com.example.weathertest.databinding.ActivityMainBinding;
import com.example.weathertest.model.WeatherModel;
import com.example.weathertest.service.WeatherAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends FragmentActivity {

        public static final String BASE_URL ="https://api.openweathermap.org/data/2.5/";
        public static final String AppId ="61e8b0259c092b1b9a15474cd800ee25";
        public static final String FRAGMENT_TAG_ARG = "tag";
        private Retrofit retrofit;
        private CompositeDisposable compositeDisposable;
        private PlaceNamesDataBase dataBase;
        private PlacesDao placesDao;
        private ActivityMainBinding binding;
        private CustomPagerAdapter mCustomPagerAdapter;
        private  ViewPager mViewPager;
        private boolean viewPagerState=false;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        dataBase = Room.databaseBuilder(getApplicationContext(),PlaceNamesDataBase.class,"Places")
                .allowMainThreadQueries()
                .build();
        placesDao = dataBase.placesDao();


        mCustomPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        mViewPager =findViewById(R.id.container);//Add fragment
        mViewPager.setAdapter(mCustomPagerAdapter);
        mCustomPagerAdapter.addPage(MainFragment.newInstance("Izmir"));
        //here

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(placesDao.getAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(MainActivity.this::handleResponse));
    }

    private void addToDatabase(WeatherModel weatherModel){
        //placesDao.insert(weatherModel);
        compositeDisposable.add(placesDao.insert(weatherModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    private void handleResponse(List<WeatherModel> weatherModels) {
        for (WeatherModel as : weatherModels){
            addNewPlaceView(as.city.name);
        }
        compositeDisposable.clear();
    }

    public void addNewPlaceView(String cityName){
            mCustomPagerAdapter.addPage(MainFragment.newInstance(cityName));
           //hideSoftKeyboard(MainActivity.this);
           mCustomPagerAdapter.notifyDataSetChanged();
           if(viewPagerState){
               mViewPager.setCurrentItem(mCustomPagerAdapter.getCount());
           }
   }

   public void addButton(View view){
       AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
       LayoutInflater layoutInflater = MainActivity.this.getLayoutInflater();

       View dialogView= layoutInflater.inflate(R.layout.alerdialog_design,null);
       final EditText editText = dialogView.findViewById(R.id.placeName);
       alertDialog.setView(dialogView);
       Button button = dialogView.findViewById(R.id.button);
       AlertDialog dialog = alertDialog.create();
       dialog.show();
       dialog.getWindow().setBackgroundDrawable(null);

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (mViewPager.getAdapter().getCount()<10){
                   String cityname = editText.getText().toString();
                   addNewPlace(cityname);
                   dialog.dismiss();
                   viewPagerState = true;
               }
               else
               Toast.makeText(MainActivity.this,"Size exceeded. Please delete one of the registered places.",Toast.LENGTH_LONG).show();
           }
       });
   }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    public void addNewPlace(String cityname){
        WeatherAPI service = retrofit.create(WeatherAPI.class);

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(service.getData(cityname,AppId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<WeatherModel>(){
                    @Override
                    public void onComplete() {
                        addNewPlaceView(cityname);
                    }
                    @Override
                    public void onNext(@NonNull WeatherModel weatherModel) {
                        //System.out.println(new Gson().toJson(weatherModel));
                        addToDatabase(weatherModel);
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this,"Invalid place name",Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
