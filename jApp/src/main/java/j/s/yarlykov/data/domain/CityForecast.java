package j.s.yarlykov.data.domain;

import java.util.Locale;
import java.util.Set;

public class CityForecast extends Forecast {

    public static enum MeteoData {
        WIND, HUMIDITY, PRESSURE
    }

    private String city;

    public CityForecast(String city, int imgId, int temperature, int wind, int humidity, int pressureMm) {
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

    @Override
    public float getPressure(boolean isMm) {
        if(pressureMm != EMPTY_VAL) {
            return isMm ? pressureMm : mmToMb(pressureMm);
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

    public String toString(){
        return String.format(Locale.US,"City %s, t=%d, w=%d, h=%d, p=%d",
                city, temperature, wind, humidity, pressureMm);
    }
}