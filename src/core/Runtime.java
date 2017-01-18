package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Runtime {
	private final List<String> operations;
	private final int width;
	
	enum Mode {STRICT,LENIENT}
	private Mode mode = Mode.STRICT;

	private String status = "No Code has been Executed";
	private String error = "";
	
	public Runtime(int width) {
		this.operations = new ArrayList<>();
		this.width = width;
	}
	
	public synchronized void setCode(String code) {
		operations.clear();
		operations.addAll(Arrays.asList(code.trim().split("\n")));
	}
	
	public synchronized LabeledMatrix run() {
		int count = 0;
		LabeledMatrix matrix = new LabeledMatrix(width);
		for(String op : operations) {
			if(!handle(matrix,op)) {
				status = "Error Line " + count + ": " + op + ", " + error;
				return matrix;
			}
			count++;
		}
		status = "Code Successfully Executed";
		return matrix;
	}
	
	public String getStatus() {
		return status;
	}
	
	private boolean handle(LabeledMatrix m, String code) {
		//Input Sanitization
		code = code.replace(" ", "");
		code = code.toUpperCase();
		
		if(code.length() == 0 ||
				code.substring(0, 1).equals("\\")) {
			return true;
		}
		
		//Duplicate Operation Handler
		if(code.indexOf("<>") != code.lastIndexOf("<>")) {
			return err("Multiple Swap Operators Present");
		}
		if(code.indexOf("->") != code.lastIndexOf("->")) {
			return err("Multiple Assignment Operators Present");
		}
		
		//Multiple Operation Handler
		if(code.contains("<>") && code.contains("->")) {
			return err("Both Swap and Assignment Operators are Present");
		}
		
		String[] cSegments;
		
		//Swap Operation Handler
		if(code.contains("<>")) {
			cSegments = code.split("<>");
					
			boolean rowErr = !validRow(cSegments[0]) || !validRow(cSegments[1]);
			
			if(rowErr) {
				return err("Invalid Row Name");
			}
			
			m.swap(cSegments[0], cSegments[1]);
			return true;
		}
		
		//Assignment Operations
		if(code.contains("->")) {
			String expression = code.split("->")[0];
			String label = code.split("->")[1];
			
			if(!validRow(label)) {
				return err("Row Name is Not Valid");
			}
			
			//Initialization Handler
			boolean oB = expression.charAt(0) == '[';
			boolean cB = expression.charAt(expression.length()-1) == ']';
			boolean dOB = expression.indexOf('[') != expression.lastIndexOf('[');
			boolean dCB = expression.indexOf(']') != expression.lastIndexOf(']');
			
			if(oB || cB) {
				if(oB && cB) {
					if(!dOB && !dCB) {
						String bracketInterior = expression.substring(1, expression.length()-1);
						String[] bracketStrings = bracketInterior.split(",");
						double[] bracketValues = new double[bracketStrings.length];
						
						if(bracketStrings.length != width) {
							return err("Row Initialization has Incorrect Number of Entries");
						}
						
						for(int i=0; i<bracketStrings.length; i++) {
							try {
								bracketValues[i] = Double.parseDouble(bracketStrings[i]);
							} catch (NumberFormatException nFE) {
								return err("Brackets Contain Non-Numeric Value");
							}
						}
						
						m.assign(label, bracketValues);
						return true;
					} else {
						return err("Statement Contains Duplicate Brackets");
					}
				} else {
					return err("Statement Contains Unmatched Brackets");
				}
			}
			
			//Assignment Handler
			String[] addComp = expression.split("\\+");
			String[] subComp = expression.split("\\-");
			
			if(addComp.length+subComp.length > 3) {
				return err("Unsupported Expression Syntax: Multiple (Addition or Subtraction) Operators Present");
			}
			
			String[] rowNames = new String[2];
			double[] coefficients = new double[2];
			
			boolean negateSecond = false;
			
			if(subComp.length > 1) {
				negateSecond = true;
				addComp = subComp;
			}
			
			String[] temp;
			
			if(addComp.length > 1) {
				for(int i=0; i<2; i++) {
					temp = addComp[i].split("\\*");
				
					if(temp.length > 1) {
						rowNames[i] = temp[1];
						try {
							if(temp[0].contains("/")) {
								String[] temp1 = temp[0].split("/");
								coefficients[i] = Double.parseDouble(temp1[0])/Double.parseDouble(temp1[1]);
							} else {
								coefficients[i] = Double.parseDouble(temp[0]);
							}	
						} catch (NumberFormatException nFE) {
							return err("Row Coefficient is Not a Number");
						}
					} else {
						rowNames[i] = temp[0];
						coefficients[i] = 1;
					}
				}
				
				if(negateSecond) {
					coefficients[1] = -1*coefficients[1];
				}
				
				double[] row1 = m.get(rowNames[0]);
				double[] row2 = m.get(rowNames[1]);
				
				if(row1 == null || row2 == null) {
					return err("Invalid Row Label in Assignment");
				}
				
				m.assign(label, Util.addRowsWithCoefficients(row1, coefficients[0], row2, coefficients[1]));
				return true;
			}	
		}
		
		if(code.contains("*>")) {
			cSegments = code.split("\\*>");
			String[] fraction = cSegments[0].split("/");
			double constant;
			
			try {
				if(fraction.length > 1) {
					constant = Double.parseDouble(fraction[0])/Double.parseDouble(fraction[1]);
				} else {
					constant = Double.parseDouble(cSegments[0]);
				}
				
				m.assign(cSegments[1], Util.multiplyRowConstant(m.get(cSegments[1]), constant));
				return true;
			} catch(NumberFormatException nFE) {
				return err("Invalid Constant in Product Assignment");
			}
		}
		
		return err("No Operation Detected");
	}
	
	private boolean validRow(String s) {
		if(s.charAt(0) == 'R' && s.length() >  1 && Character.isDigit(s.charAt(1))) {
			return true;
		}
		if(mode == Mode.LENIENT) {
			return true;
		}
		return false;
	}
	
	private boolean err(String message) {
		System.err.println(message);
		error = message;
		return false;
	}
}
