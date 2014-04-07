package hk.ust.ins.op;

import hk.ust.ins.data.ThreeDimensionalDouble;

public class Correction {
	public static ThreeDimensionalDouble LinearCorrection(ThreeDimensionalDouble originalLinearValue, ThreeDimensionalDouble calibratingLinearValue) {
		
		ThreeDimensionalDouble linearCorrectionResult = new ThreeDimensionalDouble(originalLinearValue);
		return linearCorrectionResult.subtract(calibratingLinearValue);
	}
	
	public static ThreeDimensionalDouble RotationCorrection(ThreeDimensionalDouble originalRotationValue, ThreeDimensionalDouble calibratingRotationValue) {
		
		ThreeDimensionalDouble rotationCorrectionResult = new ThreeDimensionalDouble(originalRotationValue);
		return rotationCorrectionResult.subtract(calibratingRotationValue);
	}
}