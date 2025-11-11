package auds.aud2.studenti;

/*
1. Класа Student
Претставува еден студент со основни информации: индекс, име, оцена и присуство.
Индексот треба да биде деклариран како final.
Имплементирај метод toString кој ќе го прикажува студентот во читлив формат (име, индекс, оцена и присуство).
Осигурај дека при промена на оцената, вредноста секогаш останува во опсег од 5 до 10.

2. Класа Course
Претставува универзитетски курс кој управува со низa од студенти.
Курсот треба да има наслов, низа на студенти и бројач кој означува колку студенти моментално се запишани.
Имплементирај ги следните методи:
enroll(Supplier<Student> supplier) - Додава нов студент во курсот, користејќи го Supplier<Student>, доколку има слободно место.
forEach(Consumer<Student> action) - Применува зададена акција (Consumer) врз секој студент во низата (на пример, печатење).
count(Predicate<Student> condition) - Го враќа бројот на студенти кои го исполнуваат дадениот услов.
findFirst(Predicate<Student> condition) - Го враќа првиот студент кој го исполнува условот или null ако нема таков.
filter(Predicate<Student> condition) - Враќа нова низа која ги содржи само студентите кои го исполнуваат условот.
mapToLabels(Function<Student, String> mapper) - Враќа низа од текстуални описи, добиени со трансформирање на секој студент со дадената функција.
mutate(Consumer<Student> mutator) - Применува промена на сите студенти (на пример, зголемување на оцената).
conditionalMutate(Predicate<Student> condition, Consumer<Student> mutator) - Ја применува промената само на студентите кои го исполнуваат дадениот услов.
toString() - Враќа текстуален опис на курсот, кој ги содржи насловот, бројот на запишани студенти и списокот на сите студенти.

3. Класа CourseDemo
Содржи main метод во кој се демонстрира целата функционалност.
Отвори Scanner и прочитај цел број n што го означува бројот на студенти кои ќе се внесат.
Креирај Supplier<Student> кој чита податоци за еден студент од конзолата (индекс, име, оцена, присуство) и враќа нов објект Student.
Запиши n студенти во курсот користејќи го методот enroll.
Користи Consumer<Student> заедно со forEach за да ги испечатиш сите запишани студенти.
Дефинирај Predicate<Student> за студенти кои:
имаат оцена поголема или еднаква на 6
имаат присуство најмалку 70%
Комбинирај ги двете состојби со .and() и користи го методот filter за да ги прикажеш само тие студенти.
Користи findFirst за да го пронајдеш и прикажеш првиот студент со оцена поголема или еднаква на 9.
Користи mutate за да ја зголемиш оцената на сите студенти за 1.
Користи conditionalMutate за да ја зголемиш оцената за 1 само на студентите со присуство поголемо или еднакво на 90%.
Користи mapToLabels за да ги трансформираш сите студенти во текстуални описи и испечати ги.
На крај, испечати ги сите информации за курсот со користење на методот toString.
 */

/*
sample input:
3
233002 Ana 9 70
233004 Aleksandar 8 95
231012 Sofija 5 90
 */

import java.util.Scanner;
import java.util.function.*;

class Student{
    private final String index;
    private String name;
    private int grade;
    private int attendance;

    public Student(String index, String name, int grade, int attendance) {
        this.index = index;
        this.name = name;
        this.grade = grade;
        this.attendance = attendance;
    }

    public int getGrade() {
        return grade;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setGrade(int grade) {
        if(grade > 10) {
            grade = 10;
        }
        if (grade < 5){
            grade = 5;
        }
        this.grade = grade;
    }

    @Override
    public String toString() {
        return name + " (" + index + "), grade = " + grade + ", attendance " + attendance + "%";
    }

}


class Course{

    private final String title;
    private final Student[] students;
    private int size = 0;

    public Course(String title, int capacity) {
        this.title = title;
        this.students = new Student[capacity];
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return students.length;
    }

    // 1. Додава нов студент во курсот, користејќи го Supplier<Student>, доколку има слободно место.
    public void enroll(Supplier<Student> supplier){
        if (size <= students.length) {
            students[size++] = supplier.get();
        }
    }

    // 2. Применува зададена акција (Consumer) врз секој студент во низата (на пример, печатење).
    public void forEach(Consumer<Student> action){
        for (int i = 0; i < size; i++) {
            action.accept(students[i]);
        }
    }

