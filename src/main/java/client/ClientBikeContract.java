package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import constants.Constants;
import contractDigitalizer.BikeContract;

/**
 * the class ClientBikeContract - entrypoint
 */
public class ClientBikeContract {

	public static void main(String[] args) {

		String directory = args[0];
		directory = directory.concat(File.separator);
		directory = directory.concat(Constants.BikeContract);

		BikeContract contractDigitalizer = null;
		try {
			contractDigitalizer = BikeContract.getInstance(directory);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		contractDigitalizer.showFrame();
	}
}
