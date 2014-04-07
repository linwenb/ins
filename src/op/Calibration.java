package hk.ust.ins.op;

import hk.ust.ins.data.ThreeDimensionalDouble;

public class Calibration {
	public ThreeDimensionalDouble linearValue;
	public ThreeDimensionalDouble rotationValue;
	
	public Calibration()
	{
		linearValue = new ThreeDimensionalDouble();
		rotationValue = new ThreeDimensionalDouble();
	}
}
