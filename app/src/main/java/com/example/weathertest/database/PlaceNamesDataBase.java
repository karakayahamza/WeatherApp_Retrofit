package com.example.weathertest.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.weathertest.converter.Converters;
import com.example.weathertest.model.WeatherModel;

@Database(entities = {WeatherModel.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class PlaceNamesDataBase extends RoomDatabase {
    public abstract PlacesDao placesDao();
}
