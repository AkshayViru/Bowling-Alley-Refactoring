/*
 *  constructs a prototype Lane View
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.util.*;

import static java.lang.Thread.sleep;

public class LaneView implements LaneObserver, ActionListener {

	private int roll;
	private boolean initDone = true;

	JFrame frame;
	Container cpanel;
	Vector bowlers;
	int cur;
	Iterator bowlIt;
	JLabel turn;
	int count = 0;

	JPanel[][] balls;
	JLabel[][] ballLabel;
	JPanel[][] scores;
	JLabel[][] scoreLabel;
	JPanel[][] ballGrid;
	JPanel[] pins;
	JPanel[] bowlButtonPanels;
	JButton maintenance;
	Lane lane;
	JButton bowlButtoni;
	private ActionListener bowlButtonListener;
	JButton[] bowlButtonArr;
	JLabel bowlerName;
	 JLabel res;
	 JPanel show_turn;

	public static int getSecondLargest( int size, int[] arr1){
		Arrays.sort(arr1);
		return arr1[size-2];
	}
	public static int getLargest( int size, int[] arr1){
		Arrays.sort(arr1);
		return arr1[size-1];
	}
	public int getSecondHighestPlayer(int[] finalScores){
		int firstind = -1, fmax = 0;
		int arrSize = finalScores.length;
		int idx = 0;
		while( idx < arrSize ){
			if(finalScores[idx]>fmax)
			{
				fmax = finalScores[idx];
				firstind = idx;
			}
			idx++;
		}
		idx = 0;
		int secondind = -1, smax = 0;
		while( idx < arrSize ){
			if(idx!= firstind && finalScores[idx]>smax)
			{
				smax = finalScores[idx];
				secondind = idx;
			}
			idx++;
		}
		return secondind;
	}

	public JPanel newPanel(JButton jbutton) {
		JPanel PatronPanel = new JPanel();
		PatronPanel.setLayout(new FlowLayout());
		jbutton.addActionListener(this);
		PatronPanel.add(jbutton);
		return PatronPanel;
	}
	public LaneView(Lane lane, int laneNum) {

		this.lane = lane;

		initDone = true;
		frame = new JFrame("Lane " + laneNum + ":");
		cpanel = frame.getContentPane();
		cpanel.setLayout(new BorderLayout());

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.hide();
			}
		});

		cpanel.add(new JPanel());

	}
	private void sleepT(int sleepTime){
		try {
			sleep(sleepTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void show() {
		frame.show();
	}

	public void hide() {
		frame.hide();
	}

	private JPanel makeFrame(Party party) {

		initDone = false;
		bowlers = party.getMembers();
		int numBowlers = bowlers.size();

		JPanel panel = new JPanel();
		JPanel header=new JPanel(new GridLayout(0,1));
		panel.setLayout(new GridLayout(0, 1));

		balls = new JPanel[numBowlers][24];
		ballLabel = new JLabel[numBowlers][24];
		scores = new JPanel[numBowlers][11];//10->11
		scoreLabel = new JLabel[numBowlers][11];//10->11
		ballGrid = new JPanel[numBowlers][11];//10->11
		pins = new JPanel[numBowlers];
		bowlButtonArr=new JButton[numBowlers];
		turn=new JLabel("Current turn to bowl: ");
		res=new JLabel();
//		turn.setSize(1000,100);
		show_turn=new JPanel(new FlowLayout());
		show_turn.add(turn);
		bowlerName=new JLabel(((Bowler) bowlers.get(1)).getNickName());
//		bowlerName.setSize(1000,100);
		show_turn.add(bowlerName);
		header.add(show_turn);
		header.add(res);
		panel.add(header);
		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 23; j++) {
				ballLabel[i][j] = new JLabel(" ");
				balls[i][j] = new JPanel();
				balls[i][j].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				balls[i][j].add(ballLabel[i][j]);
			}
		}

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 9; j++) {//9->10->11
				ballGrid[i][j] = new JPanel();
				ballGrid[i][j].setLayout(new GridLayout(0, 3));
				ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
			}
			int j = 9;
			ballGrid[i][j] = new JPanel();
			ballGrid[i][j].setLayout(new GridLayout(0, 3));
			ballGrid[i][j].add(balls[i][18]);
			ballGrid[i][j].add(balls[i][19]);
			ballGrid[i][j].add(balls[i][20]);
			j=10;
			ballGrid[i][j] = new JPanel();
			ballGrid[i][j].setLayout(new GridLayout(0, 3));
			ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
			ballGrid[i][j].add(balls[i][21], BorderLayout.EAST);


		}
		for (int i = 0; i < numBowlers; i++) {
			bowlButtonArr[i]=new JButton("Bowl!");
			if(i!=1)
				bowlButtonArr[i].setEnabled(false);
		}
		for (int i = 0; i <numBowlers ; i++) {
			int finalI = i;
			if(i==numBowlers-1){
				bowlButtonArr[i].addActionListener(e -> {
					lane.bowlButtonClick();
					bowlButtonArr[finalI].setEnabled(false);
					bowlerName.setText(((Bowler) bowlers.get(0)).getNickName());
					sleepT(1200);
					bowlButtonArr[0].setEnabled(true);
				});
			}
			else {


				bowlButtonArr[i].addActionListener(e -> {
					lane.bowlButtonClick();
					bowlButtonArr[finalI].setEnabled(false);
					bowlerName.setText(((Bowler) bowlers.get(finalI+1)).getNickName());
					sleepT(1200);
					bowlButtonArr[finalI + 1].setEnabled(true);
				});
			}


		}
		for (int i = 0; i != numBowlers; i++) {
			pins[i] = new JPanel();

			pins[i].setBorder(
				BorderFactory.createTitledBorder(
					((Bowler) bowlers.get(i)).getNickName()));

			pins[i].setLayout(new GridLayout(0, 11));
			for (int k = 0; k != 11; k++) {//10->11
				scores[i][k] = new JPanel();
				scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
				scores[i][k].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				scores[i][k].setLayout(new GridLayout(0, 1));
				scores[i][k].add(ballGrid[i][k], BorderLayout.EAST);
				scores[i][k].add(scoreLabel[i][k], BorderLayout.SOUTH);
				pins[i].add(scores[i][k], BorderLayout.EAST);
			}
//			bowlButtonPanels[i]=new JPanel();
//			bowlButtonPanels[i].add(bowlButtoni);
			pins[i].add(bowlButtonArr[i]);
			panel.add(pins[i]);
		}

		initDone = true;
		return panel;
	}

	public void receiveLaneEvent(LaneEvent le) {
		if (lane.isPartyAssigned()) {
			int numBowlers = le.getParty().getMembers().size();
			while (!initDone) {
				//System.out.println("chillin' here.");
				try {
					sleep(1);
				} catch (Exception e) {
				}
			}

			if (le.getFrameNum() == 1
				&& le.getBall() == 0
				&& le.getIndex() == 0) {
				System.out.println("Making the frame.");
				cpanel.removeAll();
				cpanel.add(makeFrame(le.getParty()), "Center");

				// Button Panel
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new FlowLayout());

				Insets buttonMargin = new Insets(4, 4, 4, 4);

//				bowlButton = new JButton("Bowl!");
//				JPanel bowlPanel=newPanel(bowlButton);
//				buttonPanel.add(bowlPanel);

				maintenance = new JButton("Maintenance Call");
				JPanel maintenancePanel = newPanel(maintenance);
				buttonPanel.add(maintenancePanel);


				cpanel.add(buttonPanel, "South");

				frame.pack();

			}

			int[][] lescores = le.getCumulScore();
			for (int k = 0; k < numBowlers; k++) {
				for (int i = 0; i <= le.getFrameNum() - 1; i++) {
					if (lescores[k][i] != 0)
						scoreLabel[k][i].setText(
							(new Integer(lescores[k][i])).toString());
				}
				for (int i = 0; i < 21; i++) {
					if (((int[]) ((HashMap) le.getScore())
						.get(bowlers.get(k)))[i]
						!= -1)
						if (((int[]) ((HashMap) le.getScore())
							.get(bowlers.get(k)))[i]
							== 10
							&& (i % 2 == 0 || i == 19))
							ballLabel[k][i].setText("X");
						else if (
							i > 0
								&& ((int[]) ((HashMap) le.getScore())
									.get(bowlers.get(k)))[i]
									+ ((int[]) ((HashMap) le.getScore())
										.get(bowlers.get(k)))[i
									- 1]
									== 10
								&& i % 2 == 1)
							ballLabel[k][i].setText("/");
						else if ( ((int[])((HashMap) le.getScore()).get(bowlers.get(k)))[i] == -2 ){
							
							ballLabel[k][i].setText("F");
						} else
							ballLabel[k][i].setText(
								(new Integer(((int[]) ((HashMap) le.getScore())
									.get(bowlers.get(k)))[i]))
									.toString());
				}
			}

			if( le.getFrameNum() == 10  ){
				//System.out.println( "Hello 10" );
				count += 1;
				if( count > ((2*numBowlers) - 1) ){
					int finalCumScores[] = new int[numBowlers];
					for( int i = 0 ; i < numBowlers ; i++ ){
						finalCumScores[i] = lescores[i][9];}
						int secondInd = getSecondHighestPlayer(finalCumScores);
						int largest =getLargest(finalCumScores.length,finalCumScores);
						int largesst2=getSecondLargest(finalCumScores.length,finalCumScores);
						System.out.println("Second ind"+largest +"largest"+largesst2);
						if( secondInd >= 0 ){
							int rg=Math.min((largest-largesst2),8);
							ballLabel[secondInd][21].setText(String.valueOf((int)(Math.random()*(rg))));
							res.setText("Second Highest Player was unable to cross the \nHighest player score");
//							for( int j = 0 ; j < numBowlers ; j++ ){
//								if( j!=secondInd )
//									ballLabel[j][21].setText(" ");
//							}
						}
//					}//here
				}
			}


		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(maintenance)) {
			lane.pauseGame();
		}
		else if (e.getSource().equals(bowlButtoni)) {
			lane.bowlButtonClick();
		}
	}
//	public void actionPerformed(ActionEvent e,JButton a,JButton b){
//		lane.bowlButtonClick();
//		a.setEnabled(false);
//		b.setEnabled(true);
//	}



}
//class bowlListener implements ActionListener{
//	Lane lane;
//	bowlListener(Lane lane){
//		this.lane=lane;
//	}
//	public void actionPerformed(ActionEvent e){
//		lane.bowlButtonClick();
//	}
//
//}