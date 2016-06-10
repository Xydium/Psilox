package com.xydium.geometry;

public class TrigFunction {

	private double amplitude;
	private double frequency;
	private double thetaIncrease;
	private double theta;
	private TrigFunctionType type;
	
	public TrigFunction(double amplitude, double frequency, double thetaIncrease, TrigFunctionType type) {
		this.amplitude = amplitude;
		this.frequency = frequency;
		this.thetaIncrease = thetaIncrease;
		this.theta = 0;
		this.type = type;
	}
	
	public TrigFunction(double amplitude, double frequency, TrigFunctionType type) {
		this(amplitude, frequency, Math.PI / 180, type);
	}
	
	public TrigFunction(double amplitude, TrigFunctionType type) {
		this(amplitude, 1.0, type);
	}
	
	public TrigFunction(TrigFunctionType type) {
		this(1.0, 1.0, type);
	}
	
	public TrigFunction() {
		this(TrigFunctionType.SIN);
	}
	
	public double next() {
		theta += thetaIncrease;
		return evaluate(theta - thetaIncrease);
	}
	
	public double nextInverse() {
		try {
			return 1 / next();			
		} catch(ArithmeticException e) {
			return Double.POSITIVE_INFINITY;
		}
	}
	
	public double evaluate(double x) {
		return amplitude * type.function.evaluate(frequency * x);
	}
	
	public double getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(double amplitude) {
		this.amplitude = amplitude;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public double getThetaIncrease() {
		return thetaIncrease;
	}

	public void setThetaIncrease(double thetaIncrease) {
		this.thetaIncrease = thetaIncrease;
	}

	public double getTheta() {
		return theta;
	}

	public void setTheta(double theta) {
		this.theta = theta;
	}

	public TrigFunctionType getType() {
		return type;
	}

	public void setType(TrigFunctionType type) {
		this.type = type;
	}

	//TODO: Add Inverse Trig Functions
	public enum TrigFunctionType {
		SIN(Math::sin),
		COS(Math::cos),
		TAN(Math::tan);
		
		public Function function;
		
		private TrigFunctionType(Function function) {
			this.function = function;
		}
		
		private interface Function {
			public double evaluate(double theta);
		}
	}
	
}
