package com.digitalpersona.onetouch.ui.swing.sample.Enrollment;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.digitalpersona.onetouch.*;

import layout.SpringUtilities;

public class MainForm extends JFrame {
	static String pincode = "";
	public static String TEMPLATE_PROPERTY = "template";
	private DPFPTemplate template;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		createAndShowGUI();

	}

	private static void createAndShowGUI() {
		final String[] labels = { "Please enter your Registration Code: " };
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
		JButton button = new JButton("Verify");
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
		
		
		
		
		
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				/*
				 * for (int i = 0 ; i < labels.length ; i++) {
				 * System.out.println(labels[i]+"->"+textField[i].getText());
				 * 
				 * //pass this to the form
				 * 
				 * 
				 * SwingUtilities.invokeLater(new Runnable() { public void run() { new
				 * MainForm(); } });
				 * 
				 * }
				 */
				// setting regcode to this instance
				pincode = textField[0].getText();

				System.out.println(labels[0] + "->" + textField[0].getText());

				SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						frame.dispose();
						new MainForm();
					}
				});
				

				

			}
		});
		
		
		
		
	}


	public class TemplateFileFilter extends javax.swing.filechooser.FileFilter {
		@Override
		public boolean accept(File f) {
			return f.getName().endsWith(".fpt");
		}

		@Override
		public String getDescription() {
			return "Fingerprint Template File (*.fpt)";
		}
	}

	MainForm() {

		setState(Frame.NORMAL);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("Fingerprint Enrollment and Verification Sample");
		setResizable(false);

		final JButton registration = new JButton("Register new student");
		registration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onRegistration();
			}
		});
		
		final JButton enroll = new JButton("Fingerprint Enrollment");
		enroll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onEnroll();
			}
		});

		final JButton verify = new JButton("Fingerprint Verification");
		verify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onVerify();
			}
		});

		final JButton save = new JButton("Save Student Enrollment");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSave();
			}
		});

		final JButton load = new JButton("Verify enrollment");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onLoad();
			}
		});

		final JButton quit = new JButton("Close");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		this.addPropertyChangeListener(TEMPLATE_PROPERTY, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				verify.setEnabled(template != null);
				save.setEnabled(template != null);
				if (evt.getNewValue() == evt.getOldValue())
					return;
				if (template != null)
					JOptionPane.showMessageDialog(MainForm.this,
							"The fingerprint template is ready for fingerprint verification.", "Fingerprint Enrollment",
							JOptionPane.INFORMATION_MESSAGE);
			}
		});

		JPanel center = new JPanel();
		center.setLayout(new GridLayout(4, 1, 0, 5));
		center.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));
		center.add(enroll);
		center.add(verify);
		center.add(save);
		center.add(load);

		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		bottom.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
		bottom.add(quit);

		setLayout(new BorderLayout());
		add(center, BorderLayout.CENTER);
		add(bottom, BorderLayout.PAGE_END);

		pack();
		setSize((int) (getSize().width * 1.6), getSize().height);
		setLocationRelativeTo(null);
		setTemplate(null);
		setVisible(true);
	}

	private void onEnroll() {
		EnrollmentForm form = new EnrollmentForm(this);
		form.setVisible(true);
	}
	
	private void onRegistration() {
		EnrollmentForm form = new EnrollmentForm(this);
		form.setVisible(true);
	}

	private void onVerify() {
		VerificationForm form = new VerificationForm(this);
		form.setVisible(true);
	}

	private void onSave() {
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new TemplateFileFilter());
		while (true) {
			// if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				/*
				 * File file = chooser.getSelectedFile(); if
				 * (!file.toString().toLowerCase().endsWith(".fpt")) file = new
				 * File(file.toString() + ".fpt"); if (file.exists()) { int choice =
				 * JOptionPane.showConfirmDialog(this,
				 * String.format("File \"%1$s\" already exists.\nDo you want to replace it?",
				 * file.toString()), "Fingerprint saving", JOptionPane.YES_NO_CANCEL_OPTION); if
				 * (choice == JOptionPane.NO_OPTION) continue; else if (choice ==
				 * JOptionPane.CANCEL_OPTION) break; } FileOutputStream stream = new
				 * FileOutputStream(file); getTemplate().serialize()
				 * 
				 * InputStream in;
				 */

				saveIntoDB(getTemplate().serialize(), pincode);
				break;

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(), "Fingerprint saving",
						JOptionPane.ERROR_MESSAGE);
				break;
			}
		}
		// break;
	}
	// }

	private void onLoad() {

		DPFPTemplate t = DPFPGlobal.getTemplateFactory().createTemplate();
		try {
			System.out.println(pincode + "000000000000");
			byte[] bhd = openOutofDB(pincode);
			if (bhd != null) {
				t.deserialize(bhd);
				setTemplate(t);
			} else {

				onRegistration.onREG();

			}

		} catch (IllegalArgumentException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * JFileChooser chooser = new JFileChooser(); chooser.addChoosableFileFilter(new
		 * TemplateFileFilter()); if(chooser.showOpenDialog(this) ==
		 * JFileChooser.APPROVE_OPTION) { try { FileInputStream stream = new
		 * FileInputStream(chooser.getSelectedFile()); byte[] data = new
		 * byte[stream.available()]; stream.read(data); stream.close(); DPFPTemplate t =
		 * DPFPGlobal.getTemplateFactory().createTemplate(); t.deserialize(data);
		 * setTemplate(t); } catch (Exception ex) { JOptionPane.showMessageDialog(this,
		 * ex.getLocalizedMessage(), "Fingerprint loading", JOptionPane.ERROR_MESSAGE);
		 * } }
		 */
	}

	public DPFPTemplate getTemplate() {
		return template;
	}

	public void setTemplate(DPFPTemplate template) {
		DPFPTemplate old = this.template;
		this.template = template;
		firePropertyChange(TEMPLATE_PROPERTY, old, template);
	}

	public void saveIntoDB(byte[] dbt, String pincode) throws ClassNotFoundException, SQLException {
		System.out.println(pincode + "1111111111111111");
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://www.wgh4.whoghost.com:3306/db", "user", "password");

		try {

			System.out.println("Connected and storing file into the database");
			PreparedStatement prst = con.prepareStatement("INSERT INTO captFig_ (finger, regCode ) VALUES (?,?)");

			prst.setBytes(1, dbt);
			prst.setString(2, pincode);
			prst.executeUpdate();
			prst.close();

		} finally {
			con.close();
		}
	}

	

	public byte[] openOutofDB(String pincode) throws ClassNotFoundException, SQLException {
		System.out.println(pincode + "2222222222222");
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://www.wgh4.whoghost.com:3306/ministr7_db", "ministr7_user", "C0xFingerPrintDB1234");
		byte[] byq = null;
		try {

			System.out.println("Connected and storing file into the database");
			PreparedStatement prst = con.prepareStatement("select finger from captFig_ where regCode  = ? limit 1");

			prst.setString(1, pincode);
			ResultSet rst = prst.executeQuery();

			if (rst.next()) {

				byq = rst.getBytes(1);

			} else {

			}

		} finally {
			con.close();
		}
		return byq;
	}

}
