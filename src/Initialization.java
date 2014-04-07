package hk.ust.ins;

import hk.ust.ins.data.ThreeDimensionalDouble;

public class Initialization {
	public static ThreeDimensionalDouble position;
	public static ThreeDimensionalDouble velocity;
	public static double horizontalAngle;
	public static double verticalAngle;
	public static ThreeDimensionalDouble linearValue;
	public static ThreeDimensionalDouble rotationValue;
	public static ThreeDimensionalDouble rotationAngle;
	
	public static void init(){
		position = new ThreeDimensionalDouble();
		velocity = new ThreeDimensionalDouble();
		linearValue = new ThreeDimensionalDouble();
		rotationValue = new ThreeDimensionalDouble();
		rotationAngle = new ThreeDimensionalDouble();
		horizontalAngle = 0;
		verticalAngle = 0;
	}
}
