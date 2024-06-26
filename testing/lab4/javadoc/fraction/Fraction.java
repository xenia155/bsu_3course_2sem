package fraction;

import java.util.*;

public class Fraction implements Comparable<Fraction>, Iterable<Object>, Iterator<Object> {
    // Constructors
    public int numerator, denominator;
    public static FractionSortBy sortBy = FractionSortBy.numerator;

    Fraction(int num, int denom) throws ArithmeticException{
        if(denom == 0)
            throw new ArithmeticException("Zero division exception! Invalid fraction denominator!");
        else{
            this.numerator = num;
            if(denom < 0) {
                this.denominator = -denom;
                this.numerator = -this.numerator;
            }
            else denominator = denom;
            validateFraction();
        }
    }

    private void validateFraction(){
        int gcd = GCD(this.numerator, this.denominator);
        if(gcd != 0){
            this.numerator /= gcd;
            this.denominator /= gcd;
        }
    }

    static Fraction parseFraction(String str) throws IllegalArgumentException{
        var num_denom = str.split("/|\\s");
        int numerator, denominator;
        if (num_denom.length == 1){
            numerator = Integer.parseInt(num_denom[0]);
            denominator = 1;
            return new Fraction(numerator, denominator);
        }
        else if (num_denom.length == 2){
            numerator = Integer.parseInt(num_denom[0]);
            denominator = Integer.parseInt(num_denom[1]);
            return new Fraction(numerator, denominator);
        }
        else
            throw new IllegalArgumentException("Invalid fraction format!");
    }

    private int GCD(int a, int b){
        a = Math.abs(a);
        b = Math.abs(b);
        while(a > 0 && b > 0){
            if(a > b)
                a %= b;
            else b %= a;
        }
        return a + b;
    }

    Fraction add(Fraction f){
        int LCM = (this.denominator * f.denominator)/GCD(this.denominator, f.denominator);
        return new Fraction(this.numerator * (LCM / this.denominator) + f.numerator * (LCM / f.denominator), LCM);
    }

    Fraction subtract(Fraction f){
        f.numerator *= -1;
        Fraction result = add(f);
        f.numerator *= -1;
        return result;
    }

    Fraction multiply(Fraction f){
        return new Fraction(this.numerator * f.numerator, this.denominator * f.denominator);
    }

    Fraction divide(Fraction f){
        return new Fraction(this.numerator * f.denominator, this.denominator * f.numerator);
    }
    // Comparable<Fraction>
    enum FractionSortBy{
        numerator,
        denominator
    }

    static void setSortBy(FractionSortBy value ) {
        if(value == FractionSortBy.numerator)
            sortBy = FractionSortBy.numerator;
        else
            sortBy = FractionSortBy.denominator;
    }


    public int compareTo(Fraction entry) {
//        BigInteger b1 = BigInteger.valueOf(this.numerator);
//        b1 = b1.multiply(BigInteger.valueOf(entry.denominator));
//        BigInteger b2 = BigInteger.valueOf(entry.numerator);
//        b2 = b2.multiply(BigInteger.valueOf(this.denominator));
//
//        int result = b1.subtract(b2).intValue();
//        return result;
        int first = sortBy == FractionSortBy.numerator? this.numerator: this.denominator;
        int second = sortBy == FractionSortBy.numerator? entry.numerator: entry.denominator;
        return first-second;
    }

    public Iterator<Object> iterator() {
        return this;
    }
    // Iterable
    private int iterator_idx = 0;

    private void reset() {
        iterator_idx = 0;
    }

    public boolean hasNext() {
        return iterator_idx <= 1;
    }

    public void remove() {
        // Не реализуем так как это разрушит структуру данных
    }

    public Object next() {
        if ( iterator_idx == 0 ) {
            iterator_idx++;
            return numerator;
        }
        else if ( iterator_idx == 1 ) {
            iterator_idx++;
            return denominator;
        }
        else {
            reset();
            return null;
        }
    }

    public String toString(){
        return numerator + "/" + denominator;
    }
}