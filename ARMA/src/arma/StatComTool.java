package arma;

public class StatComTool {
	// 计算均值
	static public double mean(double[] datas) {
		int len = datas.length;
		double sum = 0.0;
		for (int i = 0; i < len; i++) {
			sum += datas[i];
		}
		return sum * 1.0 / len;
	}

	// 标准化
	static public double[] standardize(double[] datas) {
		double dMean = mean(datas);
		double dStandardDeviation = standardDeviation(datas);
		int len = datas.length;
		double[] standard = new double[len];
		for (int i = 0; i < len; i++) {
			standard[i] = (datas[i] - dMean) / dStandardDeviation;
		}
		return standard;
	}
	
	// 反标准化
	static public double[] reStandardize(double[] datas, double standardDeviation, double mean) {
		double[] reStandards = new double[datas.length];
		for (int i=0; i<datas.length; i++) {
			reStandards[i] = datas[i] * standardDeviation + mean;
		}
		return reStandards;
	}

	// 计算样本方差
	static public double var(double[] datas) {
		int len = datas.length;
		double dMean = mean(datas);
		double sum = 0.0;
		for (int i = 0; i < len; i++) {
			sum += Math.pow((datas[i] - dMean), 2);
		}
		return sum / len;
	}

	// 计算标准差
	static public double standardDeviation(double[] datas) {
		return Math.sqrt(var(datas));
	}
	
	static public double sum(double[] datas, int start, int len) {
		double sum = 0.0;
		for (int i=0; i<len; i++) {
			sum += datas[i + start];
		}
		return sum;
	}
}
