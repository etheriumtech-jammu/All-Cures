package firstJDBC;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class Doctors {
	public static void main(String args[]) throws IOException {

		int position = 2;
		String str = "";
		

		String fileSeparator = System.getProperty("file.separator");
		System.out.println(fileSeparator);

		// absolute file name with path
		String absoluteFilePath = "C:" + fileSeparator + "JAVA" + fileSeparator + "Doctors.xml";
		File file = new File(absoluteFilePath);
		Path fileName = Path.of(absoluteFilePath);

		if (file.exists() == true) {
			System.out.println("File " + absoluteFilePath + " already exists");

			Files.delete(fileName);

		}
		if (file.createNewFile()) {
			System.out.println(absoluteFilePath + " File Created");
			String str1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n"
					+ "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\r\n" + "\r\n" + "</urlset>  \r\n"
					+ "";

			// Writing into the file
			Files.writeString(fileName, str1);

		}

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/all_cures", "root", "root@123");

			Statement stmt = con.createStatement();
			Statement stmt1 = con.createStatement();
			Statement stmt2=con.createStatement();
			
			ResultSet rs1 = stmt1.executeQuery("select count(*) from doctors;");
			if (rs1.next()) {
				System.out.println(rs1.getString(1));

			}
			ResultSet rs = stmt.executeQuery("Select rowno,docname_first,docname_last from doctors");
			ResultSet rs2=stmt2.executeQuery("Select article_id,title from article");
			// System.out.println(rs1.next());
			while (rs.next()) {

				List<String> lines = Files.readAllLines(fileName, StandardCharsets.UTF_8);

				str = rs.getString(1) + "-" + rs.getString(2) + "-" + rs.getString(3);

				String str2 = "<url><loc>https://all-cures.com/profile/" + str + "</loc></url>";

				lines.add(position, str2);
				System.out.println(str2);
				Files.write(fileName, lines, StandardCharsets.UTF_8);
				position++;

				// System.out.println(rs.getString(1)+" "+rs.getString(2));

			}
			
			while(rs2.next())
			{
				
				List<String> lines = Files.readAllLines(fileName, StandardCharsets.UTF_8);
				
				str = rs2.getString(1)+"-"+rs2.getString(2);
				System.out.println(str);
				
				String str2= "<url><loc>https://all-cures.com/cure/" + str + "</loc></url>" ;
				
				lines.add(position, str2);
				System.out.println(str2);  
				Files.write(fileName, lines, StandardCharsets.UTF_8);
				position++;
				
		//	System.out.println(rs.getString(1)+" "+rs.getString(2));  
						  	
			}
		
	}
		catch (Exception e) {

			e.printStackTrace();
		}
		
	//	System.out.println(rs.getString(1)+" "+rs.getString(2));  
					  	
		}


		


}

