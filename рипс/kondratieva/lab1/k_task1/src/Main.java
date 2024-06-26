
public class Main {

    private final static int RANGE_END = 1_000_000_000;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int primesCount = countPrimesInRange(2, RANGE_END);
        long elapsedTime = System.currentTimeMillis() - start;
        System.out.println("Потоки нашли " +
                primesCount + " простых чисел за " + (elapsedTime / 1000.0) + " секунд.");
    }

    private static int countPrimesInRange(int min, int max) {
        int count = 0;
        for (int num = min; num <= max; num++)
            if (isPrime(num))
                count++;
        return count;
    }
    private static boolean isPrime(int number) {
        assert number > 1;
        int top = (int) Math.sqrt(number);
        for (int i = 2; i <= top; i++)
            if (number % i == 0)
                return false;
        return true;
    }
}
