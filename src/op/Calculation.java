package hk.ust.ins.op;

import hk.ust.ins.data.ThreeDimensionalDouble;

public class Calculation {
	public static ThreeDimensionalDouble execute_update(ThreeDimensionalDouble preRotation, 
				ThreeDimensionalDouble preRotationRate, ThreeDimensionalDouble currentRotationRate, double interval){
		
		double post_alpha = preRotation.x + 0.5 * interval * (preRotationRate.x + currentRotationRate.x);
		double post_beta = preRotation.y + 0.5 * interval * (preRotationRate.y + currentRotationRate.y);
		double post_gama = preRotation.z + 0.5 * interval * (preRotationRate.z + currentRotationRate.z);
		
		return new ThreeDimensionalDouble(post_alpha, post_beta, post_gama);		
	}
}

