import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

public class ScoreSearchView implements ActionListener, ListSelectionListener {

    private final JPanel resultPanel;
    private final JTextArea resultField;
    private final JButton overallMinButton;
    private final JButton overallMaxButton;
    private final JPanel overallMax;
    private final JPanel overallMin;
    private final JPanel buttonPanel = new JPanel();
    private final JButton playerMinButton;
    private final JPanel playerMin;
    private final JButton playerMaxButton;
    private final JPanel playerMax;
    private JList allBowlers;

    private Vector bowlerdb;
    private ControlDeskView controlDeskProperty;
    private JFrame window;
    private JButton playerLastFiveButton;
    private JPanel playerLastFive;
    private JButton lastTenButton;
    private JPanel lastTen;
    private JButton lastTenHighButton;
    private JPanel lastTenHigh;
    private JButton topPlayerButton;
    private JPanel topPlayer;
    private JButton finishedButton;
    private JPanel finished;
    String nickName = "";
    private JButton playerLeastButton;
    private JPanel playerLeast;
    private JButton playerHghstButton;
    private JPanel playerHigest;

    public JPanel addPanel(String str) {
        JPanel Panel = new JPanel();
        Panel.setLayout(new FlowLayout());
        Panel.setBorder(new TitledBorder(str));
        return Panel;
    }

    public JPanel newPanel(JButton jbutton) {
        JPanel PatronPanel = new JPanel();
        PatronPanel.setSize(PatronPanel.getPreferredSize());
        // PatronPanel.setLayout(new FlowLayout());
        jbutton.addActionListener(this);
        PatronPanel.add(jbutton);
        return PatronPanel;
    }

