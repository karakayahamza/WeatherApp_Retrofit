package com.example.weathertest.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WeatherModel {

    @SerializedName("cod")
    public String cod;
    @SerializedName("message")
    public int message;
    @SerializedName("cnt")
    public int cnt;
    @SerializedName("list")
    public List<list> list;
    @SerializedName("city")
    public City city;

    public class Main{
        @SerializedName("temp")
        public double temp;
        @SerializedName("feels_like")
        public double feels_like;
        @SerializedName("temp_min")
        public double temp_min;
        @SerializedName("temp_max")
        public double temp_max;
        @SerializedName("pressure")
        public int pressure;
        @SerializedName("sea_level")
        public int sea_level;
        @SerializedName("humidity")
        public int humidity;
        @SerializedName("temp_kf")
        public double temp_kf;
    }

    public class Weather{
        @SerializedName("id")
        public int id;
        @SerializedName("main")
        public String main;
        @SerializedName("description")
        public String description;
        @SerializedName("icon")
        public String icon;
    }

    public class Clouds{
        @SerializedName("all")
        public int all;
    }

    public class Wind{
        @SerializedName("speed")
        public double speed;
        @SerializedName("deg")
        public int deg;
        @SerializedName("gust")
        public double gust;
    }

    public class Sys{
        @SerializedName("pod")
        public String pod;
    }

    public class Rain{
        @SerializedName("3h")
        public double _3h;
    }

    public class list{
        @SerializedName("dt")
        public int dt;
        @SerializedName("main")
        public Main main;
        @SerializedName("weather")
        public List<Weather> weather;
        @SerializedName("clouds")
        public Clouds clouds;
        @SerializedName("wind")
        public Wind wind;
        @SerializedName("visibility")
        public int visibility;
        @SerializedName("pop")
        public double pop;
        @SerializedName("sys")
        public Sys sys;
        @SerializedName("dt_txt")
        public String dt_txt;
        @SerializedName("rain")
        public Rain rain;
    }

    public class Coord{
        @SerializedName("lon")
        public double lon;
        @SerializedName("lat")
        public double lat;
    }

    public class City{
        public int id;
        @SerializedName("name")
        public String name;
        public Coord coord;
        public String country;
        public int population;
        public int timezone;
        public int sunrise;
        public int sunset;
    }



/////////////////////////////////////////












}
