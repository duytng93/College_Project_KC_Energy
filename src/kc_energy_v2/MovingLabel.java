package kc_energy_v2;

import java.awt.Graphics;

import javax.swing.*;


/* Physical location of a label is not changed 
 * (the label is still there even though it looks like they moved away or not showing) */
public class MovingLabel extends JLabel {
	int translateY = 30;              
    static final int MAX_Y = 30;
    boolean hidden = true;
    private JPanel parent;
    /** Creates a new instance of TranslucentButton */
    public MovingLabel(String text, JPanel parent) {
    	
        super(text);
        this.parent = parent;
        setOpaque(false);
    }
    
    /**
     * Displays our component in the location (0, translateY). Note that
     * this changes only the rendering location of the button, not the
     * physical location of it. Note, also, that rendering into g will
     * be clipped to the physical location of the button, so the button will
     * disappear as it moves away from that location.
     */
    public void paint(Graphics g) {
    		g.translate(0, translateY);
    		super.paint(g);
    }
    
    /* Reducing translateY to 0 to make the label looks like showing */
    public void show() {
    	if(hidden) {
    		Thread th = new Thread(new Runnable() {
    			@Override
    			public void run() {
    				
    				for (int i=1; i<=MAX_Y; i++) {
    					parent.repaint();   // not repaint parent component cause some graphic issue
    					translateY--;
    					try {
    						Thread.sleep(10);
    					} catch (InterruptedException e) {
    						e.printStackTrace();
    					}
    				}
    				hidden = false;
    				parent.repaint(); // not repaint parent component cause some graphic issue	
    			}
            });
            th.start(); 
    	}
    }
    
    /* Increasing translateY to Max_Y to make the label looks like hiding */
    public void hide() {
    	if(!hidden) {
    		Thread th = new Thread(new Runnable() {
    			@Override
    			public void run() {
    				for (int i=1; i<=MAX_Y; i++) {
    					parent.repaint(); // not repaint parent component cause some graphic issue
    					translateY++;
    					try {
    						Thread.sleep(10);
    					} catch (InterruptedException e) {
    						e.printStackTrace();
    					}
    				}
    				hidden = true;
    				parent.repaint(); // not repaint parent component cause some graphic issue
    			}
    		});
    		th.start();
    	}
    }
}
