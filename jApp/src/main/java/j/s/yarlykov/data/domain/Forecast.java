package j.s.yarlykov.data.domain;

import java.io.Serializable;

public class Forecast implements Serializable {

    public static final int EMPTY_VAL = -1;

    protected int imgId;
    protected int temperature;
    protected int wind;
    protected int humidity;
    protected float pressureMm;

    public Forecast(int imgId, int temperature, int wind, int humidity, float pressureMm) {
        this.imgId = imgId;
        this.temperature = temperature;
        this.wind = Math.abs(wind);
        this.humidity = Math.abs(humidity);
        this.pressureMm = Math.abs(pressureMm);
    }

    protected float mmToMb(float mm) {
        return mm * 1.333f;
    }
    protected float mbToMm(float mb) {
        return mb * 0.75006f;
    }
    public int getImgId() {
        return imgId;
    }
    public int getTemperature() {
        return temperature;
    }
    public int getWind() {
        return wind;
    }
    public int getHumidity() {
        return humidity;
    }
    public float getPressure(boolean isMm) {
        return isMm ? pressureMm : mmToMb(pressureMm);
    }
}