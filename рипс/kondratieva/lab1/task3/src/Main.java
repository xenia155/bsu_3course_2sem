import java.util.Scanner;

public class Main {

    private final static int MAX = 10_000_000;

    private static int countPrimesSequential() {
        int count = 0;
        for (int i = 2; i < MAX; i++) {
            if (isPrime(i)) {
                count++;
            }
        }
        return count;
    }

    private static int countPrimesParallel(int numberOfThreads) throws InterruptedException {
        int count = 0;
        WorkerThread[] workerThreads = new WorkerThread[numberOfThreads];
        int numbersPerThread = MAX / numberOfThreads;
        int start = 2;
        int end = start + numbersPerThread - 1;

        for (int i = 0; i < numberOfThreads; i++) {
            if (i == numberOfThreads - 1) {
                end = MAX;
            }
            workerThreads[i] = new WorkerThread(start, end);
            workerThreads[i].start();
            start = end + 1;
            end = start + numbersPerThread - 1;
        }

        for (int i = 0; i < numberOfThreads; i++) {
            workerThreads[i].join();
            count += workerThreads[i].getCount();
        }

        return count;
    }

    private static boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    private static class WorkerThread extends Thread {
        private int start;
        private int end;
        private int count;

        public WorkerThread(int start, int end) {
            this.start = start;
            this.end = end;
            this.count = 0;
        }

        public int getCount() {
            return count;
        }

        public void run() {
            for (int i = start; i <= end; i++) {
                if (isPrime(i)) {
                    count++;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Последовательная программа
        long startTimeSequential = System.currentTimeMillis();
        int countSequential = countPrimesSequential();
        long elapsedTimeSequential = System.currentTimeMillis() - startTimeSequential;
        System.out.println("Sequential program counted " +
                countSequential + " primes in " + (elapsedTimeSequential / 1000.0) + " seconds.");

        // Многопоточная программа
        Scanner scanner = new Scanner(System.in);
        int numberOfThreads;
        do {
            System.out.print("\nEnter the number of threads (1-30): ");
            numberOfThreads = scanner.nextInt();
        } while (numberOfThreads < 1 || numberOfThreads > 30);

        long startTimeParallel = System.currentTimeMillis();
        int countParallel = countPrimesParallel(numberOfThreads);
        long elapsedTimeParallel = System.currentTimeMillis() - startTimeParallel;
        System.out.println("Multithreaded program with " + numberOfThreads + " threads counted " +
                countParallel + " primes in " + (elapsedTimeParallel / 1000.0) + " seconds.");

        // Эксперименты с разным количеством потоков
        System.out.println("\nExperiments with different number of threads:");
        int[] threadCounts = {1, 2, 4, 8};
        for (int threadCount : threadCounts) {
            long startTime = System.currentTimeMillis();
            int count = countPrimesParallel(threadCount);
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Thread count: " + threadCount +
                    ", Counted " + count + " primes in " + (elapsedTime / 1000.0) + " seconds.");
        }
    }
}