package arma;

public class WNChecker {

	static public double calcPValue(double[] datas, double[] coeffs) {
		int coeffsLen = coeffs.length;
		double err = datas[coeffsLen];
		for (int i = 0; i < coeffsLen; i++) {
			err -= coeffs[i] * datas[coeffsLen - i - 1];
		}

		double C = 0.0;
		double K = 0.0;
		int start = coeffsLen + 1;
		double at = 0;
		double at1 = 0;
		for (int i = start; i < datas.length; i++) {
			at = datas[i] + err;
			at1 = datas[i - 1];
			for (int j = 0; j < coeffs.length; j++) {
				at -= coeffs[j] * datas[i - j - 1];
				at1 -= coeffs[j] * datas[i - j - 2];
			}
			C += at * at1;
			K += Math.pow(at, 2);
		}
		return C * 1.0 / K;
	}
}
