package dao;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.Article;
import util.ArticleUtils;
import util.Constant;
import util.EncodingDecodingUtil;
import util.HibernateUtil;

public class ContentDaoImpl {
	
	public static void main(String[]args){
	}
	
	
	public boolean createArticle(Integer pubStatus, Integer lang, Integer disclaimerId, Integer copyrightId, String authById, String title, String frndlyName, 
			String subhead, String contentTypeId, String keywords, String windowTitle, 
			String contentLocation, Integer reg_id, String articleContent, Integer diseaseConditionId, Integer countryId, String comments, Integer promoId, Integer promoStage
			,String type, Integer imedicineTypeId,String featured_article) {
			
		boolean artCrtStatus = false;		
		Constant.log("Saving Content in DB", 1);
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		session.beginTransaction();

		Article article = new Article();
		
		try {
			//session.getTransaction().begin();
			
			//articalstatus.setStatus_id();			
//			article.setArticle_id(articleId);
			//article.setLanguages(lang);
			//article.setArticlePubStatus(pubStatus);
			//article.setAuthor(author);
			//article.setCopyright(copy);
			//article.setDisclaimer(disclaimer);
			if(null != pubStatus) article.setPubstatus_id(pubStatus);
			if(null != lang) article.setLanguage_id(lang);
			if(null != disclaimerId) article.setDisclaimer_id(disclaimerId);
			if(null != copyrightId) article.setCopyright_id(copyrightId);
			if(null != title) article.setTitle(title);
			if(null != frndlyName) article.setFriendly_name(frndlyName);
			if(null != subhead) article.setSubheading(subhead);
			if(null != contentTypeId) article.setContent_type(contentTypeId);
			if(null != featured_article) article.setFeatured_article(featured_article);
			if(null != keywords) article.setKeywords(keywords);
			if(null != windowTitle) article.setWindow_title(windowTitle);
			if(null != imedicineTypeId && imedicineTypeId != -1 ) article.setMedicine_type(imedicineTypeId);
			//update content location after you know the id of the row that was just inserted
			//article.setContent_location(content_loc);
			article.setEdited_by(reg_id);
			if(null != authById) article.setAuthored_by(authById);
			if(null != countryId && countryId != -1) {
			article.setCountry_id(countryId);
			}
			if (null != promoId && promoId != -1) {
				article.setPromo_id(promoId);
				article.setPromo_stage(promoStage);
			}
			if(null != diseaseConditionId && diseaseConditionId != -1) article.setDisease_condition_id(diseaseConditionId);
			Constant.log("Saved Article Meta Data", 1);
			session.save(article);
			//Below will work assuming an author cannot pen 2 articles at the same time
			//Query regIdQ = session.createNativeQuery("select max(article_id) from article where edited_by = "+reg_id);
			//int articleId = regIdQ.getFirstResult();
			//article.setArticle_id(articleId);
			Constant.log("Saving Content in Filesystem with Article Id:"+article.getArticle_id(), 1);			
			artCrtStatus = ArticleUtils.updateArticleContent(contentLocation, articleContent, article.getArticle_id(), reg_id);
			article.setContent_location(ArticleUtils.getContentLocation(article.getArticle_id(), reg_id, true));
			int n = 750;
			String upToNCharacters = articleContent.substring(0, Math.min(articleContent.length(), n));
			String upToNCharacters_decoded = URLDecoder.decode(upToNCharacters.substring(0,upToNCharacters.lastIndexOf("%")), "UTF-8");
			String content500 = upToNCharacters_decoded;
			int lastInd = upToNCharacters_decoded.lastIndexOf("},");
			if (lastInd !=-1) {
				content500 = upToNCharacters_decoded.substring(0,lastInd)+"}]}";
			}else {
				if (upToNCharacters_decoded.startsWith("{") && !upToNCharacters_decoded.endsWith("}")) {
					content500 = upToNCharacters_decoded + "}";
				}
			}
			article.setContent(EncodingDecodingUtil.encodeURIComponent(content500));
			
			java.util.Date date=new java.util.Date();
			java.sql.Date sqlDate=new java.sql.Date(date.getTime());
			//java.sql.Timestamp sqlTime=new java.sql.Timestamp(date.getTime());
			article.setCreate_date(sqlDate);
			if(null != comments) article.setComments(comments);
			if(null != type) article.setType(type);
			session.save(article);
//			session.getTransaction().commit();
			session.getTransaction().commit();   //session.close();			
			artCrtStatus = true;
		}catch (Exception e) {
			e.printStackTrace();
			artCrtStatus = false;
			//session.getTransaction().commit(); //session.getTransaction().rollback();
			session.getTransaction().rollback();
		}		
		finally {
//			session.getTransaction().commit();   //session.close();
		}
		return artCrtStatus;
	}
	
	public static List<Article> dashboardDisplay(int reg_id, int authId, int state){
		List<Article> articleArr = new ArrayList<Article>();
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		//Session session = factory;
		// creating transaction object
		Transaction trans =(Transaction )session.beginTransaction();
		//String HQL= "from doctors  INNER JOIN FETCH hospital.hospital_affliated where.";
		Query query = null;
		String sQuery = "SELECT  article.article_id, article.title, article.friendly_name, article.pubstatus_id "+				
				 "FROM article where "+			 
				 "edited_by = "+reg_id;
		if(authId > 0){
			sQuery+= " and authored_by = "+authId;
		}
		if(state > 0){
			sQuery+= " and pubstatus_id = "+state;
		}
		query = session.createNativeQuery(sQuery + ";");
		List<Article> articleList = ( List<Article>) query.getResultList();
		Article  article = new Article();
		Iterator itr = articleList.iterator();
		while(itr.hasNext()){
			Object[] obj = (Object[]) itr.next();
			article.setArticle_id((Integer) obj[0]);
			article.setTitle((String) obj[1]);
			article.setFriendly_name((String) obj[2]);
			article.setPubstatus_id((Integer)obj[3]);			
			articleArr.add(article);
		}	
		session.getTransaction().commit();   //session.close();
		return articleArr;
	}
	
