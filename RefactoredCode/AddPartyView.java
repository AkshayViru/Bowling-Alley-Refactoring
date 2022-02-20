import java.awt.*;
import java.awt.event.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.event.*;
import java.util.*;
import java.util.*;
import java.text.*;
import java.text.*;
/**
/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 * Constructor for GUI used to Add Parties to the waiting party queue.
 *  
 *  
 */
 */
public class AddPartyView implements ActionListener, ListSelectionListener {
public class AddPartyView implements ActionListener, ListSelectionListener {
	private int maxSize;
	private int maxSize;
	private JFrame win;
	private JFrame win;
	private JButton addPatron, newPatron, remPatron, finished;
	private JButton addPatron, newPatron, remPatron, finished;
	private JList partyList, allBowlers;
	private JList partyList, allBowlers;
	private Vector party, bowlerdb;
	private Vector party, bowlerdb;
	private Integer lock;
	private Integer lock;
	private ControlDeskView controlDesk;
	private ControlDeskView controlDesk;
	private String selectedNick, selectedMember;
	private String selectedNick, selectedMember;
	public JPanel addPanel(String str){
	public JPanel addPanel(String str){
		JPanel Panel = new JPanel();
		JPanel Panel = new JPanel();
		Panel.setLayout(new FlowLayout());
		Panel.setLayout(new FlowLayout());
		Panel.setBorder(new TitledBorder(str));
		Panel.setBorder(new TitledBorder(str));
		return Panel;
		return Panel;
	}
	}
	public JPanel newPanel(JButton jbutton) {
	public JPanel newPanel(JButton jbutton) {
		JPanel PatronPanel = new JPanel();
		JPanel PatronPanel = new JPanel();
		PatronPanel.setLayout(new FlowLayout());
		PatronPanel.setLayout(new FlowLayout());
		jbutton.addActionListener(this);
		jbutton.addActionListener(this);
		PatronPanel.add(jbutton);
		PatronPanel.add(jbutton);
		return PatronPanel;
		return PatronPanel;
	} 
	} 
	public AddPartyView(ControlDeskView controlDesk, int max) {
	public AddPartyView(ControlDeskView controlDesk, int max) {
		this.controlDesk = controlDesk;
		this.controlDesk = controlDesk;
		maxSize = max;
		maxSize = max;
		win = new JFrame("Add Party");
		win = new JFrame("Add Party");
		win.getContentPane().setLayout(new BorderLayout());
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);
		((JPanel) win.getContentPane()).setOpaque(false);
		JPanel colPanel = new JPanel();
		JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout(1, 3));
		colPanel.setLayout(new GridLayout(1, 3));
		
		
		// Party Panel
		// Party Panel
		JPanel partyPanel = addPanel("Your Party");
		JPanel partyPanel = addPanel("Your Party");
		party = new Vector();
		party = new Vector();
		Vector empty = new Vector();
		Vector empty = new Vector();
		empty.add("(Empty)");
		empty.add("(Empty)");
		partyList = new JList(empty);
		partyList = new JList(empty);
		partyList.setFixedCellWidth(120);
		partyList.setFixedCellWidth(120);
		partyList.setVisibleRowCount(5);
		partyList.setVisibleRowCount(5);
		partyList.addListSelectionListener(this);
		partyList.addListSelectionListener(this);
		JScrollPane partyPane = new JScrollPane(partyList);
		JScrollPane partyPane = new JScrollPane(partyList);
		//        partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//        partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		partyPanel.add(partyPane);
		partyPanel.add(partyPane);
		// Bowler Database
		// Bowler Database
		JPanel bowlerPanel = addPanel("Bowler Database");
		JPanel bowlerPanel = addPanel("Bowler Database");
		try {
		try {
			bowlerdb = new Vector(BowlerFile.getBowlers());
			bowlerdb = new Vector(BowlerFile.getBowlers());
		} catch (Exception e) {
		} catch (Exception e) {
			System.err.println("File Error");
			System.err.println("File Error");
			bowlerdb = new Vector();
			bowlerdb = new Vector();
		}
		}
		allBowlers = new JList(bowlerdb);
		allBowlers = new JList(bowlerdb);
		allBowlers.setVisibleRowCount(8);
		allBowlers.setVisibleRowCount(8);
		allBowlers.setFixedCellWidth(120);
		allBowlers.setFixedCellWidth(120);
		JScrollPane bowlerPane = new JScrollPane(allBowlers);
		JScrollPane bowlerPane = new JScrollPane(allBowlers);
		bowlerPane.setVerticalScrollBarPolicy(
		bowlerPane.setVerticalScrollBarPolicy(
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		allBowlers.addListSelectionListener(this);
		allBowlers.addListSelectionListener(this);
		bowlerPanel.add(bowlerPane);
		bowlerPanel.add(bowlerPane);
		// Button Panel
		// Button Panel
		JPanel buttonPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));
		buttonPanel.setLayout(new GridLayout(4, 1));
		Insets buttonMargin = new Insets(4, 4, 4, 4);
		Insets buttonMargin = new Insets(4, 4, 4, 4);
		
		
		addPatron = new JButton("Add to Party");
		addPatron = new JButton("Add to Party");
		JPanel addPatronPanel =newPanel(addPatron);
		JPanel addPatronPanel =newPanel(addPatron);
		remPatron = new JButton("Remove Member");
		remPatron = new JButton("Remove Member");
		JPanel remPatronPanel = newPanel(remPatron);
		JPanel remPatronPanel = newPanel(remPatron);
		newPatron = new JButton("New Patron");
		newPatron = new JButton("New Patron");
		JPanel newPatronPanel = newPanel(newPatron);
		JPanel newPatronPanel = newPanel(newPatron);
		
		
		finished = new JButton("Finished");
		finished = new JButton("Finished");
		JPanel finishedPanel = newPanel(finished);
		JPanel finishedPanel = newPanel(finished);
		buttonPanel.add(addPatronPanel);
		buttonPanel.add(addPatronPanel);
		buttonPanel.add(remPatronPanel);
		buttonPanel.add(remPatronPanel);
		buttonPanel.add(newPatronPanel);
		buttonPanel.add(newPatronPanel);
		buttonPanel.add(finishedPanel);
		buttonPanel.add(finishedPanel);
		// Clean up main panel
		// Clean up main panel
		colPanel.add(partyPanel);
		colPanel.add(partyPanel);
		colPanel.add(bowlerPanel);
		colPanel.add(bowlerPanel);
		colPanel.add(buttonPanel);
		colPanel.add(buttonPanel);
		win.getContentPane().add("Center", colPanel);
		win.getContentPane().add("Center", colPanel);
		win.pack();
		win.pack();
		// Center Window on Screen
		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.show();
		win.show();
	}
	}
	public void actionPerformed(ActionEvent e) {
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addPatron)) {
		if (e.getSource().equals(addPatron)) {
			if (selectedNick != null && party.size() < maxSize) {
			if (selectedNick != null && party.size() < maxSize) {
				if (party.contains(selectedNick)) {
				if (party.contains(selectedNick)) {
					System.err.println("Member already in Party");
					System.err.println("Member already in Party");
				} else {
				} else {
					party.add(selectedNick);
					party.add(selectedNick);
					partyList.setListData(party);
					partyList.setListData(party);
				}
				}
			}
			}
		}
		}
		if (e.getSource().equals(remPatron)) {
		if (e.getSource().equals(remPatron)) {
			if (selectedMember != null) {
			if (selectedMember != null) {
				party.removeElement(selectedMember);
				party.removeElement(selectedMember);
				partyList.setListData(party);
				partyList.setListData(party);
			}
			}
		}
		}
		if (e.getSource().equals(newPatron)) {
		if (e.getSource().equals(newPatron)) {
			NewPatronView newPatron = new NewPatronView( this );
			NewPatronView newPatron = new NewPatronView( this );
		}
		}
		if (e.getSource().equals(finished)) {
		if (e.getSource().equals(finished)) {
			if ( party != null && party.size() > 0) {
			if ( party != null && party.size() > 0) {
				controlDesk.updateAddParty( this );
				controlDesk.updateAddParty( this );
			}
			}
			win.hide();
			win.hide();
		}
		}
	}
	}
