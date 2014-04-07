package hk.ust.ins.data;

public class ThreeDimensionalDouble {
	public double x;
	public double y;
	public double z;
	
	public ThreeDimensionalDouble() {
		x = 0.0;
		y = 0.0;
		z = 0.0;
	}

	public ThreeDimensionalDouble(double _x, double _y, double _z) {
		x = _x;
		y = _y;
		z = _z;
	}
	
	public ThreeDimensionalDouble(ThreeDimensionalDouble newThreeDimensionalDouble) {
		this.x = newThreeDimensionalDouble.x;
		this.y = newThreeDimensionalDouble.y;
		this.z = newThreeDimensionalDouble.z;
	}
	
	public ThreeDimensionalDouble add(ThreeDimensionalDouble adder){	
		this.x += adder.x;
		this.y += adder.y;
		this.z += adder.z;
		return this;
	}
	
	public void set_using_double(double _x, double _y, double _z) {
		this.x = _x;
		this.y = _y;
		this.z = _z;
	}
	
	public void set_using_three(ThreeDimensionalDouble newThreeDimensionalDouble) {
		this.x = newThreeDimensionalDouble.x;
		this.y = newThreeDimensionalDouble.y;
		this.z = newThreeDimensionalDouble.z;
	}
	
	public ThreeDimensionalDouble subtract(ThreeDimensionalDouble subtractor){	
		this.x -= subtractor.x;
		this.y -= subtractor.y;
		this.z -= subtractor.z;
		return this;
	}
}
