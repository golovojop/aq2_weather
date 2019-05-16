package j.s.yarlykov.data.domain;

public class History {

    private String date, temperature;
    private int img;

    public History(String date, String temperature, int img) {
        this.date = date;
        this.temperature = temperature;
        this.img = img;
    }

    public String getDate() {
        return date;
    }

    public String getTemperature() {
        return temperature;
    }

    public int getImg() {
        return img;
    }
}
