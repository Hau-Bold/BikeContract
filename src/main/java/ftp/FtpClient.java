package ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import constants.Constants;

/** the class FtpClient */
public class FtpClient {

	String USER = null;
	String PASSWORD = null;
	String SERVER = null;
	int PORT = 0;
	String DIRECTORY_REMOTE = null;
	String DESTINATION;
	private FTPClient ftpClient;

	/**
	 * Constructor
	 * 
	 * @param directory
	 *            - the directory of the contract digitalizer
	 */
	public FtpClient(String directory, String user, String password, int port, String server, String directoryRemote) {
		DESTINATION = directory + File.separator + Constants.ASSETS + File.separator + Constants.ONLINESHOP;

		this.USER = user;
		this.PASSWORD = password;
		this.PORT = port;
		this.SERVER = server;
		this.DIRECTORY_REMOTE = directoryRemote;
	}

	/**
	 * to establish a ftp connection
	 */
	public void establishFTPConnection() {

		ftpClient = new FTPClient();
		try {
			ftpClient.connect(SERVER, PORT);

			boolean status = ftpClient.login(USER, PASSWORD);
			System.out.println("login-status:" + status);
			/** for passive_local_data_connection_mode */
			/** may be asci */
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalActiveMode();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void downloadFTPDirectory() {

		try {
			FTPFile[] ftpFiles = ftpClient.listFiles(DIRECTORY_REMOTE);

			if (ftpFiles != null && ftpFiles.length > 0) {

				for (FTPFile file : ftpFiles) {
					writeFile(file);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeFile(FTPFile file) throws IOException {

		String dest = DESTINATION + File.separator + Constants.ERP_CSV;
		String remote = DIRECTORY_REMOTE + "/" + file.getName();

		OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(dest));
		boolean success = ftpClient.retrieveFile(remote, outputStream1);
		outputStream1.close();

		if (success) {
			System.out.println("File #1 has been downloaded successfully.");
		}
	}
}
