package com.example.weathertest.converter;

import androidx.room.TypeConverter;

import com.example.weathertest.model.WeatherModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public String fromWeatherModel_listList(List<WeatherModel.list> weatherModels) {
        if (weatherModels == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<WeatherModel.list>>() {}.getType();
        String json = gson.toJson(weatherModels, type);
        return json;
    }

    @TypeConverter
    public List<WeatherModel.list> toWeatherModel_listList(String countryLangString) {
        if (countryLangString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<WeatherModel.list>>() {}.getType();
        List<WeatherModel.list> countryLangList = gson.fromJson(countryLangString, type);
        return countryLangList;
    }

    @TypeConverter
    public String fromCityLangList(WeatherModel.City weatherModels) {
        if (weatherModels == null) {
            return (null);
        }
        Gson gson2 = new Gson();
        Type type2 = new TypeToken<WeatherModel.City>() {}.getType();
        String json2 = gson2.toJson(weatherModels, type2);
        return json2;
    }

    @TypeConverter
    public WeatherModel.City toCityLangList(String countryLangString) {
        if (countryLangString == null) {
            return (null);
        }
        Gson gson2 = new Gson();
        Type type2 = new TypeToken<WeatherModel.City>() {}.getType();
        WeatherModel.City countryLangList1 = gson2.fromJson(countryLangString, type2);
        return countryLangList1;
    }
}