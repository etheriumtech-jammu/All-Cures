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

public class WhatsAPITrackUsersSync {

	public static void main(String[] args) throws SQLException {

		// String apiType = "USERS";// EVENTS
//		String apiType = args[0];
//		String dcIDs_args = args[1];
//		String[] dcIDs_array_args = dcIDs_args.split(",");
//		HashMap postData = new HashMap<String, String>();// EVENTS
		ArrayList NSData = new WAPICommon().fetchDatabaseResultsForNewsletter();
		for (int i = 0; i < NSData.size(); i++) {
			String[] params = new String[5];
			params[0] = "+"+ (Integer) ((HashMap) NSData.get(i)).get("country_code");//countryCode
			params[1] = (String) ((HashMap) NSData.get(i)).get("mobile"); // mobile
			params[2] = (null != (String) (((HashMap) NSData.get(i)).get("nl_sub_type") + ""))
					? (String) (((HashMap) NSData.get(i)).get("nl_sub_type") + "")
					: "";
			params[3] = (null != (String) (((HashMap) NSData.get(i)).get("nl_subscription_disease_id") + ""))
					? (String) (((HashMap) NSData.get(i)).get("nl_subscription_disease_id") + "")
					: "";
			params[4] = (null != (String) (((HashMap) NSData.get(i)).get("nl_subscription_cures_id") + ""))
					? (String) (((HashMap) NSData.get(i)).get("nl_subscription_cures_id") + "")
					: "";
//			params[3] = (String) ((HashMap) NSData.get(i)).get("nl_subscription_disease_id"); //nl_sub_disease_id
//			params[4] = (String) ((HashMap) NSData.get(i)).get("nl_subscription_cures_id"); //nl_sub_cures_id
			try {
				WhatsAPITrackUsers.POSTRequestTrackUsers(params);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			hmRow.put("user_id", rs.getInt(1));
//			hmRow.put("nl_subscription_disease_id", rs.getString(2));
//			hmRow.put("nl_start_date", rs.getDate(3));
//			hmRow.put("nl_sub_type", rs.getInt(4));
//			hmRow.put("mobile", rs.getString(5));
//			hmRow.put("nl_subscription_cures_id", rs.getString(6));
//			hmRow.put("active", rs.getInt(7));
//			hmRow.put("nl_end_date", rs.getDate(8));
//			postData = new HashMap<String, String>();// EVENTS
//			System.out.println(((HashMap) NSData.get(i)).get("user_id"));
			// postData.put("userId", ((HashMap) NSData.get(i)).get("mobile"));
//			postData.put("phoneNumber", ((HashMap) NSData.get(i)).get("mobile"));
//			postData.put("countryCode", ((HashMap) NSData.get(i)).get("mobile"));
//			postData.put("event", "NA");
		}
	}

}
