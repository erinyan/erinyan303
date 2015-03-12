package arma;

public class Predictor {

	// 只单步预测一步
	static public double singleStep(double[] datas, int estimatorPosStart,
			double[] coeffs) {
		int datasLen = coeffs.length;
		double estimated = 0.0;
		for (int i = 0; i < datasLen; i++) {
			estimated += coeffs[i] * datas[estimatorPosStart - 1 - i];
		}
		return estimated;
	}

	// 单步预测
	static public double[] singleStep(double[] datas, int estimatorPosStart,
			int estimatorLen, double[] coeffs) {
		double[] estimateds = new double[estimatorLen];
		for (int i = 0; i < estimatorLen; i++) {
			estimateds[i] = singleStep(datas, estimatorPosStart + i, coeffs);
		}
		return estimateds;
	}

	// 多步预测
	static public double[] multiSteps(double[] datas, int estimatorPosStart,
			int estimatorLen, double[] coeffs) {
		double[] estimateds = new double[estimatorLen];
		for (int i = 0; i < estimatorLen; i++) {
			singleStep(datas, estimatorPosStart, estimateds, i, coeffs);
		}
		return estimateds;
	}

	// 多步预测一步
	static private void singleStep(double[] datas, int estimatorPosStart,
			double[] estimateds, int estimatedsLen, double[] coeffs) {
		int datasLen = coeffs.length;
		double estimated = 0.0;
		for (int i = 0; i < datasLen; i++) {
			if (i < estimatedsLen) {
				estimated += coeffs[i] * estimateds[estimatedsLen - 1 - i];
			} else {
				estimated += coeffs[i]
						* datas[estimatorPosStart - 1 - (i - estimatedsLen)];
			}
		}
		estimateds[estimatedsLen] = estimated;
	}
}
