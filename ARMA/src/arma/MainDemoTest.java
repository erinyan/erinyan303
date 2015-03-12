package arma;


public class MainDemoTest {
	@SuppressWarnings("unused")
	static public void test() {
		double[] inputs = { 19.5900, 14.9100, 15.7400, 15.4000, 13.0600,
				19.0700, 15.2800, 15.8200, 12.7700, 12.0500, 11.6900, 13.8500,
				13.8500, 10.0700, 9.1700, 10.7900, 13.4400, 21.1700, 18.6400,
				13.2100, 15.5400, 21.9400, 23.1100, 18.6400, 14.9400, 16.9000,
				15.4600, 11.1500, 13.1300, 12.4800, 12.9500, 12.5900, 10.5800,
				10.5800, 12.3900, 15.5300, 13.0600, 10.2200, 16.3300, 19.7200,
				21.3100, 18.8400, 24.8400, 15.6700, 15.5700, 12.7300, 13.5600,
				15.5400, 17.2200, 12.1400, 11.0700, 12.0200, 11.5500, 6.9200,
				10.3300, 8.3800, 12.1100, 11.4600, 12.7500, 13.3200, 13.0000,
				11.9000, 11.7900, 12.5500, 11.8400, 11.2500, 11.1500, 10.9900,
				11.7000, 14.0100, 17.5100, 17.2700, 16.9000, 15.7900, 15.4500,
				6.2400, 16.7100, 16.7700, 16.6400, 17.8000, 16.8700, 16.1300,
				15.7600, 15.6600, 15.5400, 15.3000, 15.0500, 14.6900, 14.3900,
				14.1800, 13.70, 13.66, 13.27, 13.56, 13.14, 14.19, };
		/*double[] inputs =  {405,393,350.625,384.17,468.5,502.5,440,
		414.5,390.625,305.5,187.375,166.875,175.9,195.5,205.25,231,213.25,232.25,
		237.7,254,267.25,257.2,259.125,307.1,320,316.9,301,270.7,253.375,244.5,242.7,
        268.25,284.5,328.8333333,380.125,354.625,347.25,348.75,340.5,341,357.25,347.25,
		362.6,377.5,406,404.875,369.75,360.4,371.5,374,380.5,391.25,403.25,372.6,362,
		362.4,360.3333333,362.75,358,358.33,356.5,369.5,368.2,364,366.1,358,
		363,381.3,402.5,430.25,481.8,538.33,513.75,487.67,385,348,353.2,333.5, };*/

		// 两步差分
		double[] diffed1 = Diff.diff(inputs, 0, inputs.length);
		double[] diffed2 = Diff.diff(diffed1, 0, diffed1.length);
		
		int inputsLen = 88;
		double[] datas = new double[inputsLen];
		for (int i=0; i<inputsLen; i++) {
			datas[i] = diffed2[i];
		}
		double[] standards = StatComTool.standardize(datas);
		double standardDeviation = StatComTool.standardDeviation(datas);
		double mean = StatComTool.mean(datas);
		
		// 计算自相关系数
		int autoCorrsLen = 20;
		double[] autoCorrs = AutoCorr.autoCorr(standards, 1, autoCorrsLen);

		// 计算偏相关系数
		int parCorrLen = 12;
		double[] parCorrs = ParCorr.parCorr(standards, parCorrLen);
		
		// 求误差平方和
		int sigma2sLen = 10;
		double[] sigma2s = new double[sigma2sLen];
		for (int i=0; i<sigma2sLen; i++) {
			double[] arCoeffs = ARMA.ARCoeff(standards, i+1);
			sigma2s[i] = ARMA.SSE(standards, arCoeffs);
			double aic = ARMA.AIC(sigma2s[i], standards.length, arCoeffs.length);
		}
		
		// 求AR系数
		double[] arCoeffs = ARMA.ARCoeff(standards, 5);
		double pValue = WNChecker.calcPValue(standards, arCoeffs);
		
		// 单步预测
		int estimatorPosStart = 88;
		int estimatorLen = 6;
		double[] estimateds = Predictor.singleStep(diffed2, estimatorPosStart, estimatorLen, arCoeffs);
		int diffPosStart = 88;
		double[] reDiffOne = Diff.reDiff(diffed1, diffPosStart, estimateds);
		double[] reDiffTwo = Diff.reDiff(inputs, diffPosStart + 1, reDiffOne);
		
		// 多步预测
		double[] standardsEstimator = Predictor.multiSteps(standards, estimatorPosStart,
				estimatorLen, arCoeffs);
		double[] reStandards = StatComTool.reStandardize(standardsEstimator, standardDeviation, mean);
		double lastRealValue = inputs[diffPosStart + 1];
		double lastDiff1Value = diffed1[diffPosStart];
		double[] predicts = Diff.reDiff2(lastRealValue, lastDiff1Value, reStandards);
		System.out.println("predict:");
		for(int i=0; i<predicts.length; i++) {
			System.out.println(predicts[i]);
		}
	}

	static public void main(String[] args) {
		test();
	}
}
