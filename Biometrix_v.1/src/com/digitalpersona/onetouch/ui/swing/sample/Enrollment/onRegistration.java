package com.digitalpersona.onetouch.ui.swing.sample.Enrollment;

import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.processing.*;

import layout.SpringUtilities;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;


public class onRegistration {
	public static String onREG() {
		
		int min = 1 ;
		int max = 999_999 ;
		int rnd = ThreadLocalRandom.current().nextInt( min , max + 1 );
		System.out.println(rnd);

		final String[] labels = { "Firstname: ", "Lastname: ", "School Name:", "Phone No:" };
		int labelsLength = labels.length;
		final JTextField[] textField = new JTextField[labels.length];
		// Create and populate the panel.
		JPanel p = new JPanel(new SpringLayout());
		for (int i = 0; i < labelsLength; i++) {
			JLabel l = new JLabel(labels[i], JLabel.TRAILING);
			p.add(l);
			textField[i] = new JTextField(10);
			l.setLabelFor(textField[i]);
			p.add(textField[i]);
		}
		JButton button = new JButton("Submit");
		p.add(new JLabel());
		p.add(button);

		// Lay out the panel.
		SpringUtilities.makeCompactGrid(p, labelsLength + 1, 2, // rows, cols
				14, 14, // initX, initY
				14, 14); // xPad, yPad

	
		// Create and set up the window.
		JFrame frame = new JFrame("SpringForm");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up the content pane.
		p.setOpaque(true); // content panes must be opaque
		frame.setContentPane(p);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
		JOptionPane.showMessageDialog(null, "RIGHT THUMB ONLY! ");
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				try {

					registerIntoDB(textField[0].getText(), textField[1].getText(), textField[2].getText(),
							textField[3].getText(), rnd);
					// JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(), "Fingerprint
					// loading", JOptionPane.ERROR_MESSAGE);
					System.out.println("Registration complete!! :" + rnd);
					JOptionPane.showMessageDialog(null, "Registration successful!!, please safe the registration number for reference purpose :" + rnd);
					frame.setVisible(false);

				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		
		
		return rnd+"";
		
		
	}
	public static void registerIntoDB(String fname, String lname, String addr, String phn, int rnd)
			throws ClassNotFoundException, SQLException {
		//System.out.println(pincode + "1111111111111111");
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://www.wgh4.whoghost.com:3306/ministr7_db", "ministr7_user", "C0xFingerPrintDB1234");

		try {

			System.out.println("Connected and storing file into the database");
			PreparedStatement prst = con
					.prepareStatement("INSERT INTO captFig_ ( lname, schlname, phone, regCode ) VALUES (?,?,?,?,?)");

			prst.setString(1, fname);
			prst.setString(2, lname);
			prst.setString(3, addr);
			prst.setString(4, phn);
			prst.setInt(5, rnd);
			prst.executeUpdate();
			prst.close();

		} finally {

			con.close();
		}
	}
	
}
