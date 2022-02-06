package com.example.weathertest.view;
//@HK KRKY
//https://www.linkedin.com/in/hamza-karakaya-684a101b6/

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.weathertest.R;
import com.example.weathertest.adapter.CustomPagerAdapter;
import com.example.weathertest.adapter.RecyclerAdapter;
import com.example.weathertest.database.PlaceNamesDataBase;
import com.example.weathertest.database.PlacesDao;
import com.example.weathertest.databinding.ActivityMainBinding;
import com.example.weathertest.model.WeatherModel;
import com.example.weathertest.service.WeatherAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends FragmentActivity {

        public static final String BASE_URL ="https://api.openweathermap.org/data/2.5/";
        public static final String AppId ="61e8b0259c092b1b9a15474cd800ee25";
        public static final String FRAGMENT_TAG_ARG = "tag";
        private WeatherAPI weatherAPI;
        private CompositeDisposable compositeDisposable;
        private PlaceNamesDataBase dataBase;
        private PlacesDao placesDao;
        private ActivityMainBinding binding;
        private boolean viewPagerState=false;
        CustomPagerAdapter mCustomPagerAdapter;
        ViewPager mViewPager;


    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Gson gson = new GsonBuilder().setLenient().create();

        weatherAPI = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(WeatherAPI.class);

        dataBase = Room.databaseBuilder(getApplicationContext(),PlaceNamesDataBase.class,"Places")
                .allowMainThreadQueries()
                .build();
        placesDao = dataBase.placesDao();


        mCustomPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        mViewPager =findViewById(R.id.container);//Add fragment
        mViewPager.setAdapter(mCustomPagerAdapter);
        mCustomPagerAdapter.addPage(MainFragment.newInstance("Izmir"));

        /**compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(placesDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(MainActivity.this::TEST));*/



       /**recyclerAdapter = new RecyclerAdapter();

       recyclerAdapter.setWeatherModelList(weatherModelList);
       binding.container.setAdapter(recyclerAdapter);*/


        binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


            mCustomPagerAdapter.removePage(mViewPager.getCurrentItem());
            binding.refresh.setRefreshing(false);
            Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_LONG).show();
            }
        });

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(placesDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<WeatherModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<WeatherModel> weatherModels) {
                        for (WeatherModel as : weatherModels){
                            mCustomPagerAdapter.addPage(MainFragment.newInstance(as.city.name,as));
                            //mViewPager.invalidate();
                        }

                        mCustomPagerAdapter.getItemPosition(mViewPager.getCurrentItem());
                        System.out.println("*******");

                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                }));

    }


   /** private void TEST(List<WeatherModel> weatherModels){
        for (WeatherModel as : weatherModels){
            weatherModelList.add(as);
            recyclerAdapter.notifyItemInserted(weatherModelList.size()-1);
            System.out.println(as.city.name+"*************");
        }
    }*/

    private void addToDatabase(WeatherModel weatherModel){
        //placesDao.insert(weatherModel);
        compositeDisposable.add(placesDao.insert(weatherModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
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
        //WeatherAPI service = retrofit.create(WeatherAPI.class);

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(weatherAPI.getData(cityname,AppId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<WeatherModel>(){
                    @Override
                    public void onComplete() {
                        //addNewPlaceView(cityname);

                    }
                    @Override
                    public void onNext(@NonNull WeatherModel weatherModel) {
                        //System.out.println(new Gson().toJson(weatherModel));
                        addToDatabase(weatherModel);
                        mCustomPagerAdapter.addPage(MainFragment.newInstance(weatherModel.city.name));
                        //mViewPager.invalidate();
                        /**recyclerAdapter.notifyItemInserted(weatherModelList.size()-1);*/
                        /**weatherModelList.add(weatherModel);*/
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
