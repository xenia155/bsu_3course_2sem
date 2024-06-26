package com.mypackage.guessthenumber;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private static RandNum random_number;

    @BeforeClass
    public static void setUpBeforeTest(){
        random_number = new RandNum(1, 200);
    }

    @Test
    public void invalidBordersTest() {
        try{
            random_number.setBorders(200, 1);
        }
        catch (IllegalArgumentException ex){
            assertEquals("Invalid range" ,ex.getMessage());
        }
    }

    @Test
    public void setBordersTest() {
        random_number.setBorders(5, 99);
        int bottom = random_number.getBottom();
        int top = random_number.getTop();
        assertEquals(5, bottom);
        assertEquals(99, top);
    }
    @Test
    public void newNumberGenerationTest() {
        Boolean result = true;
        int num = random_number.getNumber();
        random_number.generateRandNum();
        for (int i = random_number.getBottom(); i <= random_number.getTop(); i++){
            if (num == random_number.getNumber()){
                result = false;
                break;
            }
        }
        assertTrue(result);
    }
}