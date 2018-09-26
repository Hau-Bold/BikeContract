package contract;

import constants.Constants;

/** the class SellPosition */
public class SellPosition {

	private int idOfContract, quantity;
	private String ean, articleName, producer, articleNumber;
	private Double uvp, housePrice;

	/**
	 * Constructor.
	 * 
	 * @param idOfContract
	 *            - the contract's is
	 * @param @param
	 *            ean - the ean of the article
	 * @param quantity
	 *            - the quantity
	 * @param producer
	 *            - the producer
	 * @param articleName
	 *            - the articleName
	 * @param uvp
	 *            - the uvp
	 * @param housePrice
	 *            - the housePrice
	 */
	public SellPosition(int idOfContract, String ean, int quantity, String articleName, String producer, Double uvp,
			Double housePrice) {
		this(ean, quantity, articleName, producer, uvp, housePrice);
		this.idOfContract = idOfContract;
	}

	/**
	 * Constructor.
	 * 
	 * @param ean
	 *            - the ean
	 * @param quantity
	 *            - the quantity
	 * @param articleName
	 *            - the articleName
	 * @param producer
	 *            - the producer
	 * @param uvp
	 *            - the uvp
	 * @param housePrice
	 *            - the housePrice
	 */
	public SellPosition(String ean, int quantity, String articleName, String producer, Double uvp, Double housePrice) {

		this.ean = ean;
		this.quantity = quantity;
		this.articleName = articleName;
		this.producer = producer;
		this.uvp = uvp;
		this.housePrice = housePrice;
	}

	/** default constructor. */
	public SellPosition() {
		this(0, Constants.EMPTY_STRING, 0, Constants.EMPTY_STRING, Constants.EMPTY_STRING, .0, .0);
		this.articleNumber = Constants.EMPTY_STRING;
	}

	@Override
	public String toString() {
		return "SellPosition [idOfContract=" + idOfContract + ", quantity=" + quantity + ", producer=" + producer
				+ ", articleName=" + articleName + ", uvp=" + uvp + ", housePrice=" + housePrice + "]";
	}

	// get & set follows below here

	public int getIdOfContract() {
		return idOfContract;
	}

	public void setIdOfContract(int idOfContract) {
		this.idOfContract = idOfContract;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String brand) {
		this.producer = brand;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String sign) {
		this.articleName = sign;
	}

	public Double getUvp() {
		return uvp;
	}

	public void setUvp(Double uvp) {
		this.uvp = uvp;
	}

	public Double getHousePrice() {
		return housePrice;
	}

	public void setHousePrice(Double housePrice) {
		this.housePrice = housePrice;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public String getArticleNumber() {
		return articleNumber;
	}

	public void setArticleNumber(String wwsArticleNumber) {
		this.articleNumber = wwsArticleNumber;
	}

}
