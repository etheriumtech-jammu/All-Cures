package util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import com.mysql.jdbc.Driver;
import java.util.Properties;


public class SiteMapGenerator {
	public static void main(String args[]) throws IOException {
		FileReader reader=new FileReader("/home/etheriumtechnologies/Production/installers/gitJava/Java-All-Cures/cures/src/main/resources/whatsapi.properties");  
	      
		    Properties p=new Properties();  
		    p.load(reader);  
		      String user=p.getProperty("DB_USER");
			String pass=p.getProperty("DB_PASS");
			String db=p.getProperty("DB_DBNAME");
		   
		String str = "";

		String fileSeparator = System.getProperty("file.separator");
	//	System.out.println(fileSeparator);

		// absolute file name with path
		String absoluteFilePath = fileSeparator + "var" + fileSeparator + "www" + fileSeparator + "html" + fileSeparator
			+ "sitemap.xml";
		
		
		File file = new File(absoluteFilePath);
		// String fileName = file.getAbsolutePath();

		if (file.exists() == true) {
		//	System.out.println("File " + absoluteFilePath + " already exists");

		boolean b=	file.delete();
		//System.out.println(b);

		}
		if (file.createNewFile()) {
		//	System.out.println(absoluteFilePath + " File Created");
			String str1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n"
					+ "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\r\n" + "\r\n";

			try (FileWriter fw4 = new FileWriter(file)) {
				fw4.write(str1);
			}

		}

		try {
			Class.forName("com.mysql.jdbc.Driver");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + db, user, pass);

			Statement stmt = con.createStatement();
			Statement stmt2 = con.createStatement();

			Statement stmt3 = con.createStatement();
			Statement stmt4 = con.createStatement();


			// ResultSet rs = stmt.executeQuery("Select rowno,docname_first,docname_last from doctors");
			ResultSet rs = stmt.executeQuery("Select reg_doc_pat_id,author_firstname,author_lastname from author");

			ResultSet rs2 = stmt2.executeQuery("Select article_id,title from article");

			ResultSet rs3 = stmt3.executeQuery("select count(*) from author;");
			if (rs3.next()) {
				System.out.println("No. of Doctors" + rs3.getString(1));

			}
			
			ResultSet rs4 = stmt4.executeQuery("select count(*) from article;");
			if (rs4.next()) {
				System.out.println("No. of Articles" + rs4.getString(1));

			}
				while (rs2.next()) {

				str= rs2.getString(2);
				str = str.replaceAll(" ", "-").toLowerCase();
			str = str.replaceAll("&", "-").toLowerCase();
			//	System.out.println(str);
				
			str=rs2.getString(1)+ "-" + str;
				
				String str2= "<url><loc>https://all-cures.com/cure/" + str + "</loc></url>" ;
				
				try (FileWriter fw4 = new FileWriter(file, true)) {
					fw4.write(str2 + "\n");
				}

			}
			
			str="";
			while (rs.next()) {
				String str3=rs.getString(2);
				str3=str3.replace("Dr.", "").replace("Dr", "").replace(" ", "");
				if (str3.equals(""))				{
				str = rs.getString(1) + "-" + rs.getString(3);
				}
				else
				{ 
				str = rs.getString(1) + "-" + str3 + "-" + rs.getString(3);
				}
				
				 if (rs.getString(1) != null)
				 {
				String str2 = "<url><loc>https://all-cures.com/profile/" + str + "</loc></url>";
				str2=str2.replace(" ", "");
				try (FileWriter fw4 = new FileWriter(file, true)) {
					fw4.write(str2 + "\n");
				}	 
					 
				 }

				

			}
			
		
			
			
			try (FileWriter fw4 = new FileWriter(file, true)) {
				fw4.write("</urlset>");
			}
			
			finally {
				con.close();
				stmt.close();
				stmt2.close();
				rs.close();
				rs2.close();
			
				
			}

		}

		catch (Exception e) {

			e.printStackTrace();
		}
	

	}
}

//
