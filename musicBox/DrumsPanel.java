/**************************************
 * Authors: Arturo Lara and Brice Pratt
 *************************************/

package musicBox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Dimension;

/**
 * Represents a drums panel in where you can create patterns. If desired,
 * you can import and export the pattern.
 * @author artur & brice
 *
 */
public class DrumsPanel extends JPanel {
	private static final long serialVersionUID = 4375188501339344625L;
	private final static int BEATS_NUM = 16;
	private final static int INSTRUMENTS_NUM = Instruments.values().length;
	
	private Synthesizer synth;
	private Sequencer player;
	private Sequence sequence;
	private Track track;
	private List<JButton> buttons = new ArrayList<>();
	private List<Boolean> drumsPattern = new ArrayList<>();
	private JTextField lblSetTempo;
	private int tempo = 80;
	private Color selectedColor = new Color(66, 237, 222);
	
	/**
	 * Creating the panel.
	 */
	public DrumsPanel() {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));

		try {
			synth = MidiSystem.getSynthesizer();
			synth.open();
			
			player = MidiSystem.getSequencer();
			player.open();
			player.setTempoInBPM(tempo);

			sequence = new Sequence(Sequence.PPQ, 4);

			track = sequence.createTrack();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		
		JPanel beatPanel = createBeatPanel();
		add(beatPanel, BorderLayout.CENTER);

		JPanel controlPanel = createControlPanel();
		add(controlPanel, BorderLayout.SOUTH);

		JPanel instrumentPanel = createInstrumentPanel();
		add(instrumentPanel, BorderLayout.WEST);

	}
	
	/**
	 * Creates JButtons in a grid layout depending on the number of
	 * instruments and beats per instrument. Buttons are set to a default color,
	 * blue or dark gray, to make it easier to use.
	 * 
	 * @return
	 */
	private JPanel createBeatPanel() {
		JPanel instrumentPanel = new JPanel();
		instrumentPanel.setBackground(new Color(128, 128, 128));
		instrumentPanel.setBorder(new EmptyBorder(15, 5, 5, 5));
		instrumentPanel.setLayout(new GridLayout(INSTRUMENTS_NUM, 16, 10, 10));

		for (int i = 0; i < INSTRUMENTS_NUM * BEATS_NUM; i++) {
			buttons.add(new JButton());
			buttons.get(i).setBorder(new LineBorder(Color.BLACK, 3, true));
			buttons.get(i).setBackground(Color.WHITE);
			changeColorBtnEvent(buttons.get(i), i);
			instrumentPanel.add(buttons.get(i));
		}
		
		setDefaultBackgrounds();
		
		return instrumentPanel;
	}

	/**
	 * Event handler for the beat buttons.
	 * @param button
	 * @param index
	 */
	private void changeColorBtnEvent(JButton button, int index) {
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isSelected(button)) {
					for(int i = 0; i < 4; i++) {
						for (int j = i; j < INSTRUMENTS_NUM * BEATS_NUM; j += 8) {
							if (index == j)
								buttons.get(j).setBackground(new Color(808080));
						}
						
						for (int j = i + 4; j < INSTRUMENTS_NUM * BEATS_NUM; j += 8) {
							if (index == j) 
								buttons.get(j).setBackground(Color.DARK_GRAY);
						}
					}
				}
				else
					button.setBackground(selectedColor);
			}
		});
	}
	
	/**
	 * Returns a boolean based if the beat button is selected or not
	 * @param btn
	 * @return
	 */
	private boolean isSelected(JButton btn) {
		if (btn.getBackground().equals(selectedColor))
			return true;
		else
			return false;
	}
	
	/**
	 * Creates a new Drums Pattern based on what the beat buttons the user
	 * selected
	 */
	private void createDrumsPattern() {
		// Clearing any previous track in the sequence,
		// Creating a brand new track, and
		// Clearing any previous drum pattern
		sequence.deleteTrack(track);
		track = sequence.createTrack();
		drumsPattern.clear();

		// Populating the boolean array drumsPattern
		for (int i = 0; i < INSTRUMENTS_NUM * BEATS_NUM; i++)
			drumsPattern.add(isSelected(buttons.get(i)));

		// Adding MidiEvents to the track based on the drumsPattern
		buildTrack();
	}
	
	/**
	 * Builds the track to be played by the sequencer
	 */
	private void buildTrack() {
		for (int i = 0; i < INSTRUMENTS_NUM; i++) {
			for (int j = 0; j < BEATS_NUM; j++) {
				if (drumsPattern.get(j + i * BEATS_NUM)) {
					track.add(createMidiEvent(144, 9, Instruments.values()[i].getKey(), 100, j));
					track.add(createMidiEvent(128, 9, Instruments.values()[i].getKey(), 100, j + 1));
				}
			}
		}
		track.add(createMidiEvent(192, 9, 1, 0, BEATS_NUM));
	}
	
	/**
	 * Creates a grid layout panel for the instruments
	 * @return
	 */
	private JPanel createInstrumentPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(new Color(128, 128, 128));
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(new GridLayout(INSTRUMENTS_NUM, 1, 5, 5));

		for (int i = 0; i < INSTRUMENTS_NUM; i++) {
			JPanel instrumentPanel = new JPanel();
			instrumentPanel.setLayout(new BorderLayout());
			
			JLabel lblInstrumentName = new JLabel(Instruments.values()[i].getName());
			lblInstrumentName.setForeground(Color.WHITE);
			lblInstrumentName.setBackground(Color.DARK_GRAY);
			lblInstrumentName.setHorizontalAlignment(SwingConstants.CENTER);
			lblInstrumentName.setFont(new Font("Tw Cen MT Condensed", Font.PLAIN, 18));
			lblInstrumentName.setOpaque(true);
			instrumentPanel.add(lblInstrumentName, BorderLayout.NORTH);

			JButton instrumentIcon = new JButton();
			instrumentIcon.setBackground(Color.WHITE);
			instrumentIcon.setIcon(Instruments.values()[i].getIcon());
			instrumentBtnEvent(instrumentIcon, i);
			instrumentPanel.add(instrumentIcon, BorderLayout.CENTER);
			panel.add(instrumentPanel);
		}
		return panel;
	}
	
	/**
	 * Event handler for the instrument buttons. If the user cicks on the 
	 * button it will play the sound of the instrument
	 * @param btn
	 * @param index
	 */
	private void instrumentBtnEvent(JButton btn, int index) {
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synth.getChannels()[9].noteOn(Instruments.values()[index].getKey(), 100);	
			}
		});
	}
	
	/**
	 * Creates the control panel. 
	 * @return
	 */
	private JPanel createControlPanel() {
		JPanel controlPanel = new JPanel();
		controlPanel.setBackground(Color.LIGHT_GRAY);

		JButton btnPlay = new JButton(Character.toString('\u25B6'));
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					createDrumsPattern();
					player.open();
					player.setSequence(sequence);
					player.start();
					player.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
				} catch (InvalidMidiDataException | MidiUnavailableException e) {
					e.printStackTrace();
				}
			}
		});
		controlPanel.add(btnPlay);
		
		JButton btnStop = new JButton(Character.toString('\u23F9'));
		btnStop.setFont(new Font("Symbol", Font.PLAIN, 13));
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.stop();
				player.setMicrosecondPosition(0);
			}
		});
		controlPanel.add(btnStop);

		JButton btnClear = new JButton("CLEAR");
		btnClear.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 21));
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setDefaultBackgrounds();
			}
		});
		controlPanel.add(btnClear);

		lblSetTempo = new JTextField(String.valueOf(tempo));
		lblSetTempo.setFont(new Font("Tahoma", Font.PLAIN, 21));
		lblSetTempo.setMinimumSize(new Dimension(20, 20));
		lblSetTempo.setColumns(5);
		controlPanel.add(lblSetTempo);

		JButton btnSetTempo = new JButton("Set Tempo");
		btnSetTempo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 21));
		btnSetTempo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String tempoTemp = lblSetTempo.getText();
					tempo = Integer.parseInt(tempoTemp);
					player.setTempoInBPM(tempo);
				}
				catch(NumberFormatException e) {
					// do nothing
				}
			}
		});
		controlPanel.add(btnSetTempo);

		JButton btnExportBeat = new JButton("Export Beat");
		btnExportBeat.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 21));
		btnExportBeat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					writeToFile(drumsPattern);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		controlPanel.add(btnExportBeat);

		JButton btnImportBeat = new JButton("Import Beat");
		btnImportBeat.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 21));
		btnImportBeat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					readFromFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		controlPanel.add(btnImportBeat);

		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("segoe UI Semibold", Font.PLAIN, 21));
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		controlPanel.add(btnExit);
		return controlPanel;
	}
	
	/**
	 * Creates a new MidiEvent to be added to the track.
	 * @param command
	 * @param channel
	 * @param one
	 * @param two
	 * @param beat
	 * @return
	 */
	private MidiEvent createMidiEvent(int command, int channel, int one, int two, int beat) {
		MidiEvent event = null;
		try {
			ShortMessage message1 = new ShortMessage();
			message1.setMessage(command, channel, one, two);
			event = new MidiEvent(message1, beat);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return event;
	}
	
	/**
	 * Writes the drums pattern  to a file,it gives the user the option of where
	 * to save it
	 * @param a
	 * @throws IOException
	 */
	private void writeToFile(List<Boolean> a) throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(getComponent(0)) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			BufferedWriter writeOut = new BufferedWriter(new FileWriter(file));

			// If user hasn't played the pattern, then we need to create the
			// drumsPattern before writing to the file
			if (drumsPattern.isEmpty())
				createDrumsPattern();

			try {
				for (Boolean el : drumsPattern) {
					String temp = Boolean.toString(el);
					writeOut.write(temp);
					writeOut.newLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				writeOut.flush();
				writeOut.close();
			}
		}

	}
	
	/**
	 * Reads in a drums pattern. It gives the user the option of which pattern
	 * to load. It displays the pattern on the beat panel
	 * @throws IOException
	 */
	private void readFromFile() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(getComponent(0)) == JFileChooser.APPROVE_OPTION) {
			drumsPattern.clear();
			setDefaultBackgrounds();
			File file = fileChooser.getSelectedFile();
			BufferedReader readFromFile = new BufferedReader(new FileReader(file));
			try {
				for (int i = 0; i < INSTRUMENTS_NUM * BEATS_NUM; i++) {
					drumsPattern.add(Boolean.parseBoolean(readFromFile.readLine()));
					if (drumsPattern.get(i))
						buttons.get(i).setBackground(selectedColor);
				}
				buildTrack();
			} catch (IOException e) {

			} finally {
				readFromFile.close();
			}

		}
	}
	
	/**
	 * Sets the default backgrounds, either blue or dark gray
	 */
	private void setDefaultBackgrounds() {
		drumsPattern.clear();
		for(int i = 0; i < 4; i++) {
			for (int j = i; j < INSTRUMENTS_NUM * BEATS_NUM; j += 8)
				buttons.get(j).setBackground(new Color(808080));
			
			for (int j = i + 4; j < INSTRUMENTS_NUM * BEATS_NUM; j += 8)
				buttons.get(j).setBackground(Color.DARK_GRAY);
		}
	}
}
