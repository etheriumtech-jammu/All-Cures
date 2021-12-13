package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class WhatsAPITrackEvents {

	public static void main(String[] args) {

		try {
			System.out.println(args[0]);
			System.out.println(args[1]);
			if (null == args[0]) {
				System.out.println("Please provide argument values...like event and dc_ids");
			}

			POSTRequestTrackEvents(args);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void POSTRequestTrackEvents(String[] params) throws IOException {
		// final String POST_PARAMS = "{\"userId\": \"101\",\"event\":
		// \"OrderPlacedCures\",\"traits\": {\"orderCreatedBy\": \"Gavin
		// Roberts\",\"orderCreatedDate\":
		// \"2020-11-01T012:10:26.122Z\",\"orderNumber\": \"CUS001\",\"orderValue\":
		// \"50.00\"}}";
		// final String POST_PARAMS = "{\"phoneNumber\":
		// \"9167378338\",\"countryCode\":\"+91\",\"event\": \"Diabetes cure
		// Ayurveda-INDIA\",\"traits\": {\"country\": \"India\",\"treatmentType\":
		// \"Ayurveda\",\"diseaseCondition\": \"Diabetes\",\"dc_id\": \"10\"}}";

		// for (int i = 0; i < params.length; i++) {
		// DAILY_NL_DISEASE_IDS 4,5,6 +91 9167378338 Arthritis,Osteoporosis Ayurveda
		// India
		String event = params[0];
		String dc_ids = params[1];
		String dc_names = params[4];
		String treatmentType = params[5];
		String country = params[6];
		// }
		String fileProperties = "whatsapi.properties";
		Properties prop = new WAPICommon().readPropertiesFile(fileProperties);
		System.out.println("URL_API_EVENTS: " + prop.getProperty("URL_API_EVENTS"));

		String traits = "{\"country\": \"" + country + "\"," + "\"treatmentType\": \"" + treatmentType + "\","
		// + "\"dc_Names\": \"Arthritis,Anemia,Bones and Joints,Osteoporosis,Brain and
		// Nervous\","
				+ "\"dc_Names\": \"" + dc_names + "\"," + "\"dc_ids\": \"" + dc_ids + "\"}";
		// + "\"dc_ids\": \"7,14,15,16,17\"}";

		// String event = "DISEASESANDCONDITIONS";
		// String event = "DISEASESANDCONDITIONS";
		String countryCode = params[2];
		String mobile = params[3];
		// String countryCode = "+91";
		// String mobile = "9167378338";
		final String POST_PARAMS = "{\"phoneNumber\": \"" + mobile + "\",\"countryCode\":\"" + countryCode
				+ "\",\"event\": \"" + event + "\",\"traits\": " + traits + "}";
		System.out.println(POST_PARAMS);
//		URL obj = new URL("https://api.interakt.ai/v1/public/track/events/");
		URL obj = new URL(prop.getProperty("URL_API_EVENTS"));
		HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
		postConnection.setRequestMethod("POST");
		postConnection.setRequestProperty("Authorization", "Basic " + prop.getProperty("Authorization_Key"));
		postConnection.setRequestProperty("Content-Type", "application/json");

		postConnection.setDoOutput(true);
		OutputStream os = postConnection.getOutputStream();
		os.write(POST_PARAMS.getBytes());
		os.flush();
		os.close();

		int responseCode = postConnection.getResponseCode();
		System.out.println("POST Response Code :  " + responseCode);
		System.out.println("POST Response Message : " + postConnection.getResponseMessage());

		if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_ACCEPTED) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("POST NOT WORKED");
		}
	}

	public static void POSTRequestTrackEventsByArticleId(int article_id) throws SQLException {
		ArrayList NSData = new WAPICommon().fetchDatabaseResultsForNewsletterByArticle(article_id);
		for (int i = 0; i < NSData.size(); i++) {
			String[] params = new String[8];
			params[0] = "DAILY_NL_DISEASE_IDS";//"NEW_ARTICLE_PUBLISHED";
			if ( (int) ((HashMap) NSData.get(i)).get("nl_sub_type") == 1 )
			{
				params[1] = "All Diseases, Cures and Symptoms";//all diseased cures subscribed
			}else {
				params[1] =  (String) ((HashMap) NSData.get(i)).get("nl_subscription_disease_id") ;//DC_ID;
			}
			params[2] = "+"+(Integer) ((HashMap) NSData.get(i)).get("country_code"); //countryCode
			params[3] = (String) ((HashMap) NSData.get(i)).get("mobile"); // mobile
			params[4] = article_id+""; //DC_NAMES
			params[5] = ""; //treatement_type
			params[6] = "";//country
			// }
//			params[0] = "+91";// (String) ((HashMap) NSData.get(i)).get("mobile");//countryCode
//			params[1] = (String) ((HashMap) NSData.get(i)).get("mobile"); // mobile
//			params[2] = (null != (String) (((HashMap) NSData.get(i)).get("nl_sub_type") + ""))
//					? (String) (((HashMap) NSData.get(i)).get("nl_sub_type") + "")
//					: "";
			
			try {
				WhatsAPITrackEvents.POSTRequestTrackEvents(params);		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
