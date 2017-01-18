package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabeledMatrix {
	private Map<String,double[]> matrix = new HashMap<>();
	private List<String> keyList = new ArrayList<>();
	private final int width;
	
	public LabeledMatrix(int width) {
		this.width = width;
	}
	
	public void assign(String label, double[] value) {
		if(value.length != width)
			return;
		
		if(!keyList.contains(label)) {
			keyList.add(label);
		}
		matrix.put(label, value);
	}
	
	public void assignSum(String label, List<double[]> rows) {
		double[] sum = new double[width];
		double[] temp;
				
		for(int i=0; i<rows.size(); i++) {
			temp = rows.get(i);
			if(temp.length != width) {
				return;
			}
			sum[i] = 0;
			for(int j=0; j<width; j++) {
				sum[i] += temp[j];
			}
		}
		
		assign(label,sum);
	}
	
	public void swap(String label1, String label2) {
		double[] temp = matrix.get(label1);
		matrix.put(label1, matrix.get(label2));
		matrix.put(label2, temp);
	}
	
	public double[] get(String label) {
		return matrix.get(label);
	}
	
	public LabeledMatrix clone() {
		LabeledMatrix clone = new LabeledMatrix(width);
		for(String key : keyList) {
			clone.assign(key, matrix.get(key).clone());	
		}
		return clone;
	}
	
	public String toString() {
		StringBuilder sB = new StringBuilder();
		
		for(String key : keyList) {
			sB.append(Arrays.toString(matrix.get(key)));
			sB.append("\n");
		}
		
//		sB.delete(sB.length()-1,sB.length()-1);
		return sB.toString();
	}
}
