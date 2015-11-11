import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileController extends JFrame {

	private JTextField filename = new JTextField(), dir = new JTextField();
	private JButton open = new JButton("Open"), save = new JButton("Save");

	public FileController() {
		JPanel p = new JPanel();
		open.addActionListener(new OpenL());
		p.add(open);
		// save.addActionListener(new SaveL());
		// p.add(save);
		Container cp = getContentPane();
		cp.add(p, BorderLayout.SOUTH);
		dir.setEditable(false);
		filename.setEditable(false);
		p = new JPanel();
		p.setLayout(new GridLayout(2, 1));
		p.add(filename);
		p.add(dir);
		cp.add(p, BorderLayout.NORTH);
	}

	class OpenL implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"TXT files", "txt", "srt");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(FileController.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("You chose to open this file: "
						+ chooser.getSelectedFile().getName());
				String path = getPath(chooser);
					try {
						Main.runReformat(path);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		}

		public String getPath(JFileChooser chooser) {
			return chooser.getSelectedFile().getAbsolutePath();
		}
	}

	public static void main() {
		run(new FileController(), 250, 110);
	}

	public static void run(JFrame frame, int width, int height) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setVisible(true);
	}
}
