package shore;

import java.time.DayOfWeek;
import java.time.Month;

/**
 * the class Time
 *
 */
public class Date {

	private DayOfWeek dayOfWeek;
	private int dayOfMonth;
	private Month month;
	private int year;

	/**
	 * Constructor.
	 * 
	 * @param dayOfWeek
	 *            - the dayOfWeek
	 * @param month
	 *            - the month
	 * @param dayOfMonth
	 *            - the dayOfMonth
	 * @param year
	 *            - the year
	 */
	public Date(DayOfWeek dayOfWeek, int dayOfMonth, Month month, int year) {
		this.dayOfWeek = dayOfWeek;
		this.month = month;
		this.dayOfMonth = dayOfMonth;
		this.year = year;
	}

	// get and set follows below here
	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public Month getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dayOfMonth;
		result = prime * result + ((dayOfWeek == null) ? 0 : dayOfWeek.hashCode());
		result = prime * result + ((month == null) ? 0 : month.hashCode());
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Date other = (Date) obj;
		if (dayOfMonth != other.dayOfMonth)
			return false;
		if (dayOfWeek != other.dayOfWeek)
			return false;
		if (month != other.month)
			return false;
		if (year != other.year)
			return false;
		return true;
	}

}