	public static Article findByArticleId(Integer articleid){
		Article article = new Article();
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		Transaction trans =(Transaction )session.beginTransaction();
		//String HQL= "from doctors  INNER JOIN FETCH hospital.hospital_affliated where.";
		Query query = session.createNativeQuery("SELECT  article.article_id, article.title, article.friendly_name, article.subheading, "+
"article.content_type, article.keywords, article.window_title, article.content_location "+
 "FROM article where "+ "article_id= "+articleid +";"
);
		List<Article> list= ( List<Article>) query.getResultList();
		Article articleList =new Article();
		Iterator itr = list.iterator();
		while(itr.hasNext()){
			Object[] obj = (Object[]) itr.next();

	
				articleList.setArticle_id((Integer) obj[0]);
				articleList.setTitle((String) obj[1]);
				articleList.setFriendly_name((String) obj[2]);
				articleList.setSubheading((String) obj[3]);
				articleList.setContent_type((String) obj[4]);
				articleList.setKeywords((String) obj[5]);
				articleList.setWindow_title((String) obj[6]); 
				articleList.setContent_location((String) obj[7]);
/*				
				articleList.setAuthor_firstname((String) obj[8] );
				articleList.setAuthor_middlename((String) obj[9]);
				articleList.setAuthor_lastname((String) obj[10]);
				articleList.setAuthor_email((String)obj[11]);
				articleList.setAuthor_address((String)obj[12]);
				articleList.setAuthor_telephone((String)obj[13]);
				articleList.setLang_name((String)obj[14]);
				articleList.setCopyright_file_loc((String)obj[15]);
				articleList.setDisclaimer_file_loc((String)obj[16]);
				articleList.setStatus_discription((String)obj[17]);
*/
			}
//		session.getTransaction().commit();   //session.close();
		return articleList;
		}	
	
	public static List<Article> findByArticleTypeAndDC(Integer DC_ID){
			Session session = HibernateUtil.buildSessionFactory();

			// creating session object
			//Session session = factory;

			// creating transaction object
//			Transaction trans =(Transaction )session.beginTransaction();
			//String HQL= "from doctors  INNER JOIN FETCH hospital.hospital_affliated where.";
			Query query = session.createNativeQuery("SELECT  article.article_id, article.title, article.friendly_name, article.subheading, "+
	" article.content_type, article.keywords, article.window_title, article.content_location, article.type "+
	 " FROM article where  article.disease_condition_id = " + DC_ID + " and FIND_IN_SET(1, REPLACE(\r\n"
	 		+ "                        REPLACE(\r\n"
	 		+ "                          REPLACE(article.type, '\"', ''), \r\n"
	 		+ "                        '[', ''), \r\n"
	 		+ "                      ']','')) > 0 ;" );
			
			List<Article> articleArr = new ArrayList<Article>();
			List<Article> articleList= ( List<Article>) query.getResultList();
			Article  article = new Article();
			Iterator itr = articleList.iterator();
			while(itr.hasNext()){
				Object[] obj = (Object[]) itr.next();
				article.setArticle_id((Integer) obj[0]);
				article.setTitle((String) obj[1]);
				article.setFriendly_name((String) obj[2]);
				article.setSubheading((String) obj[3]);
				article.setContent_type((String) obj[4]);
				article.setKeywords((String)obj[5]);			
				article.setWindow_title((String)obj[6]);			
				article.setContent_location((String)obj[7]);			
				article.setType((String)obj[8]);			
				articleArr.add(article);
			}	
//			session.getTransaction().commit();   //session.close();
			return articleArr;
			}	
	
	public static boolean updateArticleMeta(String title, String f_name, String subhead, String content_type, String keyword, 
			String window_title, int articleId, int regId){
		
		boolean updateStatus = false;
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		//Session session = factory;
		// creating transaction object
		Transaction trans =(Transaction )session.beginTransaction();

		Query query6 = session.createNativeQuery("update article set title = '"+title +"',  friendly_name = '"+f_name+"', subheading = '"+subhead+"' , content_type = '"+content_type+"' ,  keywords = '"+keyword+"', window_title = '"+window_title+"' where article_id = "+articleId+";");
		int articleUpdate = query6.executeUpdate();
		if(articleUpdate > 1){
			Constant.log("Article Update Success:"+articleUpdate, 1);
			updateStatus = true;
		}
		session.getTransaction().commit();
//		session.getTransaction().commit();   //session.close();
		return updateStatus;
	}
	
	public boolean updateArticle(String Status,String lan,String D_loc, String c_loc, String first_name, String middle_name,
			String last_name, String Email,String address ,
			String no, String title, String f_name, String subhead, String content_type, String keyword, String window_title, 
			String content_loc, String articleContent,int articleId, int regId){
		
		boolean value= false;
		try{
		//Update the Content First
		value = ArticleUtils.updateArticleContent(content_loc, articleContent, articleId, regId);		
		value = true;
		}catch (Exception e) {
			e.printStackTrace();
			value = false;
		}
		return value;		
	}

	public boolean updateArticleContent(String c_loc, String articleContent, int articleId, int regId){		
		boolean value = false;
		//Update the Content First
		try{
			value = ArticleUtils.updateArticleContent(c_loc, articleContent, articleId, regId);		
			value= true;
		}catch (Exception e) {
			e.printStackTrace();
			value= false;
		}		
		return value;		
	}
}

	

