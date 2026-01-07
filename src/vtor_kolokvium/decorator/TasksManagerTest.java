package vtor_kolokvium.decorator;


/*

Decorator design pattern:

->  Component (interface Task)
    - Defines the common interface for both the original objects and the decorators.

->  Concrete Component
    - The actual object whose behavior you want to extend  (e.g. SimpleTask).

->  Base Decorator (wrappee: Component (Task task)) -> TaskDecorator
    - An abstract class that also implements the component interface and holds a reference to a component object.

->  Concrete Decorators
    - Specific wrapper classes (e.g., TimeTaskDecorator, PriorityTaskDecorator) that add new features and delegate to the wrapped component.

---------------------------------------------------------------------------------------------------------

Tasks management system
Write a class TaskManager that will be used to manage tasks for a given user.
The following methods should be implemented in the class:

readTasks(InputStream inputStream) – a method for loading the user’s tasks, where each task is given in the following format:
[category][task_name],[description],[deadline],[priority]

The deadline and the priority are optional fields.

It must not be allowed for a task to have a deadline after 02.06.2020.
In such a case, an exception of type DeadlineNotValidException should be thrown.
The exception must be caught at an appropriate place so that the loading of the remaining tasks is not interrupted.

void printTasks(OutputStream os, boolean includePriority, boolean includeCategory) – a method for printing the tasks.

If includeCategory is true, tasks should be printed grouped by category; otherwise, all entered tasks are printed together.
If includePriority is true, tasks should be printed sorted by priority (where 1 is the highest priority).
Tasks that do not have a priority or have the same priority should be additionally sorted in ascending order by the time distance between the deadline and the current date, meaning tasks with deadlines closest to today are printed first.
If includePriority is false, tasks are printed only in ascending order by the time distance between the deadline and the current date.
When printing tasks, use the default toString implementation (if working in IntelliJ), taking care that the variable names are correct.

Bonus: Use software design patterns for representing tasks and for their creation.


Simple Task:     [category, name, description]
Тime Task:       [category, name, description, deadline]
Priority Task:   [category, name, description, deadline, priority]

*/


import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


class DeadlineNotValidException extends Exception {
    public DeadlineNotValidException(LocalDateTime deadline) {
        super(String.format("The deadline %s has already passed", deadline));
    }
}


// =======================
// Component
// =======================
interface Task{
    LocalDateTime getDeadline();
    int getPriority();
    String getCategory();
}



// =======================
// Concrete Component
// =======================
class SimpleTask implements Task{

    String category;
    String name;
    String description;

    public SimpleTask(String category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
    }

    @Override
    public LocalDateTime getDeadline() {
        return LocalDateTime.MAX;
    }

    @Override
    public int getPriority() {
        return Integer.MIN_VALUE;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Task{");
        sb.append("name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}



// =======================
// Base Decorator
// =======================
abstract class TaskDecorator implements Task{
    protected Task task;

    public TaskDecorator(Task task) {
        this.task = task;
    }
}



// =======================
// Concrete Decorators
// =======================
class TimeTaskDecorator extends TaskDecorator{

    LocalDateTime deadline;

    public TimeTaskDecorator(Task task, LocalDateTime deadline) {
        super(task);
        this.deadline = deadline;
    }

    @Override
    public LocalDateTime getDeadline() {
        return deadline;
    }

    @Override
    public int getPriority() {
        return task.getPriority();
    }

    @Override
    public String getCategory() {
        return task.getCategory();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(task.toString(), 0, task.toString().length()-1);
        sb.append(", deadline=").append(deadline);
        sb.append('}');
        return sb.toString();
    }
}

class PriorityTaskDecorator extends TaskDecorator{

    int priority;

    public PriorityTaskDecorator(Task task, int priority) {
        super(task);
        this.priority = priority;
    }

    @Override
    public LocalDateTime getDeadline() {
        return task.getDeadline();
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getCategory() {
        return task.getCategory();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(task.toString(), 0, task.toString().length()-1);
        sb.append(", priority=").append(priority);
        sb.append('}');
        return sb.toString();
    }
}



class TaskFactory {
    public static Task createTask (String line) throws DeadlineNotValidException {
        String [] parts = line.split(",");
        String category = parts[0];
        String name = parts[1];
        String description = parts[2];
        SimpleTask base = new SimpleTask(category, name, description);
        if (parts.length==3) {
            return base;
        } else if (parts.length==4) {
            try {
                int priority = Integer.parseInt(parts[3]);
                return new PriorityTaskDecorator(base, priority);
            }
            catch (Exception e) { //parsing failed, it's a date
                LocalDateTime deadline = LocalDateTime.parse(parts[3]);
                checkDeadline(deadline);
                return new TimeTaskDecorator(base, deadline);
            }
        } else {
            LocalDateTime deadline = LocalDateTime.parse(parts[3]);
            checkDeadline(deadline);
            int priority = Integer.parseInt(parts[4]);
            return new PriorityTaskDecorator(new TimeTaskDecorator(base,deadline), priority);
        }
    }

    private static void checkDeadline (LocalDateTime deadline) throws DeadlineNotValidException {
        if (deadline.isBefore(LocalDateTime.of(2020, 6, 2, 22, 59, 59)))
            throw new DeadlineNotValidException(deadline);
    }
}



class TaskManager {
    Map<String, List<Task>> tasks;

    public TaskManager() {
        tasks = new TreeMap<>();
    }

    public void readTasks (InputStream inputStream) {
        tasks = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .map(line -> {
                    try {
                        return TaskFactory.createTask(line);
                    } catch (DeadlineNotValidException e) {
                        System.out.println(e.getMessage());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Task::getCategory,
                        TreeMap::new,
                        Collectors.toList())
                );
    }

    public void addTask (Task iTask) {
        tasks.computeIfAbsent(iTask.getCategory(), k -> new ArrayList<>());
        tasks.computeIfPresent(iTask.getCategory(), (k,v) -> {
            v.add(iTask);
            return v;
        });
    }

    public void printTasks(OutputStream os, boolean includePriority, boolean byCategory) {
        PrintWriter pw = new PrintWriter(os);

        Comparator<Task> priorityComparator = Comparator.comparing(Task::getPriority).thenComparing(task -> Duration.between(LocalDateTime.now(), task.getDeadline()));
        Comparator<Task> simpleComparator = Comparator.comparing(task -> Duration.between(LocalDateTime.now(), task.getDeadline()));

        if (byCategory) {
            tasks.forEach((category, t) -> {
                pw.println(category.toUpperCase());
                t.stream().sorted(includePriority ? priorityComparator : simpleComparator).forEach(pw::println);
            });
        }
        else {
            tasks.values().stream()
                    .flatMap(Collection::stream)
                    .sorted(includePriority ? priorityComparator : simpleComparator)
                    .forEach(pw::println);
        }

        pw.flush();
    }
}



// =======================
// Main class
// =======================
public class TasksManagerTest {

    public static void main(String[] args) {
        System.out.println("Tasks reading");
        TaskManager manager = new TaskManager();
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}
