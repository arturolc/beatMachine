/**************************************
 * Authors: Arturo Lara and Brice Pratt
 *************************************/

package musicBox;

import javax.swing.ImageIcon;

/**
 * Instruments provides constants representing instruments
 * @author artur and brice
 *
 */
public enum Instruments {
	KICK("Bass Drum", 35, new ImageIcon(Instruments.class.getResource(("resources/kick.png")))),
	HIHAT("Closed Hi-Hat", 42, new ImageIcon(Instruments.class.getResource(("resources/hihat.png")))),
	SNARE("Acoustic Snare", 38, new ImageIcon(Instruments.class.getResource(("resources/snare.png")))),
	CRASH("Crash Cymbal", 49, new ImageIcon(Instruments.class.getResource(("resources/crash.png")))),
	COWBELL("Cow Bell", 56, new ImageIcon(Instruments.class.getResource(("resources/cowbell.png")))),
	MARACAS("Maracas", 70, new ImageIcon(Instruments.class.getResource(("resources/maracas.png")))),
	HANDCLAP("Hand Clap", 39, new ImageIcon(Instruments.class.getResource(("resources/clap.png"))));

	private String name;
	private int key;
	private ImageIcon icon;
	
	/**
	 * Initializes the field values for an instrument
	 * @param n
	 * @param k
	 * @param i
	 */
	private Instruments(String n, int k, ImageIcon i) {
		name = n;
		key = k;
		icon = i;
	}
	
	/**
	 * Returns the "formal" name of the instrument
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the Image of the instrument
	 * @return
	 */
	public ImageIcon getIcon() {
		return icon;
	}
	
	/**
	 * Returns the key that represents the instrument in the 
	 * MidiEvents
	 * @return
	 */
	public int getKey(){
		return key;
	}
}
