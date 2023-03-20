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
import javax.net.ssl.HttpsURLConnection;
public class WhatsAPITemplateMessage {

	public static void main(String[] args) throws IOException, SQLException {
//		dynamic_disease_template_kb TCM +91 9167378338 https://etheriumtech.com/images/illustrations/service-3.jpg 628 description_little_here

		POSTRequestTemplateMessage(args);
		// POSTRequestTrackEventsByArticleId(628);//628 is TCM video article
	}

	public static void POSTRequestTemplateMessage(String[] params) throws IOException {
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
//		String dc_ids = params[1];
//		String dc_names = params[4];
//		String treatmentType = params[5];
//		String country = params[6];
		// }

//		String traits = "{\"country\": \"" + country + "\"," + "\"treatmentType\": \"" + treatmentType + "\","
//		// + "\"dc_Names\": \"Arthritis,Anemia,Bones and Joints,Osteoporosis,Brain and
//		// Nervous\","
//				+ "\"dc_Names\": \"" + dc_names + "\"," + "\"dc_ids\": \"" + dc_ids + "\"}";
//		// + "\"dc_ids\": \"7,14,15,16,17\"}";

		// String event = "DISEASESANDCONDITIONS";
		// String event = "DISEASESANDCONDITIONS";
		String template_name = params[0];// dynamic_disease_template_kb
		// String countryCode = params[2];
		String mobile = params[3];
		String header1_imgpath = params[4];
		String BV1_art_id = params[5];
		String BV2_dc_name = params[1];
		String BV3_desc = params[6];//actually link of article
		String countryCode = params[7];
		String author_medicine_type = params[8];
		String disease_name = params[9];
		// String mobile = "9167378338";
//		final String POST_PARAMS = "{\"phoneNumber\": \"" + mobile + "\",\"countryCode\":\"" + countryCode
//				+ "\",\"event\": \"" + event + "\",\"traits\": " + traits + "}";
//		if (params[1].equals("All Disease , Symptoms and Cures")) {
//			
//		}
		if (params[1].contains(",")) {
			for (String par_1 : params[1].split(",")) {
				BV2_dc_name = par_1;
				BV3_desc = BV3_desc.replace("#", BV2_dc_name);
				WhatsAPITemplateMessage.runInteraktAPI(template_name, BV2_dc_name, countryCode, mobile, header1_imgpath,
						BV1_art_id, BV3_desc, author_medicine_type, disease_name);
			}

		} else {
			WhatsAPITemplateMessage.runInteraktAPI(template_name, BV2_dc_name, countryCode, mobile, header1_imgpath,
					BV1_art_id, BV3_desc, author_medicine_type, disease_name);
		}
	}

