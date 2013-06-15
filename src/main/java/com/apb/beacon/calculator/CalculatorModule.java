package com.apb.beacon.calculator;

import com.google.inject.AbstractModule;

public class CalculatorModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Calculator.class).to(CalculatorImpl.class);
	}
}
