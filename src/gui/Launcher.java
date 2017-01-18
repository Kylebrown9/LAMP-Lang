package gui;

import javax.swing.JOptionPane;

public class Launcher {
	public static void main(String[] args) {
		init();
	}
	
	public static void init() {
		String wString = JOptionPane.showInputDialog(
				null,
				"Matrix Width Specification",
				"",
				JOptionPane.QUESTION_MESSAGE);
		
		try {
			int wNum = Integer.parseInt(wString);
			@SuppressWarnings("unused")
			MatrixFrame m = MatrixFrame.create(wNum);
		} catch (NumberFormatException nFE) {
			init();
		}
	}
}
