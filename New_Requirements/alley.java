import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static java.lang.Thread.sleep;

public class alley implements ItemListener, ActionListener {
	static JLabel label1, label2;
	static JComboBox lanesComb,patronComb;
	static JFrame alleyOptions;
	static JButton createAlley;
	int numLanesVal=3;
	int maxPatronVal=6;
	boolean pauseAlley=true;
	public static void main(String[] args) {
		alleyOptions=new JFrame("Configure Alley");

		alley a1 = new alley();

		alleyOptions.setLayout(new GridLayout(4,1));

		String s1[] = {"3","4","5","6","7" };//values possible for numLanes
		String s2[] = {"3","4","5","6"};//values possible for maxPatronsPer Party

		lanesComb = new JComboBox(s1);
		patronComb = new JComboBox(s2);
		createAlley=new JButton("Create Alley");
		lanesComb.addItemListener(a1);
		patronComb.addItemListener(a1);
		createAlley.addActionListener(a1);

		label1 = new JLabel("Select number of Lanes : ");
		label2 = new JLabel("Max patrons possible : ");

		JPanel p1 = new JPanel();
		p1.add(label1);
		p1.add(lanesComb);

		JPanel p2 = new JPanel();
		p2.add(label2);
		p2.add(patronComb);
		JPanel crPanel=new JPanel(new FlowLayout());
		crPanel.add(createAlley);
		// add panel to frame
		alleyOptions.add(p1);
		alleyOptions.add(p2);
		alleyOptions.add(crPanel);
		JLabel img=new JLabel(new ImageIcon("./imgs/b.jpg"));

//		JPanel imgPanel=new JPanel();
//
//		imgPanel.add(img);
//		imgPanel.setPreferredSize(new Dimension(100,100));
//		imgPanel.setSize(100,100);
//		img.setLocation(new Point(alleyOptions.getWidth()-10,alleyOptions.getHeight()));
		alleyOptions.add(img);
		alleyOptions.setSize(500,600);
		// alleyOptions.setVisible(true);

		alleyOptions.show();


		while (a1.pauseAlley){
			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		int numLanes = a1.numLanesVal;
		int maxPatronsPerParty= a1.maxPatronVal;
		ControlDesk controlDesk = new ControlDesk( numLanes );

		ControlDeskView cdv = new ControlDeskView( controlDesk, maxPatronsPerParty);
		controlDesk.addObserver( cdv );

	}

	public void itemStateChanged(ItemEvent e)
	{
		// if the state combobox is changed
		if (e.getSource() == lanesComb) {
			numLanesVal=Integer.parseInt((String) lanesComb.getSelectedItem());
		}
		else if(e.getSource()==patronComb){
			maxPatronVal=Integer.parseInt((String) patronComb.getSelectedItem());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		pauseAlley=false;
		alleyOptions.hide();
	}
}