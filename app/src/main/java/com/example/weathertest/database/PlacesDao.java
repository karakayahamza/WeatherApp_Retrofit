package com.example.weathertest.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weathertest.model.WeatherModel;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.rxjava3.core.Completable;

@Dao
public interface PlacesDao {

    @Query("SELECT * FROM Places")
    Single<List<WeatherModel>> getAll();

    @Query("SELECT * FROM places WHERE instr(city,'name'||char(34)||':'||char(34)||:cityname||char(34)) > 0")
    Single <WeatherModel> getByCityName(String cityname);

    @Insert
    io.reactivex.Completable insert(WeatherModel weatherModel);

    @Delete
    io.reactivex.Completable  delete(WeatherModel weatherModel);

    @Update
    io.reactivex.Completable  upDate(WeatherModel weatherModel);
}
