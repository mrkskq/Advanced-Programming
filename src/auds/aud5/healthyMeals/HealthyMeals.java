package auds.aud5.healthyMeals;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/*
Пример влез
salad apple yogurt
101 salad pizza salad soup
102 apple yogurt coffee
103 burger fries

Пример излез
Person ID: 101 (healthy meals: 1)
Person ID: 102 (healthy meals: 2)
Person ID: 103 (healthy meals: 0)
 */

class Person{
    private String id;
    private Set<String> meals;
    private ArrayList<String> healthyMeals;

    public Person() {
        meals = new HashSet<String>();
    }

    public Person(String id, Set<String> meals, ArrayList<String> healthyMeals) {
        this.id = id;
        this.meals = meals;
        this.healthyMeals = healthyMeals;
    }

    public int countHealthyMeals() {
        int count = 0;
        for (String meal : meals) {
            if (healthyMeals.contains(meal)) {
                count++;
            }
        }
        return count;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("Person ID: %s (healthy meals: %d)", id, countHealthyMeals());
    }
}

public class HealthyMeals {

    private final ArrayList<String> healthyMeals;
    private ArrayList<Person> people;

    public HealthyMeals() {
        this.healthyMeals = new ArrayList<String>();
        this.people = new ArrayList<Person>();
    }

    private void evaluate(InputStream in, PrintStream out) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        line = br.readLine(); //healthyMeals
        String []parts = line.split(" ");
        healthyMeals.addAll(Arrays.asList(parts));

        while (true) {
            line = br.readLine();
            if (line == null || line.isEmpty()) {
                break;
            }
            String []tokens = line.split(" ");
            Set<String> meals = new HashSet<String>();
            for (int i = 1; i < tokens.length; i++) {
                meals.add(tokens[i]);
            }
            Person person = new Person(tokens[0], meals, healthyMeals);
            people.add(person);
        }

        PrintWriter pw = new PrintWriter(out);

        people.stream().sorted(Comparator.comparing(Person::countHealthyMeals)
                        .reversed()
                        .thenComparing(Person::getId))
                        .forEach(person -> pw.println(person.toString()));

        pw.flush();
    }

    public static void main(String[] args) throws IOException {
        HealthyMeals healthyMeals = new HealthyMeals();
        healthyMeals.evaluate(System.in, System.out);
    }

}
