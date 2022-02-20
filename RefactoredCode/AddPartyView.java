

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.util.*;

/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 *
 */

public class AddPartyView implements ActionListener, ListSelectionListener {

	private int maxSize;
	private JFrame win;
	private JButton addPatron, newPatron, remPatron, finished;
	private JList partyList, allBowlers;
	private Vector party, bowlerdb;


	private ControlDeskView controlDesk;

	private String selectedNick, selectedMember;
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
	public AddPartyView(ControlDeskView controlDesk, int max) {

		this.controlDesk = controlDesk;
		maxSize = max;

		win = new JFrame("Add Party");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout(1, 3));

		// Party Panel
		JPanel partyPanel = addPanel("Your Party");

		party = new Vector();
		Vector empty = new Vector();
		empty.add("(Empty)");

		partyList = new JList(empty);
		partyList.setFixedCellWidth(120);
		partyList.setVisibleRowCount(5);
		partyList.addListSelectionListener(this);
		JScrollPane partyPane = new JScrollPane(partyList);
		//        partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		partyPanel.add(partyPane);

		// Bowler Database
		JPanel bowlerPanel = addPanel("Bowler Database");

		try {
			bowlerdb = new Vector(BowlerFile.getBowlers());
		} catch (Exception e) {
			System.err.println("File Error");
			bowlerdb = new Vector();
		}
		allBowlers = new JList(bowlerdb);
		allBowlers.setVisibleRowCount(8);
		allBowlers.setFixedCellWidth(120);
		JScrollPane bowlerPane = new JScrollPane(allBowlers);
		bowlerPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		allBowlers.addListSelectionListener(this);
		bowlerPanel.add(bowlerPane);

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));

		Insets buttonMargin = new Insets(4, 4, 4, 4);

		addPatron = new JButton("Add to Party");
		JPanel addPatronPanel =newPanel(addPatron);

		remPatron = new JButton("Remove Member");
		JPanel remPatronPanel = newPanel(remPatron);

		newPatron = new JButton("New Patron");
		JPanel newPatronPanel = newPanel(newPatron);

		finished = new JButton("Finished");
		JPanel finishedPanel = newPanel(finished);

		buttonPanel.add(addPatronPanel);
		buttonPanel.add(remPatronPanel);
		buttonPanel.add(newPatronPanel);
		buttonPanel.add(finishedPanel);

		// Clean up main panel
		colPanel.add(partyPanel);
		colPanel.add(bowlerPanel);
		colPanel.add(buttonPanel);

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
		if (e.getSource().equals(addPatron)) {
			if (selectedNick != null && party.size() < maxSize) {
				if (party.contains(selectedNick)) {
					System.err.println("Member already in Party");
				} else {
					party.add(selectedNick);
					partyList.setListData(party);
				}
			}
		}
		if (e.getSource().equals(remPatron)) {
			if (selectedMember != null) {
				party.removeElement(selectedMember);
				partyList.setListData(party);
			}
		}
		if (e.getSource().equals(newPatron)) {
			NewPatronView newPatron = new NewPatronView( this );
		}
		if (e.getSource().equals(finished)) {
			if ( party != null && party.size() > 0) {
				controlDesk.updateAddParty( this );
			}
			win.hide();
		}

	}

	/**
	 * Handler for List actions
	 * @param e the ListActionEvent that triggered the handler
	 */

	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(allBowlers)) {
			selectedNick =
					((String) ((JList) e.getSource()).getSelectedValue());
		}
		if (e.getSource().equals(partyList)) {
			selectedMember =
					((String) ((JList) e.getSource()).getSelectedValue());
		}
	}

	/**
	 * Accessor for Party
	 */

	public Vector getNames() {
		return party;
	}

	/**
	 * Called by NewPatronView to notify AddPartyView to update
	 *
	 * @param newPatron the NewPatronView that called this method
	 */

	public void updateNewPatron(NewPatronView newPatron) {
		try {
			Bowler checkBowler = BowlerFile.getBowlerInfo( newPatron.getNickName() );
			if ( checkBowler == null ) {
				BowlerFile.putBowlerInfo(
						newPatron.getNickName(),
						newPatron.getFull(),
						newPatron.getEmail());
				bowlerdb = new Vector(BowlerFile.getBowlers());
				allBowlers.setListData(bowlerdb);
				party.add(newPatron.getNickName());
				partyList.setListData(party);
			} else {
				System.err.println( "A Bowler with that name already exists." );
			}
		} catch (Exception e2) {
			System.err.println("File I/O Error");
		}
	}

	/**
	 * Accessor for Party
	 */

	public Vector getParty() {
		return party;
	}

}
