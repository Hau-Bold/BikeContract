package erp;

/** the class ErpDataSet */
public class ErpDataSet {

	String ean, articleName, uvpErp, sellPriceErp, producer, productsEsl1, productsEsl2, productsEsl3, productsUrlKey,
			ArticleNumber;

	/**
	 * Constructor.
	 * 
	 * @param ean
	 *            - the ean (this is the unique identifier)
	 * @param articleName
	 *            - the name of the article
	 * @param uvpErp
	 *            - the uvp
	 * @param sellPriceErp
	 *            - the sell price
	 * @param producer
	 *            - the producer
	 * @param productsEsl1
	 *            - the productsEsl1
	 * @param productsEsl2
	 *            - the productsEsl2
	 * @param productsEsl3
	 *            - the productsEsl3
	 * @param productsUrlKey
	 *            - the products url key
	 */
	public ErpDataSet(String ean, String articleName, String uvpErp, String sellPriceErp, String producer,
			String productsEsl1, String productsEsl2, String productsEsl3, String productsUrlKey) {
		super();
		this.ean = ean;
		this.articleName = articleName;
		this.uvpErp = uvpErp;
		this.sellPriceErp = sellPriceErp;
		this.producer = producer;
		this.productsEsl1 = productsEsl1;
		this.productsEsl2 = productsEsl2;
		this.productsEsl3 = productsEsl3;
		this.productsUrlKey = productsUrlKey;
	}

	/**
	 * Constructor.
	 * 
	 * @param ean
	 *            - the ean (this is the unique identifier)
	 * @param articleName
	 *            - the name of the article
	 * @param uvpErp
	 *            - the uvp
	 * @param sellPriceErp
	 *            - the sell price
	 * @param producer
	 *            - the producer
	 * @param ArticleNumber
	 *            - the ArticleNumber
	 */
	public ErpDataSet(String ean, String articleName, String uvpErp, String sellPriceErp, String producer,
			String wwsArticleNumber) {

		this.ean = ean;
		this.articleName = articleName;
		this.uvpErp = uvpErp;
		this.sellPriceErp = sellPriceErp;
		this.producer = producer;
		this.ArticleNumber = wwsArticleNumber;

	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public String getUvpErp() {
		return uvpErp;
	}

	public void setUvpErp(String uvpErp) {
		this.uvpErp = uvpErp;
	}

	public String getSellPriceErp() {
		return sellPriceErp;
	}

	public void setSellPriceErp(String sellPriceErp) {
		this.sellPriceErp = sellPriceErp;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getProductsEsl1() {
		return productsEsl1;
	}

	public void setProductsEsl1(String productsEsl1) {
		this.productsEsl1 = productsEsl1;
	}

	public String getProductsEsl2() {
		return productsEsl2;
	}

	public void setProductsEsl2(String productsEsl2) {
		this.productsEsl2 = productsEsl2;
	}

	public String getProductsEsl3() {
		return productsEsl3;
	}

	public void setProductsEsl3(String productsEsl3) {
		this.productsEsl3 = productsEsl3;
	}

	public String getProductsUrlKey() {
		return productsUrlKey;
	}

	public void setProductsUrlKey(String productsUrlKey) {
		this.productsUrlKey = productsUrlKey;
	}

	public String getArticleNumber() {
		return ArticleNumber;
	}

}
