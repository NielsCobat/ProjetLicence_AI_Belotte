package AI;

/*
 * Toutes les formules math�matiques n�c�ssaires au bon d�roulement de l'entra�nement de l'ia
 * Le code a �t� cr�e par Suyash Sonawane
 */

public class Matrix {
	double[][] data;
	int rows, cols;

	public Matrix(int rows, int cols) {
		data = new double[rows][cols];
		this.rows = rows;
		this.cols = cols;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				data[i][j] = Math.random() * 2 - 1;
			}
		}
	}

	public Matrix(int rows, int cols, String filename) { // TODO matrice � initialiser en fonction du fichier
		data = new double[rows][cols];
		this.rows = rows;
		this.cols = cols;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				data[i][j] = Math.random() * 2 - 1;
			}
		}
	}

	public void add(Matrix m) {
		if (cols != m.cols || rows != m.rows) {
			System.out.println("Shape Mismatch");
			return;
		}

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				this.data[i][j] += m.data[i][j];
			}
		}
	}

	public static Matrix multiply(Matrix a, Matrix b) {
		Matrix temp = new Matrix(a.rows, b.cols);
		for (int i = 0; i < temp.rows; i++) {
			for (int j = 0; j < temp.cols; j++) {
				double sum = 0;
				for (int k = 0; k < a.cols; k++) {
					sum += a.data[i][k] * b.data[k][j];
				}
				temp.data[i][j] = sum;
			}
		}
		return temp;
	}

	public void sigmoid() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++)
				this.data[i][j] = 1 / (1 + Math.exp(-this.data[i][j]));
		}

	}

	public static Matrix fromArray(double[] x) {
		Matrix temp = new Matrix(x.length, 1);
		for (int i = 0; i < x.length; i++)
			temp.data[i][0] = x[i];
		return temp;

	}

	/*
	 * fonction n'appartenant pas � Suyash Sonawane
	 */
	public double[] toDouble() {
		double[] temp = new double[rows];

		for (int i = 0; i < rows; i++) {
			temp[i] = data[i][0];
		}
		return temp;
	}

}
