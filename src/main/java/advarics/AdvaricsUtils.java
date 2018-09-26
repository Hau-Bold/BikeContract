package advarics;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import erp.ErpDataSet;

/** the class AdvaricsUtils */
public class AdvaricsUtils {

	/**
	 * returns the adress for request informations corresponding to scanned ean
	 * 
	 * example:
	 * https://wohlleben.advarics.net/external/ArticleInfos(Gtin='4025931193726',StoreNo=0)
	 * 
	 * @param ean
	 *            - the scanned ean
	 * @return - the address for the request
	 */
	public static String buildURL(String ean) {

		return AdvaricsConstants.ENTITY_REQUEST_URL + "/" + AdvaricsConstants.ARTICLE_INFOS + "("
				+ AdvaricsConstants.GTIN + "=\'" + ean + "'," + AdvaricsConstants.STORE_NO + "=0)";
	}

	public static List<JSONObject> readAll(BufferedReader bufferedReader) throws IOException, ParseException {

		List<JSONObject> response = new ArrayList<JSONObject>();

		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			JSONParser parser = new JSONParser();
			Object object = parser.parse(line);
			JSONObject jsonobject = (JSONObject) object;
			response.add(jsonobject);

		}
		return response;
	}

	public static ErpDataSet generateErpDataSet(List<JSONObject> jsonObjects) throws ParseException {

		JSONObject jsonObject = jsonObjects.get(0);
		ErpDataSet response = null;

		if (jsonObject != null) {

			String offerPrice = (String.valueOf(jsonObject.get(AdvaricsConstants.OFFERPRICE)));
			String salesPrice = (String.valueOf(jsonObject.get(AdvaricsConstants.SALESPRICE)));
			String articleName = (String) jsonObject.get(AdvaricsConstants.ARTICLE_NAME);
			String articleNo = (String) jsonObject.get(AdvaricsConstants.ARTICLE_NO);
			String supplierName = (String) jsonObject.get(AdvaricsConstants.SUPPLIER_NAME);
			String gtin = (String) jsonObject.get(AdvaricsConstants.GTIN);

			response = new ErpDataSet(gtin, articleName, salesPrice, offerPrice, supplierName, articleNo);
		}
		return response;

	}

}
