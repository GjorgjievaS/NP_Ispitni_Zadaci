//package Уникатни_имиња;

import java.util.*;
import java.util.stream.Collectors;

class Names{
    Map<String,Integer> names;

    public Names() {
        this.names = new TreeMap<>();
    }

    public void addName(String name){
        Integer count = names.computeIfAbsent(name,k->0);
        names.put(name,++count);
    }

    public int uniqueLetters(String name){
        Set<Character> letters = new HashSet<>();
        for (Character c : name.toCharArray())
            letters.add(Character.toLowerCase(c));
        return letters.size();
    }

    public void printN(int n){
        names.forEach((k,v)->{
            if (v >= n)
                System.out.printf("%s (%d) %d\n",k,v,uniqueLetters(k));
        });
    }

    public String findName(int len, int x){
        List<String> list = names.keySet().stream()
                .filter(name->name.length() < len)
                .collect(Collectors.toList());
        return list.get(x % list.size());
    }
}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}
