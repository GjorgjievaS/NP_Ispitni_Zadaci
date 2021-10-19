package Ф1_Трка;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class F1Race {

    List<Driver> drivers;

    public F1Race(){
        this.drivers = new ArrayList<>();
    }

    public void readResults(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            List<String> laps = new ArrayList<>();
            for (int i=1; i<parts.length;i++){
                laps.add(parts[i]);
            }
            laps.sort(String::compareTo);

            drivers.add(new Driver(parts[0], laps.get(0)));
        }
    }

    public void printSorted(OutputStream outputStream) {
        drivers.sort(Driver::compareTo);

        IntStream.range(0, drivers.size()).
                forEach(d->
                        System.out.println(String.format("%d. %s", d+1, drivers.get(d))));
    }

}

class Driver implements Comparable<Driver> {

    private String name;
    private String bestLap;

    public Driver(String name, String bestLap) {
        this.name = name;
        this.bestLap = bestLap;
    }

    @Override
    public int compareTo(Driver o) {
        return this.bestLap.compareTo(o.bestLap);
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s", name, bestLap);
    }
}