	public static void runInteraktAPI(String template_name, String BV2_dc_name, String countryCode, String mobile,
			String header1_imgpath, String BV1_art_id, String BV3_desc, String author_medicine_type,
			String disease_name) throws IOException {

		String fileProperties = "whatsapi.properties";
		Properties prop = new WAPICommon().readPropertiesFile(fileProperties);
		System.out.println("URL_API_TEMPLATES: " + prop.getProperty("URL_API_TEMPLATES"));

//		final String POST_PARAMS = "{\"countryCode\": \"" + countryCode + "\", \"phoneNumber\": \"" + mobile + "\","
//				+ " \"type\": \"Template\"," + " \"template\": {\"name\": \"" + template_name
//				+ "\",\"languageCode\": \"en\"," + " \"headerValues\": [\"" + header1_imgpath + "\"],"
//				+ " \"bodyValues\": [\"" + BV1_art_id + "\",\"" + BV2_dc_name + "\",\"" + BV3_desc + "\"]" + "}}";

//		final String POST_PARAMS = "{\"countryCode\": \"" + countryCode + "\", \"phoneNumber\": \"" + mobile + "\","
//				+ " \"type\": \"Template\"," + " \"template\": {\"name\": \"" + template_name
//				+ "\",\"languageCode\": \"en\"," + " \"headerValues\": [\"" + header1_imgpath + "\"],"
//				+ " \"bodyValues\": [\"Subscriber\",\"" +  BV3_desc + "\"]" + "}}";
		final String POST_PARAMS = "{\"countryCode\": \"" + countryCode + "\", \"phoneNumber\": \"" + mobile + "\","
				+ " \"type\": \"Template\"," + " \"template\": {\"name\": \"" + template_name
				+ "\",\"languageCode\": \"en\"," + " \"headerValues\": [\"" + header1_imgpath + "\"],"
				+ " \"bodyValues\": [ \"" + disease_name + "\", \"" + BV3_desc + "\",\"" + author_medicine_type + "\"]"
				+ "}}";
		
		System.setProperty("javax.net.ssl.trustStoreType", "jks");
	       System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/certs/java/cacerts");
	        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

		System.out.println(POST_PARAMS);
//		URL obj = new URL("https://api.interakt.ai/v1/public/track/events/");
		URL obj = new URL(prop.getProperty("URL_API_TEMPLATES"));
//		HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
		HttpsURLConnection postConnection = (HttpsURLConnection) obj.openConnection();
		
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

	public static void POSTRequestTrackEventsByArticleId(String art_title, int article_id, String type, int dc_id,
			String article_location_relative_image, String author_medicine_type, String disease_name)
			throws SQLException {
		ArrayList NSData = new WAPICommon().fetchDatabaseResultsForNewsletterByArticle(article_id, type, dc_id);
		// @TODO remove duplicates and run for all CSV disease and cures id's & all for
		// sub_type =1
		String fileProperties = "whatsapi.properties";
		// set defalut template name
		String templateName = "25feballcures";
		try {
			Properties prop = new WAPICommon().readPropertiesFile(fileProperties);
			templateName = prop.getProperty("subscriber_template_name");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		for (int i = 0; i < NSData.size(); i++) {
			String[] params = new String[10];
			// params[0] = "dynamic_disease_template_kb";//
			// "DAILY_NL_DISEASE_IDS";//"NEW_ARTICLE_PUBLISHED";
			params[0] = templateName;// "prod17janimageandlink";//"prod24decimageandlink";//
										// "DAILY_NL_DISEASE_IDS";//"NEW_ARTICLE_PUBLISHED";
			if ((int) ((HashMap) NSData.get(i)).get("nl_sub_type") == 1) {
				params[1] = "All Disease Symptoms and Cures";// all diseased cures subscribed
			} else if (type.contains("1") || type.contains("3")) {
				params[1] = (String) ((HashMap) NSData.get(i)).get("nl_subscription_disease_id");// DC_ID;
			} else if (type.contains("2")) {
				params[1] = (String) ((HashMap) NSData.get(i)).get("nl_subscription_cures_id");// C_ID;
			}
			params[3] = (String) ((HashMap) NSData.get(i)).get("mobile"); // mobile
			params[4] = article_location_relative_image; // article_image
//			params[4] = "https://etheriumtech.com/images/illustrations/service-3.jpg"; // DC_NAMES
			params[5] = "" + article_id; // dc name
			// params[6] = "Also New link pased here dynamically
			// https://all-cures.com/cure/"+article_id+" ... Here goes the decription of the
			// disease #" + params[1];// detailing
//			params[6] = "https://all-cures.com/cure/"+URLEncoder.encode(art_title,"UTF-8");
			params[6] = article_id + "-" + art_title.replaceAll(" ", "-");
			params[7] = "+" + (Integer) ((HashMap) NSData.get(i)).get("country_code"); // +countryCode

			// }
//			params[0] = "+91";// (String) ((HashMap) NSData.get(i)).get("mobile");//countryCode
//			params[1] = (String) ((HashMap) NSData.get(i)).get("mobile"); // mobile
//			params[2] = (null != (String) (((HashMap) NSData.get(i)).get("nl_sub_type") + ""))
//					? (String) (((HashMap) NSData.get(i)).get("nl_sub_type") + "")
//					: "";

			params[8] = author_medicine_type;
			params[9] = disease_name;

			try {
				WhatsAPITemplateMessage.POSTRequestTemplateMessage(params);
					try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
