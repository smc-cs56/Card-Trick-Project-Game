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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class JYCardTrick extends JFrame implements ActionListener {
	protected final int nGroupNumber = 4;
	protected final int nCardNumber = 4;
	private Map<String, Integer> mapCards;
	private List<String> selectedCards;
	private int nSelectedGroup;
	private JFrame userCard = new JFrame("Your Card");
	private JPanel userCardPanel = new JPanel();

	// Player can only click a button twice
	private int buttonPressedCounter = 0;
	
	// JPanel for game. JFrame should be from Menu GUI. JButton - groups
	private  JPanel panel = new JPanel();
	//private  JFrame frame = new JFrame();
	private List<JButton> groupButtons = new ArrayList<JButton>();

	private static JYCardTrick singletonCardTrick = null;//new JYCardTrick();

	private JYCardTrick() {
		//this.frame.setSize(600, 600);
		this.setSize(800,600);
		//this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		for (int nGroup = 1; nGroup <= nGroupNumber; nGroup++)
		{
			JButton button = new JButton("Group " + nGroup);
			groupButtons.add(button);
		}
		JButton button = new JButton("Close");
		groupButtons.add(button);

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
		int index = 0; // used for groupButtons[]

		// JPanels will stack whenever this method is called.
		// Remove the old JPanel when user selects a group.
		this.setPanel();
		this.panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		List<ImageIcon> theCards = new ArrayList<ImageIcon>();

		for (Map.Entry<String, Integer> entry : mapData.entrySet()) {
			// get the cards in card/ directory
			theCards.add(new ImageIcon("card/" + entry.getKey() + ".png"));
		}
		
		int cardIndex = 0;
		gbc.insets = new Insets(10, 10, 10, 10); // spacing

		// add the cards and buttons onto the panel
		while (cardIndex != theCards.size()) {
			for (int i = 0; i < 4; i++) {
				gbc.gridy = i;
				for (int j = 0; j < 4; j++) {
					gbc.gridx = j;
					this.panel.add(new JLabel(theCards.get(cardIndex++)), gbc);
					
					if (gbc.gridx == 3) { // four cards one row, add button
						gbc.gridx = 4;
						this.panel.add(this.groupButtons.get(index++), gbc);
					}
				}
			}
		}

		gbc.gridy = 4;
		gbc.gridx = 4;
		this.panel.add(groupButtons.get(4), gbc); // add close button, 5th row, 5th column

		this.panel.setBackground(Color.gray);
		this.add(panel);
		this.setVisible(true);
	}

	/**
	* Checks which group was chosen
	*/
	public void actionPerformed(ActionEvent e) {

		for (int nIndex = 0; nIndex < nGroupNumber; nIndex++)
		{
			if ( (buttonPressedCounter != 2) && (e.getSource() == groupButtons.get(nIndex)) ) {
				nSelectedGroup = nIndex + 1;
				buttonPressedCounter++;
			}
		}

		if (e.getSource() == groupButtons.get(4)) { // if close, open TestMenu
			singletonCardTrick = null; // can instantiate only one time. Need to set to null again.
			this.dispose();
			userCard.dispose();
			new TestMenu();
		} else if (buttonPressedCounter < 2) { // shuffle deck
			selectedCards = JYUtil.getKeyFromValue(mapCards, nSelectedGroup);

			// Create new groups and split selected group cards
			mapCards = JYUtil.regroupWithSelectedGroup(nGroupNumber, nCardNumber, selectedCards);

			// Sort cards by group
			mapCards = JYUtil.sortByComparator(mapCards);

			//this.frame.remove(panel); 
			this.remove(panel);
			this.showCardsByGroup(mapCards);

			if (buttonPressedCounter == 1) {
				ImageIcon backCard = new ImageIcon("500.png");
				JOptionPane.showMessageDialog(null, "Where is your card? Select a group(1~" + nGroupNumber + "): ");
			}
		} else if (buttonPressedCounter == 2) { // show user card
			for (Map.Entry<String, Integer> entry : mapCards.entrySet()) {
				if (entry.getKey() == selectedCards.get(nSelectedGroup - 1)) {
					ImageIcon sampleCard = new ImageIcon("card/" + entry.getKey() + ".png");
					JLabel text = new JLabel("Your card is: ");

					userCardPanel.add(text);
					userCardPanel.add(new JLabel(sampleCard));
					userCard.add(userCardPanel);
					userCard.pack();
					userCard.setVisible(true);
					buttonPressedCounter++;
//					userCard.setDefaultCloseOperation(EXIT_ON_CLOSE);
					break;
				}
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

		// when closing it should I set singletonCardTrick back to null?
		// be able to repeat this stuff
	}

	//public static void main(String[] args) {

	//	JYCardTrick cardTrick = new JYCardTrick();

	//}

	
}
