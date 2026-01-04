package labs.lab5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

// todo: implement the necessary classes

class Book {
    private String isbn;
    private String title;
    private String author;
    private int year;

    private int numberOfCopies;
    private int totalBorrows;
    private List<Member> currentBorrowers;
    private Queue<Member> waitingMembers;

    public Book(String isbn, String title, String author, int year) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
        this.numberOfCopies = 1;
        this.totalBorrows = 0;
        this.currentBorrowers = new ArrayList<>();
        this.waitingMembers = new LinkedList<Member>();
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public Queue<Member> getWaitingMembers() {
        return waitingMembers;
    }

    public List<Member> getCurrentBorrowers() {
        return currentBorrowers;
    }

    public void setTotalBorrows(int totalBorrows) {
        this.totalBorrows = totalBorrows;
    }

    public int getTotalBorrows() {
        return totalBorrows;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return String.format("%s - \"%s\" by %s (%d), available: %d, total borrows: %d", isbn, title, author, year, numberOfCopies, totalBorrows);
    }
}

class Member {
    private String id;
    private String name;

    private int totalBorrows;
    private List<Book> borrowedBooks;

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
        this.totalBorrows = 0;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public int getTotalBorrows() {
        return totalBorrows;
    }

    public void setTotalBorrows(int totalBorrows) {
        this.totalBorrows = totalBorrows;
    }

    public int getBorrowedBooksSize() {
        return borrowedBooks.size();
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - borrowed now: %d, total borrows: %d", name, id, borrowedBooks.size(), totalBorrows);
    }
}

class LibrarySystem {
    private String name;
    private HashMap<String, Member> members;
    private HashMap<String, Book> books;

    LibrarySystem(String name) {
        this.name = name;
        this.members = new HashMap<>();
        this.books = new HashMap<>();
    }

    public void registerMember(String id, String fullName) {
        Member member = new Member(id, fullName);
        members.put(id, member);
    }

    public void addBook(String isbn, String title, String author, int year) {
        if (books.containsKey(isbn)) {
            Book book = books.get(isbn);
            book.setNumberOfCopies(books.get(isbn).getNumberOfCopies() + 1);
            return;
        }
        books.put(isbn, new Book(isbn, title, author, year));
    }

    public void borrowBook(String memberId, String isbn) {
        Member member = members.get(memberId);

        if (books.containsKey(isbn)) {
            Book book = books.get(isbn);

            if (book.getNumberOfCopies() <= 0) { //nema slobodni primeroci od knigata
                book.getWaitingMembers().add(member);
            } else { // ima slobodni primeroci od knigata
                book.getCurrentBorrowers().add(member);
                book.setNumberOfCopies(book.getNumberOfCopies() - 1);
                book.setTotalBorrows(book.getTotalBorrows() + 1);
                member.getBorrowedBooks().add(book);
                member.setTotalBorrows(member.getTotalBorrows() + 1);
            }
        }
    }

    public void returnBook(String memberId, String isbn) {
        Member member = members.get(memberId);
        Book book = books.get(isbn);

        book.getCurrentBorrowers().remove(member);
        member.getBorrowedBooks().remove(book);
        book.setNumberOfCopies(book.getNumberOfCopies() + 1);

        if (!book.getWaitingMembers().isEmpty()) {
            Member first = book.getWaitingMembers().poll();
            borrowBook(first.getId(), isbn);
        }
    }

    public void printMembers() {
        members.values().stream()
                .sorted(Comparator.comparing(Member::getBorrowedBooksSize).reversed()
                        .thenComparing(Comparator.comparing(Member::getName)))
                .forEach(System.out::println);
    }

    public void printBooks() {
        books.values().stream()
                .sorted(Comparator.comparing(Book::getTotalBorrows).reversed()
                        .thenComparing(Comparator.comparing(Book::getYear)))
                .forEach(System.out::println);
    }

    public void printBookCurrentBorrowers(String isbn) {
        Book book = books.get(isbn);
        List<String> membersIDs = members.values().stream()
                .filter(m -> m.getBorrowedBooks().contains(book))
                .sorted(Comparator.comparing(Member::getId))
                .map(Member::getId)
                .collect(Collectors.toList());

        System.out.println(String.join(", ", membersIDs));
    }

    public void printTopAuthors(){
        // Author - TotalNumberOfBorrows
        Map<String, Integer> map = books.values().stream()
                .collect(Collectors.groupingBy(
                        Book::getAuthor,
                        Collectors.summingInt(Book::getTotalBorrows)
                ));

        map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry::getKey))
                .forEach(e -> System.out.println(e.getKey() + " - " + e.getValue()));
    }
}

public class LibraryTester {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            String libraryName = br.readLine();
            //   System.out.println(libraryName); //test
            if (libraryName == null) return;

            libraryName = libraryName.trim();
            LibrarySystem lib = new LibrarySystem(libraryName);

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("END")) break;
                if (line.isEmpty()) continue;

                String[] parts = line.split(" ");

                switch (parts[0]) {

                    case "registerMember": {
                        lib.registerMember(parts[1], parts[2]);
                        break;
                    }

                    case "addBook": {
                        String isbn = parts[1];
                        String title = parts[2];
                        String author = parts[3];
                        int year = Integer.parseInt(parts[4]);
                        lib.addBook(isbn, title, author, year);
                        break;
                    }

                    case "borrowBook": {
                        lib.borrowBook(parts[1], parts[2]);
                        break;
                    }

                    case "returnBook": {
                        lib.returnBook(parts[1], parts[2]);
                        break;
                    }

                    case "printMembers": {
                        lib.printMembers();
                        break;
                    }

                    case "printBooks": {
                        lib.printBooks();
                        break;
                    }

                    case "printBookCurrentBorrowers": {
                        lib.printBookCurrentBorrowers(parts[1]);
                        break;
                    }

                    case "printTopAuthors": {
                        lib.printTopAuthors();
                        break;
                    }

                    default:
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

