package com.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class BlackjackGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	enum Action {
		HIT, STAND, DOUBLE, SPLIT
	}

	static Random random = new Random();

	// Card deck (2-10 are their values, face cards (J, Q, K) are 10, and Ace is 11)
	Card[] singleDeck = { new Card(2), new Card(3), new Card(4), new Card(5), new Card(6), new Card(7), new Card(8), new Card(9), new Card(10),

			new Card(10), new Card(10), new Card(10),

			new Card(11) };
	List<Card> deck = new ArrayList<>();
	final int DECK_COUNT = 2; // Number of decks used in the game

	private JTextArea textArea;
	private JButton hitButton;
	private JButton standButton;
	private JButton doubleButton;
	private JButton splitButton;
	private JButton newGameButton;

	private ArrayList<Hand> playerHands = new ArrayList<>(4);
	private Hand dealerHand;
	private boolean handFinished = false;

	Hand currentHand = null;

	public BlackjackGUI() {
		setTitle("Blackjack Game");
		setSize(600, 400);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		textArea = new JTextArea();
		textArea.setEditable(false);
		add(new JScrollPane(textArea), BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		hitButton = new JButton("Hit");
		standButton = new JButton("Stand");
		doubleButton = new JButton("Double");
		splitButton = new JButton("Split");
		newGameButton = new JButton("New Game");

		buttonPanel.add(hitButton);
		buttonPanel.add(standButton);
		buttonPanel.add(doubleButton);
		buttonPanel.add(splitButton);
		buttonPanel.add(newGameButton);

		add(buttonPanel, BorderLayout.SOUTH);

		hitButton.addActionListener(new ButtonListener());
		standButton.addActionListener(new ButtonListener());
		doubleButton.addActionListener(new ButtonListener());
		splitButton.addActionListener(new ButtonListener());
		newGameButton.addActionListener(e -> startNewGame());

		initializeDeck();
		startNewGame();
	}

	private void initializeDeck() {
		deck.clear();
		for (int i = 0; i < DECK_COUNT; i++) {
			for (Card card : singleDeck) {
				deck.add(card);
			}
		}
		shuffleDeck();
	}

	private void shuffleDeck() {
		Collections.shuffle(deck, random);
		textArea.append("Deck shuffled!\n");
		System.out.println("Deck shuffled!");
	}

	private void dealerPlay() {
		while (dealerHand.getValue() < 17 || (dealerHand.getValue() == 17 && dealerHand.isSoft)) {
			dealerHand.addCard(dealCard());
		}
	}

	private void startNewGame() {
		playerHands.clear();
		playerHands.add(new Hand(dealCard(), dealCard()));
		currentHand = playerHands.get(0);
		dealerHand = new Hand(dealCard(), dealCard());
		handFinished = false;
		updateTextArea();
		newGameButton.setEnabled(false);
		hitButton.setEnabled(true);
		standButton.setEnabled(true);
		doubleButton.setEnabled(true);
		splitButton.setEnabled(playerHands.get(0).isPair());
	}

	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (handFinished) {
				return;
			}

			JButton source = (JButton) e.getSource();
			if (source == hitButton) {
				hit();
			} else if (source == standButton) {
				stand();
			} else if (source == doubleButton) {
				doubleDown();
			} else if (source == splitButton) {
				textArea.append("Can't split!");
				System.out.println("Can't split!");
				// split();
			}
		}

		@SuppressWarnings("unused")
		private void split() {
			if (currentHand.isPair()) {
				// Perform the split
				playerHands.add(new Hand(currentHand.cards.get(1), dealCard()));
				currentHand.cards.remove(1);
				currentHand.addCard(dealCard());

				handFinished = false;

				textArea.append("\nHands split. Play the first hand.\n");

				hitButton.setEnabled(true);
				standButton.setEnabled(true);
				doubleButton.setEnabled(true);
				splitButton.setEnabled(false);
			} else {
				textArea.append("\nCannot split with the current hand.\n");
			}
		}

		private void doubleDown() {
			currentHand.addCard(dealCard());
			handFinished = true;
			dealerPlay();
			updateTextArea();
			determineResult();
		}

		private void stand() {
			handFinished = true;
			dealerPlay();
			updateTextArea();
			determineResult();
		}

		private void hit() {
			currentHand.addCard(dealCard());
			updateTextArea();
			if (currentHand.isBust()) {
				textArea.append("\nHand busted!\n");
				handFinished = true;
				dealerPlay();
				updateTextArea();
				determineResult();
			}
		}
	}

	private void updateTextArea() {
		textArea.setText("");
		textArea.append("Your hand: " + currentHand + "\n");
		textArea.append("Dealer's upcard: " + dealerHand.cards.get(0).getValue() + "\n");

		if (handFinished) {
			textArea.append("Dealer's hand: " + dealerHand + "\n");
		}
	}

	private void determineResult() {
		int dealerValue = dealerHand.getValue();
		int playerValue1 = currentHand.getValue();

		// Check result for first hand
		if (currentHand.isBust()) {
			textArea.append("Hand lost!\n");
		} else if (dealerValue > 21) {
			textArea.append("Dealer busts! Hand wins!\n");
		} else if (playerValue1 > dealerValue) {
			textArea.append("Hand wins!\n");
		} else if (playerValue1 == dealerValue) {
			textArea.append("Hand ties with dealer.\n");
		} else {
			textArea.append("Hand lost to dealer.\n");
		}

		if ((playerHands.size() - 1) > playerHands.indexOf(currentHand)) {
			currentHand = playerHands.get((playerHands.indexOf(currentHand) - 1) + 1);
			return;
		}

		// Disable action buttons and enable the New Game button
		hitButton.setEnabled(false);
		standButton.setEnabled(false);
		doubleButton.setEnabled(false);
		splitButton.setEnabled(false);
		newGameButton.setEnabled(true);
	}

	private Card dealCard() {
		if (deck.size() < (DECK_COUNT * singleDeck.length / 2)) {
			initializeDeck();
		}
		return deck.remove(deck.size() - 1);
	}

	public static void main(String[] args) {
		new BlackjackGUI().setVisible(true);
	}
}