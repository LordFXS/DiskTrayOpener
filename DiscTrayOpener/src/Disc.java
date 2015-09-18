import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;

import javax.management.loading.PrivateClassLoader;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
		disc = "F:";
		
		open = new JButton("open the drive");
		open.setVerticalTextPosition(AbstractButton.CENTER);
		open.setHorizontalTextPosition(AbstractButton.LEADING);
		open.setActionCommand("Open");
		open.setToolTipText("Click here to open the disc");
		open.addActionListener(this);

		close = new JButton("close the drive");
		close.setVerticalTextPosition(AbstractButton.CENTER);
		close.setHorizontalTextPosition(AbstractButton.LEADING);
		close.setActionCommand("Close");
		close.setToolTipText("Click here to close the disc");
		close.addActionListener(this);

		drop = new JComboBox<String>();


		File[] paths;
		FileSystemView fileSysView = FileSystemView.getFileSystemView();

		paths = File.listRoots();

		for (File path : paths) {
			if (fileSysView.getSystemTypeDescription(path).equals("Cd-drev")) {
				drop.addItem(path.toString().replace("\\", ""));
			}

		}

		drop.setActionCommand("Change");
		drop.addActionListener(this);


		add(drop);
		add(open);
		add(close);

	}

	public static void main(String[] args) {
		createAndShowGui();

	}

	private static void createAndShowGui() {
		JFrame frame = new JFrame("Disc controle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Disc disc = new Disc();
		disc.setOpaque(true);

		frame.setContentPane(disc);

		frame.setAlwaysOnTop(true);
		frame.pack();
		frame.setVisible(true);
	}

	public void open(String drive) {
		File file = null;

		try {
			file = File.createTempFile("tmp", ".vbs");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (file == null) return;

		file.deleteOnExit();

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fileWriter == null) return;

		String cmd = "Set wmp = CreateObject(\"WMPlayer.OCX\") \n"
				+ "Set cd = wmp.cdromCollection.getByDriveSpecifier(\""
				+ drive + "\") \n"
				+ "cd.Eject";

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


	public void close(String drive) {
		File file = null;
		try {
			file = File.createTempFile("tmp", ".vbs");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (file == null) return;

		file.deleteOnExit();

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fileWriter == null) return;

		String cmd = "Set wmp = CreateObject(\"WMPlayer.OCX\") \n"
				+ "Set cd = wmp.cdromCollection.getByDriveSpecifier(\""
				+ drive + "\") \n"
				+ "cd.Eject \n "
				+ "cd.Eject ";

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


	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {

		if("Open".equals(e.getActionCommand())) {
			open(disc);
		} else if ("Close".equals(e.getActionCommand())) {
			close(disc);
		} else if ("Change".equals(e.getActionCommand())) {
			JComboBox<String> tmp = (JComboBox<String>) e.getSource();
			String selected = (String) tmp.getSelectedItem();
			disc = selected;
		}

	}
}
