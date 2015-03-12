package arma;

public class StatComTool {
	// �����ֵ
	static public double mean(double[] datas) {
		int len = datas.length;
		double sum = 0.0;
		for (int i = 0; i < len; i++) {
			sum += datas[i];
		}
		return sum * 1.0 / len;
	}

	// ��׼��
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
	
	// ����׼��
	static public double[] reStandardize(double[] datas, double standardDeviation, double mean) {
		double[] reStandards = new double[datas.length];
		for (int i=0; i<datas.length; i++) {
			reStandards[i] = datas[i] * standardDeviation + mean;
		}
		return reStandards;
	}

	// ������������
	static public double var(double[] datas) {
		int len = datas.length;
		double dMean = mean(datas);
		double sum = 0.0;
		for (int i = 0; i < len; i++) {
			sum += Math.pow((datas[i] - dMean), 2);
		}
		return sum / len;
	}

	// �����׼��
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
