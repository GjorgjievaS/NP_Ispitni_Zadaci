package Мерна_Станица;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.*;


class Measurement implements Comparable<Measurement> {
    float temperature;
    float wind;
    float humidity;
    float visibility;
    Date date;

    public Measurement(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    public float getTemperature() {
        return temperature;
    }

    public Date getDate() {
        return date;
    }


    @Override
    public int compareTo(Measurement o) {
        long t1 = this.date.getTime() / 1000;
        long t2 = o.date.getTime() / 1000;

        if (Math.abs(t1 - t2) < 150) {
            return 0;
        }
        return this.date.compareTo(o.date);
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz YYYY");
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s",
                temperature, wind, humidity, visibility, df.format(date));
    }

}

class WeatherStation{
    int days;
    Set<Measurement> measurements;

    public WeatherStation(int days){
        this.days = days;
        this.measurements = new TreeSet<>();
    }

    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date){
        measurements.add(new Measurement(temperature, wind, humidity, visibility, date));

        Iterator<Measurement> it = measurements.iterator();
        while (it.hasNext()) {
            Measurement temp = it.next();
            long t1  =temp.date.getMonth()*31+temp.date.getDate();
            long t2 = date.getMonth()*31+date.getDate();
            if (t2 - t1 >= days) {
                it.remove();
            }
        }
    }

    public int total(){
        return measurements.size();
    }

    public void status(Date from, Date to){
        double sum=0;
        int count=0;

        for (Measurement m : measurements){

            if (!(m.date.before(from) || m.date.after(to))) {

                System.out.println(m);
                sum += m.getTemperature();
                count++;
            }
        }

        if (count == 0)
            throw new RuntimeException();
        else
            System.out.printf("Average temperature: %.2f", sum/count);
    }
}

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}