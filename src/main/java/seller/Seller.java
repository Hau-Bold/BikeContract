package seller;

/**
 * the class Seller
 *
 */
public class Seller {

	private int id;
	private String prename;
	private String name;
	private String number;
	private String mobile;
	private String nameOfImage;
	private byte[] byteArray;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            - the id of the seller
	 * 
	 * @param prename
	 *            - the prename
	 * @param name
	 *            - the name
	 * @param number
	 *            - the number
	 * @param mobile
	 *            - the mobile number
	 * @param nameOfImage
	 *            - the name of the image
	 */
	public Seller(int id, String prename, String name, String number, String mobile, String nameOfImage) {
		this(prename, name, number, mobile, nameOfImage);
		this.id = id;
	}

	/**
	 * Constructor.
	 * 
	 * @param prename
	 *            - the prename
	 * @param name
	 *            - the name
	 * @param number
	 *            - the number
	 * @param mobile
	 *            - the mobile number
	 * @param nameOfImage
	 *            - the name of the image
	 */
	public Seller(String prename, String name, String number, String mobile, String nameOfImage) {

		this.prename = prename;
		this.name = name;
		this.number = number;
		this.mobile = mobile;
		this.nameOfImage = nameOfImage;
	}

	/**
	 * Constructor.
	 * 
	 * @param prename
	 *            - the prename
	 * @param name
	 *            - the name
	 * @param number
	 *            - the number
	 * @param mobile
	 *            - the mobile number
	 * @param byteArray
	 *            - the image as byteArray
	 */
	public Seller(String prename, String name, String number, String mobile, byte[] byteArray) {

		this.prename = prename;
		this.name = name;
		this.number = number;
		this.mobile = mobile;
		this.byteArray = byteArray;
	}

	// get & set follows below here

	public int getId() {
		return id;
	}

	public String getPrename() {
		return prename;
	}

	public String getName() {
		return name;
	}

	public String getNumber() {
		return number;
	}

	public String getMobile() {
		return mobile;
	}

	public String getNameOfImage() {
		return nameOfImage;
	}

	public byte[] getByteArray() {
		return byteArray;
	}

	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}

}
