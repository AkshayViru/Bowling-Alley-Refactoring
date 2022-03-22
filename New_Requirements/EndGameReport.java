/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.text.*;

public class EndGameReport implements ActionListener, ListSelectionListener {

	private JFrame win;
	private JButton printButton, finished;
	private JList memberList;
	private Vector myVector;
	private Vector retVal;
	JPanel resultPanel;
	JTextArea resultField;
	Lane lane;

	{
		try {
			lane = new Lane();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int result;

	private String selectedMember;
	public JPanel addPanel(String str){
		JPanel Panel = new JPanel();
		Panel.setLayout(new FlowLayout());
		Panel.setBorder(new TitledBorder(str));
		return Panel;
	}
	public JPanel newPanel(JButton jbutton) {
		JPanel PatronPanel = new JPanel();
		PatronPanel.setLayout(new FlowLayout());
		jbutton.addActionListener(this);
		PatronPanel.add(jbutton);
		return PatronPanel;
	}
	public EndGameReport( String partyName, Party party ) {
	
		result =0;
		retVal = new Vector();
		win = new JFrame("End Game Report for " + partyName + "?" );
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout( 1, 2 ));

		// Member Panel
		JPanel partyPanel = new JPanel();
		partyPanel.setLayout(new FlowLayout());
		partyPanel.setBorder(new TitledBorder("Party Members"));
		
		Vector myVector = new Vector();
		Iterator iter = (party.getMembers()).iterator();
		while (iter.hasNext()){
			myVector.add( ((Bowler)iter.next()).getNickName() );
		}

		memberList = new JList(myVector);
		memberList.setFixedCellWidth(120);
		memberList.setVisibleRowCount(5);
		memberList.addListSelectionListener(this);
		JScrollPane partyPane = new JScrollPane(memberList);
		//        partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		partyPanel.add(partyPane);

		partyPanel.add( memberList );

		// Button Panel
		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 1));

		Insets buttonMargin = new Insets(4, 4, 4, 4);
		
		printButton = new JButton("Print Report");
		JPanel printButtonPanel = newPanel(printButton);
		finished = new JButton("Finished");
		JPanel finishedPanel = newPanel(finished);
//
		buttonPanel.add(printButton);
		buttonPanel.add(finished);
		resultPanel = new JPanel();
//		resultPanel.setLayout(new FlowLayout());
		resultPanel.setBorder(new TitledBorder("Results"));
		resultField=new JTextArea(10,50);
		resultField.setEditable(false);
		// Clean up main panel
		colPanel.add(partyPanel);
		colPanel.add(buttonPanel);
		resultPanel.add(resultField);
		colPanel.add(resultPanel,"West");

		win.getContentPane().add("Center", colPanel);

		win.pack();

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.show();

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(printButton)) {
			if(selectedMember!=null){
				Vector scoreList = new Vector();

					try {
						scoreList = ScoreSearch.getPlayerLastFive(selectedMember);
					} catch (IOException ex) {
						ex.printStackTrace();
					}


				int minVal = Math.min(scoreList.size(), 5);
				resultField.setText("");
				for (int i = 0; i < minVal; i++) {
					resultField.append(scoreList.get(i) + "\n");
			}
			}
			else{
			//Add selected to the vector.
			retVal.add(selectedMember);
//			System.out.println("Print report clicked");
			String s="";
			try {
				s= Files.readString(Path.of("CURRENTHISTORY.DAT"));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			resultField.setText(s);
//			System.out.println(s);
//			lane.getReport();
		}}
		if (e.getSource().equals(finished)) {
			win.hide();
			RandomAccessFile out = null;
			try {
				System.out.println("emptying");
				out = new RandomAccessFile("CURRENTHISTORY.DAT", "rw");
				out.setLength(0);
				out.close();
			} catch (FileNotFoundException s) {
				s.printStackTrace();
			} catch (IOException s) {
				s.printStackTrace();
			}
			result = 1;
		}

	}

	public void valueChanged(ListSelectionEvent e) {
		selectedMember =
			((String) ((JList) e.getSource()).getSelectedValue());
	}

	public Vector getResult() {
		while ( result == 0 ) {
			try {
				Thread.sleep(10);
			} catch ( InterruptedException e ) {
				System.err.println( "Interrupted" );
			}
		}
		return retVal;	
	}
	
	public void destroy() {
		win.hide();
	}

//	public static void main( String args[] ) {
//		Vector bowlers = new Vector();
//		for ( int i=0; i<4; i++ ) {
//			bowlers.add( new Bowler( "aaaaa", "aaaaa", "aaaaa" ) );
//		}
//		Party party = new Party( bowlers );
//		String partyName="abc";
//		EndGameReport e = new EndGameReport( partyName, party );
//	}
	
}
