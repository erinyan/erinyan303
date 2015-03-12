package arma;

import Jama.Matrix;

public class ParCorr {
	// 计算延迟偏自相关系数
	static public double calcParCorrByYWEquation(double[] datas, int delay) {
		double[] dAutoCorrs = AutoCorr.autoCorr(datas, 1, delay);
		double[][] toeplitz = Toeplitz.genToeplitz(dAutoCorrs, 0, dAutoCorrs.length - 1);
		Matrix tMatrix = new Matrix(toeplitz);
		Matrix ret = tMatrix.inverse().times(new Matrix(dAutoCorrs, dAutoCorrs.length));
		return ret.get(dAutoCorrs.length-1, 0);
	}

	// 计算延迟偏自相关系数
	static public double[] parCorr(double[] datas, int delay) {
		double[][] dAllParCorrs = calcAllParCorr(datas, delay);
		double[] dParCorrs = new double[delay];
		for (int i = 0; i < delay; i++) {
			dParCorrs[i] = dAllParCorrs[i][i];
		}
		return dParCorrs;
	}
	
	// 计算延迟所有偏自相关系数
	static public double[][] calcAllParCorr(double[] datas, int delay) {
		double[][] parCorrs = new double[delay][];
		for (int i = 0; i < delay; i++) {
			parCorrs[i] = new double[delay];
			for (int j = 0; j < delay; j++) {
				parCorrs[i][j] = 0.0;
			}
		}

		if (delay == 1) {
			parCorrs[0][0] = AutoCorr.autoCorr(datas, delay);
			return parCorrs;
		}

		double[] dAutoCorrs = AutoCorr.autoCorr(datas, 1, delay);
		parCorrs[0][0] = dAutoCorrs[0];

		double s1, s2;
		for (int k = 1; k < delay; k++) {
			s1 = dAutoCorrs[k];
			s2 = 1;
			for (int j = 0; j < k; j++) {
				s1 -= dAutoCorrs[k - j - 1] * parCorrs[k - 1][j];
				s2 -= dAutoCorrs[j] * parCorrs[k - 1][j];
			}
			parCorrs[k][k] = s1 * 1.0 / s2;

			for (int j = 0; j < k; j++) {
				parCorrs[k][j] = parCorrs[k - 1][j] - parCorrs[k][k]
						* parCorrs[k - 1][k - j - 1];
			}
		}
		return parCorrs;
	}
}
