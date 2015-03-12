package arma;

public class ARMA {

	// 计算AR模型的系数
	static public double[] ARCoeff(double[] datas, int delay) {
		double[][] dAllParCorrs = ParCorr.calcAllParCorr(datas, delay);
		double[] arCoeffs = new double[delay];
		for (int i = 0; i < delay; i++) {
			arCoeffs[i] = dAllParCorrs[delay - 1][i];
		}
		return arCoeffs;
	}

	//	计算残差平方和 sum square error
	static public double SSE (double[] datas, double[] coeffs) {
		int coeffLen = coeffs.length;
		double sum = 0;
		double estimated = 0;
		for (int i=coeffLen; i<datas.length; i++) {
			estimated = 0;
			for (int j=0; j<coeffLen; j++) {
				estimated += coeffs[j] * datas[i-j-1];
			}
			sum += Math.pow(datas[i] - estimated, 2);
		}
		
		return sum * 1.0 / (datas.length - coeffLen);
	}
	
	// AIC信息量
	static public double AIC(double sigma2, int dataCount, int argsCount) {
		return dataCount * Math.log(sigma2 * 10) + 2 * argsCount;
	}
}
