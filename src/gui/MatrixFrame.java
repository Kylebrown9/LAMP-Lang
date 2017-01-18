package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import core.Runtime;
import gui.DynamicTextArea.UpdateListener;

@SuppressWarnings("serial")
public class MatrixFrame extends JFrame implements UpdateListener {
	private Runtime runtime;
	private DynamicTextArea inputArea;
	private JTextArea displayArea;
	private JTextField log;
	
	public static MatrixFrame create(int width) {
		MatrixFrame m = new MatrixFrame(width);
		m.init();
		
		m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return m;
	}
	
	private MatrixFrame(int width) {
		super("LAMP - Environment");
		
		runtime = new Runtime(width);
		inputArea = DynamicTextArea.create();
		displayArea = new JTextArea();
		log = new JTextField();
		
		displayArea.setEditable(false);
		log.setEditable(false);

		inputArea.setFont(new Font("test",Font.PLAIN,28));
		displayArea.setFont(new Font("test",Font.PLAIN,28));
		log.setFont(new Font("test",Font.PLAIN,28));
	}
	
	private void init() {
		Container root = this.getContentPane();
		BorderLayout rootLayout = new BorderLayout();
		root.setLayout(rootLayout);
		
		JPanel columnPanel = new JPanel();
		GridLayout columnLayout = new GridLayout(1,2,15,0);
		columnPanel.setLayout(columnLayout);
		columnPanel.add(displayArea);
		columnPanel.add(inputArea);
		
		root.add(columnPanel,BorderLayout.CENTER);
		root.add(log,BorderLayout.SOUTH);
		
		inputArea.setUpdateListener(this);
		
		this.setVisible(true);
		this.setSize(new Dimension(1000,800));
	}

	@Override
	public void update(String update) {
		runtime.setCode(update);
		displayArea.setText(runtime.run().toString());
		log.setText(runtime.getStatus());
	}
}
