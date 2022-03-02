package util;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

import dao.ArticleDaoImpl;
import dao.SearchDaoImpl;
import model.Article;
import model.Article_dc_name;

public class WAPICommon {

	public static void main(String[] args) throws IOException, SQLException {
		// TODO Auto-generated method stub
//		String fileProperties = "api.properties";
//		Properties prop = new Common().readPropertiesFile(fileProperties);
//		System.out.println("URL_USERS: " + prop.getProperty("URL_API_USERS"));
		System.out.println(new WAPICommon().fetchDatabaseResultsForNewsletter());
	}

	public static Properties readPropertiesFile(String fileName) throws IOException {
		FileInputStream fis = null;
		Properties prop = null;
		try {
			ClassPathResource resource = new ClassPathResource(fileName);
			InputStream inputStream = resource.getInputStream();
			//fis = new FileInputStream(fileName);
			prop = new Properties();
			prop.load(inputStream);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			//fis.close();
		}
		return prop;
	}

	public static ArrayList fetchDatabaseResultsForNewsletter() throws SQLException {
		Connection con = null;
		ResultSet rs = null;
		Statement stmt = null;
		ArrayList hmFinal = new ArrayList();
		try {
			String fileProperties = "whatsapi.properties";
			Properties prop = new WAPICommon().readPropertiesFile(fileProperties);
			System.out.println("DB_DBNAME : " + prop.getProperty("DB_DBNAME"));
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + prop.getProperty("DB_DBNAME"),
					prop.getProperty("DB_USER"), prop.getProperty("DB_PASS"));
			// here allcures_schema is database name, next is username and password
			stmt = con.createStatement();
			rs = stmt.executeQuery(
					"select user_id, nl_subscription_disease_id, nl_start_date, nl_sub_type, mobile, nl_subscription_cures_id, active, nl_end_date from newsletter");
			while (rs.next()) {
				HashMap<String, Object> hmRow = new HashMap<String, Object>();
				hmRow.put("user_id", rs.getInt(1));
				hmRow.put("nl_subscription_disease_id", rs.getString(2));
				hmRow.put("nl_start_date", rs.getDate(3));
				hmRow.put("nl_sub_type", rs.getInt(4));
				hmRow.put("mobile", rs.getString(5));
				hmRow.put("nl_subscription_cures_id", rs.getString(6));
				hmRow.put("active", rs.getInt(7));
				hmRow.put("nl_end_date", rs.getDate(8));
				hmFinal.add(hmRow);
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e);
			stmt.close();
			rs.close();
			con.close();
		}finally {
			con.close();
		}
		return hmFinal;
	}
	public static ArrayList fetchDatabaseResultsForNewsletterByArticle(int article_id) throws SQLException {
		Article_dc_name artDet =  new ArticleDaoImpl().getArticleDetails(article_id);
		//List artList = SearchDaoImpl.findRegionsNameForDiseaseId(artDet.getDisease_condition_id());
		//System.out.println(artList);//article_id's get it from this list
		int dc_id = artDet.getDisease_condition_id();
		String type = artDet.getType();
		String article_location = artDet.getContent_location();
		
		String whereStr = " nl_sub_type = 1 ";
		
		if (type.contains("1") || type.contains("3"))
			whereStr += " or FIND_IN_SET ("+dc_id+", nl_subscription_disease_id) > 0 ";
		if (type.contains("2"))
			whereStr += " or FIND_IN_SET ("+dc_id+", nl_subscription_cures_id) > 0 ";
		whereStr += " and active=1 ";

		Connection con = null;
		ResultSet rs = null;
		Statement stmt = null;
		ArrayList hmFinal = new ArrayList();
		try {
			String fileProperties = "whatsapi.properties";
			Properties prop = new WAPICommon().readPropertiesFile(fileProperties);
			System.out.println("DB_DBNAME : " + prop.getProperty("DB_DBNAME"));
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + prop.getProperty("DB_DBNAME"),
					prop.getProperty("DB_USER"), prop.getProperty("DB_PASS"));
			// here allcures_schema is database name, next is username and password
			stmt = con.createStatement();
			String query = 
					"select user_id, nl_subscription_disease_id, nl_start_date, nl_sub_type, mobile, nl_subscription_cures_id, active, nl_end_date, country_code, count(*) as count from newsletter "
					+ " where " + whereStr 
					+ " group by country_code, mobile,nl_sub_type ,nl_subscription_disease_id, nl_subscription_cures_id "
					+ " order by country_code, mobile,nl_sub_type ,nl_subscription_disease_id, nl_subscription_cures_id";
			
			System.out.println("For article#"+article_id+", SQL for fetchDatabaseResultsForNewsletterByArticle "+query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				HashMap<String, Object> hmRow = new HashMap<String, Object>();
				hmRow.put("user_id", rs.getInt(1));
				hmRow.put("nl_subscription_disease_id", rs.getString(2));
				hmRow.put("nl_start_date", rs.getDate(3));
				hmRow.put("nl_sub_type", rs.getInt(4));
				hmRow.put("mobile", rs.getString(5));
				hmRow.put("nl_subscription_cures_id", rs.getString(6));
				hmRow.put("active", rs.getInt(7));
				hmRow.put("nl_end_date", rs.getDate(8));
				hmRow.put("country_code", rs.getInt(9));
				hmRow.put("count", rs.getInt(10));
				hmRow.put("article_location", article_location);
				hmFinal.add(hmRow);
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e);
			stmt.close();
			rs.close();
			con.close();
		}finally {
			con.close();
		}
		return hmFinal;
	}
	public static ArrayList fetchDatabaseResultsForNewsletterByArticle(int article_id, String type, int dc_id) throws SQLException {
		String whereStr = " nl_sub_type = 1 ";
		
		if (type.contains("1") || type.contains("3"))
			whereStr += " or FIND_IN_SET ("+dc_id+", nl_subscription_disease_id) > 0 ";
		if (type.contains("2"))
			whereStr += " or FIND_IN_SET ("+dc_id+", nl_subscription_cures_id) > 0 ";
		whereStr += " and active=1 ";

		Connection con = null;
		ResultSet rs = null;
		Statement stmt = null;
		ArrayList hmFinal = new ArrayList();
		try {
			String fileProperties = "whatsapi.properties";
			Properties prop = new WAPICommon().readPropertiesFile(fileProperties);
			System.out.println("DB_DBNAME : " + prop.getProperty("DB_DBNAME"));
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + prop.getProperty("DB_DBNAME")+"?useSSL=false",
					prop.getProperty("DB_USER"), prop.getProperty("DB_PASS"));
			// here allcures_schema is database name, next is username and password
			stmt = con.createStatement();
			String query = 
					"select user_id, nl_subscription_disease_id, nl_start_date, nl_sub_type, CONCAT(\"\",mobile), nl_subscription_cures_id, active, nl_end_date, country_code, count(*) as count from newsletter "
					+ " where " + whereStr 
					+ " group by country_code, mobile " //,nl_sub_type ,nl_subscription_disease_id, nl_subscription_cures_id "
					+ " order by country_code, mobile " ; //,nl_sub_type ,nl_subscription_disease_id, nl_subscription_cures_id";
			
			System.out.println("For article#"+article_id+", SQL for fetchDatabaseResultsForNewsletterByArticle "+query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				HashMap<String, Object> hmRow = new HashMap<String, Object>();
				hmRow.put("user_id", rs.getInt(1));
				hmRow.put("nl_subscription_disease_id", rs.getString(2));
				hmRow.put("nl_start_date", rs.getDate(3));
				hmRow.put("nl_sub_type", rs.getInt(4));
				hmRow.put("mobile", ""+rs.getString(5));
				hmRow.put("nl_subscription_cures_id", rs.getString(6));
				hmRow.put("active", rs.getInt(7));
				hmRow.put("nl_end_date", rs.getDate(8));
				hmRow.put("country_code", rs.getInt(9));
				hmRow.put("count", rs.getInt(10));
				hmFinal.add(hmRow);
			}
//			con.close();
		} catch (Exception e) {
			System.out.println(e);
			stmt.close();
			rs.close();
			con.close();
		}finally {
			try{if (null !=stmt) stmt.close();}catch(Exception ex) {System.out.println("exception in stmp close"+ex.getMessage());}
			try{if (null !=rs) rs.close();}catch(Exception ex) {System.out.println("exception in rs close"+ex.getMessage());}
			try{if (null !=con) con.close();}catch(Exception ex) {System.out.println("exception in con close"+ex.getMessage());}
		}
		return hmFinal;
	}
}
