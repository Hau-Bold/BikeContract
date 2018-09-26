package shore;

/***
 * the class DayAndMonth
 *
 */
public class DayAndMonth {
	int day;
	int month;

	/**
	 * Constructor.
	 * 
	 * @param day
	 *            - the day
	 * @param month
	 *            - the month
	 */
	public DayAndMonth(int day, int month) {
		super();
		this.day = day;
		this.month = month;
	}

	// get and set follows below here
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

}
