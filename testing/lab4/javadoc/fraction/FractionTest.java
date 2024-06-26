package fraction;

import static org.junit.Assert.*;

import fraction.Fraction;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

public class FractionTest {

    @Test
    public void testConstructorWithValidInputs() {
        Fraction f = new Fraction(4, 3);
        assertEquals("4/3", f.toString());
    }

    @Test(expected = ArithmeticException.class)
    public void testConstructorWithZeroDenominator() {
        new Fraction(4, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsingFromInvalidString() {
        Fraction.parseFraction("invalid");
    }

    @Test
    public void testAddition() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.add(f2);
        assertEquals("5/6", result.toString());
    }

    @Test
    public void testSubtraction() {
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(1, 4);
        Fraction result = f1.subtract(f2);
        assertEquals("1/2", result.toString());
    }

    @Test
    public void testMultiplication() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(3, 4);
        Fraction result = f1.multiply(f2);
        assertEquals("1/2", result.toString());
    }

    @Test
    public void testDivision() {
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(3, 4);
        Fraction result = f1.divide(f2);
        assertEquals("8/9", result.toString());
    }

    @Test
    public void testIterator() {
        Fraction f = new Fraction(3, 4);
        Iterator<Object> iterator = f.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(4, iterator.next());
        assertFalse(iterator.hasNext());
    }
}
