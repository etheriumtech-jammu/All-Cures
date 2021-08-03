package util;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.Article;
import model.ArticlePubStatus;
import model.Author;
import model.Copyright;
import model.Disclaimer;
import model.Doctor;
import model.Doctors;
import model.Languages;
import model.Registration;
import net.bytebuddy.asm.Advice.Return;
import net.bytebuddy.asm.Advice.This;
import util.Constant;
import util.HibernateUtil;
import util.SolrUtil;


public class ArticleUtils {
	
	public static String getContentLocation(int artId, int userId, boolean returnFileName){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
        LocalDateTime now = LocalDateTime.now();
		String contentDir="test/"+userId+"/"+dtf.format(now);
		String fileName = "/article_"+artId+".json";
		if(returnFileName)
			return contentDir + fileName;
		else
			return contentDir;
	}
	
	public static String getContentLocation(Article article, Registration user, boolean returnFileName){
		String contentLoc = getContentLocation(article.getArticle_id(), user.getRegistration_id(), returnFileName);
		return contentLoc;
	}	
	
	public static boolean updateArticleContent(String artLoc, String articleContent, int articleId, int userId){
		boolean updateStatus = false;
		try{
			Constant.log("Writing Article to Filesystem with articleId:"+articleId, 1);			
//Will Wipe Out Other Files Written on the same day; Need to put ArticleId and userId in the Path
			File artFile = null;
			File artDir = null;
			if(artLoc == null){
				//New File Usecase
				Constant.log("Since art loc does not exist, creating a new file", 1);
				String file = getContentLocation(articleId, userId, false);
				//file = file.replace("\\", "/");//.replace("/", "/");

				artDir = new File(file);
				boolean isCreated = artDir.mkdirs();
				file = getContentLocation(articleId, userId, true);
				//file = file.replace("\\", "/");//.replace("/", "/");

				artFile = new File(file);
				System.out.println("111111111>>>>"+artFile);
				artFile.createNewFile();
				Constant.log("Created Article File to Filesystem", 1);
			}else{
				//Editing an existing File
				//artLoc = artLoc.replace("\\", "/");//.replace("/", "/");
				System.out.println("22222222>>>>>>>>"+artLoc);
				artFile = new File(artLoc);
			}
			FileWriter myWriter = new FileWriter(artFile);
			Constant.log(artFile.toString(),1);
			Constant.log("Writing Article Content to Filesystem", 1);
			System.out.println("_______________________");
			System.out.println(articleContent);
			myWriter.write(articleContent);
			myWriter.flush();
			Constant.log("Wrote Article Content to Filesystem", 1);
			myWriter.close();
			updateStatus = true;
		}catch (Exception e) {
			updateStatus = false;
			e.printStackTrace();
		}
		return updateStatus;
		
	}
	

}
