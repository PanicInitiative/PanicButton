package com.apb.beacon.calculator;

import static com.apb.beacon.calculator.Calculator.Button.DIVIDE;
import static com.apb.beacon.calculator.Calculator.Button.EQUALS;
import static com.apb.beacon.calculator.Calculator.Button.MINUS;
import static com.apb.beacon.calculator.Calculator.Button.MULTIPLY;
import static com.apb.beacon.calculator.Calculator.Button.ONE;
import static com.apb.beacon.calculator.Calculator.Button.PLUS;
import static com.apb.beacon.calculator.Calculator.Button.THREE;
import static com.apb.beacon.calculator.Calculator.Button.TWO;
import static com.apb.beacon.calculator.Calculator.Button.ZERO;
import junit.framework.TestCase;

import org.junit.Test;

import com.apb.beacon.calculator.Calculator.Button;

public class CalculatorImplTest extends TestCase {

	private String pressButtons(Calculator c, Button... buttons) {
		String result = null;
		for(Button b : buttons) result = c.handleButtonPress(b);
		return result;
	}

	@Test
	public void testAddition() {
		Calculator c = new CalculatorImpl();
		String result = pressButtons(c, ONE, PLUS, ONE, EQUALS);
		assertEquals("2", result);
	}

	@Test
	public void testSubtraction() {
		Calculator c = new CalculatorImpl();
		String result = pressButtons(c, TWO, MINUS, ONE, EQUALS);
		assertEquals("1", result);
	}

	@Test
	public void testMultiplication() {
		Calculator c = new CalculatorImpl();
		String result = pressButtons(c, TWO, MULTIPLY, TWO, EQUALS);
		assertEquals("4", result);
	}

	@Test
	public void testDivision() {
		Calculator c = new CalculatorImpl();
		String result = pressButtons(c, ONE, DIVIDE, TWO, EQUALS);
		assertEquals("0.5", result);
	}

	@Test
	public void testDivisionByZeroClearsScreen() {
		Calculator c = new CalculatorImpl();
		String result = pressButtons(c, ONE, DIVIDE, ZERO, EQUALS);
		assertEquals("0", result);
	}

	@Test
	public void testDivisionWithRecurringResultIsTruncated() {
		Calculator c = new CalculatorImpl();
		String result = pressButtons(c, ONE, DIVIDE, THREE, EQUALS);
		assertEquals("0.3333333", result);
	}

	@Test
	public void testMultiplicationWithNegativeFirstOperand() {
		Calculator c = new CalculatorImpl();
		String result = pressButtons(c, MINUS, TWO, MULTIPLY, THREE, EQUALS);
		assertEquals("-6", result);
	}

	@Test
	public void testMultiplicationWithNegativeSecondOperand() {
		Calculator c = new CalculatorImpl();
		String result = pressButtons(c, TWO, MULTIPLY, MINUS, THREE, EQUALS);
		assertEquals("-6", result);
	}

	@Test
	public void testSubtractionWithNegativeSecondOperand() {
		Calculator c = new CalculatorImpl();
		String result = pressButtons(c, ONE, MINUS, MINUS, TWO, EQUALS);
		assertEquals("3", result);
	}

	@Test
	public void testOperatorWithoutOperandClearsScreen() {
		Calculator c = new CalculatorImpl();
		String result = pressButtons(c, PLUS);
		assertEquals("0", result);
	}

	@Test
	public void testEqualsWithoutOperandOrOperatorClearsScreen() {
		Calculator c = new CalculatorImpl();
		String result = pressButtons(c, EQUALS);
		assertEquals("0", result);
	}

	@Test
	public void testEqualsWithoutOperatorClearsScreen() {
		Calculator c = new CalculatorImpl();
		String result = pressButtons(c, ONE, EQUALS);
		assertEquals("0", result);
	}

	@Test
	public void testEqualsWithoutSecondOperandClearsScreen() {
		Calculator c = new CalculatorImpl();
		String result = pressButtons(c, ONE, PLUS, EQUALS);
		assertEquals("0", result);
	}

	@Test
	public void testDigitAfterResultStartsNewCalculation() {
		Calculator c = new CalculatorImpl();
		String result = pressButtons(c, THREE, PLUS, ONE, EQUALS, TWO);
		assertEquals("2", result);
	}

	@Test
	public void testOperatorAfterResultContinuesCalculation() {
		Calculator c = new CalculatorImpl();
		String result = pressButtons(c, ONE, PLUS, ONE, EQUALS);
		assertEquals("2", result);
		result = pressButtons(c, PLUS, ONE, EQUALS);
		assertEquals("3", result);
	}

	@Test
	public void testChainedCalculations() {
		Calculator c = new CalculatorImpl();
		String result = pressButtons(c, TWO, MULTIPLY, TWO, MINUS, ONE, EQUALS);
		assertEquals("3", result);
	}
}
