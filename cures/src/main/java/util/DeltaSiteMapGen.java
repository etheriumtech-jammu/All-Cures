package util;

import java.io.File;
import org.w3c.dom.Node;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.w3c.dom.Text;

public class DeltaSiteMapGen {

	public static void main(String args[]) throws IOException, ParserConfigurationException, SAXException {
		FileReader file_reader = new FileReader(
				"/home/etheriumtechnologies/Production/installers/gitJava/Java-All-Cures/cures/src/main/resources/whatsapi.properties");

		Properties p = new Properties();
		p.load(file_reader);
		String user = p.getProperty("DB_USER");
		String pass = p.getProperty("DB_PASS");
		String db = p.getProperty("DB_DBNAME");
		
		String fileSeparator = System.getProperty("file.separator");
		String absoluteFilePath = fileSeparator + "var" + fileSeparator + "www" + fileSeparator + "html" + fileSeparator +  "sitemap.xml";
		String deltaFilePath = fileSeparator + "var" + fileSeparator + "www" + fileSeparator + "html" + fileSeparator +  "delta_sitemap.xml";
		Connection con = null;
		Statement stmt = null;
		Statement stmt2 = null;

		ResultSet rs2_cure = null;
		ResultSet rs_doc = null;
		String str_add = null;
		File file = new File(absoluteFilePath);
		File file_delta = new File(deltaFilePath);
		
		Path basepath = Path.of(absoluteFilePath);
		Path deltapath = Path.of(deltaFilePath);
		Scanner scanner = new Scanner(System.in);
		System.out.println("Press 1 to create new SiteMap File\nPress 2 to Generate complete SiteMap\nPress 3 for delta approach");
		int val = scanner.nextInt();
		
		scanner.close();
		switch (val) {
		
		case 1:
		
			try {
				file.createNewFile();
				System.out.println("New Base SiteMap Created");
				file_delta.createNewFile();
				System.out.println("New Delta SiteMap Created");
				
				String str1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n"
						+ "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\r\n" + "</urlset>";
				// Writing into the file
				 Files.writeString(basepath, str1);
				 Files.writeString(deltapath, str1);
				}catch (IOException e) {

					e.printStackTrace();
				}
		// End of Case 1
		
		
		case 2:
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
//		Document sitemap = (Document) builder.newDocument();
			Document baseSitemap = builder.parse(file);
			
			try {
	
				Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + db, user, pass);
		
			stmt = con.createStatement();
			stmt2 = con.createStatement();

			rs_doc = stmt
					.executeQuery("Select rowno,docname_first,docname_last from doctors ");
			System.out.println(rs_doc.getFetchSize());
			while (rs_doc.next()) {
				String doc_fname = rs_doc.getString(2);
				doc_fname = doc_fname.replace("Dr.", "").replace("Dr", "").replace(" ", "");
				if (doc_fname.equals("")) {
					str_add = rs_doc.getString(1) + "-" + rs_doc.getString(3);
				} else {
					str_add = rs_doc.getString(1) + "-" + doc_fname + "-" + rs_doc.getString(3);
				}

				String final_string = "https://all-cures.com/profile/" + str_add + "";
				System.out.println(final_string);
				updateSitemap(baseSitemap, final_string, "add");
				
			}
			str_add = "";
			
			rs2_cure = stmt2.executeQuery("Select article_id,title from article ");
			System.out.println(rs2_cure.getFetchSize());
			while (rs2_cure.next()) {
				String title = rs2_cure.getString(2);
				title = title.replaceAll(" ", "-").toLowerCase();
				title = title.replaceAll("&", "-").toLowerCase();
				// System.out.println(str);
				System.out.println();
				str_add = rs2_cure.getString(1) + "-" + title;
				
				String final_string = "https://all-cures.com/cure/" + str_add + "";
				System.out.println(final_string);
				updateSitemap(baseSitemap, final_string, "add");
			
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(baseSitemap);
			StreamResult streamResult1 = new StreamResult(file);
			transformer.transform(domSource, streamResult1);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
			break;
			//End of Case 2
		case 3:
			DocumentBuilderFactory factory1 = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder1 = factory1.newDocumentBuilder();
//		Document sitemap = (Document) builder.newDocument();
			Document baseSitemap1 = builder1.parse(file);
			Document deltaSitemap1 = builder1.parse(file_delta);
			NodeList urlList = deltaSitemap1.getElementsByTagName("url");
			// Element elem=(Element) deltaSitemap.getElementsByTagName("urlset");
			// Remove the previous URL from the delta sitemap
			System.out.println(urlList.getLength());
			int urlListLength = urlList.getLength();
			
			for (int i = 0; i < urlListLength; i++) {
			    Element urlElement = (Element) urlList.item(0); // Get the element at the current index 'i'
			    if (urlElement != null) {
			        urlElement.getParentNode().removeChild(urlElement);
			        System.out.println("URL deleted");
			    }
			}
			
			try {
				Path path = file.toPath();
				BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
				FileTime lastModifiedTime = attributes.lastModifiedTime();

				
				// Convert to LocalDateTime
				Instant instant = lastModifiedTime.toInstant();
				LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			
				// Format the LocalDateTime as a string
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String formattedDateTime = dateTime.format(formatter);

				System.out.println("Last Modified Time: " + formattedDateTime);
		
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + db, user, pass);
				stmt = con.createStatement();
				stmt2 = con.createStatement();

				rs_doc = stmt
						.executeQuery("Select rowno,docname_first,docname_last from doctors where create_date > '"
								+ formattedDateTime + "'");
				
				System.out.println(rs_doc.getFetchSize());
				while (rs_doc.next()) {
					String doc_fname = rs_doc.getString(2);
					doc_fname = doc_fname.replace("Dr.", "").replace("Dr", "").replace(" ", "");
					if (doc_fname.equals("")) {
						str_add = rs_doc.getString(1) + "-" + rs_doc.getString(3);
					} else {
						str_add = rs_doc.getString(1) + "-" + doc_fname + "-" + rs_doc.getString(3);
					}

					String final_string = "https://all-cures.com/profile/" + str_add + "";
					System.out.println(final_string);
					updateSitemap(baseSitemap1, final_string, "update");
					updateDeltaSitemap(deltaSitemap1, final_string);
				}
				str_add = "";

				rs2_cure = stmt2.executeQuery("Select article_id,title from article where create_time >  '"
						+ formattedDateTime + "'");
				System.out.println(rs2_cure.getFetchSize());
				while (rs2_cure.next()) {
					String title = rs2_cure.getString(2);
					title = title.replaceAll(" ", "-").toLowerCase();
					title = title.replaceAll("&", "-").toLowerCase();
					// System.out.println(str);
					System.out.println();
					str_add = rs2_cure.getString(1) + "-" + title;

					String final_string = "https://all-cures.com/cure/" + str_add + "";
					System.out.println(final_string);
					updateDeltaSitemap(deltaSitemap1, final_string);
					updateSitemap(baseSitemap1, final_string, "update");
					

				}
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Enable indentation
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "1"); // Set indentation level
	//			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes"); // Omit XML declaration

				DOMSource domSource = new DOMSource(baseSitemap1);
				DOMSource domSource1 = new DOMSource(deltaSitemap1);
				StreamResult streamResult = new StreamResult(file_delta);

				transformer.transform(domSource1, streamResult);
				StreamResult streamResult1 = new StreamResult(file);
				transformer.transform(domSource, streamResult1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;	
		}
		// End of Case 3
		
	}

	private static void updateSitemap(Document sitemap, String url, String action) {

		// Use the Document to create elements

		Element urlElement = sitemap.createElement("url");
		Element locElement = sitemap.createElement("loc");

		locElement.setTextContent(url);
		urlElement.appendChild(locElement);
		Text newline = sitemap.createTextNode("\n");

		if (action.equals("add") || action.equals("update")) {
			Element rootElement = sitemap.getDocumentElement();

			rootElement.appendChild(urlElement);
			rootElement.appendChild(newline);
		}
	}

	private static void updateDeltaSitemap(Document deltaSitemap, String newUrl) {

		// Add the new URL to the delta sitemap
		Element urlElement = deltaSitemap.createElement("url");
		Element locElement = deltaSitemap.createElement("loc");
		 removeEmptyTextNodes(deltaSitemap);
		locElement.setTextContent(newUrl);
		urlElement.appendChild(locElement);
		Text newline = deltaSitemap.createTextNode("\n");

		deltaSitemap.getDocumentElement().appendChild(urlElement);
		deltaSitemap.getDocumentElement().appendChild(newline);
		
	}

	private static void removeEmptyTextNodes(Node node) {
	    NodeList childNodes = node.getChildNodes();
	    for (int i = childNodes.getLength() - 1; i >= 0; i--) {
	        Node childNode = childNodes.item(i);
	        if (childNode.getNodeType() == Node.TEXT_NODE && childNode.getTextContent().trim().isEmpty()) {
	            node.removeChild(childNode);
	        } else if (childNode.getNodeType() == Node.ELEMENT_NODE) {
	            removeEmptyTextNodes(childNode);
	        }
	    }
	}

	
}
