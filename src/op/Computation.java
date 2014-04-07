package hk.ust.ins.op;

import hk.ust.ins.data.ThreeDimensionalDouble;

public class Computation {
	public static ThreeDimensionalDouble excute (ThreeDimensionalDouble lastVelocity,
			ThreeDimensionalDouble velocity, double timeInterval) {
		
		double x = formula(timeInterval, lastVelocity.x, velocity.x);
		double y = formula(timeInterval, lastVelocity.y, velocity.y);
		double z = formula(timeInterval, lastVelocity.z, velocity.z);

		return new ThreeDimensionalDouble (x, y, z);
	}
	
	public static ThreeDimensionalDouble calculateVelocity (ThreeDimensionalDouble lastVelocity,
			ThreeDimensionalDouble lastAcceleration, ThreeDimensionalDouble acceleration, double timeInterval) {
		
		ThreeDimensionalDouble velocity = new ThreeDimensionalDouble(lastVelocity);
		
		double x = formula(timeInterval, lastAcceleration.x, acceleration.x);
		double y = formula(timeInterval, lastAcceleration.y, acceleration.y);
		double z = formula(timeInterval, lastAcceleration.z, acceleration.z);
		
		return velocity.add(new ThreeDimensionalDouble(x, y, z));
	}
	
	private static double formula (double a, double b, double c) {
		return 0.5 * a * (b + c);
	}
}
