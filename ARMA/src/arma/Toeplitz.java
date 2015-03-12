package arma;

public class Toeplitz {
	// 1 2 3 4 5
	// 2 1 2 3 4
	// 3 2 1 2 3
	// 4 3 2 1 2
	// 5 4 3 2 1
	static public double[][] genToeplitz(double[] datas, int start, int len) {
		int matrixLen = len + 1;
		double[][] matrix = new double[matrixLen][];
		for (int i = start; i < matrixLen; i++) {
			matrix[i] = new double[matrixLen];
			for (int j = start; j < matrixLen; j++) {
				if (j > i) {
					matrix[i][j] = datas[j - i - 1];
				} else if (j == i) {
					matrix[i][j] = 1;
				} else {
					matrix[i][j] = datas[i - j - 1];
				}
			}
		}
		return matrix;
	}
}
