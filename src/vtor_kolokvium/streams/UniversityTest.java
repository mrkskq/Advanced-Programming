package vtor_kolokvium.streams;

//package midterms.m1;

import java.util.*;
import java.util.stream.*;

class Course {
    private String code;
    private String name;
    private int credits;
    private int difficulty;
    private int enrolledStudents;

    public Course(String code, String name, int credits, int difficulty, int enrolledStudents) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.difficulty = difficulty;
        this.enrolledStudents = enrolledStudents;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getEnrolledStudents() {
        return enrolledStudents;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", code, name);
    }
}


class Department {
    private String name;
    private List<Course> courses;

    public Department(String name, List<Course> courses) {
        this.name = name;
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    @Override
    public String toString() {
        return name;
    }
}


class University {

    private List<Department> departments;

    public University(List<Department> departments) {
        this.departments = departments;
    }


    public List<String> getAllCourseNames() {

        //TODO
        return departments.stream()
                .flatMap(d -> d.getCourses().stream())
                .map(c -> c.getName())
                .collect(Collectors.toList());
    }

    public List<Course> getCoursesWithMinCredits(int minCredits) {

        //TODO
        return departments.stream()
                .flatMap(d -> d.getCourses().stream())
                .filter(c -> c.getCredits() == minCredits)
                .collect(Collectors.toList());
    }

    public int getTotalStudentCount() {

        //TODO
        return departments.stream()
                .flatMap(d -> d.getCourses().stream())
                .map(c -> c.getEnrolledStudents())
                .reduce(0, Integer::sum);
    }

    public Optional<Course> getHardestCourse() {

        //TODO
        return departments.stream()
                .flatMap(d -> d.getCourses().stream())
                .max(Comparator.comparing(Course::getDifficulty));
    }

    public Map<Integer, List<Course>> groupByDifficulty() {

        //TODO
        return departments.stream()
                .flatMap(d -> d.getCourses().stream())
                .collect(Collectors.groupingBy(
                        c -> c.getDifficulty()
                ));
    }

    public Map<String, Integer> getCourseEnrollmentMap() {

        //TODO
        return departments.stream()
                .flatMap(d -> d.getCourses().stream())
                .collect(Collectors.toMap(
                        c -> c.getCode(),
                        c -> c.getEnrolledStudents()
                ));
    }

    public double getAverageEnrollmentPerCourse() {

        //TODO
        return departments.stream()
                .flatMap(d -> d.getCourses().stream())
                .collect(Collectors.averagingDouble(c -> c.getEnrolledStudents()));
    }

    public List<String> getSortedCourseCodes() {

        //TODO
        return departments.stream()
                .flatMap(d -> d.getCourses().stream())
                .map(c -> c.getCode())
                .sorted()
                .collect(Collectors.toList());
    }

    public Map<String, List<String>> getDepartmentToCourseNames() {
        //TODO
        return departments.stream()
                .collect(Collectors.toMap(
                        d -> d.getName(),
                        d -> d.getCourses().stream()
                                .map(c -> c.getName())
                                .collect(Collectors.toList())
                ));
    }

    public List<Course> getAllCourses() {
        //TODO
        return departments.stream()
                .flatMap(d -> d.getCourses().stream())
                .collect(Collectors.toList());
    }

    public Optional<Department> getMostPopularDepartment() {
        //TODO
        return departments.stream()
                .max(Comparator.comparingInt(
                        d -> d.getCourses().stream()
                                .mapToInt(c -> c.getEnrolledStudents())
                                .sum()
                ));
    }

    public Map<Integer, Integer> getStudentsByDifficulty() {
        //TODO
        return departments.stream()
                .flatMap(d -> d.getCourses().stream())
                .collect(Collectors.groupingBy(
                        c -> c.getDifficulty(),
                        Collectors.summingInt(c -> c.getEnrolledStudents())
                ));
    }

    public List<Course> getCoursesByDifficultyRange(int min, int max) {
        //TODO
        return departments.stream()
                .flatMap(d -> d.getCourses().stream())
                .filter(c -> c.getDifficulty() >= min && c.getDifficulty() <= max)
                .collect(Collectors.toList());
    }

