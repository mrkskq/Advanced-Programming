package prv_kolokvium.ex25_Discounts;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Discounts
 */

class DiscountInfo{
    private int popust;
    private int cena_na_popust;
    private int cena;

    public DiscountInfo(int popust, int cena_na_popust, int cena){
        this.popust = popust;
        this.cena_na_popust = cena_na_popust;
        this.cena = cena;
    }

    public int getPopust() {
        return popust;
    }

    public int getCena_na_popust() {
        return cena_na_popust;
    }

    public int getCena() {
        return cena;
    }
}

class Store{
    private String ime;
    private ArrayList<Integer> ceni_na_popust;
    private ArrayList<Integer> ceni;

    public Store(){
        this.ime = "";
        ceni_na_popust = new ArrayList<Integer>();
        ceni = new ArrayList<Integer>();
    }

    public Store(String ime, ArrayList<Integer> ceni_na_popust, ArrayList<Integer> ceni) {
        this.ime = ime;
        this.ceni_na_popust = ceni_na_popust;
        this.ceni = ceni;
    }

    public String getIme() {
        return ime;
    }

    public int popustVoProcenti(int cena, int namalena_cena){ // procent = 100 - ((namalena_cena / cena) * 100)
        double temp = ((double)namalena_cena / cena) * 100;
        return (int)(100 - temp);
    }

    public float prosecenPopustVoProcenti(){
        float proseci = 0;
        int count = 0;
        for (int i = 0; i < ceni.size(); i++) {
            int p = popustVoProcenti(ceni.get(i), ceni_na_popust.get(i));
            proseci += p;
            count++;
        }
        return proseci / count;
    }

    public int absolutenPopust(){
        int sum = 0;
        for (int i = 0; i < ceni.size(); i++){
            sum += Math.abs(ceni.get(i) - ceni_na_popust.get(i));
        }
        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ime + "\n");
        sb.append(String.format("Average discount: %.1f%%\n", prosecenPopustVoProcenti()));
        sb.append("Total discount: " + absolutenPopust() + "\n");

        List<DiscountInfo> discountInfos = new ArrayList<>();

        for (int i = 0; i < ceni.size(); i++){
            //sb.append(popustVoProcenti(ceni.get(i), ceni_na_popust.get(i)) + "% " + ceni_na_popust.get(i) + "/" + ceni.get(i) + "\n");
            DiscountInfo info = new DiscountInfo(popustVoProcenti(ceni.get(i), ceni_na_popust.get(i)), ceni_na_popust.get(i), ceni.get(i));
            discountInfos.add(info);
        }

        //discountInfos.sort((a,b) -> Integer.compare(b.getPopust(), a.getPopust()));
        discountInfos = discountInfos.stream()
                .sorted(Comparator.comparing(DiscountInfo::getPopust).reversed()
                        .thenComparing(DiscountInfo::getCena_na_popust, Comparator.reverseOrder())
                        .thenComparing(DiscountInfo::getCena, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        discountInfos.forEach(i -> sb.append(String.format("%d%% %d/%d\n", i.getPopust(), i.getCena_na_popust(), i.getCena())));

        return sb.toString().trim();
    }
}

class Discounts{
    private ArrayList<Store> stores;

    public Discounts(){
        stores = new ArrayList<Store>();
    }

    public int readStores(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while (true){
            line = br.readLine();
            if (line == null || line.isEmpty()){
                break;
            }
            String[] parts = line.split("\\s+");
            String ime = parts[0];
            ArrayList<Integer> ceni_na_popust = new ArrayList<Integer>();
            ArrayList<Integer> ceni = new ArrayList<Integer>();

            for (int i = 1; i < parts.length; i++) {
                String[] tokens = parts[i].split(":");
                String cena_na_popust = tokens[0];
                String cena = tokens[1];
                ceni_na_popust.add(Integer.parseInt(cena_na_popust));
                ceni.add(Integer.parseInt(cena));
            }

            Store store = new Store(ime, ceni_na_popust, ceni);
            stores.add(store);
        }
        return stores.size();
    }

    public List<Store> byTotalDiscount(){
        return stores.stream()
                .sorted(Comparator.comparing(Store::absolutenPopust).reversed()
                        .thenComparing(Store::getIme))
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Store> byAverageDiscount() {
        return stores.stream()
                .sorted(Comparator.comparing(Store::prosecenPopustVoProcenti).reversed()
                        .thenComparing(Store::getIme))
                .limit(3)
                .collect(Collectors.toList());
    }
}

public class DiscountsTest {
    public static void main(String[] args) throws IOException {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde