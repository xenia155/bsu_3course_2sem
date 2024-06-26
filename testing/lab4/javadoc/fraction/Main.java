package fraction;

import java.lang.reflect.Array;
import java.util.Arrays;
/**
 * Главный класс для демонстрации работы с дробями.
 */
public class Main {
    /**
     * Метод main запускает демонстрационные операции с дробями.
     *
     * @param args Аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        System.out.println("Hard code constructor...");
        Fraction f1 = new Fraction(4, 3);
        System.out.println("f1: " + f1.toString());
        System.out.println("Parsing from string...");
        Fraction f2 = Fraction.parseFraction("-15/-9");
        System.out.println("f2: " + f2.toString());
        System.out.println("*****************************************************************");
        System.out.println("Checking simple math operations...");
        System.out.println("f1 + f2 = " + f1.add(f2));
        System.out.println("f1 - f2 = " + f1.subtract(f2));
        System.out.println("f1 * f2 = " + f1.multiply(f2));
        System.out.println("f1 / f2 = " + f1.divide(f2));

        System.out.println("foreach in fraction...");
        for(var i : f1)
            System.out.println(i);
        System.out.println("*****************************************************************");

        System.out.println("Array of fractions, [f1, f2, f3], sorted by numerator, foreach...");
        Fraction[] fractions = new Fraction[3];
        Fraction f3 = new Fraction(1, 4);
        fractions[0] = f2;
        fractions[1] = f1;
        fractions[2] = f3;
        Arrays.sort(fractions);
        for (var f : fractions){
            System.out.println("Fraction: " + f.toString());
        }
        System.out.println("*****************************************************************");
        System.out.println("Array of fractions, [f1, f2, f3], sorted by denominator, foreach...");
        Fraction.setSortBy(Fraction.FractionSortBy.denominator);
        Arrays.sort(fractions);
        for (var f : fractions){
            System.out.println("Fraction " + f.toString());
        }

    }
}