    // 3. Го враќа бројот на студенти кои го исполнуваат дадениот услов.
    public int count(Predicate<Student> predicate){
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(students[i])) {
                count++;
            }
        }
        return count;
    }

    // 4. Го враќа првиот студент кој го исполнува условот или null ако нема таков.
    public Student findFirst(Predicate<Student> predicate){
        for (int i = 0; i < size; i++) {
            if (predicate.test(students[i])) {
                return students[i];
            }
        }
        return null;
    }

    // 5. Враќа нова низа која ги содржи само студентите кои го исполнуваат условот.
    public Student[] filter(Predicate<Student> predicate){
        int matches = count(predicate); //kolku go ispolnuvat uslovot za tolkava da e novata niza
        Student[] filteredStudents = new Student[matches];
        for (int i = 0, j = 0; i < size; i++) {
            if (predicate.test(students[i])) {
                filteredStudents[j++] = students[i];
            }
        }
        return filteredStudents;
    }

    // 6. Враќа низа од текстуални описи, добиени со трансформирање на секој студент со дадената функција.
    public String[] mapToLabels(Function<Student, String> mapper){
        String[] labels = new String[size];
        for (int i = 0; i < size; i++) {
            labels[i] = mapper.apply(students[i]); //primi student vrati string
        }
        return labels;
    }

    // 7. Применува промена на сите студенти (на пример, зголемување на оцената).
    public void mutate(Consumer<Student> mutator){
        for (int i = 0; i < size; i++) {
            mutator.accept(students[i]);
        }
    }

    // 8. Ја применува промената само на студентите кои го исполнуваат дадениот услов.
    public void conditionalMutate(Predicate<Student> predicate, Consumer<Student> mutator){
        for (int i = 0; i < size; i++) {
            if (predicate.test(students[i])) {
                mutator.accept(students[i]);
            }
        }
    }

    // 9. Враќа текстуален опис на курсот, кој ги содржи насловот, бројот на запишани студенти и списокот на сите студенти.

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Course: " + title + "( " + size + "/" + students.length + " students)");
        for (Student student : students) {
            sb.append(student).append("\n");
        }
        return sb.toString();
    }
}


public class CourseDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Course course = new Course("Advanced Programming", 3);
        int n = sc.nextInt();

        // supplier that reads one student per line
        Supplier<Student> studentFromInput = () -> {
//          System.out.println("Enter student (index name grade attendance):");
            String index = sc.next();
            String name = sc.next();
            int grade = sc.nextInt();
            int attendance = sc.nextInt();
            return new Student(index, name, grade, attendance);
        };

        // enroll n students using a supplier
        for (int i = 0; i < n; i++) {
            course.enroll(studentFromInput);
        }

        // print all enrolled students using Consumer<Student> + forEach
        System.out.println("\n--- Enrolled students ---");
        Consumer<Student> printStudent = s -> System.out.println(s);
        course.forEach(printStudent);

        // use predicate to filter students with grade >= 6
        Predicate<Student> isPassing = s -> s.getGrade() >= 6;

        // use predicate to filter students with attendace >= 70%
        Predicate<Student> highAttendance = s -> s.getAttendance() >= 70;

        // use predicate to filter students with grade >= 6 and attendance >= 70
        Predicate<Student> passedWithHighAttendance = isPassing.and(highAttendance);
        System.out.println("\n--- Students with passing grade and high attendance ---");
        Student[] filtered = course.filter(passedWithHighAttendance);
        for (Student student : filtered) {
            System.out.println(student);
        }

        // find first student with grade >= 9
        System.out.println("\n--- First student with grade >= 9 ---");
        System.out.println(course.findFirst(s -> s.getGrade() >= 9));

        // add +1 to each student's grade
        System.out.println("\n--- Adding +1 to each student's grade ---");
        Consumer<Student> increaseGrade = s -> s.setGrade(s.getGrade() + 1);
        course.mutate(increaseGrade);
        System.out.println("--- Each student's grade +1: ---");
        course.forEach(printStudent);

        // add +1 to each student's grade if they have attendance >= 90
        System.out.println("\n--- Adding +1 to the student's grade, only if they have attendance >= 90% ---");
        course.conditionalMutate(
                s -> s.getAttendance() >= 90,
                s -> s.setGrade(s.getGrade() + 1)
        );
        course.forEach(printStudent);

    }
}
