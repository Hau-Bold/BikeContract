package shore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.apache.commons.io.IOUtils;

import constants.Constants;
import constants.ShoreConstants;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import utils.InformationProvider;

/** the class ShoreUtils */
public class ShoreUtils {

	/**
	 * requests the shore appointments from Pi and moves it to pathToShore
	 * 
	 * @param path
	 *            - the path
	 * @param user
	 *            - the user
	 * @param password
	 *            - the password
	 * @param pathToShore
	 *            - the path to the shore folder
	 * @throws IOException
	 *             - in case of technical error
	 */
	public static void requestAppointments(String path, String user, String password, String pathToShore)
			throws IOException {

		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, user, password);

		SmbFile smbFile = new SmbFile(path, auth);

		if (smbFile.exists()) {

			SmbFile[] files = smbFile.listFiles();

			SmbFileInputStream in = null;
			FileOutputStream out = null;
			for (SmbFile smbfile : files) {

				File file = new File(pathToShore + File.separator + smbfile.getName());

				in = new SmbFileInputStream(smbfile);
				out = new FileOutputStream(file);
				IOUtils.copy(in, out);
			}

			in.close();
			out.close();
		} else {
			new InformationProvider(Constants.WARNING, JOptionPane.WARNING_MESSAGE,
					ShoreConstants.CONNECTION_NOT_EXISTING).run();
		}

	}

	/**
	 * to clear the folder with the appointments
	 * 
	 * @param pathToShore
	 *            - the path to the folder with the appointments
	 */
	static void clearAppointments(String pathToShore) {

		File dir = new File(pathToShore);

		for (File file : dir.listFiles()) {
			file.delete();
		}

	}

	/**
	 * handles the service
	 * 
	 * @param service
	 *            - the service
	 * @return the handled service
	 */
	public static String handleService(String service) {

		if (service != null) {
			if (service.contains("(")) {

				int indexOfLeftParenthesis = service.indexOf("(");

				return service.substring(0, indexOfLeftParenthesis - 1).trim();

			}
		}
		return service;
	}

	/**
	 * extracts the encapsulated time from the received date
	 * 
	 * @param date
	 *            - the received date
	 * @return the encapsulated time
	 */
	public static shore.Time extractTime(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		return new shore.Time(hour, minute, second);

	}

	/**
	 * extracts the encapsulated time.Date from the received date
	 * 
	 * @param date
	 *            - the received date
	 * @return the encapsulated time.Date
	 */
	public static shore.Date extractDate(Date date) {
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		DayOfWeek dayOfWeek = localDate.getDayOfWeek();
		int dayOfMonth = localDate.getDayOfMonth();
		Month month = localDate.getMonth();
		int year = localDate.getYear();

		return new shore.Date(dayOfWeek, dayOfMonth, month, year);
	}

	/**
	 * extracts the value corresponding to an identifier from the receiving.
	 * 
	 * @param receiving
	 *            - StringArray
	 * @param identifier
	 *            - the identifier
	 * @return the value
	 */
	public static String extractIdentifier(String[] receiving, String identifier) {

		String result = null;

		for (String content : receiving) {
			if (content.contains(identifier)) {
				int indexOfColon = content.indexOf(Constants.COLON);
				result = content.substring(indexOfColon + 1, content.length());
			}
		}

		if (result != null) {
			return result.trim();
		}
		return result;
	}

	/**
	 * handles the customer
	 * 
	 * @param customer
	 *            - the customer
	 * @return the handled customer
	 */
	public static shore.Customer handleCustomer(String customer) {

		shore.Customer response = new shore.Customer();

		if (customer != null) {
			customer = customer.replace("  ", " ");

			if (customer.contains("(")) {

				/** remove unnecessary information */
				int indexOfLeftParenthesis = customer.indexOf("(");
				customer = customer.substring(0, indexOfLeftParenthesis).trim();
			}

			String[] customerSplitted = customer.split(" ");

			if (customerSplitted.length >= 3) {
				/** Salutation is contained ? */

				String salutation = customerSplitted[0].trim();

				if (salutation.equals(ShoreConstants.SALUTATION_MALE)
						|| salutation.equals(ShoreConstants.SALUTATION_MALE)) {
					/** salutation is a salutation! */
					response.setSalutation(salutation);

					StringBuilder builder = new StringBuilder();

					for (int position = 1; position < customerSplitted.length - 1; position++) {
						builder.append(customerSplitted[position]);
						builder.append(Constants.EMPTY_SPACE);
					}
					response.setPreName(builder.toString().trim());

				} else {

					/** salutation is not a salutation! */
					StringBuilder builder = new StringBuilder();

					for (int position = 0; position < customerSplitted.length - 1; position++) {
						builder.append(customerSplitted[position]);
						builder.append(Constants.EMPTY_SPACE);
					}
					response.setPreName(builder.toString().trim());
				}
			}

			else if (Integer.compare(customerSplitted.length, 2) == 0) {
				/** No Salutation is contained */
				String preName = customerSplitted[0];

				if (!(preName == ShoreConstants.SALUTATION_MALE) || !(preName == ShoreConstants.SALUTATION_FEMALE)) {
					response.setPreName(customerSplitted[0].trim());
				} else {
					response.setPreName(preName);
				}
			}

			response.setCustomerName(customerSplitted[customerSplitted.length - 1]);
		}
		return response;
	}

	/**
	 * generates an appointment form the received *.ics file
	 * 
	 * @param file
	 *            - the *.ics file
	 * @return - an appointment
	 * @throws IOException
	 *             - in case of technical error
	 * @throws ParseException
	 *             - in case of technical error
	 */
	public static Appointment generateAppointmentFromFile(File file) throws IOException, ParseException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.GERMAN);
		Appointment appointment = new Appointment();
		StringBuilder builder = new StringBuilder();
		Boolean isReaderInLineOfDescription = Boolean.FALSE;

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

		String line;
		while ((line = bufferedReader.readLine()) != null) {

			if (!isReaderInLineOfDescription) {
				if (line.startsWith(ShoreConstants.DTSTART)) {

					int indexOfColon = line.indexOf(Constants.COLON);

					String startDateAsString = line.substring(indexOfColon + 1, line.length());
					Date startDate = simpleDateFormat.parse(startDateAsString);

					/** setting start time */
					appointment.setStartTime(ShoreUtils.extractTime(startDate));
					/** setting day of appointment */
					appointment.setDate(ShoreUtils.extractDate(startDate));
				}

				else if (line.startsWith(ShoreConstants.DTEND)) {
					int indexOfColon = line.indexOf(Constants.COLON);

					String endDateAsString = line.substring(indexOfColon + 1, line.length());
					Date endDate = simpleDateFormat.parse(endDateAsString);

					/** setting end time */
					appointment.setEndTime(ShoreUtils.extractTime(endDate));

				} else if (line.startsWith(ShoreConstants.DESCRIPTION)) {
					isReaderInLineOfDescription = Boolean.TRUE;
				}
			}

			else {
				/** reader is in or after line starting with description */

				if (line.startsWith(ShoreConstants.SUMMARY)) {
					break;
				}
				builder.append(line.trim());
			}

		}

		bufferedReader.close();

		String descriptionAsString = builder.toString();
		String[] descriptionSplittet = descriptionAsString.split("\\\\n");
		String subject = ShoreUtils.extractIdentifier(descriptionSplittet, ShoreConstants.SUBJECT);
		String employee = ShoreUtils.extractIdentifier(descriptionSplittet, ShoreConstants.EMPLOYEE);
		String customer = ShoreUtils.extractIdentifier(descriptionSplittet, Constants.CUSTOMER);
		String service = ShoreUtils.extractIdentifier(descriptionSplittet, ShoreConstants.SERVICE);

		appointment.setSubject(subject);
		appointment.setEmployee(employee);
		appointment.setCustomer(ShoreUtils.handleCustomer(customer));
		appointment.setService(ShoreUtils.handleService(service));

		return appointment;
	}

	/**
	 * removes the appointments from receving not belonging to current week
	 * 
	 * @param receiving
	 *            - the list of appointments
	 * @param monday
	 *            - startdate
	 * @param saturday
	 *            - enddate
	 * @return the appointments of the current week
	 */
	public static List<Appointment> removeAppointmentsNotBelongingToCurrentWeek(List<Appointment> receiving,
			DayAndMonth monday, DayAndMonth saturday) {

		if ((receiving == null) || receiving.isEmpty()) {
			return Collections.emptyList();
		}

		/** computing date of Monday in current week */
		int dayOfMonday = monday.getDay();
		int monthOfMonday = monday.getMonth();
		int dayOfSaturday = saturday.getDay();
		int monthOfSaturday = saturday.getMonth();

		Iterator<Appointment> iterator = receiving.iterator();
		Boolean isWholeCurrentWeekInSameYear = monthOfMonday <= monthOfSaturday;

		while (iterator.hasNext()) {
			Appointment current = iterator.next();
			shore.Date currentDate = current.getDate();
			int currentMonth = currentDate.getMonth().getValue() - 1;
			int currentDay = currentDate.getDayOfMonth();

			if (isWholeCurrentWeekInSameYear) {
				/** case No Filtering Across Annual Limits: */

				/** case: same month */
				if (Integer.compare(monthOfMonday, monthOfSaturday) == 0) {

					// Appointment not in period
					if (Integer.compare(currentMonth, monthOfMonday) == 0) {

						/** appointment in current month */

						if ((currentDay < dayOfMonday) || (currentDay > dayOfSaturday)) {
							iterator.remove();
						}

					} else {
						iterator.remove();
					}

				}
				/** case: month of Saturday is after month of Monday */
				else if (Integer.compare(monthOfMonday, monthOfSaturday - 1) == 0) {
					// Appointment lies in month of Monday
					if (Integer.compare(monthOfMonday, currentMonth) == 0) {

						// Appointment not in period
						if (dayOfMonday > currentDay) {
							iterator.remove();
						}
					}

					// Appointment lies in month of Saturday
					else if (Integer.compare(monthOfSaturday, currentMonth) == 0) {

						// Appointment not in period
						if (dayOfSaturday < currentDay) {
							iterator.remove();
						}
					}

				}

			} else {
				/** case Filtering Across Annual Limits: */
				/** case: month of Monday is in old year month of Saturday in new year */

				// Appointment lies in month of Monday
				if (Integer.compare(monthOfMonday, currentMonth) == 0) {

					// Appointment not in period
					if (dayOfMonday > currentDay) {
						iterator.remove();
					}
				}

				// Appointment lies in month of Saturday
				else if (Integer.compare(monthOfSaturday, currentMonth) == 0) {

					// Appointment not in period
					if (dayOfSaturday < currentDay) {
						iterator.remove();
					}
				}
			}
		}

		return receiving;
	}

	/**
	 * computes the date of Monday in current week
	 * 
	 * @return the computed date
	 * @throws ParseException
	 */
	public static DayAndMonth getDateOfMonday() {
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		/** computing date of Monday in current week */
		calendar.add(Calendar.DAY_OF_WEEK, -(dayOfWeek - Calendar.MONDAY));

		return new DayAndMonth(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH));
	}

	/**
	 * computes the date of Saturday in current week
	 * 
	 * @return the computed date
	 */
	public static DayAndMonth getDateOfSaturday() {
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		/** computing date of Friday in current week */
		calendar.add(Calendar.DAY_OF_WEEK, Calendar.SATURDAY - dayOfWeek);
		return new DayAndMonth(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH));
	}

	/**
	 * yields the prename in selected row of table
	 * 
	 * @param row
	 *            - the row
	 * @param table
	 *            - the tabel
	 * @return - the prename
	 */
	public static String getPreNameFromTable(int row, JTable table) {
		return (String) table.getValueAt(row, 3);
	}

	/**
	 * yields the customer name in selected row of table
	 * 
	 * @param row
	 *            - the row
	 * @param table
	 *            - the tabel
	 * @return - the customer name
	 */
	public static String getCustomerNameFromTable(int row, JTable table) {
		return (String) table.getValueAt(row, 4);
	}

	/**
	 * yields the service selected row of table
	 * 
	 * @param row
	 *            - the row
	 * @param table
	 *            - the tabel
	 * @return - the service
	 */
	public static String getServiceFromTable(int row, JTable table) {
		return (String) table.getValueAt(row, 6);
	}

	/**
	 * yields the day of week in selected row of table
	 * 
	 * @param row
	 *            - the row
	 * @param table
	 *            - the tabel
	 * @return - the day of week
	 */
	public static Integer getDayOfWeekFromTable(int row, JTable table) {
		return (Integer) table.getValueAt(row, 0);
	}

	/**
	 * yields the time in selected row of table
	 * 
	 * @param row
	 *            - the row
	 * @param table
	 *            - the tabel
	 * @return - the time
	 */
	public static String getTimeFromTable(int row, JTable table) {
		return (String) table.getValueAt(row, 1);
	}

	/**
	 * yields a list of Shore appointments
	 * 
	 * @return list of Shore appointments
	 * 
	 * @throws IOException
	 *             - in case of technical error
	 */
	static List<Appointment> getListOfAppointments(String pathToShore) throws IOException {

		List<Appointment> appointments = new ArrayList<Appointment>();
		File shoreFile = new File(pathToShore);
		for (File file : shoreFile.listFiles()) {

			try {
				appointments.add(ShoreUtils.generateAppointmentFromFile(file));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return appointments;
	}

	public static int getDayAsInt(String dayOfWeek) {

		if (dayOfWeek.equals(ShoreConstants.MONDAY)) {
			return 2;
		} else if (dayOfWeek.equals(ShoreConstants.TUESDAY)) {
			return 3;
		} else if (dayOfWeek.equals(ShoreConstants.WEDNESDAY)) {
			return 4;
		} else if (dayOfWeek.equals(ShoreConstants.THURSDAY)) {
			return 5;
		} else if (dayOfWeek.equals(ShoreConstants.FRIDAY)) {
			return 6;
		} else if (dayOfWeek.equals(ShoreConstants.SATURDAY)) {
			return 7;
		} else if (dayOfWeek.equals(ShoreConstants.SUNDAY)) {
			return 1;
		}
		return 0;
	}

	public static String getDayAsString(Integer dayOfWeek) {

		if (Integer.compare(dayOfWeek, 2) == 0) {
			return ShoreConstants.MONDAY;
		} else if (Integer.compare(dayOfWeek, 3) == 0) {
			return ShoreConstants.TUESDAY;
		} else if (Integer.compare(dayOfWeek, 4) == 0) {
			return ShoreConstants.WEDNESDAY;
		} else if (Integer.compare(dayOfWeek, 5) == 0) {
			return ShoreConstants.THURSDAY;
		} else if (Integer.compare(dayOfWeek, 6) == 0) {
			return ShoreConstants.FRIDAY;
		} else if (Integer.compare(dayOfWeek, 7) == 0) {
			return ShoreConstants.SATURDAY;
		} else if (Integer.compare(dayOfWeek, 1) == 0) {
			return ShoreConstants.SUNDAY;
		}
		return null;
	}

}
