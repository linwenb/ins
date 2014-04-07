package hk.ust.ins.op;

import hk.ust.ins.data.ThreeDimensionalDouble;

public class Update {
	public static ThreeDimensionalDouble excute(ThreeDimensionalDouble position, ThreeDimensionalDouble displacement) {
		
		ThreeDimensionalDouble newPosition = new ThreeDimensionalDouble(position);
		return newPosition.add(displacement);
	}
}