    public List<String> getPopularCourseCodes(int minStudents) {
        //TODO
        return departments.stream()
                .flatMap(d -> d.getCourses().stream())
                .filter(c -> c.getEnrolledStudents() >= minStudents)
                .map(c -> c.getCode())
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getTotalCreditsPerDepartment() {
        //TODO
        return departments.stream()
                .collect(Collectors.toMap(
                        d -> d.getName(),
                        d -> d.getCourses().stream()
                                .mapToInt(c -> c.getCredits())
                                .sum()
                ));
    }

    public List<Course> getTop3HardestCourses() {
        //TODO
        return departments.stream()
                .flatMap(d -> d.getCourses().stream())
                .sorted(Comparator.comparing(Course::getDifficulty).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }


    public Map<String, Double> getAverageDifficultyPerDepartment() {
        //TODO
        return departments.stream()
                .collect(Collectors.toMap(
                        d -> d.getName(),
                        d -> d.getCourses().stream()
                                .mapToDouble(c -> c.getDifficulty())
                                .average()
                                .orElse(0.0)
                ));
    }



    public IntSummaryStatistics getEnrollmentStatistics() {
        //TODO
        return departments.stream()
                .flatMap(d -> d.getCourses().stream())
                .collect(Collectors.summarizingInt(c -> c.getEnrolledStudents()));
    }


    public University mergeFourSmallestDepartments() {

        //TODO

        // Sortirani spored vkupen broj na studenti
        List<Department> sorted = departments.stream()
                .sorted(Comparator.comparingInt(
                        d -> d.getCourses().stream()
                                .mapToInt(c -> c.getEnrolledStudents())
                                .sum()
                ))
                .collect(Collectors.toList());

        // Zemi gi 4-te najmali
        List<Department> smallest4 = sorted.stream()
                .limit(4)
                .collect(Collectors.toList());

        // Zemi gi ostanatite departments (bez tie 4)
        List<Department> remaining = sorted.stream()
                .skip(4)
                .collect(Collectors.toList());

        // Kreiraj go imeto na noviot merge department
        String mergedName = smallest4.stream()
                .map(d -> d.getName())
                .collect(Collectors.joining(" & "));

        // Soberi gi site courses od 4te departments
        List<Course> mergedCourses = smallest4.stream()
                .flatMap(d -> d.getCourses().stream())
                .collect(Collectors.toList());

        Department mergedDepartment = new Department(mergedName, mergedCourses);

        // Dodaj go noviot department vo listata so ostanatite
        remaining.add(mergedDepartment);

        return new University(remaining);
    }
}

public class UniversityTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        List<Department> departments = new ArrayList<>();

        departments.add(new Department("Computer Science", Arrays.asList(
                new Course("SP", "Structural Programming", 6, 5, 1100),
                new Course("APS", "Algorithms", 6, 8, 500),
                new Course("OOP", "Object-Oriented Programming", 6, 7, 1000),
                new Course("NP", "Advanced Programming", 6, 9, 600)
        )));

        departments.add(new Department("Mathematics", Arrays.asList(
                new Course("CAL1", "Calculus I", 6, 6, 230),
                new Course("ALG", "Linear Algebra", 6, 7, 210),
                new Course("STAT", "Statistics", 6, 6, 170),
                new Course("CAL2", "Calculus II", 6, 8, 190)
        )));

        departments.add(new Department("Physics", Arrays.asList(
                new Course("PHY1", "Mechanics", 5, 7, 150),
                new Course("PHY2", "Electromagnetism", 5, 8, 140),
                new Course("PHY3", "Quantum Physics", 6, 9, 100),
                new Course("PHY4", "Optics", 4, 6, 130)
        )));

        departments.add(new Department("Chemistry", Arrays.asList(
                new Course("CH1", "Organic Chemistry", 6, 8, 120),
                new Course("CH2", "Inorganic Chemistry", 5, 7, 140),
                new Course("CH3", "Physical Chemistry", 6, 9, 110),
                new Course("CH4", "Analytical Chemistry", 5, 6, 130)
        )));

        departments.add(new Department("Biology", Arrays.asList(
                new Course("BIO1", "Cell Biology", 6, 7, 200),
                new Course("BIO2", "Genetics", 6, 8, 160),
                new Course("BIO3", "Ecology", 5, 6, 180),
                new Course("BIO4", "Biochemistry", 6, 9, 140)
        )));

        departments.add(new Department("Economics", Arrays.asList(
                new Course("MIC", "Microeconomics", 6, 6, 250),
                new Course("MAC", "Macroeconomics", 6, 7, 240),
                new Course("FIN", "Finance", 6, 8, 220),
                new Course("ACC", "Accounting", 6, 5, 260)
        )));

        departments.add(new Department("Languages", Arrays.asList(
                new Course("ENG", "English Language", 4, 4, 300),
                new Course("GER", "German Language", 4, 5, 180),
                new Course("FRE", "French Language", 4, 5, 160),
                new Course("ITA", "Italian Language", 4, 4, 170)
        )));

        departments.add(new Department("History", Arrays.asList(
                new Course("HIS1", "Ancient History", 5, 6, 140),
                new Course("HIS2", "Medieval History", 5, 7, 130),
                new Course("HIS3", "Modern History", 5, 6, 150),
                new Course("HIS4", "Contemporary History", 5, 7, 140)
        )));

        University university = new University(departments);

        String method = sc.nextLine().trim();

        System.out.println("Testing method: " + method);

        if (method.equals("getAllCourseNames")) {
            university.getAllCourseNames().forEach(System.out::println);

        } else if (method.equals("getCoursesWithMinCredits")) {
            int x = sc.nextInt();
            university.getCoursesWithMinCredits(x).forEach(System.out::println);

        } else if (method.equals("getTotalStudentCount")) {
            System.out.println(university.getTotalStudentCount());

        } else if (method.equals("getHardestCourse")) {
            System.out.println(university.getHardestCourse());

        } else if (method.equals("groupByDifficulty")) {
            university.groupByDifficulty().forEach((k, v) -> {
                System.out.println(String.format("Difficulty: %d", k));
                v.forEach(System.out::println);
            });

        } else if (method.equals("getCourseEnrollmentMap")) {
            university.getCourseEnrollmentMap().forEach((k, v) -> System.out.println(String.format("%s -> %d", k, v)));

        } else if (method.equals("getAverageEnrollmentPerCourse")) {
            System.out.println(university.getAverageEnrollmentPerCourse());

        } else if (method.equals("getSortedCourseCodes")) {
            university.getSortedCourseCodes().forEach(System.out::println);

        } else if (method.equals("getDepartmentToCourseNames")) {
            university.getDepartmentToCourseNames().forEach((k,v) -> {
                System.out.println(String.format("Department: %s", k));
                v.forEach(System.out::println);
            });

        } else if (method.equals("getAllCourses")) {
            university.getAllCourses().forEach(System.out::println);

        } else if (method.equals("getMostPopularDepartment")) {
            System.out.println(university.getMostPopularDepartment());

        } else if (method.equals("getStudentsByDifficulty")) {
            university.getStudentsByDifficulty().forEach((k,v) -> System.out.println(String.format("%s -> %d", k, v)));

        } else if (method.equals("getCoursesByDifficultyRange")) {
            int min = sc.nextInt();
            int max = sc.nextInt();
            university.getCoursesByDifficultyRange(min, max).forEach(System.out::println);

        } else if (method.equals("getPopularCourseCodes")) {
            int minStudents = sc.nextInt();
            university.getPopularCourseCodes(minStudents).forEach(System.out::println);

        } else if (method.equals("getTotalCreditsPerDepartment")) {
            university.getTotalCreditsPerDepartment().forEach((k,v) -> System.out.println(String.format("%s -> %d", k, v)));

        } else if (method.equals("getTop3HardestCourses")) {
            university.getTop3HardestCourses().forEach(System.out::println);

        } else if (method.equals("getAverageDifficultyPerDepartment")) {
            university.getAverageDifficultyPerDepartment().forEach((k,v)-> System.out.println(String.format("%s -> %.2f", k, v)));

        } else if (method.equals("getEnrollmentStatistics")) {
            System.out.println(university.getEnrollmentStatistics());

        } else if (method.equals("mergeFourSmallestDepartments")) {
            University updated = university.mergeFourSmallestDepartments();
            System.out.println("Merged University Departments:");
            updated.getDepartmentToCourseNames().forEach((k,v) -> {
                System.out.println(String.format("Department: %s", k));
                v.forEach(System.out::println);
            });

        } else {
            System.out.println("Unknown method!");
        }
    }
}