    public ScoreSearchView(ControlDeskView cd) {
        window = new JFrame("Query Scores");
        // window.getContentPane().setLayout(new BorderLayout());

        window.setSize(2000, 1000);
        window.setLayout(new GridLayout(1, 3));
        this.controlDeskProperty = cd;

        // Result view
        resultPanel = new JPanel();
        // resultPanel.setLayout(new FlowLayout());
        resultPanel.setBorder(new TitledBorder("Results"));
        resultField = new JTextArea(100, 50);
        resultField.setEditable(false);
        resultPanel.add(resultField);
        window.add(resultPanel);
        // Bowler List
        JPanel bowlerPanel = addPanel("Bowler List");

        try {
            bowlerdb = new Vector(BowlerFile.getBowlers());
        } catch (Exception e) {
            System.err.println("File Error");
            bowlerdb = new Vector();
        }
        allBowlers = new JList(bowlerdb);
        allBowlers.setVisibleRowCount(10);
        allBowlers.setFixedCellWidth(120);
        JScrollPane bowlerPane = new JScrollPane(allBowlers);
        bowlerPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        allBowlers.addListSelectionListener(this);
        bowlerPanel.add(bowlerPane);
        window.add(bowlerPanel);

        overallMinButton = new JButton("Overall Min Score");
        overallMin = newPanel(overallMinButton);
        buttonPanel.add(overallMin);

        overallMaxButton = new JButton("Overall Max Score");
        overallMax = newPanel(overallMaxButton);
        buttonPanel.add(overallMax);

        playerLeastButton = new JButton("Player Minimum");
        playerLeast = newPanel(playerLeastButton);
        buttonPanel.add(playerLeast);

        playerHghstButton = new JButton("Player Maximum");
        playerHigest = newPanel(playerHghstButton);
        buttonPanel.add(playerHigest);

        playerMinButton = new JButton("Player Least 5");
        playerMin = newPanel(playerMinButton);
        buttonPanel.add(playerMin);

        playerMaxButton = new JButton("Player Top 5");
        playerMax = newPanel(playerMaxButton);
        buttonPanel.add(playerMax);

        playerLastFiveButton = new JButton("Player Last 5");
        playerLastFive = newPanel(playerLastFiveButton);
        buttonPanel.add(playerLastFive);

        lastTenButton = new JButton("Last 10 Scores");
        lastTen = newPanel(lastTenButton);
        buttonPanel.add(lastTen);

        lastTenHighButton = new JButton("Top 10 Highest");
        lastTenHigh = newPanel(lastTenHighButton);
        buttonPanel.add(lastTenHigh);

        topPlayerButton = new JButton("Top Player");
        topPlayer = newPanel(topPlayerButton);
        buttonPanel.add(topPlayer);

        finishedButton = new JButton("Finished");
        finished = newPanel(finishedButton);
        buttonPanel.add(finished);

        buttonPanel.setLayout(new GridLayout(6, 2));

        window.add(buttonPanel);

        window.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(overallMinButton)) {
            // System.out.println("overall min clicked");
            int a = 0;
            try {
                a = ScoreSearch.getOverallMin();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // System.out.println("Min Val"+a);
            resultField.setText(Integer.toString(a));
        } else if (e.getSource().equals(overallMaxButton)) {
            int a = 0;
            try {
                a = ScoreSearch.getOverallMax();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            resultField.setText(Integer.toString(a));
        }
        
        else if (e.getSource().equals(playerLeastButton)) {
            int a = 0;
            try {
                a = ScoreSearch.getPlayerLowest(nickName);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            resultField.setText(Integer.toString(a));
        } 
        
        else if (e.getSource().equals(playerHghstButton)) {
            int a = 0;
            try {
                a = ScoreSearch.getPlayerHighest(nickName);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            resultField.setText(Integer.toString(a));
        } 
        
        
        
        else if (e.getSource().equals(playerMinButton)) {
            Vector<Integer> scoreList = new Vector<Integer>();

            if (nickName != null) {
                try {
                    scoreList = ScoreSearch.getPlayerMin(nickName);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            int minVal = Math.min(scoreList.size(), 5);
            resultField.setText("");
            for (int i = 0; i < minVal; i++) {
                resultField.append(Integer.toString(scoreList.get(i)) + "\n");
            }
        }

        else if (e.getSource().equals(playerMaxButton)) {
            Vector<Integer> scoreList = new Vector<Integer>();
            if (nickName != null) {
                try {
                    scoreList = ScoreSearch.getPlayerMax(nickName);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            int minVal = Math.min(scoreList.size(), 5);
            resultField.setText("");
            for (int i = 0; i < minVal; i++) {
                resultField.append(Integer.toString(scoreList.get(i)) + "\n");
            }
        }

        else if (e.getSource().equals(playerLastFiveButton)) {
            Vector scoreList = new Vector();
            if (nickName != null) {
                try {
                    scoreList = ScoreSearch.getPlayerLastFive(nickName);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            int minVal = Math.min(scoreList.size(), 5);
            resultField.setText("");
            for (int i = 0; i < minVal; i++) {
                resultField.append(scoreList.get(i) + "\n");
            }
        }

        else if (e.getSource().equals(lastTenButton)) {
            Vector scoreList = new Vector();
           
                try {
                    scoreList = ScoreSearch.getLastTen();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            int minVal = Math.min(scoreList.size(), 10);
            resultField.setText("");
            for (int i = 0; i < minVal; i++) {
                resultField.append(scoreList.get(i) + "\n");
            }
        }
        else if (e.getSource().equals(lastTenHighButton)) {
            Vector scoreList = new Vector();
           
                try {
                    scoreList = ScoreSearch.getTenHighest();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            int minVal = Math.min(scoreList.size(), 10);
            resultField.setText("");
            for (int i = 0; i < minVal; i++) {
                resultField.append(scoreList.get(i) + "\n");
            }
        }

        else if (e.getSource().equals(topPlayerButton)) {
            String topPlayer = "";
                try {
                    topPlayer = ScoreSearch.getTopPlayer();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                resultField.setText(topPlayer);

        }
        else if (e.getSource().equals(finishedButton)){
            window.hide();
        }


    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource().equals(allBowlers)) {
            nickName = ((String) ((JList) e.getSource()).getSelectedValue());
        }
    }
}
