package com.main;

import java.util.ArrayList;
import java.util.List;

public class Hand {

	List<Card> cards = new ArrayList<>();
	boolean isSoft = false; // True if the hand contains an ace counted as 11

	int value = -1;

	public Hand(Card... initialCards) {
		for (Card card : initialCards) {
			cards.add(card);
		}
		updateValue(); // Calculate initial value and check for soft hand
	}

	private void updateValue() {
		int aceCount = 0;
		List<Card> aceCards = new ArrayList<>();
		int sum = 0;
		for (Card card : cards) {
			sum += card.getValue();
			if (card.getValue() == 11) {
				aceCount++;
				aceCards.add(card);
			}
		}

		value = sum;

		// Adjust for Aces counted as 1 if the total exceeds 21
		while (value > 21 && !aceCards.isEmpty()) {
			aceCards.get(aceCount - 1).setValue(1);
			value -= 10;
			aceCards.remove(aceCards.get(aceCount - 1));
		}

		isSoft = aceCount > 0;
	}

	public int getValue() {
		return value;
	}

	public boolean isBust() {
		return value > 21;
	}

	public boolean isPair() {
		return cards.size() == 2 && cards.get(0).getValue() == (cards.get(1).getValue());
	}

	public void addCard(Card card) {
		cards.add(card);
		updateValue();
	}

	@Override
	public String toString() {
		return "Hand{" + "cards=" + cards + ", value=" + getValue() + "}";
	}
}