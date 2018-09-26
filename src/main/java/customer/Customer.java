package customer;

import constants.Constants;

/** the class Customer */
public class Customer {

	private int idOfCustomer;
	private String customerName;
	private String prename;
	private String street;
	private String number;
	private String postalCode;
	private String place;
	private String mobile;
	private String email;
	private String createdAt;
	private String lastModified;

	/**
	 * Constructor.
	 * 
	 * @param idOfCustomer
	 *            - the id of the customer
	 * 
	 * @param customerName
	 *            - the customerName
	 * @param prename
	 *            - the prename
	 * @param street
	 *            - the street
	 * @param number
	 *            - the number
	 * @param postalCode
	 *            - the postalCode
	 * @param place
	 *            - the place
	 * @param mobile
	 *            - the mobile
	 * @param email
	 *            - the email
	 * @param createdAt
	 *            - the createdAt
	 * @param lastModified
	 *            - the date of beeing last modified
	 */
	public Customer(int idOfCustomer, String customerName, String surname, String street, String number,
			String postalCode, String place, String mobile, String email, String createdAt, String lastModified) {

		this(customerName, surname, street, number, postalCode, place, mobile, email, createdAt, lastModified);
		this.idOfCustomer = idOfCustomer;
	}

	/**
	 * Constructor.
	 * 
	 * @param customerName
	 *            - the customerName
	 * @param prename
	 *            - the prename
	 * @param street
	 *            - the street
	 * @param number
	 *            - the number
	 * @param place
	 *            - the place
	 * @param mobile
	 *            - the mobile
	 */
	public Customer(String customerName, String prename, String street, String number, String postalCode, String place,
			String mobile, String email) {

		this.customerName = customerName;
		this.prename = prename;
		this.street = street;
		this.number = number;
		this.postalCode = postalCode;
		this.place = place;
		this.mobile = mobile;
		this.email = email;
	}

	/**
	 * default Constructor.
	 */
	public Customer() {
		this.setCustomerName(Constants.EMPTY_STRING);
		this.setPrename(Constants.EMPTY_STRING);
		this.setStreet(Constants.EMPTY_STRING);
		this.setNumber(Constants.EMPTY_STRING);
		this.setPostalCode(Constants.EMPTY_STRING);
		this.setPlace(Constants.EMPTY_STRING);
		this.setMobile(Constants.EMPTY_STRING);
		this.setEmail(Constants.EMPTY_STRING);
	}

	// set & get follows below here

	public Customer(String customerName, String surname, String street, String number, String place, String postalCode,
			String mobile, String email, String createdAt, String lastModified) {
		this(customerName, surname, street, number, place, postalCode, mobile, email);

		this.createdAt = createdAt;
		this.lastModified = lastModified;

	}

	public int getIdOfCustomer() {
		return idOfCustomer;
	}

	public void setIdOfCustomer(int idOfCustomer) {
		this.idOfCustomer = idOfCustomer;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPrename() {
		return prename;
	}

	public void setPrename(String prename) {
		this.prename = prename;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Customer [idOfCustomer=" + idOfCustomer + ", customerName=" + customerName + ", prename=" + prename
				+ ", street=" + street + ", number=" + number + ", postalCode=" + postalCode + ", place=" + place
				+ ", mobile=" + mobile + ", email=" + email + ", createdAt=" + createdAt + ", lastModified="
				+ lastModified + "]";
	}

}
