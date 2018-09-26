package advarics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import constants.Constants;
import contractDigitalizer.BikeContract;
import erp.ErpDataSet;
import utils.InformationProvider;

public class RequestAdvaricsData {

	static URL url = null;
	static HttpURLConnection con = null;

	public static ErpDataSet getErpData(String barCode) throws ParseException {

		String urlAddress = AdvaricsUtils.buildURL(barCode);

		ErpDataSet response = null;

		try {
			url = new URL(urlAddress);
			con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			/** setting request headers */
			con.setRequestMethod("GET");
			con.setRequestProperty(AdvaricsConstants.CONTENT_TYPE, AdvaricsConstants.CONTENT_TYPE_VALUE);

			String authorizationKey = BikeContract.access.get(Constants.AUTHORIZATION);
			con.setRequestProperty(Constants.AUTHORIZATION, authorizationKey);

		} catch (Exception e) {
			e.printStackTrace();
		}

		int responseCode = 0;
		try {
			responseCode = con.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (Integer.compare(responseCode, HttpURLConnection.HTTP_OK) == 0) {

			/** read response */
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(con.getInputStream(), AdvaricsConstants.CHARSET));

				List<JSONObject> results = AdvaricsUtils.readAll(in);

				/* here generate erpdata **/
				response = AdvaricsUtils.generateErpDataSet(results);

			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {

			new InformationProvider(Constants.WARNING, JOptionPane.WARNING_MESSAGE,
					AdvaricsConstants.REQUEST_NOT_POSSIBLE, String.valueOf(responseCode)).run();
		}
		return response;

	}

}
