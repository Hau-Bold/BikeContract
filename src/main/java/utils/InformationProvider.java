package utils;

import java.util.Locale;

import javax.swing.JOptionPane;

/** the class InformationProvider */
public class InformationProvider implements Runnable {

	private String information;
	private int capability;
	private String message;
	private Object[] valuesToFormat;
	private static int counter = 0;

	/**
	 * Constructor.
	 * 
	 * @param information
	 *            - the information
	 * @param capability
	 *            - the capability
	 * @param message
	 *            - the message
	 * @param VARARGS
	 *            - the arguments
	 */
	public InformationProvider(String information, int capability, String message, String... VARARGS) {
		this.message = message;

		this.information = information;
		this.capability = capability;
		valuesToFormat = new Object[VARARGS.length];

		for (String string : VARARGS) {
			valuesToFormat[counter] = string;
			counter++;
		}
		counter = 0;

	}

	@Override
	public void run() {
		JOptionPane.showMessageDialog(null, String.format(Locale.GERMAN, message, valuesToFormat), information,
				capability);

	}

}
