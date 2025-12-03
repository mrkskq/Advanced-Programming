package labs.lab5ex2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Anagrams {

    public static void main(String[] args) throws IOException {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) throws IOException {
        // Vasiod kod ovde
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;

        Map<String, TreeSet<String>> map = new TreeMap<>();

        while (true){
            line = br.readLine();

            if (line == null || line.isEmpty()){
                break;
            }

            char []sortedWord = line.toCharArray();
            Arrays.sort(sortedWord);
            String key = String.valueOf(sortedWord);

            map.putIfAbsent(key, new TreeSet<>());
            map.get(key).add(line);

        }

        map.values().stream()
                .filter(list -> list.size() >= 5)
                .sorted(Comparator.comparing(TreeSet::first))
                .forEach(x -> System.out.println(String.join(" ", x)));
    }
}
