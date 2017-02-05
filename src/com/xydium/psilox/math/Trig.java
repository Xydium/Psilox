package com.xydium.psilox.math;

public class Trig {

	private double amplitude;
	private double frequency;
	private double thetaIncrease;
	private double theta;
	private TrigFunctionType type;
	
	public Trig(double amplitude, double frequency, double thetaIncrease, TrigFunctionType type) {
		this.amplitude = amplitude;
		this.frequency = frequency;
		this.thetaIncrease = thetaIncrease;
		this.theta = 0;
		this.type = type;
	}
	
	public Trig(double amplitude, double frequency, TrigFunctionType type) {
		this(amplitude, frequency, Math.PI / 180, type);
	}
	
	public Trig(double amplitude, TrigFunctionType type) {
		this(amplitude, 1.0, type);
	}
	
	public Trig(TrigFunctionType type) {
		this(1.0, 1.0, type);
	}
	
	public Trig() {
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

	public enum TrigFunctionType {
		SIN(Math::sin),
		COS(Math::cos),
		TAN(Math::tan),
		CSC(theta -> {
			double sin = Math.sin(theta);
			try {
				return 1 / sin; 
			} catch(Exception e) {
				if(Math.sin(theta - 0.01) > 0) {
					return Double.POSITIVE_INFINITY;
				} else {
					return Double.NEGATIVE_INFINITY;
				}
			}
		}),
		SEC(theta -> {
			double cos = Math.cos(theta);
			try {
				return 1 / cos; 
			} catch(Exception e) {
				if(Math.cos(theta - 0.01) > 0) {
					return Double.POSITIVE_INFINITY;
				} else {
					return Double.NEGATIVE_INFINITY;
				}
			}
		}),
		COT(theta -> {
			double tan = Math.tan(theta);
			try {
				return 1 / tan; 
			} catch(Exception e) {
				if(Math.tan(theta - 0.01) > 0) {
					return Double.POSITIVE_INFINITY;
				} else {
					return Double.NEGATIVE_INFINITY;
				}
			}
		});
		
		public Function function;
		
		private TrigFunctionType(Function function) {
			this.function = function;
		}
		
		private interface Function {
			public double evaluate(double theta);
		}
	}
	
}