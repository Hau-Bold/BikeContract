package shore;

/**
 * the class Customer
 *
 */
public class Customer {

	private String preName;
	private String customerName;
	private String salutation;

	/**
	 * Constructor.
	 * 
	 * @param preName
	 *            - the preName
	 * @param customerName
	 *            - the customerName
	 * @param salutation
	 *            - the salutation
	 */
	public Customer(String preName, String customerName, String salutation) {
		this.preName = preName;
		this.customerName = customerName;
		this.salutation = salutation;
	}

	/**
	 * Constructor.
	 * 
	 * @param preName
	 *            - the preName
	 * @param customerName
	 *            - the customerName
	 */
	public Customer(String preName, String customerName) {
		this.preName = preName;
		this.customerName = customerName;
	}

	/**
	 * Default Constructor.
	 */
	public Customer() {
		preName = "";
		customerName = "";
		salutation = "";
	}

	// get and set follows below here
	public String getPreName() {
		return preName;
	}

	public void setPreName(String preName) {
		this.preName = preName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String surName) {
		this.customerName = surName;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

}
