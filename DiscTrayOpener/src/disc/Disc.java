package disc;

import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class Disc extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JButton open, close;
	protected JComboBox<String> drop;
	private String disc;

	public Disc() {
		//Standard chosen disc
		disc = "F:";
		
		
		//Button to open drive
		open = new JButton("open the drive");
		open.setVerticalTextPosition(AbstractButton.CENTER);
		open.setHorizontalTextPosition(AbstractButton.LEADING);
		open.setActionCommand("Open");
		open.setToolTipText("Click here to open the disc");
		open.addActionListener(this);

		//Button to close drive
		close = new JButton("close the drive");
		close.setVerticalTextPosition(AbstractButton.CENTER);
		close.setHorizontalTextPosition(AbstractButton.LEADING);
		close.setActionCommand("Close");
		close.setToolTipText("Click here to close the disc");
		close.addActionListener(this);

		//List to choose drive from - only disk drives
		drop = new JComboBox<String>();
		//Stuff to look at drive on the computer
		File[] paths;
		FileSystemView fileSysView = FileSystemView.getFileSystemView();
		paths = File.listRoots();
		for (File path : paths) {
			//Cd-drev is only for the computer I wrote this one, as it was windows with danish setup
			if (fileSysView.getSystemTypeDescription(path).equals("Cd-drev")) {
				drop.addItem(path.toString().replace("\\", ""));
			}else if (fileSysView.getSystemTypeDescription(path).equals("Cd-drive")) {
				drop.addItem(path.toString().replace("\\", ""));
			}
		}
		drop.setActionCommand("Change");
		drop.addActionListener(this);


		//Add the buttons and list to the view
		add(drop);
		add(open);
		add(close);

	}

	public static void main(String[] args) {
		createAndShowGui();

	}

	//Gui setup 
	private static void createAndShowGui() {
		//JFrame as the frame for the gui
		JFrame frame = new JFrame("Disc controle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Make an instance of self and put it in the gui
		Disc disc = new Disc();
		disc.setOpaque(true);
		frame.setContentPane(disc);

		//Make the gui and show it
		frame.setAlwaysOnTop(true);
		frame.pack();
		frame.setVisible(true);
	}

	
	//Open a disk drive
	public void openClose(String command) {
		//Lots of setup with you run of the mill try-catch blocks
		File file = null;
		try {
			file = File.createTempFile("tmp", ".vbs");
		} catch (IOException e) {
			e.printStackTrace();
		}

		//If the file still is null exit
		if (file == null) return;

		file.deleteOnExit();

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//If the filewriter still is null exit
		if (fileWriter == null) return;

		//Commands found stackoverflow
		String cmd = ""; 
		
		if("Open".equals(command)) {
			cmd = "Set wmp = CreateObject(\"WMPlayer.OCX\") \n"
					+ "Set cd = wmp.cdromCollection.getByDriveSpecifier(\""
					+ disc + "\") \n"
					+ "cd.Eject";
		} else if ("Close".equals(command)) {
			cmd = "Set wmp = CreateObject(\"WMPlayer.OCX\") \n"
					+ "Set cd = wmp.cdromCollection.getByDriveSpecifier(\""
					+ disc + "\") \n"
					+ "cd.Eject \n "
					+ "cd.Eject ";
		}
		

		//Execution of the command
		try {
			fileWriter.write(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Runtime.getRuntime().exec("wscript " + file.getPath()).waitFor();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	//Check which action the user performed, and choose the right
	//action for the program to take
	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {

		if("Open".equals(e.getActionCommand())) {
			openClose("Open");
		} else if ("Close".equals(e.getActionCommand())) {
			openClose("Close");
		} else if ("Change".equals(e.getActionCommand())) {
			JComboBox<String> tmp = (JComboBox<String>) e.getSource();
			String selected = (String) tmp.getSelectedItem();
			disc = selected;
		}

	}
}
