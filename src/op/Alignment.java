package hk.ust.ins.op;

import hk.ust.ins.data.ThreeDimensionalDouble;

public class Alignment {
	
	public static ThreeDimensionalDouble execute(ThreeDimensionalDouble acceleration, 
			ThreeDimensionalDouble angle) {

		double sin_a = Math.sin(angle.x);
		double sin_b = Math.sin(angle.y);
		double sin_c = Math.sin(angle.z);
		double cos_a = Math.cos(angle.x);
		double cos_b = Math.cos(angle.y);
		double cos_c = Math.cos(angle.z);
		
		double x = acceleration.x * ( cos_c * cos_a - sin_c * sin_b * sin_a )
				 + acceleration.y * ( - sin_c * cos_b )
				 + acceleration.z * ( sin_a * cos_c + cos_a * sin_b * sin_c );

		double y = acceleration.x * ( cos_c * sin_b * sin_a + sin_c * cos_a )
				 + acceleration.y * ( cos_c * cos_b )
				 + acceleration.z * ( sin_a * sin_c - cos_a * sin_b * cos_c );
				
		double z = acceleration.x * ( - cos_b * sin_a )
				 + acceleration.y * ( sin_b )
				 + acceleration.z * ( cos_a * cos_b );
		
		return new ThreeDimensionalDouble(x, y, z);
	}
}
