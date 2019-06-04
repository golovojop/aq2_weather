package j.s.yarlykov.data.domain;

import java.util.Set;

public class CityForecast extends Forecast {

    public enum MeteoData {
        WIND, HUMIDITY, PRESSURE
    }

    private String city;

    public CityForecast(String city, int imgId, int temperature, float wind, int humidity, int pressureMm) {
        super(imgId, temperature, wind, humidity, pressureMm);
        this.city = city;
    }

    public CityForecast(String city,
                        int imgId,
                        int temperature,
                        float wind,
                        int humidity,
                        int pressureMm,
                        long timeStamp) {
        super(imgId, temperature, wind, humidity, pressureMm, timeStamp);
        this.city = city;
    }

    public CityForecast(String city, Forecast forecast) {
        super(forecast.imgId,
                forecast.temperature,
                forecast.wind,
                forecast.humidity,
                forecast.pressureMm);
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    @Override
    public int getPressure(boolean isMm) {
        if(pressureMm != EMPTY_VAL) {
            return isMm ? mbToMm(pressureMm) : pressureMm;
        }
        return EMPTY_VAL;
    }

    /**
     * TODO: Удалить не требуемые данные
     */
    public CityForecast clearUnused(Set<MeteoData> interestingSet) {
        if(!interestingSet.contains(MeteoData.WIND)) {
            wind = EMPTY_VAL;
        }
        if(!interestingSet.contains(MeteoData.HUMIDITY)) {
            humidity = EMPTY_VAL;
        }
        if(!interestingSet.contains(MeteoData.PRESSURE)) {
            pressureMm = EMPTY_VAL;
        }
        return this;
    }
}