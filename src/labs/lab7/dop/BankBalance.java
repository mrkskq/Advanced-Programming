package labs.lab7.dop;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class BankBalance {

    // Shared bank account
    public static class BankAccount {
        private int balance;
        private final ReadWriteLock lock = new ReentrantReadWriteLock();


        public BankAccount(int initialBalance) {
            this.balance = initialBalance;
        }

        public boolean deposit(int amount) {
            lock.writeLock().lock();
            try {
                balance += amount;
                return true;
            } finally {
                lock.writeLock().unlock();
            }
        }

        public boolean withdraw(int amount) {
            lock.writeLock().lock();
            try {
                if (balance >= amount) {
                    balance -= amount;
                    return true;
                }
                return false;
            } finally {
                lock.writeLock().unlock();
            }
        }

        public int getBalance() {
            lock.readLock().lock();
            try {
                return balance;
            } finally {
                lock.readLock().unlock();
            }
        }
    }


    // Operation result
    public static class OperationResult {
        public final int operationId;
        public final String type; // -------------> dali e deposit ili withdraw
        public final int amount; // -------------> amount za deposit ili withdraw
        public final boolean success;

        public OperationResult(int operationId, String type, int amount, boolean success) {
            this.operationId = operationId;
            this.type = type;
            this.amount = amount;
            this.success = success;
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        int initialBalance = sc.nextInt();
        int n = sc.nextInt(); // number of operations

        BankAccount account = new BankAccount(initialBalance);

        List<Callable<OperationResult>> tasks = new ArrayList<>();

        long lockTimeoutMs = 100; // max time to wait for the lock

        for (int i = 0; i < n; i++) {
            String type = sc.next();
            int amount = sc.nextInt();
            int operationId = i + 1;

            tasks.add(() -> {

                Thread.sleep(3000);

                boolean success;
                if (type.equals("deposit")) {
                    success = account.deposit(amount);
                } else { // withdraw
                    success = account.withdraw(amount);
                }

                return new OperationResult(operationId, type, amount, success);
            });
        }

        ExecutorService executor =
                Executors.newFixedThreadPool(4);

        List<Future<OperationResult>> futures = executor.invokeAll(tasks);

        List<OperationResult> results = new ArrayList<>();
        for (Future<OperationResult> f : futures) {
            results.add(f.get());
        }

        executor.shutdown();

        results.sort(Comparator.comparingInt(i -> i.operationId)); // -------------> sortiraj gi operaciite po id

        //  -------------> deterministicki log na site operacii
        for (OperationResult r : results) {
            System.out.printf("Operation %d: %s %d %s\n", r.operationId, r.type, r.amount, (r.success ? "SUCCESS" : "FAILED"));
        }

        // Deterministic final balance
        System.out.println("FINAL_BALANCE " + account.getBalance());
    }

}