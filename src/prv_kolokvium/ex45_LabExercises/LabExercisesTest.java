package prv_kolokvium.ex45_LabExercises;

import java.util.*;
import java.util.stream.Collectors;

class Student{
    private String id;
    private List<Integer> points;

    public Student(){
        points = new ArrayList<>();
    }

    public Student(String id, List<Integer> points){
        this.id = id;
        this.points = points;
    }

    public String getId(){
        return id;
    }

    public List<Integer> getPoints(){
        return points;
    }

    public int getYear(){
        String yy = id.substring(0,3);
        return 21-Integer.parseInt(yy);
    }

    public float getSumOfPoints(){
        return (float) (points.stream().mapToInt(p -> p).sum()) / 10;
    }

    public double getAveragePoints(){
        return (double) (points.stream().mapToInt(p -> p).sum()) / points.size();
    }

    public boolean isPassed(){
        return getSumOfPoints() >= 50;
    }

    @Override
    public String toString() {
        return String.format("%6s %-3s %4.2f", id, isPassed() ? "YES" : "NO", getAveragePoints());
    }

}

class LabExercises{
    private Collection<Student> students;
    private List<Student> failedStudents;

    public LabExercises(){
        students = new ArrayList<>();
        failedStudents = new ArrayList<>();
    }

    public void addStudent (Student student){
        students.add(student);
    }

    public void printByAveragePoints (boolean ascending, int n){
        if (ascending){
            students.stream()
                    .sorted(Comparator.comparing(Student::getAveragePoints).thenComparing(Student::getId))
                    .limit(n)
                    .forEach(System.out::println);
        }
        else {
            students.stream()
                    .sorted(Comparator.comparing(Student::getAveragePoints).thenComparing(Student::getId).reversed())
                    .limit(n)
                    .forEach(System.out::println);
        }
    }

    public List<Student> failedStudents (){
        List<Student> filteredStudents = new ArrayList<>();
        students.stream()
                .filter(s -> s.getPoints().contains(0) || s.getPoints().isEmpty())
                .forEach(filteredStudents::add);

        for (Student s : filteredStudents) {
            List<Integer> points = s.getPoints();
            int count = 0;
            for (Integer p : points) {
                if (p == 0) count++;
            }
            if (count > 2){
                failedStudents.add(s);
            }
        }

        return failedStudents;
    }

    public Map<Integer,Double> getStatisticsByYear(){
        students = students.stream()
                .filter(s -> !failedStudents.contains(s))
                .collect(Collectors.toCollection(ArrayList::new));

        Map<Integer,Double> grouped = new TreeMap<>();
        for (Student s : students) {
            grouped.putIfAbsent(s.getYear(), s.getAveragePoints());
        }
        return grouped;
    }
}

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}
