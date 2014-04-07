package hk.ust.ins.op;

public class LinearWeightedMovingAverage
{
	private double circularBuffer[];
	private double avg;
	private double sum;
	private int circularIndex; 
	private int count;
	private int weightSum;
	
	public LinearWeightedMovingAverage(int k) {
		circularBuffer = new double[k]; 
		count = 0;
		circularIndex = 0;
		avg = 0;
		sum = 0;
		weightSum = 0;
	}

	public double getAverageValue() {
		return avg / weightSum;  
	}
	
	public void pushValue(double x) {

		if(count < circularBuffer.length) {
			count++;
			avg += x * count;
			weightSum += count;
			sum += x;
		}
		else {
			avg = avg - sum + x * count;
			sum = sum - circularBuffer[circularIndex] + x;
		}
		
		circularBuffer[circularIndex] = x;
		circularIndex = nextIndex(circularIndex);
	}
	
	private int nextIndex(int curIndex) {
		if (curIndex + 1 >= circularBuffer.length) {
			return 0; 
		}
		return curIndex + 1;
	}
}