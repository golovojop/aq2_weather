package j.s.yarlykov.data.domain;

import java.io.Serializable;
import java.util.Date;

public class Forecast implements Serializable {

    public static final int EMPTY_VAL = -1;

    protected int imgId;
    protected int temperature;
    protected float wind;
    protected int humidity;
    protected int pressureMm;
    private long timeStamp;

    public Forecast(int imgId, int temperature, float wind, int humidity, int pressureMm) {
        this.imgId = imgId;
        this.temperature = temperature;
        this.wind = Math.abs(wind);
        this.humidity = Math.abs(humidity);
        this.pressureMm = Math.abs(pressureMm);
        this.timeStamp = new Date().getTime();
    }

    public Forecast(int imgId, int temperature, float wind, int humidity, int pressureMm, long timeStamp) {
        this.imgId = imgId;
        this.temperature = temperature;
        this.wind = Math.abs(wind);
        this.humidity = Math.abs(humidity);
        this.pressureMm = Math.abs(pressureMm);
        this.timeStamp = timeStamp;
    }

    protected int mmToMb(int mm) {
        return (int)(mm * 1.333f);
    }
    protected int mbToMm(int mb) {
        return (int)(mb * 0.75006f);
    }
    public int getImgId() {
        return imgId;
    }
    public int getTemperature() {
        return temperature;
    }
    public float getWind() {
        return wind;
    }
    public int getHumidity() {
        return humidity;
    }
    public int getPressure(boolean isMm) {
        return isMm ? pressureMm : mmToMb(pressureMm);
    }
    public long getTimestamp() {
        return timeStamp;
    }
}
