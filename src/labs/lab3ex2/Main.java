package labs.lab3ex2;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;
import java.util.stream.Collectors;

// todo: complete the implementation of the Ad, AdRequest, and AdNetwork classes
class Ad implements Comparable<Ad>{
    private String id;
    private String category;
    private double bidValue;
    private double ctr;
    private String content;
    private double totalScore;

    public Ad(String id, String category, double bidValue, double ctr, String content) {
        this.id = id;
        this.category = category;
        this.bidValue = bidValue;
        this.ctr = ctr;
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public double getBidValue() {
        return bidValue;
    }

    public double getCtr() {
        return ctr;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public static final Comparator<Ad> byTotalScore = Comparator.comparing(Ad::getTotalScore);

    @Override
    public String toString() {
        char sign = '%';
        return String.format("%s %s (bid=%.2f, ctr=%.2f%c) %s",id,category,bidValue,ctr*100,sign,content);
    }

    @Override
    public int compareTo(Ad o) {
        int cmp = Double.compare(o.bidValue,this.bidValue); //opagjacki
        if (cmp != 0) return cmp;
        return this.id.compareTo(o.id); //rastecki
    }
}

class AdRequest{
    private String id;
    private String category;
    private double floorBid;
    private String keywords;

    public AdRequest(String id, String category, double floorBid, String keywords) {
        this.id = id;
        this.category = category;
        this.floorBid = floorBid;
        this.keywords = keywords;
    }

    public String getCategory() {
        return category;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s %s (floor=%.2f): %s",id,category,floorBid,keywords);
    }
}

class AdNetwork {
    private ArrayList<Ad> ads;

    public AdNetwork() {
        ads = new ArrayList<>();
    }

    public void readAds(BufferedReader in) throws IOException {
        BufferedReader br = in;
        String line;
        while (true){
            line = br.readLine();
            if (line == null || line.isEmpty()){
                break;
            }
            String[] parts = line.split("\\s+");
            String id = parts[0];
            String category = parts[1];
            double bidValue = Double.parseDouble(parts[2]);
            double ctr = Double.parseDouble(parts[3]);
            String content = String.join(" ", Arrays.copyOfRange(parts, 4, parts.length));
            ads.add(new Ad(id, category, bidValue, ctr, content));
        }
    }

    public List placeAds(BufferedReader inputStream, int k, PrintWriter outputStream) throws IOException {

        // Чита едно барање за реклама (AdRequest) од дадениот влезен тек во формат:
        //ID CATEGORY FLOOR_BID KEYWORD1 KEYWORD2 KEYWORD3…
        BufferedReader br = inputStream;
        String line;
        line = br.readLine();
        String[] parts = line.split("\\s+");
        String id = parts[0];
        String category = parts[1];
        double floorBid = Double.parseDouble(parts[2]);
        String keywords = String.join(" ", Arrays.copyOfRange(parts, 3, parts.length));
        AdRequest request = new AdRequest(id, category, floorBid, keywords);

        //Ги исклучува сите реклами кои имаат bidValue помал од floorBid во барањето за реклама
        ads.removeIf(ad -> ad.getBidValue() < floorBid);

        //За секоја реклама ја пресметува вкупната вредност (score) според следната формула:
        //totalScore = relevanceScore(ad, request) + x * bidValue + y * ctr
        for (Ad ad : ads) {
            double totalScore = relevanceScore(ad, request) + 5.0 * ad.getBidValue() + 100.0 * ad.getCtr();
            ad.setTotalScore(totalScore);
        }

        //Рекламите се подредуваат според totalScore во опаѓачки редослед,
        //се земаат топ k реклами, а потоа се подредуваат според „природниот“ редослед на класата.
        List<Ad> topK_ads = ads.stream()
                .sorted(Ad.byTotalScore.reversed())
                .limit(k)
                .collect(Collectors.toList());
        Collections.sort(topK_ads);

        //Резултатите се печатат со PrintWriter во дадениот излезен тек во следниот формат:
        //Top ads for request AR001:
        //AD003 tech (bid=3.00, ctr=9.00%) Powerful gaming laptop
        //AD001 tech (bid=2.50, ctr=12.00%) Amazing new phone
        PrintWriter pw = outputStream;
        pw.println("Top ads for request "+request.getId()+":");
        for (Ad ad : topK_ads) {
            pw.println(ad);
        }
        return topK_ads;
    }

    private int relevanceScore(Ad ad, AdRequest req) {
        int score = 0;
        if (ad.getCategory().equalsIgnoreCase(req.getCategory())) score += 10;
        String[] adWords = ad.getContent().toLowerCase().split("\\s+");
        String[] keywords = req.getKeywords().toLowerCase().split("\\s+");
        for (String kw : keywords) {
            for (String aw : adWords) {
                if (kw.equals(aw)) score++;
            }
        }
        return score;
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        AdNetwork network = new AdNetwork();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));

        int k = Integer.parseInt(br.readLine().trim());

        if (k == 0) {
            network.readAds(br);
            network.placeAds(br, 1, pw);
        } else if (k == 1) {
            network.readAds(br);
            network.placeAds(br, 3, pw);
        } else {
            network.readAds(br);
            network.placeAds(br, 8, pw);
        }

        pw.flush();
    }
}