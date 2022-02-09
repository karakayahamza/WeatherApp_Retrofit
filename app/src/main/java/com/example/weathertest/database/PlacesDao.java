package com.example.weathertest.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weathertest.model.WeatherModel;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface PlacesDao {

    @Query("SELECT * FROM Places")
    Single<List<WeatherModel>> getAll();

    @Query("SELECT * FROM places WHERE instr(city,'name'||char(34)||':'||char(34)||:cityname||char(34)) > 0")
    Single<WeatherModel> getByCityName(String cityname);

    @Insert
    io.reactivex.Completable insert(WeatherModel weatherModel);

    @Query("UPDATE Places SET city=:city,list=:list1 WHERE instr(city,'name'||char(34)||':'||char(34)||:cityname||char(34)) > 0")
    io.reactivex.Completable upDate(WeatherModel.City city,List<WeatherModel.list> list1,String cityname);
                //,List<WeatherModel.list> list
//
    /*@Update(entity = WeatherModel.class)
    io.reactivex.Completable upDate(WeatherModel weatherModel);*/
}
