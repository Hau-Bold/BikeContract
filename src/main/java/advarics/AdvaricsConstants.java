package advarics;

import java.nio.charset.Charset;

/** the class AdvaricsConstants */
public class AdvaricsConstants {

	public final static String ENTITY_REQUEST_URL = "https://wohlleben.advarics.net/external";
	public final static String STORE_NO = "StoreNo";
	public final static String ARTICLE_INFOS = "ArticleInfos";
	public static final String GTIN = "Gtin"; // aka ean
	public final static String CONTENT_TYPE = "Content-Type";
	public final static String CONTENT_TYPE_VALUE = "application/json";
	public static final String STORES = "Stores";
	public static final int RESPONSE_CODE_OK = 200;
	public static final String REQUEST_NOT_POSSIBLE = "<html>Abfrage nicht möglich: <br><font color=\"red\">Error-Code:</font> %s </html>";
	public static final Charset CHARSET = Charset.forName("UTF-8");

	public static final String OFFERPRICE = "OfferPrice"; // aka houseprice
	public static final String SALESPRICE = "SalesPrice"; // aka uvp
	public static final String BRANDNAME = "BrandName"; // artikelgruppe
	public static final String SUPPLIER_NAME = "SupplierName"; // aka producer
	public static final String ARTICLE_NAME = "ArticleName";// ala ArticleName
	public static final String ARTICLE_NO = "ArticleNo"; // aka? wwsarticlenumber

}
