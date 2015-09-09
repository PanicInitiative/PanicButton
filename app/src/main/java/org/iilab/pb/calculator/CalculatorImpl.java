package org.iilab.pb.calculator;

import java.math.BigDecimal;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL32;

public class CalculatorImpl {

	public enum Button {
		ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, ZERO,
		EQUALS, PLUS, MINUS, MULTIPLY, DIVIDE, CHAR_C, DECIMAL_POINT
	}
	
	private enum Operation { PLUS, MINUS, MULTIPLY, DIVIDE };

	private boolean clearOnDigit = false, negativeNumber = false;
	private boolean decimalPoint = false;
	private int decimalPointPlace = 0;
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
		case CHAR_C:
			return clearDisplay().toPlainString();
		case DECIMAL_POINT:
			return pushDecimal().toPlainString();
		default:
			throw new IllegalStateException();
		}
	}

	private BigDecimal pushDigit(int digit) {
		if(clearOnDigit || operand1 == null) {
			if(negativeNumber) operand1 = BigDecimal.valueOf(-digit);
			else operand1 = BigDecimal.valueOf(digit);
		} else {
			if(!decimalPoint){
				operand1 = operand1.multiply(TEN);
			}
			if(operand1.signum() == -1) {
				if(decimalPoint){
					BigDecimal temp = BigDecimal.valueOf(digit);
					temp = temp.movePointLeft(decimalPointPlace);
					double v = operand1.doubleValue()-temp.doubleValue();
					operand1 = BigDecimal.valueOf(v);
					decimalPointPlace = decimalPointPlace+1;
				}else{
					operand1 = operand1.subtract(BigDecimal.valueOf(digit));
				}
			} else {
				if(decimalPoint){
					BigDecimal temp = BigDecimal.valueOf(digit);
					temp = temp.movePointLeft(decimalPointPlace);
					double v = operand1.doubleValue()+temp.doubleValue();
					operand1 = BigDecimal.valueOf(v);
					decimalPointPlace = decimalPointPlace+1;
				}else{
					operand1 = operand1.add(BigDecimal.valueOf(digit));
				}
			}
		}
		
		clearOnDigit = negativeNumber = false;
		return operand1;
	}
	
	private BigDecimal pushDecimal() {
		if(!decimalPoint){
			decimalPointPlace = 0;
			if(clearOnDigit || operand1 == null) {
				if(negativeNumber) operand1 = BigDecimal.valueOf(-0.);
				else operand1 = BigDecimal.valueOf(0.);
			}
			if (operand1.signum() == -1) {
				decimalPointPlace = String.valueOf(operand1.intValue())
						.length() - 1;
			} else {
				decimalPointPlace = String.valueOf(operand1.intValue())
						.length();
			}
			decimalPoint = true;
			
		}
		/*if (operand1 != null) {
			if (!decimalPoint) {
				decimalPointPlace = 0;
				if (operand1.signum() == -1) {
					decimalPointPlace = String.valueOf(operand1.intValue())
							.length() - 1;
				} else {
					decimalPointPlace = String.valueOf(operand1.intValue())
							.length();
				}
				decimalPoint = true;
			}
			return operand1;
		}else{
			decimalPointPlace = 0;
			if(clearOnDigit || operand1 == null) {
				if(negativeNumber) operand1 = BigDecimal.valueOf(-0.);
				else operand1 = BigDecimal.valueOf(0.);
			}
			if (operand1.signum() == -1) {
				decimalPointPlace = String.valueOf(operand1.intValue())
						.length() - 1;
			} else {
				decimalPointPlace = String.valueOf(operand1.intValue())
						.length();
			}
			decimalPoint = true;
			
			return operand1;
		}
		*/
		return operand1;
		
		
	}

	private boolean pushOperation(Operation o) {
		decimalPoint=false;
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
		decimalPoint=false;
	}
	
	private BigDecimal clearDisplay(){
		clear();
		return ZERO;
		
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
		decimalPoint=false;
		return result;
	}
}
