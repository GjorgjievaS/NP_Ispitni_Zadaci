//package Audition;

import java.util.*;


class Participant {
    String city;
    String code;
    String name;
    int age;

    public Participant(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }


    @Override
    public String toString() {
        return String.format("%s %s %d", code,name,age);
    }

    public String getCity() {
        return city;
    }
}

class Audition {
    Map<String, Set<Participant>> participantsByCity;

    Audition(){
        this.participantsByCity = new HashMap<>();
    }

    public void addParticpant(String city, String code, String name, int age){

        //  participantsByCity.putIfAbsent(city, new TreeSet<>());

        if (!participantsByCity.containsKey(city))
            participantsByCity.put(city, new HashSet<>());

        participantsByCity.get(city).add(new Participant(city, code, name, age));
    }

    public void listByCity(String city){
        participantsByCity.getOrDefault(city, new HashSet<>())
                .stream()
                .filter(c->c.getCity().equals(city))
                .sorted(Comparator.comparing(Participant::getName).thenComparing(Participant::getAge).thenComparing(Participant::getCode))
                .forEach(p -> System.out.println(p));
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}
