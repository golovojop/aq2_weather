package j.s.yarlykov.data.domain;

public class CityForecast extends Forecast {
    private String city;

    public CityForecast(String city, int imgId, int temperature, int wind, int humidity, float pressureMm) {
        super(imgId, temperature, wind, humidity, pressureMm);
        this.city = city;
    }

    public CityForecast(String city, Forecast forecast) {
        super(forecast.imgId, forecast.temperature, forecast.wind, forecast.humidity, forecast.pressureMm);
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}
