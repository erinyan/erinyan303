package arma;

import Jama.Matrix;

public class ARModel {

	static public Matrix calcCoeff(Matrix X, Matrix Y) { // 使用最小二乘估计系数值
		Matrix coeff = null;
		try {
			coeff = X.transpose().times(X).inverse().times(X.transpose()).times(Y);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return coeff;
	}

	static public double calcSigma2(int allArguCount, int usedArguCount,
			Matrix coeff, Matrix X, Matrix Y) { // 计算方差
		return ((Y.minus(X.times(coeff)).transpose()).times(Y.minus(X
				.times(coeff))).times(1.0 / (allArguCount - usedArguCount)))
				.get(0, 0);
	}

	static public double calcFPE(int allArguCount, int usedArguCount,
			double sigma2) { // (N+n)/(N-n)*sigma^2
		return 1.0 * (allArguCount + usedArguCount)
				/ (allArguCount - usedArguCount) * sigma2;
	}

	static public double calcAIC(int allArguCount, int usedArguCount,
			double sigma2) { // N*In(sigma^2)+2*n
		return 1.0 * allArguCount * Math.log(sigma2) + 2.0 * usedArguCount;
	}

	static public double calcBIC(int allArguCount, int usedArguCount,
			double sigma2) { // N*In(sigma^2) + n*In(N);
		return 1.0 * allArguCount * Math.log(sigma2) + usedArguCount
				* Math.log(allArguCount);
	}

	static public int chooseSpan(double[] input, int maxTrySpan) {
		int allArguCount = input.length;
		double[][] criteria = new double[maxTrySpan][3]; // FPE,AIC,BIC
		for (int i = 0; i < maxTrySpan; i++) {
			int usedArguCount = i + 1;
			Equation eq = new Equation(input, allArguCount, usedArguCount);
			Matrix coeff = calcCoeff(eq.X, eq.Y);
			if (coeff == null) {
				criteria[i][0] = Double.MIN_VALUE;
				criteria[i][1] = Double.MIN_VALUE;
				criteria[i][2] = Double.MIN_VALUE;
			} else {
				double sigma2 = calcSigma2(allArguCount, usedArguCount, coeff,
						eq.X, eq.Y);
				criteria[i][0] = calcFPE(allArguCount, usedArguCount, sigma2);
				criteria[i][1] = calcAIC(allArguCount, usedArguCount, sigma2);
				criteria[i][2] = calcBIC(allArguCount, usedArguCount, sigma2);
			}
		}

		int index = selectRelativeMin(criteria);
		return index;
	}

	static public int selectRelativeMin(double[][] criteria) {
		double min = Double.MAX_VALUE;
		int index = -1;
		for (int i = 1; i <= criteria.length; i++) {
			if (criteria[i-1][2] > Double.MIN_VALUE && min > criteria[i - 1][2]) {
				min = criteria[i - 1][2];
				index = i;
			}
		}
		return index;
	}

	static public Matrix assembleX(double[] input, int XCount) {
		Matrix X = new Matrix(1, XCount);
		for (int i = 0; i < XCount; i++) {
			X.set(0, i, input[input.length - 1 - i]);
		}
		return X;
	}

	static public Matrix assembleX(double[] input, double[] estimated,
			int XCount) {
		Matrix X = new Matrix(1, XCount);
		for (int i = 0; i < XCount; i++) {
			if (i < estimated.length) {
				X.set(0, i, estimated[i]);
			} else {
				X.set(0, i, input[input.length - 1 - (i - estimated.length)]);
			}
		}
		return X;
	}

	static public double estimate(Matrix X, Matrix coeff) {
		return X.times(coeff).get(0, 0);
	}
	
	static public String print(Matrix m) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < m.getRowDimension(); i++) {
			for (int j = 0; j < m.getColumnDimension(); j++) {
				sb.append(m.get(i, j)).append(" ");
			}
			sb.append("\n");
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

	static public class Equation {
		public Matrix X;
		public Matrix Y; // Y为列向量

		// [a0,a1,a2,a3,a4,a5,a6,a7,a8,a9]
		// arguCount=4
		// Y=[a4,a5,a6,a7,a8,a9]'
		// X=
		// a3,a2,a1,a0
		// a4,a3,a2,a1
		// a5,a4,a3,a2
		// a6,a5,a4,a3
		// a7,a6,a5,a4
		// a8,a7,a6,a5
		public Equation(double[] input, int allCount, int arguCount) {
			Y = new Matrix(allCount - arguCount, 1);
			for (int i = arguCount; i < allCount; i++) {
				Y.set(i - arguCount, 0, input[i]);
			}

			int row = allCount - arguCount; // X列的个数
			X = new Matrix(row, arguCount);
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < arguCount; j++) {
					X.set(i, j, input[arguCount - 1 + i - j]);
				}
			}
		}

