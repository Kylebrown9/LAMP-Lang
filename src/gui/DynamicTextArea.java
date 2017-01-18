package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class DynamicTextArea extends JTextArea implements KeyListener {
	private UpdateListener uL = null;
	
	public static DynamicTextArea create() {
		DynamicTextArea dTA = new DynamicTextArea();
		dTA.addKeyListener(dTA);
		dTA.setEditable(true);
		return dTA;
	}
	
	private DynamicTextArea() {}
	
	public void setUpdateListener(UpdateListener uL) {
		this.uL = uL;
	}

	private boolean ctrlPressed = false;
	private boolean rPressed = false;
	private boolean active;
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_CONTROL) {
			ctrlPressed = true;
		}
		
		if(arg0.getKeyCode() == KeyEvent.VK_R) {
			rPressed = true;
		}
		
		if(!active && ctrlPressed && rPressed) {
			active = true;
			if(uL != null) {
				uL.update(this.getText());
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_CONTROL) {
			ctrlPressed = false;
			active = false;
		}
		
		if(arg0.getKeyCode() == KeyEvent.VK_R) {
			rPressed = false;
			active = false;
		}
	}
	
	public void keyTyped(KeyEvent arg0) {}	
	
	interface UpdateListener {
		public void update(String update);
	}
}
