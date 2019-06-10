package j.s.yarlykov.data.domain;

import java.io.Serializable;

public class CityForecast implements Serializable {

   private int id;
   private String city;
   private String country;
   private int temperature;
   private int icon;
   private float wind;
   private int humidity;
   private int pressure;

    public CityForecast(String city, String country, int temperature, int icon, float wind, int humidity, int pressure) {
        this.id = 0;
        this.city = city;
        this.country = country;
        this.temperature = temperature;
        this.icon = icon;
        this.wind = wind;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public int getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getIcon() {
        return icon;
    }

    public float getWind() {
        return wind;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public int getPressure(boolean isMm) {
        return isMm ? mbToMm(pressure) : pressure;
    }

    protected int mmToMb(int mm) {
        return (int)(mm * 1.333f);
    }
    protected int mbToMm(int mb) {
        return (int)(mb * 0.75006f);
    }
}