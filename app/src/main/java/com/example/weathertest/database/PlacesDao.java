package com.example.weathertest.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.weathertest.model.WeatherModel;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.rxjava3.core.Completable;

@Dao
public interface PlacesDao {

    @Query("SELECT * FROM Places")
    Flowable<List<WeatherModel>> getAll();

    @Insert
    io.reactivex.Completable insert(WeatherModel weatherModel);

    @Delete
    io.reactivex.Completable  delete(WeatherModel weatherModel);


}
