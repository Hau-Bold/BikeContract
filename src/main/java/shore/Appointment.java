package shore;

/**
 * the class Appointment
 *
 */
public class Appointment {

	private shore.Time startTime;
	private shore.Time endTime;
	private shore.Date date;
	private Customer customer;
	private String employee;
	private String service;
	private String subject;

	// get and set follows here
	public shore.Time getStartTime() {
		return startTime;

	}

	public void setStartTime(shore.Time startTime) {
		this.startTime = startTime;
	}

	public shore.Time getEndTime() {
		return endTime;
	}

	public void setEndTime(shore.Time endTime) {
		this.endTime = endTime;
	}

	public shore.Date getDate() {
		return date;
	}

	public void setDate(shore.Date date) {
		this.date = date;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}
