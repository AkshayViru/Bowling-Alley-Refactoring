import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.util.*;
import java.text.*;

public class Emoticon {
	private JFrame win;
    
	public Emoticon(int emoticonId) {
        win = new JFrame("Emoticon");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);
		win.setSize(512,512);

		if(emoticonId <=3) {
			win.add(new JLabel(new ImageIcon("./imgs/0.jpg")));
		} else if(emoticonId <=6) {
			win.add(new JLabel(new ImageIcon("./imgs/1.jpg")));
		} else if(emoticonId <= 8) {
			win.add(new JLabel(new ImageIcon("./imgs/9.jpg")));
		} else if(emoticonId >= 9) {
			win.add(new JLabel(new ImageIcon("./imgs/10.png")));
		}

		win.show();
		try {
			Thread.sleep(1500);
		} catch (Exception e) {}
		win.hide();
	}

}