/**
/**
 * Handler for List actions
 * Handler for List actions
 * @param e the ListActionEvent that triggered the handler
 * @param e the ListActionEvent that triggered the handler
 */
 */
	public void valueChanged(ListSelectionEvent e) {
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(allBowlers)) {
		if (e.getSource().equals(allBowlers)) {
			selectedNick =
			selectedNick =
				((String) ((JList) e.getSource()).getSelectedValue());
				((String) ((JList) e.getSource()).getSelectedValue());
		}
		}
		if (e.getSource().equals(partyList)) {
		if (e.getSource().equals(partyList)) {
			selectedMember =
			selectedMember =
				((String) ((JList) e.getSource()).getSelectedValue());
				((String) ((JList) e.getSource()).getSelectedValue());
		}
		}
	}
	}
/**
/**
 * Accessor for Party
 * Accessor for Party
 */
 */
	public Vector getNames() {
	public Vector getNames() {
		return party;
		return party;
	}
	}
/**
/**
 * Called by NewPatronView to notify AddPartyView to update
 * Called by NewPatronView to notify AddPartyView to update
 * 
 * 
 * @param newPatron the NewPatronView that called this method
 * @param newPatron the NewPatronView that called this method
 */
 */


	public void updateNewPatron(NewPatronView newPatron) {
	public void updateNewPatron(NewPatronView newPatron) {
		try {
		try {
			Bowler checkBowler = BowlerFile.getBowlerInfo( newPatron.getNick() );
			Bowler checkBowler = BowlerFile.getBowlerInfo( newPatron.getNickName() );
			if ( checkBowler == null ) {
			if ( checkBowler == null ) {
				BowlerFile.putBowlerInfo(
				BowlerFile.putBowlerInfo(
					newPatron.getNick(),
					newPatron.getNickName(),
					newPatron.getFull(),
					newPatron.getFull(),
					newPatron.getEmail());
					newPatron.getEmail());
				bowlerdb = new Vector(BowlerFile.getBowlers());
				bowlerdb = new Vector(BowlerFile.getBowlers());
				allBowlers.setListData(bowlerdb);
				allBowlers.setListData(bowlerdb);
				party.add(newPatron.getNick());
				party.add(newPatron.getNickName());
				partyList.setListData(party);
				partyList.setListData(party);
			} else {
			} else {
				System.err.println( "A Bowler with that name already exists." );
				System.err.println( "A Bowler with that name already exists." );
			}
			}
		} catch (Exception e2) {
		} catch (Exception e2) {
			System.err.println("File I/O Error");
			System.err.println("File I/O Error");
		}
		}
	}
	}
/**
/**
 * Accessor for Party
 * Accessor for Party
 */
 */
	public Vector getParty() {
	public Vector getParty() {
		return party;
		return party;
	}
	}
}
}
