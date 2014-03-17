package com.apb.beacon.calculator;

import java.math.BigDecimal;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL32;

public class CalculatorImpl {

	public enum Button {
		ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, ZERO,
		EQUALS, PLUS, MINUS, MULTIPLY, DIVIDE
	}
	
	private enum Operation { PLUS, MINUS, MULTIPLY, DIVIDE };

	private boolean clearOnDigit = false, negativeNumber = false;
	private BigDecimal operand1 = null, operand2 = null;
	private Operation operation = null;

	public String handleButtonPress(Button button) {
		switch(button) {
		case ONE:
			return pushDigit(1).toPlainString();
		case TWO:
			return pushDigit(2).toPlainString();
		case THREE:
			return pushDigit(3).toPlainString();
		case FOUR:
			return pushDigit(4).toPlainString();
		case FIVE:
			return pushDigit(5).toPlainString();
		case SIX:
			return pushDigit(6).toPlainString();
		case SEVEN:
			return pushDigit(7).toPlainString();
		case EIGHT:
			return pushDigit(8).toPlainString();
		case NINE:
			return pushDigit(9).toPlainString();
		case ZERO:
			return pushDigit(0).toPlainString();
		case EQUALS:
			return popResult().toPlainString();
		case PLUS:
			return pushOperation(Operation.PLUS) ? "+" : "0";
		case MINUS:
			return pushOperation(Operation.MINUS) ? "-" : "0";
		case MULTIPLY:
			return pushOperation(Operation.MULTIPLY) ? "\u00d7" : "0";
		case DIVIDE:
			return pushOperation(Operation.DIVIDE) ? "\u00f7" : "0";
		default:
			throw new IllegalStateException();
		}
	}

	private BigDecimal pushDigit(int digit) {
		if(clearOnDigit || operand1 == null) {
			if(negativeNumber) operand1 = BigDecimal.valueOf(-digit);
			else operand1 = BigDecimal.valueOf(digit);
		} else {
			operand1 = operand1.multiply(TEN);
			if(operand1.signum() == -1) {
				operand1 = operand1.subtract(BigDecimal.valueOf(digit));
			} else {
				operand1 = operand1.add(BigDecimal.valueOf(digit));
			}
		}
		clearOnDigit = negativeNumber = false;
		return operand1;
	}

	private boolean pushOperation(Operation o) {
		if(operand1 == null) {
			if(o == Operation.MINUS && !negativeNumber) {
				negativeNumber = true;
				return true;
			}
			clear();
			return false;
		}
		if(operand2 == null) {
			clearOnDigit = negativeNumber = false;
			operand2 = operand1;
			operand1 = null;
			operation = o;
			return true;
		}
		clearOnDigit = negativeNumber = false;
		operand2 = popResult();
		operand1 = null;
		operation = o;
		return true;
	}

	private void clear() {
		clearOnDigit = negativeNumber = false;
		operand1 = operand2 = null;
		operation = null;
	}

	private BigDecimal popResult() {
		if(operand1 == null || operand2 == null || operation == null) {
			clear();
			return ZERO;
		}
		BigDecimal result = null;
		switch(operation) {
		case PLUS:
			result = operand2.add(operand1);
			break;
		case MINUS:
			result = operand2.subtract(operand1);
			break;
		case MULTIPLY:
			result = operand2.multiply(operand1);
			break;
		case DIVIDE:
			try {
				result = operand2.divide(operand1, DECIMAL32);
			} catch(ArithmeticException e) {
				result = ZERO;
			}
			break;
		default:
			throw new IllegalStateException();
		}
		clearOnDigit = true;
		negativeNumber = false;
		operand1 = result;
		operand2 = null;
		operation = null;
		return result;
	}
}
