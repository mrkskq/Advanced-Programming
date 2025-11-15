package prv_kolokvium.ex20_Subtitles;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

class Subtitles{
    private ArrayList<String> lines;

    public Subtitles(){
        lines = new ArrayList<String>();
    }

    public int loadSubtitles(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        int countSubtitles = 0;

        br.lines().forEach(l -> lines.add(l));
        for (String line : lines) {
            if (line.matches("\\d+")){ //kolku sho imat linii so brojki samo tolku imat subtitles
                countSubtitles++;
            }
        }

        return countSubtitles;
    }


    public void print() {
        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println();
    }

    public void shift(int shift) {
        ArrayList<String> subtitles = new ArrayList<>();
        subtitles.addAll(lines);

        String start;
        String end;
        int newStartInMs = 0;
        int newEndInMs = 0;
        String newStart;
        String newEnd;

        for (int i = 0; i < lines.size(); i++) {
            String s = lines.get(i);

            if (s.contains("-->")){
                start = s.split(" --> ")[0];
                end = s.split(" --> ")[1];

                newStartInMs = convertFromStringToMilliseconds(start) + shift;
                newEndInMs = convertFromStringToMilliseconds(end) + shift;

                newStart = convertFromMillisecondsToString(newStartInMs);
                newEnd = convertFromMillisecondsToString(newEndInMs);

                lines.set(i, newStart + " --> " + newEnd);
            }
        }


    }

    private int convertFromStringToMilliseconds(String time) {
        String []parts = time.split("[:,]");
        int h = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        int s = Integer.parseInt(parts[2]);
        int ms = Integer.parseInt(parts[3]);
        return ((h * 3600 + m * 60 + s) * 1000) + ms;
    }

    private String convertFromMillisecondsToString(int ms) {
        if (ms < 0){
            ms = 0;
        }
        int h = ms / 3600000;
        ms = ms % 3600000;
        int m = ms / 60000;
        ms = ms % 60000;
        int s = ms / 1000;
        int millis = ms % 1000;
        return String.format("%02d:%02d:%02d,%03d", h, m, s, millis);
    }
}

public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

// Вашиот код овде

