package org.iilab.pb.calculator;


import junit.framework.TestCase;

import org.iilab.pb.calculator.CalculatorImpl;
import org.iilab.pb.calculator.CalculatorImpl.Button;
import org.junit.Test;

import static org.iilab.pb.calculator.CalculatorImpl.Button.DIVIDE;
import static org.iilab.pb.calculator.CalculatorImpl.Button.EIGHT;
import static org.iilab.pb.calculator.CalculatorImpl.Button.EQUALS;
import static org.iilab.pb.calculator.CalculatorImpl.Button.FIVE;
import static org.iilab.pb.calculator.CalculatorImpl.Button.FOUR;
import static org.iilab.pb.calculator.CalculatorImpl.Button.MINUS;
import static org.iilab.pb.calculator.CalculatorImpl.Button.MULTIPLY;
import static org.iilab.pb.calculator.CalculatorImpl.Button.NINE;
import static org.iilab.pb.calculator.CalculatorImpl.Button.ONE;
import static org.iilab.pb.calculator.CalculatorImpl.Button.PLUS;
import static org.iilab.pb.calculator.CalculatorImpl.Button.SEVEN;
import static org.iilab.pb.calculator.CalculatorImpl.Button.SIX;
import static org.iilab.pb.calculator.CalculatorImpl.Button.THREE;
import static org.iilab.pb.calculator.CalculatorImpl.Button.TWO;
import static org.iilab.pb.calculator.CalculatorImpl.Button.ZERO;

public class CalculatorImplTest extends TestCase {

	private String pressButtons(CalculatorImpl c, Button... buttons) {
		String result = null;
		for(Button b : buttons) result = c.handleButtonPress(b);
		return result;
	}

	@Test
	public void testAddition() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, ONE, PLUS, TWO, EQUALS);
		assertEquals("3", result);
	}

	@Test
	public void testAdditionWithNegativeFirstOperand() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, MINUS, THREE, PLUS, FOUR, EQUALS);
		assertEquals("1", result);
	}

	@Test
	public void testAdditionWithNegativeSecondOperand() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, FOUR, PLUS, MINUS, FIVE, EQUALS);
		assertEquals("-1", result);
	}

	@Test
	public void testAdditionWithMultiDigitOperands() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, MINUS, SIX, SEVEN, PLUS, EIGHT, NINE,
				EQUALS);
		assertEquals("22", result);
	}

	@Test
	public void testSubtraction() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, TWO, MINUS, ONE, EQUALS);
		assertEquals("1", result);
	}

	@Test
	public void testMultiplication() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, TWO, MULTIPLY, TWO, EQUALS);
		assertEquals("4", result);
	}

	@Test
	public void testDivision() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, ONE, DIVIDE, TWO, EQUALS);
		assertEquals("0.5", result);
	}

	@Test
	public void testDivisionByZeroClearsScreen() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, ONE, DIVIDE, ZERO, EQUALS);
		assertEquals("0", result);
	}

	@Test
	public void testDivisionWithRecurringResultIsTruncated() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, ONE, DIVIDE, THREE, EQUALS);
		assertEquals("0.3333333", result);
	}

	@Test
	public void testMultiplicationWithNegativeFirstOperand() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, MINUS, TWO, MULTIPLY, THREE, EQUALS);
		assertEquals("-6", result);
	}

	@Test
	public void testMultiplicationWithNegativeSecondOperand() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, TWO, MULTIPLY, MINUS, THREE, EQUALS);
		assertEquals("-6", result);
	}

	@Test
	public void testSubtractionWithNegativeSecondOperand() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, ONE, MINUS, MINUS, TWO, EQUALS);
		assertEquals("3", result);
	}

	@Test
	public void testMinusOperatorWithoutOperandStartsNegativeOperand() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, MINUS);
		assertEquals("-", result);
	}

	@Test
	public void testNonMinusOperatorWithoutOperandClearsScreen() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, PLUS);
		assertEquals("0", result);
		result = pressButtons(c, MULTIPLY);
		assertEquals("0", result);
		result = pressButtons(c, DIVIDE);
		assertEquals("0", result);
	}

	@Test
	public void testEqualsWithoutOperandOrOperatorClearsScreen() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, EQUALS);
		assertEquals("0", result);
	}

	@Test
	public void testEqualsWithoutOperatorClearsScreen() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, ONE, EQUALS);
		assertEquals("0", result);
	}

	@Test
	public void testEqualsWithoutSecondOperandClearsScreen() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, ONE, PLUS, EQUALS);
		assertEquals("0", result);
	}

	@Test
	public void testDigitAfterResultStartsNewCalculation() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, THREE, PLUS, ONE, EQUALS, TWO);
		assertEquals("2", result);
	}

	@Test
	public void testOperatorAfterResultContinuesCalculation() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, ONE, PLUS, ONE, EQUALS);
		assertEquals("2", result);
		result = pressButtons(c, PLUS, ONE, EQUALS);
		assertEquals("3", result);
	}

	@Test
	public void testChainedCalculations() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, TWO, MULTIPLY, TWO, MINUS, ONE, EQUALS);
		assertEquals("3", result);
	}

	@Test
	public void testMinusSignIsDisplayed() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, MINUS, ONE, TWO, THREE);
		assertEquals("-123", result);
		result = pressButtons(c, PLUS, ONE, EQUALS);
		assertEquals("-122", result);
	}

	@Test
	public void testOperatorsAreDisplayed() {
		CalculatorImpl c = new CalculatorImpl();
		String result = pressButtons(c, ONE, PLUS);
		assertEquals("+", result);
		result = pressButtons(c, ONE, MINUS);
		assertEquals("-", result);
		result = pressButtons(c, ONE, MULTIPLY);
		assertEquals("\u00d7", result);
		result = pressButtons(c, ONE, DIVIDE);
		assertEquals("\u00f7", result);
		result = pressButtons(c, ONE, EQUALS);
		assertEquals("1", result);
	}
}
