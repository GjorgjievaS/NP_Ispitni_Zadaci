package Discounts;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


class StoreItem{
    int discount;
    int regular;
    int percentage;

    public StoreItem(int discount, int regular, int percentage) {
        this.discount = discount;
        this.regular = regular;
        this.percentage = percentage;
    }

    public int getDiscount() {
        return discount;
    }

    public int getRegular() {
        return regular;
    }

    public int getPercentage() {
        return percentage;
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d",percentage,discount,regular);
    }
}

class Store{
    String name;
    List<Integer> discountPrices;
    List<Integer> regularPrices;

    public Store(String name, List<Integer> discountPrices, List<Integer> regularPrices) {
        this.name = name;
        this.discountPrices = discountPrices;
        this.regularPrices = regularPrices;
    }

    public String getName() {
        return name;
    }

    public int totalDiscount(){
        int sum = 0;
        for (int i=0;i<discountPrices.size();i++){
            sum += regularPrices.get(i) - discountPrices.get(i);
        }
        return sum;
    }

    public int getPercentage(int discount, int regular){
        return (int) (100 - (((float) discount / regular ) * 100));
    }

    public float averageDiscount(){
        int sum = 0;
        for (int i=0;i<discountPrices.size();i++){
            sum += getPercentage(discountPrices.get(i),regularPrices.get(i));
        }
        return (float) (sum*1.0/discountPrices.size());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name + "\n");
        sb.append(String.format("Average discount: %.1f%%\n",averageDiscount()));
        sb.append("Total discount: " + totalDiscount() + "\n");

        List<StoreItem> items = new ArrayList<>();
        for (int i=0;i<discountPrices.size();i++){
            items.add(new StoreItem(discountPrices.get(i), regularPrices.get(i),
                    getPercentage(discountPrices.get(i),regularPrices.get(i))));
        }

        items.sort(Comparator.comparing(StoreItem::getPercentage).thenComparing(StoreItem::getDiscount).reversed());
        for (int i=0;i<items.size();i++){
            if (i == items.size()-1)
                sb.append(items.get(i));
            else
                sb.append(items.get(i) + "\n");
        }
        return sb.toString();
    }
}

class Discounts{
    List<Store> stores;

    public Discounts(){
        this.stores = new ArrayList<>();
    }

    public int readStores(InputStream inputStream){
        Scanner sc = new Scanner(inputStream);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String name = parts[0];
            List<Integer> discountPrices = new ArrayList<>();
            List<Integer> regularPrices = new ArrayList<>();

            for (int i=1;i<parts.length;i++){
                String[] parts2 = parts[i].split(":");
                discountPrices.add(Integer.parseInt(parts2[0]));
                regularPrices.add(Integer.parseInt(parts2[1]));
            }
            stores.add(new Store(name,discountPrices,regularPrices));
        }
        return stores.size();
    }

    public List<Store> byTotalDiscount(){
        stores.sort(Comparator.comparing(Store::totalDiscount).thenComparing(Store::getName));
        return stores.stream().limit(3).collect(Collectors.toList());
    }

    public List<Store> byAverageDiscount(){
        stores.sort(Comparator.comparing(Store::averageDiscount).reversed().thenComparing(Store::getName));
        return stores.stream().limit(3).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        stores.forEach(sb::append);
        return sb.toString();
    }
}

public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}