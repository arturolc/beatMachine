/**************************************
 * Authors: Arturo Lara and Brice Pratt
 *************************************/

package musicBox;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JTextPane;
import java.awt.Dimension;
import java.awt.Canvas;

/**
 * MusicGui is a graphical user interface that represents the
 * drums beat machine
 * @author artur
 *
 */
public class MusicGui extends JFrame {

	private static final long serialVersionUID = 4128313993714981438L;
	private JPanel contentPane;
	private DrumsPanel drums;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MusicGui frame = new MusicGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MusicGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 850);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setVisible(true);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.textInactiveText);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblLogo = createLblLogo();
		panel.add(lblLogo, BorderLayout.CENTER);
		
		JTextPane textPane = createHowToPane();
		textPane.setVisible(false);
		panel.add(textPane, BorderLayout.SOUTH);
		
		Canvas canvas = createCanvas();
		panel.add(canvas, BorderLayout.NORTH);
		
		JPanel controlPanel = createControlPanel();
		contentPane.add(controlPanel, BorderLayout.SOUTH);
		
		JButton btnHowTo = createBtnHowTo(textPane);
		controlPanel.add(btnHowTo);
		
		JButton playBeatMachBtn = createBtnPlayBeatMachine();
		controlPanel.add(playBeatMachBtn);
		contentPane.add(panel, BorderLayout.CENTER);
		
		drums = new DrumsPanel();

	}
	
	/**
	 * Creates the control panel
	 * @return
	 */
	private JPanel createControlPanel() {
		JPanel controlPanel = new JPanel();
		controlPanel.setBackground(new Color(808080));
		controlPanel.setBorder(new EmptyBorder(10,10,10,10));
		{
			JButton btnExit = new JButton("Exit");
			btnExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}
			});
			btnExit.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 30));
			controlPanel.add(btnExit);
		}
		return controlPanel;
	}
	
	/**
	 * Sets the logo of the project
	 * @return
	 */
	private JLabel createLblLogo() {
		JLabel lblLogo = new JLabel("");
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogo.setIcon(new ImageIcon(MusicGui.class.getResource("/musicBox/resources/beatmachine.png")));
		return lblLogo;
	}
	
	/**
	 * Creates a play beat machine button
	 * @return
	 */
	private JButton createBtnPlayBeatMachine() {
		JButton playBeatMachBtn = new JButton("Play Beat Machine");
		playBeatMachBtn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 21));
		playBeatMachBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					setContentPane(drums);
					invalidate();
					validate();
			}
		});
		playBeatMachBtn.setBorder(new EmptyBorder(10, 10, 10, 10));
		return playBeatMachBtn;
	}
	
	/**
	 * Creates a how to button that contains the instructions
	 * @param textPane
	 * @return
	 */
	private JButton createBtnHowTo(JTextPane textPane) {
		JButton btnHowTo = new JButton("How To");
		btnHowTo.setBorder(new EmptyBorder(10, 10, 10, 10));
		btnHowTo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 21));
		btnHowTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			if(textPane.isVisible() == false)
			{
				textPane.setVisible(true);
			}
			else textPane.setVisible(false);
			}
		});
		return btnHowTo;
	}

	/**
	 * Creates a canvas that sets the look and feel of the gui
	 * @return
	 */
	private Canvas createCanvas() {
		Canvas canvas = new Canvas();
		canvas.setSize(new Dimension(65, 65));
		canvas.setBackground(new Color(808080));
		return canvas;
	}
	
	/**
	 * Creates a textPane that contains the instructions of the beat machine
	 * @return
	 */
	private JTextPane createHowToPane() {
		JTextPane textPane = new JTextPane();
		textPane.setForeground(SystemColor.text);
		textPane.setBorder(new EmptyBorder(50, 50, 50, 50));
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 21));
		textPane.setBackground(SystemColor.textInactiveText);
		textPane.setText("If you would like to change the tempo, first make sure the sequencer is stopped,"
				+ " and then simply enter the desired tempo and click set tempo."
				+ "To import a pattern click on Import Beat, and then select the file you would like to use, and click save."
				+ "To export a pattern click on Export Beat, select the directory you wish to save the pattern to, then type"
				+ " in the name and hit save."
				+ "To make any changes to the current pattern first stop the sequencer, make the desired changes, then hit play.");
		return textPane;
	}

}
