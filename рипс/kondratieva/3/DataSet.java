import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataSet {
    /**
     * Constructs an empty data set.
     */
    public DataSet(double[] values) {
        sum = 0;
        this.values = values;
        myLock = new ReentrantLock();
    }

    private class RangeProcessor implements Runnable {
        public RangeProcessor(int start, int end) {
            this.start = start;
            this.end = Math.min(end, values.length);
        }

        public void run() {
            double total = 0;
            for (int i = start; i < end; i++)
                total += values[i];
            
            // Acquire the lock before updating shared variables
            myLock.lock();
            try {
                sum += total;
                completionCount++;
            } finally {
                // Release the lock
                myLock.unlock();
            }
        }

        private int start;
        private int end;
    }

    /**
     * Gets the average of the added data.
     *
     * @return the average or 0 if no data has been added
     */
    public double getAverage() {
        if (values.length == 0) return 0;

        final int THREADS = 10;
        int size = values.length / THREADS;
        for (int i = 0; i < THREADS; i++) {
            Runnable r = new RangeProcessor(i * size, (i + 1) * size);
            Thread t = new Thread(r);
            t.start();
        }

        // busy waiting is not an optimal solution
        final int DELAY = 100;
        try {
            while (completionCount < THREADS) {
                Thread.sleep(DELAY);
            }
        } catch (InterruptedException ex) {
            return 0;
        }

        return sum / values.length;
    }

    private double[] values;
    private double sum;
    private int completionCount;
    private Lock myLock;

    // this method is used to check your work
    public static void main(String[] args) {
        final int SIZE = 10000000;
        double[] v = new double[SIZE];
        for (int i = 0; i < SIZE; i++) v[i] = i;
        DataSet data = new DataSet(v);
        double avg = data.getAverage();
        System.out.println(avg);
    }
}

