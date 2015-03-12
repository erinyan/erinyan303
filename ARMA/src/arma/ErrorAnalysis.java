package arma;

public class ErrorAnalysis {

	static double absoluteError(double real, double estimated) {
		return Math.abs(real - estimated);
	}

	// MAE=mean absolute error
	static double MAE(double[] reals, int realsStart, int realsLen,
			double[] estimateds, int estimatedsStart) {
		double sum = 0.0;
		for (int i=0; i<realsLen; i++) {
			sum += absoluteError(reals[realsStart + i], estimateds[estimatedsStart + i]);
		}
		return sum / realsLen;
	}
}
