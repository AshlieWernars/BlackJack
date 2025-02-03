package com.main;

import java.awt.Color;

public class Card {

	int value;
	Color color;

	public Card(int value, Color color) {
		this.value = value;
		this.color = color;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		return "" + getValue();
	}
}