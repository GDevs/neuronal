import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.awt.Dimension;

public class FrmMain extends JFrame implements ActionListener {

	private JPanel contentPane;
	private GraphicalWorld grWorld;

	/**
	 * Create the frame.
	 */
	public FrmMain() {
		setMinimumSize(new Dimension(600, 500));
		setTitle("Neuronale Simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 500, 54, 0 };
		gbl_contentPane.rowHeights = new int[] { 500, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0,
				Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.EAST;
		gbc_panel.fill = GridBagConstraints.VERTICAL;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 67, 0 };
		gbl_panel.rowHeights = new int[] { 25, 35, 25, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JButton btnSave = new JButton("save");
		btnSave.addActionListener(this);
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.insets = new Insets(0, 0, 5, 0);
		gbc_btnSave.gridx = 0;
		gbc_btnSave.gridy = 0;
		panel.add(btnSave, gbc_btnSave);

		JButton btnLoad = new JButton("load");
		btnLoad.addActionListener(this);

		JButton btnSaveAndClose = new JButton("save and close");
		GridBagConstraints gbc_btnSaveAndClose = new GridBagConstraints();
		gbc_btnSaveAndClose.insets = new Insets(0, 0, 5, 0);
		gbc_btnSaveAndClose.gridx = 0;
		gbc_btnSaveAndClose.gridy = 1;
		panel.add(btnSaveAndClose, gbc_btnSaveAndClose);
		GridBagConstraints gbc_btnLoad = new GridBagConstraints();
		gbc_btnLoad.gridx = 0;
		gbc_btnLoad.gridy = 2;
		panel.add(btnLoad, gbc_btnLoad);
		btnSaveAndClose.addActionListener(this);
	}

	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		if (ae.getActionCommand() == "load") {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Simulationen", "data");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				String filename = chooser.getSelectedFile().getPath();
				try {
					if (!filename.matches("(C:)?(.+)\\.data")) {
						System.err.println("Filename »" + filename
								+ "« Invalid");
						return;
					}

					FileInputStream fis = new FileInputStream(filename);
					ObjectInputStream ois = new ObjectInputStream(fis);
					grWorld.setWorld((World) ois.readObject());
					ois.close();
					fis.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
					return;
				} catch (ClassNotFoundException c) {
					System.out.println("Class not found");
					c.printStackTrace();
					return;
				}
			}
		} else if (ae.getActionCommand() == "save") {
			save();
		} else if (ae.getActionCommand() == "save and close") {
			if (save()) {
				System.exit(0);
			}
		}
	}

	public void addGraphicalWorld(GraphicalWorld grWorld) {
		this.grWorld = grWorld;
		JPanel panel_1 = grWorld;
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 0, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		gbc_panel_1.gridwidth = 500;
		gbc_panel_1.gridheight = 500;
		contentPane.add(panel_1, gbc_panel_1);
		panel_1.setSize(500, 500);
	}

	protected boolean save() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Simulationen", "data");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			String filename = chooser.getSelectedFile().getPath();

			if (!filename.matches("(C:)?(.+)\\.data")) {
				if (filename.matches("(C:)?(.+)")) {
					filename = filename + ".data";
				} else {
					System.err.println("Filename »" + filename + "« Invalid");
					return false;
				}
			}

			try {
				FileOutputStream fos = new FileOutputStream(filename);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(grWorld.getWorld());
				oos.close();
				fos.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

}
