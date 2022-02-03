package com.example.weathertest.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import java.util.List;

@Entity(tableName = "Places")
public class WeatherModel  {

    @PrimaryKey(autoGenerate = true)
    public int uuid = 0;

    @ColumnInfo(name = "cod")
    @SerializedName("cod")
    public String cod;
    @ColumnInfo(name = "message")
    @SerializedName("message")
    public int message;
    @ColumnInfo(name = "cnt")
    @SerializedName("cnt")
    public int cnt;
    @SerializedName("list")
    @ColumnInfo(name = "list")
    public List<list> list;
    @ColumnInfo(name = "city")
    @SerializedName("city")
    public City city;

    public class Main{
        @ColumnInfo(name = "temp")
        @SerializedName("temp")
        public double temp;
        @ColumnInfo(name = "feels_like")
        @SerializedName("feels_like")
        public double feels_like;
        @ColumnInfo(name = "temp_min")
        @SerializedName("temp_min")
        public double temp_min;
        @ColumnInfo(name = "temp_max")
        @SerializedName("temp_max")
        public double temp_max;
        @ColumnInfo(name = "pressure")
        @SerializedName("pressure")
        public int pressure;
        @ColumnInfo(name = "sea_level")
        @SerializedName("sea_level")
        public int sea_level;
        @ColumnInfo(name = "humidity")
        @SerializedName("humidity")
        public int humidity;
        @ColumnInfo(name = "temp_kf")
        @SerializedName("temp_kf")
        public double temp_kf;
    }

    public class Weather{
        @ColumnInfo(name = "id")
        @SerializedName("id")
        public int id;
        @ColumnInfo(name = "main")
        @SerializedName("main")
        public String main;
        @ColumnInfo(name = "description")
        @SerializedName("description")
        public String description;
        @ColumnInfo(name = "icon")
        @SerializedName("icon")
        public String icon;
    }

    public class Clouds{
        @ColumnInfo(name = "all")
        @SerializedName("all")
        public int all;
    }

    public class Wind{
        @ColumnInfo(name = "speed")
        @SerializedName("speed")
        public double speed;
        @ColumnInfo(name = "deg")
        @SerializedName("deg")
        public int deg;
        @ColumnInfo(name = "gust")
        @SerializedName("gust")
        public double gust;
    }

    public class Sys{
        @ColumnInfo(name = "pod")
        @SerializedName("pod")
        public String pod;
    }

    public class Rain{
        @ColumnInfo(name = "_3h")
        @SerializedName("3h")
        public double _3h;
    }

    public class list{
        @ColumnInfo(name = "dt")
        @SerializedName("dt")
        public int dt;
        @ColumnInfo(name = "main")
        @SerializedName("main")
        public Main main;
        @ColumnInfo(name = "weather")
        @SerializedName("weather")
        public List<Weather> weather;
        @ColumnInfo(name = "clouds")
        @SerializedName("clouds")
        public Clouds clouds;
        @ColumnInfo(name = "wind")
        @SerializedName("wind")
        public Wind wind;
        @ColumnInfo(name = "visibility")
        @SerializedName("visibility")
        public int visibility;
        @ColumnInfo(name = "pop")
        @SerializedName("pop")
        public double pop;
        @ColumnInfo(name = "sys")
        @SerializedName("sys")
        public Sys sys;
        @ColumnInfo(name = "dt_txt")
        @SerializedName("dt_txt")
        public String dt_txt;
        @ColumnInfo(name = "rain")
        @SerializedName("rain")
        public Rain rain;
    }

    public class Coord{
        @ColumnInfo(name = "lon")
        @SerializedName("lon")
        public double lon;
        @ColumnInfo(name = "lat")
        @SerializedName("lat")
        public double lat;
    }

    public class City{
        public int id;
        @ColumnInfo(name = "name")
        @SerializedName("name")
        public String name;
        public Coord coord;
        public String country;
        public int population;
        public int timezone;
        public int sunrise;
        public int sunset;
    }

    public WeatherModel(String cod, int message, int cnt, List<WeatherModel.list> list, City city) {
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
        this.list = list;
        this.city = city;
    }
}
