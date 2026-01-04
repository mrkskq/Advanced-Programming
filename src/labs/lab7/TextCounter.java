package labs.lab7;

import java.util.*;
import java.util.concurrent.*;

public class TextCounter {

    // Result holder
    public static class Counter {
        public final int textId;
        public final int lines;
        public final int words;
        public final int chars;

        public Counter(int textId, int lines, int words, int chars) {
            this.textId = textId;
            this.lines = lines;
            this.words = words;
            this.chars = chars;
        }

        @Override
        public String toString() {
            return "Counter{" +
                    "textId=" + textId +
                    ", lines=" + lines +
                    ", words=" + words +
                    ", chars=" + chars +
                    '}';
        }


    }


    public static Callable<Counter> getTextCounter(int textId, String text) {
        //TODO
        return  new Callable<Counter>() {
            @Override
            public Counter call() throws Exception {
                int lines = 0;
                int words = 0;
                int chars = 0;

                //String[] w = text.trim().split(" ");

                lines = text.split("\n").length;
                words = text.split(" ").length + lines - 1; // zborojte sho se izmegju so \n ne gi brojt ko 2 zbora, zato + kolku sho imat lines-1 (kolku sho imat \n)
                chars = text.length();

                return new Counter(textId, lines, words, chars);
            }
        };
    }



    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();       // number of texts
        sc.nextLine();              // consume newline

        List<Callable<Counter>> tasks = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int textId = sc.nextInt();
            sc.nextLine();          // consume newline

            int lines = sc.nextInt();   // number of lines for this text
            sc.nextLine();              // consume newline

            StringBuilder text = new StringBuilder();
            for (int j = 0; j < lines; j++) {
                text.append(sc.nextLine());
                if (j < lines - 1) {
                    text.append("\n");
                }
            }

            //TODO add a Callable<Counter> for each text read in the tasks list

            tasks.add(getTextCounter(textId, text.toString()));
        }

        ExecutorService executor =
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


        //TODO invoke All tasks on the executor and create a List<Future<?>>

        List<Future<Counter>> futures = new ArrayList<>();
        for (Callable<Counter> task : tasks) {
            futures.add(executor.submit(task));
        }


        List<Counter> results = new ArrayList<>();

        //TODO extract results from the List<Future>

        for (Future<Counter> future : futures) {
            results.add(future.get());
        }

        executor.shutdown();


        // Sorting by textId (important concept!)
        results.sort(Comparator.comparingInt(c -> c.textId));

        // Output (optional for debugging / demonstration)
        for (Counter c : results) {
            System.out.printf(
                    "%d %d %d %d%n",
                    c.textId, c.lines, c.words, c.chars
            );
        }
    }
}
