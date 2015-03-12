package arma;

public class Diff {
	// 计算一阶差分
	static public double[] diff(double[] datas, int start, int len) {
		double[] diffOne = new double[len - 1];
		for (int i = 0; i < len - 1; i++) {
			diffOne[i] = datas[start + i + 1] - datas[start + i];
		}
		return diffOne;
	}

	// 一阶反差分
	static public double[] reDiff(double[] diffed, int start, double[] deltas) {
		int len = deltas.length;
		double[] reDiffs = new double[len];
		for (int i = 0; i < len; i++) {
			reDiffs[i] = diffed[start + i] + deltas[i];
		}
		return reDiffs;
	}

	static public double reDiff(double lastRealValue, double diff) {
		return lastRealValue + diff;
	}

	static public double[] reDiff2(double lastRealValue, double lastDiff1Value,
			double[] diff2) {
		double[] reDiff2s = new double[diff2.length];
		double estimated = lastRealValue;
		for (int i = 0; i < diff2.length; i++) {
			reDiff2s[i] = reDiff2(estimated, lastDiff1Value, diff2[i]);
			lastDiff1Value = lastDiff1Value + diff2[i];
			estimated = reDiff2s[i];
		}
		return reDiff2s;
	}

	static public double reDiff2(double lastRealValue, double lastDiff1Value,
			double diff2) {
		double diff1 = lastDiff1Value + diff2;
		double estimated = lastRealValue + diff1;
		return estimated;
	}
}
