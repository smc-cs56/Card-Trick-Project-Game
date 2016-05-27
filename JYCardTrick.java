// DOES NOT USE JYOutputHelper or JYInputHelper. Instead, I put the method
// from JYOutputHelper in here.

// Things To improve on: Background Image for JPanel
// Images for JButtons, and resizing the JButtons
// Show image of player's card in a better way.
// Better way to ask the user to choose a group.
// Fixing location of JFrame. ie, move the JFrame --> click a group, and the location
// resets. That doesn't look aesthetically nice.
// 
// Things we need: JFrame from Menu GUI allowed to be access by this class.
// We need one JFrame for this project.
// ie JFrameFromMenuGUI.add.(this.panel);

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

// Maybe its easier if we just use multiple JFrames
public class JYCardTrick extends JFrame implements ActionListener {
	protected final int nGroupNumber = 4;
	protected final int nCardNumber = 4;
	private Map<String, Integer> mapCards;
	private List<String> selectedCards;
	private int nSelectedGroup;

private JButton close;
	// Player can only click a button twice
	private int buttonPressedCounter = 0;
	
	// JPanel for game. JFrame should be from Menu GUI. JButton - groups
	private  JPanel panel = new JPanel();
	
	
	
	
		
	//private  JFrame frame = new JFrame();
	private List<JButton> groupButtons = new ArrayList<JButton>();

	private static JYCardTrick singletonCardTrick = null;//new JYCardTrick();

	private JYCardTrick() 
	{
		
	
   
		
		this.setSize(600, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		for (int nGroup = 1; nGroup <= nGroupNumber; nGroup++)
		{
			JButton button = new JButton("Group " + nGroup);
			groupButtons.add(button);
		}

		mapCards = JYUtil.createMapData(nGroupNumber, nCardNumber);

		// make these buttons do something.
		for (JButton jbutton : groupButtons)
		{
			jbutton.addActionListener(this);
		}

		if (mapCards.size() > 0)
		{
			// Sort map by value
			mapCards = JYUtil.sortByComparator(mapCards);

			// Sort cards by group and display the JPanel on JFrame
			this.showCardsByGroup(mapCards);
		}
	}

	/**
	* Adds the cards and buttons to the JPanel and display JFrame
	*
	* Input: Map - mapData ---> String - Card Suit, Int - number of Card
	*/
	public void showCardsByGroup(Map<String, Integer> mapData)
	{
		
	
		
		
		int fourCardsOneRow = 0; // if four cards one row, add JButton
		int index = 0; // used for groupButtons[]

		// JPanels will stack whenever this method is called.
		// Remove the old JPanel when user selects a group.
		this.setPanel();
		this.panel.setLayout(new GridLayout(4, 5));

		for (Map.Entry<String, Integer> entry : mapData.entrySet()) {

			// get the cards in card/ directory
			ImageIcon sampleCard = new ImageIcon("card/" + entry.getKey() + ".png");
			this.panel.add(new JLabel(sampleCard));
			fourCardsOneRow++;

			// add button at the end of each row
			if (fourCardsOneRow == 4) {
				this.panel.add(this.groupButtons.get(index++));
				fourCardsOneRow = 0; 
			}
		}

		this.panel.setBackground(Color.gray);
		this.add(panel);
		this.setVisible(true);
	}

	/**
	* Checks which group was chosen
	*/
	public void actionPerformed(ActionEvent e) {
				
	
	
	
	
	
		if(e.getSource()==close)
 		{
			
			dispose();
			TestMenu menu = new TestMenu();
			
 		}
		
		
		for (int nIndex = 0; nIndex < nGroupNumber; nIndex++)
		{
			if ( (buttonPressedCounter != 2) && (e.getSource() == groupButtons.get(nIndex)) ) {
				nSelectedGroup = nIndex + 1;
				buttonPressedCounter++;
			}
		}

		// display user card.
		if (buttonPressedCounter == 2) {
			for (Map.Entry<String, Integer> entry : mapCards.entrySet()) {
				if (entry.getKey() == selectedCards.get(nSelectedGroup - 1)) {
					JOptionPane.showMessageDialog(null, "Your card is " + entry.getKey());
					TestMenu menu = new TestMenu();
					dispose();
		
		
					
					break;
					// I want this to be an image of the card instead.
				}
			}
		}

		// Replace shuffles the deck after choosing a group
		if (buttonPressedCounter < 2){
			selectedCards = JYUtil.getKeyFromValue(mapCards, nSelectedGroup);

			// Create new groups and split selected group cards
			mapCards = JYUtil.regroupWithSelectedGroup(nGroupNumber, nCardNumber, selectedCards);

			// Sort cards by group
			mapCards = JYUtil.sortByComparator(mapCards);

			this.remove(panel);
			this.showCardsByGroup(mapCards);

			if (buttonPressedCounter == 1) {
				JOptionPane.showMessageDialog(null, "Where is your card? Select a group(1~" + nGroupNumber + "): ");
			}
		}
	}

	public JPanel getPanel() {
		return panel;
		
	}

	private void setPanel() {
		this.panel = new JPanel();
		
	}

	// Singleton class fo dat ass bruh
	public synchronized static JYCardTrick getInstance() {
		if (singletonCardTrick == null) {
			singletonCardTrick = new JYCardTrick();
		}

		return singletonCardTrick;
	}

	public static void main(String[] args) {

		JYCardTrick cardTrick = new JYCardTrick();

	}

	
}
