package arma;

public class AutoCorr {

	// �����ӳ�kЭ����
	static public double autoCovariance(double[] datas, int delay) {
		double dMean = StatComTool.mean(datas);
		int len = datas.length;
		double dAutoCorr = 0.0;
		for (int i = 0; i < len - delay; i++) {
			dAutoCorr += (datas[i] - dMean) * (datas[i + delay] - dMean);
		}
		return dAutoCorr * 1.0 / len;
	}
	
	// �����ӳ������ϵ��
	static public double autoCorr(double[] datas, int delay) {
		double dAutoCovar = autoCovariance(datas, delay);
		double dVar = StatComTool.var(datas);
		return dAutoCovar * 1.0 / dVar;
	}

	// �����ӳ������ϵ��
	static public double[] autoCorr(double[] datas, int startDelay, int len) {
		double[] dAutoCorrs = new double[len];
		double dVar = StatComTool.var(datas);
		for (int i = 0; i < len; i++) {
			dAutoCorrs[i] = autoCovariance(datas, i + startDelay) * 1.0 / dVar;
		}
		return dAutoCorrs;
	}
}
