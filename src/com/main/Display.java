package com.main;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public class Display {

	private final JFrame frame;

	public Display(int width, int height, String title, BlackjackGUI blackJack) {
		frame = new JFrame();
		frame.setTitle(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(new JScrollPane(blackJack.getTextArea()), BorderLayout.CENTER);
		frame.add(blackJack.getButtonPanel(), BorderLayout.SOUTH);
		frame.setResizable(false);
		frame.add(blackJack);
		frame.setVisible(true);
	}
}