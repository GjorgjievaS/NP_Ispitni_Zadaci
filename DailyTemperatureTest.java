//package DailyTemperatures;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;


class Day{
    private int day;
    private List<Double> temperatures;
    private char scale;

    public Day(int day, List<Double> temperatures, char scale) {
        this.day = day;
        this.temperatures = temperatures;
        this.scale = scale;
    }

    public int getDay() {
        return day;
    }

    public List<Double> getTemperatures() {
        return temperatures;
    }

    public char getScale() {
        return scale;
    }

    public double getMinTemp(){
        return temperatures.stream().min(Double::compareTo).get();
    }

    public double getMaxTemp(){
        return temperatures.stream().max(Double::compareTo).get();
    }

    public double getAvgTemp(){
        return temperatures.stream().mapToDouble(t->t).sum()/(temperatures.size()*1.0);
    }

    public void convertToC(){
        for (int i=0;i<temperatures.size();i++){
            temperatures.set(i, ((temperatures.get(i)-32)*5)/9);
        }
        this.scale = 'C';
    }

    public void convertToF(){
        for (int i=0;i<temperatures.size();i++){
            temperatures.set(i, ((temperatures.get(i)*9)/5)+32);
        }
        this.scale = 'F';
    }
}

class DailyTemperatures{
    Set<Day> days;

    public DailyTemperatures(){
        this.days = new TreeSet<>(Comparator.comparing(Day::getDay));
    }

    public void readTemperatures(InputStream inputStream){
        Scanner sc = new Scanner(inputStream);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            Integer day = Integer.parseInt(parts[0]);
            char scale = parts[1].charAt(parts[1].length()-1);
            List<Double> temps = new ArrayList<>();

            for (int i=1;i<parts.length;i++){
                temps.add(Double.parseDouble(parts[i].substring(0,parts[i].length()-1)));
            }

            days.add(new Day(day,temps,scale));
        }
    }

    public void writeDailyStats(OutputStream outputStream, char scale) {

        days.stream().forEach(day -> {
            if (day.getScale() != scale) {
                if (scale == 'F')
                    day.convertToF();
                else
                    day.convertToC();
            }

            System.out.println(String.format("%3d: Count: %3d Min: %6.2f%c Max: %6.2f%c Avg: %6.2f%c",
                    day.getDay(), day.getTemperatures().size(), day.getMinTemp(), scale, day.getMaxTemp(), scale, day.getAvgTemp(), scale));
        });
    }
}

public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}
