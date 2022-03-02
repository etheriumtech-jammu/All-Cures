package util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.Properties;

public class WhatsAPITrackUsers {

	public static void main(String[] args) {

		try {

			POSTRequestTrackUsers(args);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void POSTRequestTrackUsers(String[] params) throws IOException {
		// final String POST_PARAMS = "{\"userId\":\"101\",
		// \"phoneNumber\":\"7889761896\", \"countryCode\":\"+91\",
		// \"traits\":{\"name\":\"An test09nov\", \"email\":\"arnav@gmail.com\"},
		// \"createdAt\":\"2020-11-09T13:26:52.926Z\"}";
		String fileProperties = "whatsapi.properties";
		Properties prop = new WAPICommon().readPropertiesFile(fileProperties);
		System.out.println("URL_USERS: " + prop.getProperty("URL_API_USERS"));
		String countryCode = params[0];
		String mobile = params[1];
		String nl_sub_type = params[2];
		String nl_sub_disease_id = params[3];
		String nl_sub_cures_id = params[4];
		Instant created_at = java.time.Clock.systemUTC().instant();

//		final String POST_PARAMS = "{\"userId\":\"101\", \"phoneNumber\":\"7889761896\", \"countryCode\":\"+91\", \"traits\":{\"pincode\":\"180002\"}, \"createdAt\":\"2020-11-09T13:26:52.926Z\"}";
		final String POST_PARAMS = "{\"phoneNumber\":\"" + mobile + "\", \"countryCode\":\"" + countryCode + "\","
				+ " \"traits\":{\"nl_sub_type\":\"" + nl_sub_type + "\"," + " \"nl_sub_disease_id\":\""
				+ nl_sub_disease_id + "\"," + " \"nl_sub_cures_id\":\"" + nl_sub_cures_id + "\"}," + " \"createdAt\":\""
				+ created_at + "\"}";

		System.out.println(POST_PARAMS);
//		URL obj = new URL("https://api.interakt.ai/v1/public/track/users/");
		URL obj = new URL(prop.getProperty("URL_API_USERS"));
		HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
		postConnection.setRequestMethod("POST");
		postConnection.setRequestProperty("Authorization", "Basic " + prop.getProperty("Authorization_Key"));
		// "Basic S0p6VDAwTXE0N2dhM0w5Uk1KOVBVU1dhOE56emRodkYwQWFwZTk3VXcxczo="); //old
		// one
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
}
