package com.example.demo;

import com.tenjinworkbench.annotations.Tenjin;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CalculatorTest {

    @Test
    @Tenjin(ref ="TC_01")
    public void testAddition() {
        Calculator calculator = new Calculator();
        int result = calculator.add(2, 3);
        Assert.assertEquals(result, 5);
    }

    @Test
    @Tenjin(ref ="TC_02")
    public void testDivision() {
        Calculator calculator = new Calculator();
        int result = calculator.divide(10, 2);
        Assert.assertEquals(result, 5);
    }

    @Test(expectedExceptions = ArithmeticException.class)
    @Tenjin(ref ="TC_03")
    public void testDivisionByZero() {
        Calculator calculator = new Calculator();
        calculator.divide(10, 0);
    }
}
