package j.s.yarlykov.data.provider;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import j.s.yarlykov.R;

public class ForecastProvider {

    private static ForecastProvider instance;
    public static ForecastProvider getInstance() {
        if (instance == null) {
            instance = new ForecastProvider();
        }
        return instance;
    }

    private List<Forecast> forecasts;
    private ForecastProvider() {
        forecasts = Arrays.asList(
                new Forecast(R.drawable.rain, 12, 10, 89, 750),
                new Forecast(R.drawable.sunny, 25, 5, 73, 771),
                new Forecast(R.drawable.sun, 18, 4, 85, 769),
                new Forecast(R.drawable.snow, -3, 1, 59, 741),
                new Forecast(R.drawable.cloud2, 7, 2, 51, 749),
                new Forecast(R.drawable.cloud1, 11, 6, 64, 742)
        );
    }

    class Forecast implements Serializable {

        private int imgId;
        protected int temperature;
        private float wind;
        private int humidity;
        private int pressureMm;
        private long timeStamp;

        public Forecast(int imgId, int temperature, float wind, int humidity, int pressureMm) {
            this.imgId = imgId;
            this.temperature = temperature;
            this.wind = Math.abs(wind);
            this.humidity = Math.abs(humidity);
            this.pressureMm = Math.abs(pressureMm);
            this.timeStamp = new Date().getTime();
        }

        protected int mmToMb(int mm) {
            return (int)(mm * 1.333f);
        }
    }
}
