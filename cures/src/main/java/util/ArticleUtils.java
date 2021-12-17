package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

import model.Article;
import model.Registration;

public class ArticleUtils {

	private static String OS = System.getProperty("os.name").toLowerCase();

	public static String getContentLocation(int artId, int userId, boolean returnFileName) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime now = LocalDateTime.now();
		String OS = System.getProperty("os.name").toLowerCase();
		System.out.println("os.name: " + OS);

		String curesProperties = "cures.properties";
		Properties prop = null;
		try {
			prop = new ArticleUtils().readPropertiesFile(curesProperties);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("ARTICLES_UPLOAD_DIR : " + prop.getProperty("ARTICLES_UPLOAD_DIR"));
		String cures_articleimages = prop.getProperty("cures_articleimages");
		String path = System.getProperty( "catalina.base" ) + "/webapps/"+cures_articleimages;

		System.out.println(path);
		String art_upload_dir = path;

		String contentDir = art_upload_dir + "/" + userId + "/" + dtf.format(now);
		if (isWindows()) {
			System.out.println("This is Windows");
			contentDir = art_upload_dir + "/" + userId + "/" + dtf.format(now);
		}
		String fileName = "/article_" + artId + ".json";
		if (returnFileName)
			return contentDir + fileName;
		else
			return contentDir;
	}

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}

	public static boolean isSolaris() {
		return (OS.indexOf("sunos") >= 0);
	}

	public static String getContentLocation(Article article, Registration user, boolean returnFileName) {
		String contentLoc = getContentLocation(article.getArticle_id(), user.getRegistration_id(), returnFileName);
		return contentLoc;
	}

	public static boolean updateArticleContent(String artLoc, String articleContent, int articleId, int userId) {
		boolean updateStatus = false;
		try {
			Constant.log("Writing Article to Filesystem with articleId:" + articleId, 1);
//Will Wipe Out Other Files Written on the same day; Need to put ArticleId and userId in the Path
			File artFile = null;
			File artDir = null;
			if (artLoc == null) {
				// New File Usecase
				Constant.log("Since art loc does not exist, creating a new file", 1);
				String file = getContentLocation(articleId, userId, false);
				// file = file.replace("\\", "/");//.replace("/", "/");

				artDir = new File(file);
				boolean isCreated = artDir.mkdirs();
				file = getContentLocation(articleId, userId, true);
				// file = file.replace("\\", "/");//.replace("/", "/");

				artFile = new File(file);
				System.out.println("111111111>>>>" + artFile);
				artFile.createNewFile();
				Constant.log("Created Article File to Filesystem", 1);
			} else {
				// Editing an existing File
				// artLoc = artLoc.replace("\\", "/");//.replace("/", "/");
				System.out.println("22222222>>>>>>>>" + artLoc);
				artFile = new File(artLoc);
			}
			FileWriter myWriter = new FileWriter(artFile);
			Constant.log(artFile.toString(), 1);
			Constant.log("Writing Article Content to Filesystem", 1);
			System.out.println("_______________________");
			// System.out.println(articleContent);
//			EncodingUtil encodeUtil = new EncodingUtil();
//			System.out.println(encodeUtil.encodeURIComponent(articleContent));
//			System.out.println(encodeUtil.decodeURIComponent(articleContent));
//			Decoder decoder = Base64.getDecoder();
//			byte[] bytes = decoder.decode(articleContent);
//					
//			System.out.println(new String(bytes));
			System.out.println("<<<------###################--------------------->>>>>");
			System.out.println(articleContent);
			myWriter.write(articleContent);
			myWriter.flush();
			Constant.log("Wrote Article Content to Filesystem", 1);
			myWriter.close();
			updateStatus = true;
		} catch (Exception e) {
			updateStatus = false;
			e.printStackTrace();
		}
		return updateStatus;

	}

	public Properties readPropertiesFile(String fileName) throws IOException {
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

}
