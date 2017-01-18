package core;

public class Util {
	public static double[] multiplyRowConstant(double[] row, double constant) {
		double[] output = new double[row.length];
		
		for(int i=0; i<row.length; i++) {
			output[i] = row[i]*constant;
		}
		
		return output;
	}
	
	public static double[] addRows(double[] row1, double[] row2) {
		double[] output = new double[Math.max(row1.length, row2.length)];
		
		int temp;
		for(int i=0; i<output.length; i++) {
			temp = 0;
			
			if(i < row1.length) {
				temp += row1[i];
			}
			if(i < row2.length) {
				temp += row2[i];
			}
			
			output[i] = temp;
		}
		
		return output;
	}
	
	public static double[] addRowsWithCoefficients(double[] row1, double c1, double[] row2, double c2) {
		return addRows(multiplyRowConstant(row1,c1),multiplyRowConstant(row2,c2));
	}
}