		public String toString() {
			String strX = change2String(X);
			String strY = change2String(Y);
			return "X=\n" + strX + "\n" + "Y=\n" + strY;
		}

		private String change2String(Matrix m) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < m.getRowDimension(); i++) {
				for (int j = 0; j < m.getColumnDimension(); j++) {
					sb.append(m.get(i, j)).append(" ");
				}
				sb.append("\n");
			}
			return sb.toString();
		}
		//
		// public static void test() {
		// double[] input = { 175, 162, 156, 174, 157, 154, 177, 159, 171,
		// 166, };
		// Equation eq = new Equation(input, 10, 4);
		// System.out.println(eq.toString());
		// }
	}

	static public void test() {
		double[] input = { 
				2083.0,
				4232.0,
				6405.0,
				7510.0,
				8384.0,
				8055.0,
				7723.0,
				8860.0,
				8827.0,
				8777.0,
				9705.0,
				10795.0,
				11093.0,
				11517.0,
				12055.0,
				12860.0,
				14308.0,
				14784.0,
				17239.0,
				16403.0,
				12431.0,
//				13982.0,
//				14902.0,
//				15724.0,
//				19168.0,
//				13739.0,
//				9092.0,
//				7813.0,
//				6762.0,
//				5929.0,
//				5279.0,
//				4666.0,
//				4852.0,
//				3771.0,
//				3597.0,
//				3358.0,
//				2982.0,
//				2906.0,
//				2531.0,
//				2347.0,
//				2209.0,
//				1928.0,
//				1642.0,
//				1501.0,
//				1442.0,
//				1389.0,
//				1182.0,
//				1075.0,
//				1019.0,
//				923.0, 
		};

		int maxTrySpan = 10;
		int allArguCount = input.length;
		for (int usedArguCount = 1; usedArguCount <= maxTrySpan; usedArguCount++) {
			Equation eq = new Equation(input, allArguCount, usedArguCount);
			Matrix coeff = calcCoeff(eq.X, eq.Y);
			if (coeff == null) {
				System.out.println("usedArguCount=" + usedArguCount + "\tcoeff=null");
			} else {
				double sigma2 = calcSigma2(allArguCount, usedArguCount, coeff,
						eq.X, eq.Y);
				double fpe = calcFPE(allArguCount, usedArguCount, sigma2);
				double aic = calcAIC(allArguCount, usedArguCount, sigma2);
				double bic = calcBIC(allArguCount, usedArguCount, sigma2);
				System.out
						.println("usedArguCount=" + usedArguCount + "\tsigma2="
								+ sigma2 + "\tfpe=" + fpe + "\taic=" + aic
								+ "\tbic=" + bic);
			}
		}

		System.out.println("-------");
		int span = chooseSpan(input, maxTrySpan);
		System.out.println(span);
		Equation eq = new Equation(input, allArguCount, span);
		
		print(eq.X);
		print(eq.Y);
		Matrix coeff = calcCoeff(eq.X, eq.Y);
		System.out.println("coeff:\n" + print(coeff));

		
		System.out.println("预估七天数据：");
		Matrix X = assembleX(input, span);
		double estimated = estimate(X, coeff);
		System.out.println(estimated);
		
		X = assembleX(input, new double[] {estimated}, span);
		double estimated1 = estimate(X, coeff);
		System.out.println(estimated1);
		
		X = assembleX(input, new double[] {estimated1, estimated}, span);
		double estimated2 = estimate(X, coeff);
		System.out.println(estimated2);
		
		X = assembleX(input, new double[] {estimated2, estimated1, estimated}, span);
		double estimated3 = estimate(X, coeff);
		System.out.println(estimated3);
		
		X = assembleX(input, new double[] {estimated3, estimated2, estimated1, estimated}, span);
		double estimated4 = estimate(X, coeff);
		System.out.println(estimated4);
		
		X = assembleX(input, new double[] {estimated4, estimated3, estimated2, estimated1, estimated}, span);
		double estimated5 = estimate(X, coeff);
		System.out.println(estimated5);
		
		X = assembleX(input, new double[] {estimated5, estimated4, estimated3, estimated2, estimated1, estimated}, span);
		double estimated6 = estimate(X, coeff);
		System.out.println(estimated6);
	}

	public static void main(String[] args) {
		test();
	}
}