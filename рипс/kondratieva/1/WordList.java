import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WordList {
    public WordList(Scanner in) {
        words = new ArrayList<>();
        myLock = new ReentrantLock();
        while (in.hasNext()) words.add(in.next());
    }

    private class Swapper implements Runnable {
        private volatile boolean hasWork = true; // Shared flag for termination

        public void run() {
            while (hasWork) {
                myLock.lock();
                try {
                    // find a pair to swap
                    int i = 0;
                    while (i < words.size() - 1 && words.get(i).compareTo(words.get(i + 1)) <= 0)
                        i++;
                    if (i < words.size() - 1) {
                        String word1 = words.get(i);
                        String word2 = words.get(i + 1);
                        words.set(i, word2);
                        words.set(i + 1, word1);
                    } else {
                        hasWork = false; // No work left, terminate the thread
                    }
                } finally {
                    myLock.unlock();
                }
            }
        }
    }

    private class DupeRemover implements Runnable {
        private volatile boolean hasWork = true; // Shared flag for termination

        public void run() {
            while (hasWork) {
                myLock.lock();
                try {
                    // find a duplicate
                    Set<String> uniqueWords = new HashSet<>(words);
                    ArrayList<String> newWords = new ArrayList<>(uniqueWords);
                    if (newWords.size() < words.size()) {
                        words = newWords;
                    } else {
                        hasWork = false; // No work left, terminate the thread
                    }
                } finally {
                    myLock.unlock();
                }
            }
        }
    }

    public void slowSort() throws InterruptedException {
        Runnable r1 = new Swapper();
        Runnable r2 = new DupeRemover();
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(words);
    }

    private ArrayList<String> words;
    private Lock myLock;

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner in = new Scanner(new FileReader("data.txt"));
        WordList list = new WordList(in);
        list.slowSort();
    }
}
