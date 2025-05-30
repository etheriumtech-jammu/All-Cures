package dao;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.StoredProcedureQuery;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.commons.io.IOUtils;
import org.hibernate.query.NativeQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Article;
import model.Article_dc_name;
import model.EmailDTO;
import model.Registration;
import service.SendEmailService;
import util.ArticleUtils;
import util.Constant;
import util.HibernateUtil;
import util.WhatsAPITemplateMessage;
import Chat_Function.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
//1	active
//7	WorkInProgress
//@Component makes sure it is picked up by the ComponentScan (if it is in the right package). This allows @Autowired to work in other classes for instances of this class
@Component
public class ArticleDaoImpl {

	@Autowired
	private SendEmailService emailUtil;

	private static ArrayList list = new ArrayList();

 	static	ContentDaoImpl contentDao = new ContentDaoImpl();

	public static ArrayList<Article> findPublishedArticle(Registration user) {

//		HibernateUtil hu = new HibernateUtil();
//		Session session = hu.getSession();
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		/*
		 * HibernateUtil hu = new HibernateUtil(); Session session = hu.getSession();
		 */
		// creating transaction object
//		session.beginTransaction();
		String conditionMatch = "";
		// Admin user
		if (user.getRegistration_type() == 9)
			conditionMatch = "  ";
		// Reviewer
		if (user.getRegistration_type() == 7)
			conditionMatch = " and  published_by = " + user.getRegistration_id();
		// Editorial
		if (user.getRegistration_type() == 4)
			conditionMatch = " and 1=-1 ";
		// Author
		if (user.getRegistration_type() == 3)
			conditionMatch = " and 1=-1 ";
		// patient or doctor
		if (user.getRegistration_type() == 2 || user.getRegistration_type() == 1)
			conditionMatch = " and 1=-1 ";

		// conditionMatch = " and ( authored_by =" + user.getRegistration_id() + " or
		// edited_by = " + user.getRegistration_id() +" ) ";

		Query query = session
				.createNativeQuery("select  article_id  from article  where pubstatus_id = 3 " + conditionMatch + " ;");
		ArrayList<Article> list = (ArrayList<Article>) query.getResultList();
//		System.out.println("result list article@@@@@@@@@@@@@" + list.size());

//		session.getTransaction().commit();   
		//session.close();;
		return list;
	}

	public static ArrayList<Article> findDraftAricle(Registration user) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();

		String conditionMatch = "";
		// Admin user
		if (user.getRegistration_type() == 9)
			conditionMatch = "  ";
		// Reviewer
		if (user.getRegistration_type() == 7)
			conditionMatch = " and ( authored_by =" + user.getRegistration_id() + " or  edited_by = "
					+ user.getRegistration_id() + " ) ";
		// Editorial
		if (user.getRegistration_type() == 4)
			conditionMatch = " and edited_by = " + user.getRegistration_id() + "  ";
		// Author
		if (user.getRegistration_type() == 3)
			conditionMatch = " and ( authored_by =" + user.getRegistration_id() + " or  edited_by = "
					+ user.getRegistration_id() + " ) ";
		// paitent or doctor
		if (user.getRegistration_type() == 2 || user.getRegistration_type() == 1)
			conditionMatch = " and ( authored_by =" + user.getRegistration_id() + " or  edited_by = "
					+ user.getRegistration_id() + " ) ";

		Query query = session
				.createNativeQuery("select  article_id  from article where pubstatus_id = 1 " + conditionMatch + " ;");
		ArrayList<Article> list = (ArrayList<Article>) query.getResultList();
//		System.out.println("result list article@@@@@@@@@@@@@" + list.size());
//		session.getTransaction().commit();   
		//session.close();
		return list;
	}

	public static ArrayList<Article> findReviwArticle(Registration user) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		// creating transaction object
//		session.beginTransaction();

		String conditionMatch = "";
		// Admin user
		if (user.getRegistration_type() == 9)
			conditionMatch = "  ";
		// Reviewer
		if (user.getRegistration_type() == 7)
			conditionMatch = " and  published_by = " + user.getRegistration_id() + " ";
		// Editorial
		if (user.getRegistration_type() == 4)
			conditionMatch = " and 1=-1  ";
		// Author
		if (user.getRegistration_type() == 3)
			conditionMatch = " and 1=-1 ";
		// paitent or doctor
		if (user.getRegistration_type() == 2 || user.getRegistration_type() == 1)
			conditionMatch = " and 1=-1 ";

		Query query = session
				.createNativeQuery("select  article_id  from article  where pubstatus_id = 2 " + conditionMatch + " ;");
		ArrayList<Article> list = (ArrayList<Article>) query.getResultList();
//		System.out.println("result list article@@@@@@@@@@@@@" + list.size());
//		session.getTransaction().commit();   //session.close();
		return list;
	}

	public static ArrayList<Article> findApprovalArticle(Registration user) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();

		String conditionMatch = "";
		// Admin user
		if (user.getRegistration_type() == 9)
			conditionMatch = "  ";
		// Reviewer
		if (user.getRegistration_type() == 7)
			conditionMatch = " and 1=-1 ";
		// Editorial
		if (user.getRegistration_type() == 4)
			conditionMatch = " and  edited_by = " + user.getRegistration_id() + " ";
		// Author
		if (user.getRegistration_type() == 3)
			conditionMatch = " and ( authored_by =" + user.getRegistration_id() + " or  edited_by = "
					+ user.getRegistration_id() + " ) ";
		// patient or doctor
		if (user.getRegistration_type() == 2 || user.getRegistration_type() == 1)
			conditionMatch = " and ( authored_by =" + user.getRegistration_id() + " or  edited_by = "
					+ user.getRegistration_id() + " ) ";

		Query query = session
				.createNativeQuery("select  article_id  from article  where pubstatus_id = 2 " + conditionMatch + " ;");
		// needs other condition too but unable to find correct column
		ArrayList<Article> list = (ArrayList<Article>) query.getResultList();
//		System.out.println("result list article@@@@@@@@@@@@@" + list.size());
//		session.getTransaction().commit();   //session.close();

		return list;
	}

	public Article_dc_name getArticleDetails(int article_id) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();

		Query query = session.createNativeQuery(" SELECT `article`.`article_id`,\r\n" + "    `article`.`title`,\r\n"
				+ "    `article`.`friendly_name`,\r\n" + "    `article`.`subheading`,\r\n"
				+ "    `article`.`content_type`,\r\n" + "    `article`.`keywords`,\r\n"
				+ "    `article`.`window_title`,\r\n" + "    `article`.`content_location`,\r\n"
				+ "    `article`.`authored_by`,\r\n" + "    `article`.`published_by`,\r\n"
				+ "    `article`.`edited_by`,\r\n" + "    `article`.`copyright_id`,\r\n"
				+ "    `article`.`disclaimer_id`,\r\n" + "    `article`.`create_date`,\r\n"
				+ "    `article`.`published_date`,\r\n" + "    `article`.`pubstatus_id`,\r\n"
				+ "    `article`.`language_id`,\r\n" + "    `article`.`content`,\r\n"
				+ "    `article`.`country_id`,\r\n" + "    `article`.`disease_condition_id`,\r\n"
				+ "    `article`.`type`,\r\n" + "    `dc`.`dc_name`,\r\n" + "    `article`.`comments`,\r\n"
				+ "    `article`.`over_allrating`,\r\n"
				
				+ " (select group_concat(a.author_firstname,\" \",a.author_lastname) from author a \r\n"
				
				+ " where a.author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))  \r\n"
				+ " ) as authors_name ,"
				
				+ " (select reg_type from author where author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))  "
				+ "		 ) as reg_type, "
		        + "         (select reg_doc_pat_id from author where author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))  "
		        + "		 ) as reg_doc_pat_id ,"
		    + "    `article`.`medicine_type`,\r\n"
					+ "    (\r\n"
					+ "        SELECT m.name\r\n"
					+ "        FROM medicinetype m\r\n"
					+ "        WHERE m.id = `article`.`medicine_type`\r\n"
					+ "    ) AS medicine_type_name,\r\n"
					+ "    (\r\n"
					+ "        SELECT p.name\r\n"
					+ "        FROM medicinetype m\r\n"
					+ "        LEFT JOIN medicinetype p ON m.parent_med_type = p.id\r\n"
					+ "        WHERE m.id = `article`.`medicine_type`\r\n"
					+ "    ) AS parent_medicine_type,\r\n"
					+ "    `article`.`featured_article`,\r\n"
					+ "    `article`.`introduction`,\r\n"
					+ "    `article`.`description`\r\n"
				+ " FROM `article`\r\n"
				+ " left join disease_condition dc on dc.dc_id = `article`.`disease_condition_id` \r\n"
				+ " where article_id =  " + article_id + ";");
		ArrayList<Article> articleList = (ArrayList<Article>) query.getResultList();
		Article_dc_name article = new Article_dc_name();
		Iterator itr = articleList.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
	//		System.out.println((Integer) obj[0]);
	//		System.out.println((String) obj[1]);
			article.setArticle_id((Integer) obj[0]);
			article.setTitle((String) obj[1]);
			article.setFriendly_name((String) obj[2]);
			article.setSubheading((String) obj[3]);
			article.setContent_type((String) obj[4]);
			article.setKeywords((String) obj[5]);
			article.setWindow_title((String) obj[6]);
			article.setContent_location((String) obj[7]);
	//		System.out.println((String) obj[7]);
			String file = (String) obj[7];
			// file = "/home/administrator/uat/"+
			// file = file.replace("\\", "/");//.replace("/", "/");
	//		System.out.println("FILENAME===>>>>>>>>>>" + file);
			// String file = "C:\\" + (String) obj[7];
//			file = "C:\\test\\14\\2021\\05\\26\\article_"+(Integer) obj[0]+".json";
			String contents = "";
			InputStream is = null;
			DataInputStream dis = null;
			try {
				// create file input stream
				is = new FileInputStream(file);

				// create new data input stream
				dis = new DataInputStream(is);

				// available stream to be read
				int length = dis.available();

				// create buffer
				byte[] buf = new byte[length];

				// read the full data into the buffer
				dis.readFully(buf);

				// for each byte in the buffer
				for (byte b : buf) {

					// convert byte to char
					char c = (char) b;

					// prints character
		//			System.out.print(c);
					contents = contents + c;
				}

			} catch (Exception e) {
				// if any error occurs
				e.printStackTrace();
			} finally {
				// releases all system resources from the streams
				if (is != null)
					try {
						is.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				if (dis != null)
					try {
						dis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
	//		System.out.println(contents);

			article.setAuthored_by((String) obj[8]);
			article.setPublished_by((Integer) obj[9]);
			article.setEdited_by((Integer) obj[10]);
			article.setCopyright_id((Integer) obj[11]);
			article.setDisclaimer_id((Integer) obj[12]);
			article.setCreate_date((Date) obj[13]);
			article.setPublished_date((Date) obj[14]);
			article.setPubstatus_id((Integer) obj[15]);
			article.setLanguage_id((Integer) obj[16]);
			article.setContent_small((String) obj[17]);
			article.setCountry_id((Integer) obj[18]);
			article.setDisease_condition_id((Integer) obj[19]);
			article.setType((String) obj[20]);
			article.setContent(contents);
			article.setDc_name((String) obj[21]);
			article.setComments((String) obj[22]);
			article.setOver_allrating((Float) obj[23]);
			article.setAuthors_name((String) obj[24]);
			article.setReg_type(""+(Integer) obj[25]);
			article.setReg_doc_pat_id(""+(Integer) obj[26]);
			article.setMedicine_type((Integer) obj[27]);
			article.setMedicine_type_name((String) obj[28]);
			article.setParent_Medicine_type((String) obj[29]);
			article.setFeatured_article((String) obj[30]);
			article.setIntroduction((String) obj[31]);
			article.setDescription((String) obj[32]);
		}
//		session.getTransaction().commit();   
		//session.close();

		return article;
	}
	
	public Article_dc_name getArticleDetails(String article_title) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();

		Query query = session.createNativeQuery(" SELECT `article`.`article_id`,\r\n" + "    `article`.`title`,\r\n"
				+ "    `article`.`friendly_name`,\r\n" + "    `article`.`subheading`,\r\n"
				+ "    `article`.`content_type`,\r\n" + "    `article`.`keywords`,\r\n"
				+ "    `article`.`window_title`,\r\n" + "    `article`.`content_location`,\r\n"
				+ "    `article`.`authored_by`,\r\n" + "    `article`.`published_by`,\r\n"
				+ "    `article`.`edited_by`,\r\n" + "    `article`.`copyright_id`,\r\n"
				+ "    `article`.`disclaimer_id`,\r\n" + "    `article`.`create_date`,\r\n"
				+ "    `article`.`published_date`,\r\n" + "    `article`.`pubstatus_id`,\r\n"
				+ "    `article`.`language_id`,\r\n" + "    `article`.`content`,\r\n"
				+ "    `article`.`country_id`,\r\n" + "    `article`.`disease_condition_id`,\r\n"
				+ "    `article`.`type`,\r\n" + "    `dc`.`dc_name`,\r\n" + "    `article`.`comments`,\r\n"
				+ "    `article`.`over_allrating`,\r\n"
				
				+ " (select group_concat(a.author_firstname,\" \",a.author_lastname) from author a \r\n"
				+ " where a.author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))  \r\n"
				+ " ) as authors_name ,"
				
				+ " (select reg_type from author where author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))  "
				+ "		 ) as reg_type, "
		        + "         (select reg_doc_pat_id from author where author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))  "
		        + "		 ) as reg_doc_pat_id "
				
				+ " FROM `article`\r\n"
				+ " left join disease_condition dc on dc.dc_id = `article`.`disease_condition_id` \r\n"
				+ " where title =  '" + article_title + "' ;");
		ArrayList<Article> articleList = (ArrayList<Article>) query.getResultList();
		Article_dc_name article = new Article_dc_name();
		Iterator itr = articleList.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
	//		System.out.println((Integer) obj[0]);
	//		System.out.println((String) obj[1]);
			article.setArticle_id((Integer) obj[0]);
			article.setTitle((String) obj[1]);
			article.setFriendly_name((String) obj[2]);
			article.setSubheading((String) obj[3]);
			article.setContent_type((String) obj[4]);
			article.setKeywords((String) obj[5]);
			article.setWindow_title((String) obj[6]);
			article.setContent_location((String) obj[7]);
	//		System.out.println((String) obj[7]);
			String file = (String) obj[7];
			// file = "/home/administrator/uat/"+
			// file = file.replace("\\", "/");//.replace("/", "/");
	//		System.out.println("FILENAME===>>>>>>>>>>" + file);
			// String file = "C:\\" + (String) obj[7];
//			file = "C:\\test\\14\\2021\\05\\26\\article_"+(Integer) obj[0]+".json";
			String contents = "";
			InputStream is = null;
			DataInputStream dis = null;
			try {
				// create file input stream
				is = new FileInputStream(file);

				// create new data input stream
				dis = new DataInputStream(is);

				// available stream to be read
				int length = dis.available();

				// create buffer
				byte[] buf = new byte[length];

				// read the full data into the buffer
				dis.readFully(buf);

				// for each byte in the buffer
				for (byte b : buf) {

					// convert byte to char
					char c = (char) b;

					// prints character
		//			System.out.print(c);
					contents = contents + c;
				}

			} catch (Exception e) {
				// if any error occurs
				e.printStackTrace();
			} finally {
				// releases all system resources from the streams
				if (is != null)
					try {
						is.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				if (dis != null)
					try {
						dis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
	//		System.out.println(contents);

			article.setAuthored_by((String) obj[8]);
			article.setPublished_by((Integer) obj[9]);
			article.setEdited_by((Integer) obj[10]);
			article.setCopyright_id((Integer) obj[11]);
			article.setDisclaimer_id((Integer) obj[12]);
			article.setCreate_date((Date) obj[13]);
			article.setPublished_date((Date) obj[14]);
			article.setPubstatus_id((Integer) obj[15]);
			article.setLanguage_id((Integer) obj[16]);
			article.setContent_small((String) obj[17]);
			article.setCountry_id((Integer) obj[18]);
			article.setDisease_condition_id((Integer) obj[19]);
			article.setType((String) obj[20]);
			article.setContent(contents);
			article.setDc_name((String) obj[21]);
			article.setComments((String) obj[22]);
			article.setOver_allrating((Float) obj[23]);
			article.setAuthors_name((String) obj[24]);
			article.setReg_type(""+(Integer) obj[25]);
			article.setReg_doc_pat_id(""+(Integer) obj[26]);
		}
//		session.getTransaction().commit();   
		//session.close();

		return article;
	}

	public static ArrayList<Article> getArticlesListAll(Integer limit, Integer offset) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();

		String limit_str = "";
		if (null != limit)
			limit_str = " limit " + limit;
		String offset_str = "";
		if (null != offset)
			offset_str = " offset " + offset;

		Query query = session.createNativeQuery("SELECT `article`.`article_id`,\r\n" + "    `article`.`title`,\r\n"
				+ "    `article`.`friendly_name`,\r\n" + "    `article`.`subheading`,\r\n"
				+ "    `article`.`content_type`,\r\n" + "    `article`.`keywords`,\r\n"
				+ "    `article`.`window_title`,\r\n" + "    `article`.`content_location`,\r\n"
				+ "    `article`.`authored_by`,\r\n" + "    `article`.`published_by`,\r\n"
				+ "    `article`.`edited_by`,\r\n" + "    `article`.`copyright_id`,\r\n"
				+ "    `article`.`disclaimer_id`,\r\n" + "    `article`.`create_date`,\r\n"
				+ "    `article`.`published_date`,\r\n" + "    `article`.`pubstatus_id`,\r\n"
				+ "    `article`.`language_id`,\r\n" + "    `article`.`content`,\r\n" + "    `article`.`type`,\r\n"
				+ "    `article`.`comments`\r\n" + "FROM `article`"
				+ " order by `article`.`article_id` desc " + limit_str + offset_str + " ;\r\n" + ";");
		// needs other condition too but unable to find correct column
		ArrayList<Article> list = (ArrayList<Article>) query.getResultList();
//		System.out.println("result list article@@@@@@@@@@@@@" + list.size());
//		session.getTransaction().commit();   
		//session.close();

		return list;
	}

	public static List getArticlesListAllKeysbyAuthIdandregType(Integer reg_type, Integer reg_doc_pat_id, Integer limit, Integer offset) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();
		String limit_str = "";
		if (null != limit)
			limit_str = " limit " + limit;
		String offset_str = "";
		if (null != offset)
			offset_str = " offset " + offset;

		Query query = session.createNativeQuery("\r\n"
				+ "select \r\n"
				+ "ar.article_id,     ar.title,\r\n"
				+ "				   ar.friendly_name,     ar.subheading,\r\n"
				+ "				   ar.content_type,     ar.keywords,\r\n"
				+ "				   ar.window_title,     ar.content_location,\r\n"
				+ "				   ar.authored_by,     ar.published_by,\r\n"
				+ "				   ar.edited_by,     ar.copyright_id,\r\n"
				+ "				   ar.disclaimer_id,     ar.create_date,\r\n"
				+ "				   ar.published_date,     ar.pubstatus_id,\r\n"
				+ "				   ar.language_id,     ar.content,     dc.dc_name,\r\n"
				+ "				ar.comments,  ar.type, ar.country_id,\r\n"
				+ "				ar.over_allrating, \r\n"
				+ "\r\n"
				+ "au.reg_doc_pat_id, au.reg_type from article ar\r\n"
				+ " left join disease_condition dc on dc.dc_id = ar.disease_condition_id "
				+ " inner join author au\r\n"
				+ " on au.author_id in (trim(trailing ']' from trim(leading '[' from ar.authored_by))) \r\n"
				+ "and  au.reg_type="+reg_type+"\r\n"
				+ "where ar.pubstatus_id = 3 and au.reg_doc_pat_id = "+reg_doc_pat_id+" order by ar.article_id desc \r\n" + limit_str + offset_str + ";");
		// needs other condition too but unable to find correct column
		List<Object[]> results = (List<Object[]>) query.getResultList();
//		System.out.println("result list article@@@@@@@@@@@@@" + results.size());
//		session.getTransaction().commit();   //session.close();

		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			int article_id = (int) objects[0];
			String title = (String) objects[1];
			String friendly_name = (String) objects[2];
			String subheading = (String) objects[3];
			String content_type = (String) objects[4];
			String keywords = (String) objects[5];
			String window_title = (String) objects[6];
			String content_location = (String) objects[7];
			String authored_by = (String) objects[8];
			int published_by = objects[9] != null ? (int) objects[9] : 0;
			int edited_by = (int) objects[10];
			int copyright_id = (int) objects[11];
			int disclaimer_id = (int) objects[12];
			java.sql.Date create_date = (java.sql.Date) objects[13];
			java.sql.Date published_date = (java.sql.Date) objects[14];
			int pubstatus_id = (int) objects[15];
			int language_id = (int) objects[16];
			String content = (String) objects[17];
			String dc_name = (String) objects[18];
			String comments = (String) objects[19];
			String type = (String) objects[20];
			int country_id = objects[21] != null ? (int) objects[21] : 0;
			float over_allrating = (float) (objects[22] != null ? (Float) objects[22] : 0.0);
			int reg_doc_pat_id_ = (int) objects[23];
			int reg_type_ = (int) objects[24];


			hm.put("article_id", article_id);
			hm.put("title", title);
			hm.put("friendly_name", friendly_name);
			hm.put("subheading", subheading);
			hm.put("content_type", content_type);
			hm.put("keywords", keywords);
			hm.put("window_title", window_title);
			hm.put("content_location", content_location);
			hm.put("authored_by", authored_by);
			hm.put("published_by", published_by);
			hm.put("edited_by", edited_by);
			hm.put("copyright_id", copyright_id);
			hm.put("disclaimer_id", disclaimer_id);
			hm.put("create_date", create_date);
			hm.put("published_date", published_date);
			hm.put("pubstatus_id", pubstatus_id);
			hm.put("language_id", language_id);
			hm.put("content", content);
			hm.put("dc_name", dc_name);
			hm.put("comments", comments);
			hm.put("type", type);
			hm.put("country_id", country_id);
			hm.put("over_allrating", over_allrating);
			hm.put("reg_doc_pat_id", reg_doc_pat_id_);
			hm.put("reg_type", reg_type_);

			hmFinal.add(hm);
//			System.out.println(hm);
		}
//		session.getTransaction().commit();   
		//session.close();

		return hmFinal;
	}
	public static List getArticlesListAllKeys(Integer limit, Integer offset, String searchStr, String orderByStr) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();		
		// creating session object
		//Session session = factory;
		
		// creating transaction object
//		session.beginTransaction();
		String limit_str = "";
		if (null != limit)
			limit_str = " limit " + limit;
		String offset_str = "";
		if (null != offset)
			offset_str = " offset " + offset;
		String orderby_str = " order by `article`.`published_date` desc ";
		if (null != orderByStr) {
			String[] orderArr = orderByStr.split(":");
			orderby_str = " order by  `article`.`" + orderArr[0] +"` "+orderArr[1];
		}
		String search_str = "";
		if (null != searchStr) {
			search_str = " where ";
//			search_str = " where title like '%" + searchStr + "%'  or  article_id like '%"+searchStr+"%'";
			String[] searchArrColums = searchStr.split("~");
			for (String columsDetail : searchArrColums) {
				String[] columsDetailArr = columsDetail.split(":");
				search_str += columsDetailArr[0] +" like '%"+ columsDetailArr[1] + "%' AND ";
			}
			//replace last AND with blank
			search_str = search_str.substring(0,search_str.lastIndexOf("AND"));
		}
		
		Query query = session.createNativeQuery("SELECT `article`.`article_id`,\r\n" + "    `article`.`title`,\r\n"
				+ "    `article`.`friendly_name`,\r\n" + "    `article`.`subheading`,\r\n"
				+ "    `article`.`content_type`,\r\n" + "    `article`.`keywords`,\r\n"
				+ "    `article`.`window_title`,\r\n" + "    `article`.`content_location`,\r\n"
				+ "    `article`.`authored_by`,\r\n" + "    `article`.`published_by`,\r\n"
				+ "    `article`.`edited_by`,\r\n" + "    `article`.`copyright_id`,\r\n"
				+ "    `article`.`disclaimer_id`,\r\n" + "    `article`.`create_date`,\r\n"
				+ "    `article`.`published_date`,\r\n" + "    `article`.`pubstatus_id`,\r\n"
				+ "    `article`.`language_id`,\r\n" + "    `article`.`content`,\r\n" + "    `dc`.`dc_name`\r\n,"
				+ "	`article`.`comments`\r\n," + " `article`.`type`\r\n, `article`.`country_id`\r\n,  "
				+ " `article`.`over_allrating`, \r\n "
				
				+ " (select group_concat(a.author_firstname,\" \",a.author_lastname) from author a \r\n"
				+ " where a.author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))  \r\n"
				+ " ) as authors_name, "
				+ " (select count(*) from article) as count ,"
				+ " (select reg_doc_pat_id from author where author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))) as docID \r\n" 
				+ " , `article`.`medicine_type` \r\n"
				+ " FROM `article` \r\n"
				+ " left join disease_condition dc on dc.dc_id = `article`.`disease_condition_id` "
				+ "where `article`.`type` = '[2]'"
				+ " AND `article`.`pubstatus_id` = 3 \r\n"
				+  search_str + orderby_str
				+ limit_str + offset_str + " ;");
		// needs other condition too but unable to find correct column
		List<Object[]> results = (List<Object[]>) query.getResultList();
//		System.out.println("result list article@@@@@@@@@@@@@" + results.size());
//		session.getTransaction().commit();   //session.close();
		
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			int article_id = (int) objects[0];
			String title = (String) objects[1];
			String friendly_name = (String) objects[2];
			String subheading = (String) objects[3];
			String content_type = (String) objects[4];
			String keywords = (String) objects[5];
			String window_title = (String) objects[6];
			String content_location = (String) objects[7];
			String authored_by = (String) objects[8];
			int published_by = objects[9] != null ? (int) objects[9] : 0;
			int edited_by = (int) objects[10];
			int copyright_id = (int) objects[11];
			int disclaimer_id = (int) objects[12];
			java.sql.Date create_date = (java.sql.Date) objects[13];
			java.sql.Date published_date = (java.sql.Date) objects[14];
			int pubstatus_id = (int) objects[15];
			int language_id = (int) objects[16];
			String content = (String) objects[17];
			String dc_name = (String) objects[18];
			String comments = (String) objects[19];
			String type = (String) objects[20];
			int country_id = objects[21] != null ? (int) objects[21] : 0;
			float over_allrating = (float) (objects[22] != null ? (Float) objects[22] : 0.0);
			String authors_name = (String) objects[23];
			BigInteger count = (BigInteger) objects[24];
			int docID = objects[25] != null ? (int) objects[25] : 0;
			int medicine_type = objects[26] != null ? (int) objects[26] : 0;
			
			
			
			hm.put("article_id", article_id);
			hm.put("title", title);
			hm.put("friendly_name", friendly_name);
			hm.put("subheading", subheading);
			hm.put("content_type", content_type);
			hm.put("keywords", keywords);
			hm.put("window_title", window_title);
			hm.put("content_location", content_location);
			hm.put("authored_by", authored_by);
			hm.put("published_by", published_by);
			hm.put("edited_by", edited_by);
			hm.put("copyright_id", copyright_id);
			hm.put("disclaimer_id", disclaimer_id);
			hm.put("create_date", create_date);
			hm.put("published_date", published_date);
			hm.put("pubstatus_id", pubstatus_id);
			hm.put("language_id", language_id);
			hm.put("content", content);
			hm.put("dc_name", dc_name);
			hm.put("comments", comments);
			hm.put("type", type);
			hm.put("country_id", country_id);
			hm.put("over_allrating", over_allrating);
			hm.put("authors_name", authors_name);
			hm.put("count", count);
			hm.put("docID", docID);
			hm.put("medicine_type", medicine_type);
			
			hmFinal.add(hm);
//			System.out.println(hm);
		}
//		session.getTransaction().commit();   
		//session.close();
		
		return hmFinal;
	}
	
	
public static List getArticlesListAllKeysFeatured(Integer limit, Integer offset, String searchStr, String orderByStr) {
		
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		
		// creating session object
		//Session session = factory;
		
		// creating transaction object
//		session.beginTransaction();
		String limit_str = "";
		if (null != limit)
			limit_str = " limit " + limit;
		String offset_str = "";
		if (null != offset)
			offset_str = " offset " + offset;
		String orderby_str = " order by `article`.`published_date` desc ";
		if (null != orderByStr) {
			String[] orderArr = orderByStr.split(":");
			orderby_str = " order by  `article`.`" + orderArr[0] +"` "+orderArr[1];
		}
		String search_str = "";
//		if (null != searchStr) {
////			search_str = " where ";
//		search_str = " where article_id in ("+searchStr.split(":")[1]+")";
//		}
		search_str = " WHERE featured_article IN ('[1]', '1')\r\n"
				+ "";
		Query query = session.createNativeQuery("SELECT `article`.`article_id`,\r\n" + "    `article`.`title`,\r\n"
				+ "    `article`.`friendly_name`,\r\n" + "    `article`.`subheading`,\r\n"
				+ "    `article`.`content_type`,\r\n" + "    `article`.`keywords`,\r\n"
				+ "    `article`.`window_title`,\r\n" + "    `article`.`content_location`,\r\n"
				+ "    `article`.`authored_by`,\r\n" + "    `article`.`published_by`,\r\n"
				+ "    `article`.`edited_by`,\r\n" + "    `article`.`copyright_id`,\r\n"
				+ "    `article`.`disclaimer_id`,\r\n" + "    `article`.`create_date`,\r\n"
				+ "    `article`.`published_date`,\r\n" + "    `article`.`pubstatus_id`,\r\n"
				+ "    `article`.`language_id`,\r\n" + "    `article`.`content`,\r\n" + "    `dc`.`dc_name`\r\n,"
				+ "	`article`.`comments`\r\n," + " `article`.`type`\r\n, `article`.`country_id`\r\n,  "
				+ " `article`.`over_allrating`, \r\n "
				
				+ " (select group_concat(a.author_firstname,\" \",a.author_lastname) from author a \r\n"
				+ " where a.author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))  \r\n"
				+ " ) as authors_name, "
				+ " (select count(*) from article) as count , "
				+ " (select reg_doc_pat_id from author where author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))) as docID  \r\n" 
				+ " , `article`.`medicine_type`, \r\n"
				+ "    (\r\n"
				+ "        SELECT m.name\r\n"
				+ "        FROM medicinetype m\r\n"
				+ "        WHERE m.id = `article`.`medicine_type`\r\n"
				+ "    ) AS medicine_type_name,  d.img_loc \r\n"
				
				+ " FROM `article` \r\n"
				+ " left join disease_condition dc on dc.dc_id = `article`.`disease_condition_id` "
				+ "left join Doctors_New d  \r\n"
			   	+ "on d.docid = (SELECT reg_doc_pat_id FROM author WHERE author_id = CAST(TRIM(TRAILING ']' FROM TRIM(LEADING '[' FROM article.authored_by)) AS UNSIGNED) LIMIT 1) \r\n"
	  			+  search_str + orderby_str
				+ limit_str + offset_str + " ;");
		// needs other condition too but unable to find correct column
		List<Object[]> results = (List<Object[]>) query.getResultList();
//		System.out.println("result list article@@@@@@@@@@@@@" + results.size());
//		session.getTransaction().commit();   //session.close();
		
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			int article_id = (int) objects[0];
			String title = (String) objects[1];
			String friendly_name = (String) objects[2];
			String subheading = (String) objects[3];
			String content_type = (String) objects[4];
			String keywords = (String) objects[5];
			String window_title = (String) objects[6];
			String content_location = (String) objects[7];
			String authored_by = (String) objects[8];
			int published_by = objects[9] != null ? (int) objects[9] : 0;
			int edited_by = (int) objects[10];
			int copyright_id = (int) objects[11];
			int disclaimer_id = (int) objects[12];
			java.sql.Date create_date = (java.sql.Date) objects[13];
			java.sql.Date published_date = (java.sql.Date) objects[14];
			int pubstatus_id = (int) objects[15];
			int language_id = (int) objects[16];
			String content = (String) objects[17];
			String dc_name = (String) objects[18];
			String comments = (String) objects[19];
			String type = (String) objects[20];
			int country_id = objects[21] != null ? (int) objects[21] : 0;
			float over_allrating = (float) (objects[22] != null ? (Float) objects[22] : 0.0);
			String authors_name = (String) objects[23];
			BigInteger count = (BigInteger) objects[24];
			int docID = objects[25] != null ? (int) objects[25] : 0;	
			int medicine_type = objects[26] != null ? (int) objects[26] : 0;
			String med_type_name = (String) objects[27];
			String image_location= (String) objects[28];
			hm.put("article_id", article_id);
			hm.put("title", title);
			hm.put("friendly_name", friendly_name);
			hm.put("subheading", subheading);
			hm.put("content_type", content_type);
			hm.put("keywords", keywords);
			hm.put("window_title", window_title);
			hm.put("content_location", content_location);
			hm.put("authored_by", authored_by);
			hm.put("published_by", published_by);
			hm.put("edited_by", edited_by);
			hm.put("copyright_id", copyright_id);
			hm.put("disclaimer_id", disclaimer_id);
			hm.put("create_date", create_date);
			hm.put("published_date", published_date);
			hm.put("pubstatus_id", pubstatus_id);
			hm.put("language_id", language_id);
			hm.put("content", content);
			hm.put("dc_name", dc_name);
			hm.put("comments", comments);
			hm.put("type", type);
			hm.put("country_id", country_id);
			hm.put("over_allrating", over_allrating);
			hm.put("authors_name", authors_name);
			hm.put("count", count);
			hm.put("docID", docID);
			hm.put("medicine_type", medicine_type);
			hm.put("med_type_name", med_type_name);
			hm.put("image_location", image_location);
			hmFinal.add(hm);
//			System.out.println(hm);
		}
//		session.getTransaction().commit(); 
		
		//session.close();
		
		return hmFinal;
	}

public static List getArticlesListAllKeysFavourite(Integer limit, Integer offset, String searchStr, String orderByStr) {
	
	// creating seession factory object
	Session session = HibernateUtil.buildSessionFactory();
	
	// creating session object
	//Session session = factory;
	
	// creating transaction object
//	session.beginTransaction();
	String limit_str = "";
	if (null != limit)
		limit_str = " limit " + limit;
	String offset_str = "";
	if (null != offset)
		offset_str = " offset " + offset;
	String orderby_str = " order by `article`.`published_date` desc ";
	if (null != orderByStr) {
		String[] orderArr = orderByStr.split(":");
		orderby_str = " order by  `article`.`" + orderArr[0] +"` "+orderArr[1];
	}
	String search_str = "";
//	if (null != searchStr) {
////		search_str = " where ";
//	search_str = " where article_id in ("+searchStr.split(":")[1]+")";
//	}
	
	search_str = " where favourite_article ='[1]'";

	Query query = session.createNativeQuery("SELECT `article`.`article_id`,\r\n" + "    `article`.`title`,\r\n"
			+ "    `article`.`friendly_name`,\r\n" + "    `article`.`subheading`,\r\n"
			+ "    `article`.`content_type`,\r\n" + "    `article`.`keywords`,\r\n"
			+ "    `article`.`window_title`,\r\n" + "    `article`.`content_location`,\r\n"
			+ "    `article`.`authored_by`,\r\n" + "    `article`.`published_by`,\r\n"
			+ "    `article`.`edited_by`,\r\n" + "    `article`.`copyright_id`,\r\n"
			+ "    `article`.`disclaimer_id`,\r\n" + "    `article`.`create_date`,\r\n"
			+ "    `article`.`published_date`,\r\n" + "    `article`.`pubstatus_id`,\r\n"
			+ "    `article`.`language_id`,\r\n" + "    `article`.`content`,\r\n" + "    `dc`.`dc_name`\r\n,"
			+ "	`article`.`comments`\r\n," + " `article`.`type`\r\n, `article`.`country_id`\r\n,  "
			+ " `article`.`over_allrating`, \r\n "
			
			+ " (select group_concat(a.author_firstname,\" \",a.author_lastname) from author a \r\n"
			+ " where a.author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))  \r\n"
			+ " ) as authors_name, "
			+ " (select count(*) from article) as count , "
			+ " (select reg_doc_pat_id from author where author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))) as docID  \r\n" 
			+ " , `article`.`medicine_type` \r\n"
			+ " FROM `article` \r\n"
			+ " left join disease_condition dc on dc.dc_id = `article`.`disease_condition_id` "
			+  search_str + orderby_str
			+ limit_str + offset_str + " ;");
	// needs other condition too but unable to find correct column
	List<Object[]> results = (List<Object[]>) query.getResultList();
//	System.out.println("result list article@@@@@@@@@@@@@" + results.size());
//	session.getTransaction().commit();   //session.close();
	
	List hmFinal = new ArrayList();
	for (Object[] objects : results) {
		HashMap hm = new HashMap();
		int article_id = (int) objects[0];
		String title = (String) objects[1];
		String friendly_name = (String) objects[2];
		String subheading = (String) objects[3];
		String content_type = (String) objects[4];
		String keywords = (String) objects[5];
		String window_title = (String) objects[6];
		String content_location = (String) objects[7];
		String authored_by = (String) objects[8];
		int published_by = objects[9] != null ? (int) objects[9] : 0;
		int edited_by = (int) objects[10];
		int copyright_id = (int) objects[11];
		int disclaimer_id = (int) objects[12];
		java.sql.Date create_date = (java.sql.Date) objects[13];
		java.sql.Date published_date = (java.sql.Date) objects[14];
		int pubstatus_id = (int) objects[15];
		int language_id = (int) objects[16];
		String content = (String) objects[17];
		String dc_name = (String) objects[18];
		String comments = (String) objects[19];
		String type = (String) objects[20];
		int country_id = objects[21] != null ? (int) objects[21] : 0;
		float over_allrating = (float) (objects[22] != null ? (Float) objects[22] : 0.0);
		String authors_name = (String) objects[23];
		BigInteger count = (BigInteger) objects[24];
		int docID = objects[25] != null ? (int) objects[25] : 0;	
		int medicine_type = objects[26] != null ? (int) objects[26] : 0;
		
		hm.put("article_id", article_id);
		hm.put("title", title);
		hm.put("friendly_name", friendly_name);
		hm.put("subheading", subheading);
		hm.put("content_type", content_type);
		hm.put("keywords", keywords);
		hm.put("window_title", window_title);
		hm.put("content_location", content_location);
		hm.put("authored_by", authored_by);
		hm.put("published_by", published_by);
		hm.put("edited_by", edited_by);
		hm.put("copyright_id", copyright_id);
		hm.put("disclaimer_id", disclaimer_id);
		hm.put("create_date", create_date);
		hm.put("published_date", published_date);
		hm.put("pubstatus_id", pubstatus_id);
		hm.put("language_id", language_id);
		hm.put("content", content);
		hm.put("dc_name", dc_name);
		hm.put("comments", comments);
		hm.put("type", type);
		hm.put("country_id", country_id);
		hm.put("over_allrating", over_allrating);
		hm.put("authors_name", authors_name);
		hm.put("count", count);
		hm.put("docID", docID);
		hm.put("medicine_type", medicine_type);
		
		hmFinal.add(hm);
//		System.out.println(hm);
	}
//	session.getTransaction().commit(); 
	
	//session.close();
	
	return hmFinal;
}


	public static ArrayList getTablesDataListAll(String table_name) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		Query query;
		// creating session object
		//Session session = factory;

		// creating transaction object
//		session.beginTransaction();

		if ("registration".equals(table_name)) {
	        // If the table name is "registration", select only specific columns
			 query = session.createNativeQuery("SELECT registration_id, first_name, last_name, email_address, registration_type FROM `" + table_name + "`;\r\n" + ";");
	    } else {
	        // For other tables, select all columns
	    	query = session.createNativeQuery("SELECT * FROM `" + table_name + "`;\r\n" + ";");
			
	    }
		// needs other condition too but unable to find correct column
		ArrayList list = (ArrayList) query.getResultList();
//		System.out.println("result list " + table_name + " all@@@@@@@@@" + list.size());
//		session.getTransaction().commit();   
		//session.close();

		return list;
	}

	public int updateArticleId(int article_id, HashMap articleMap, String baseUrl) {

		// SendEmailUtil.shootEmail(null, "Article updated top ", "Hi
		// aritcleid="+article_id);

		String[] params = new String[6];

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();

		String updatestr = "";
		if (articleMap.containsKey("title")) {
//			String titleParam = (String) articleMap.get("title");
//			Article_dc_name artExistingTitle = this.getArticleDetails(titleParam);
//			
//			if (artExistingTitle.getTitle().equalsIgnoreCase(titleParam)) {
//				Constant.log("Article Title already exist for article_id "+artExistingTitle.getArticle_id(), 0); 
//				return -3;
//			}

			updatestr += "`title` = '" + articleMap.get("title") + "',\r\n";
		}
		if (articleMap.containsKey("friendly_name")) {
			updatestr += "`friendly_name` = '" + articleMap.get("friendly_name") + "',\r\n";
		}
		if (articleMap.containsKey("subheading")) {
			updatestr += "`subheading` = '" + articleMap.get("subheading") + "',\r\n";
		}
		if (articleMap.containsKey("content_type")) {
			updatestr += "`content_type` = '" + articleMap.get("content_type") + "',\r\n";
		}
		if (articleMap.containsKey("type")) {
			updatestr += "`type` = '" + articleMap.get("type") + "',\r\n";
		}
		if (articleMap.containsKey("keywords")) {
			updatestr += "`keywords` = '" + articleMap.get("keywords") + "',\r\n";
		}
		if (articleMap.containsKey("window_title")) {
			updatestr += "`window_title` = '" + articleMap.get("window_title") + "',\r\n";
		}
		if (articleMap.containsKey("content_location")) {
			updatestr += "`content_location` = '" + articleMap.get("content_location") + "',\r\n";
		}
		if (articleMap.containsKey("authored_by")) {
			updatestr += "`authored_by` = '" + articleMap.get("authored_by") + "',\r\n";
		}
		if (articleMap.containsKey("published_by")) {
			updatestr += "`published_by` = " + articleMap.get("published_by") + ",\r\n";
		}
		if (articleMap.containsKey("edited_by")) {
			updatestr += "`edited_by` = " + articleMap.get("edited_by") + ",\r\n";
		}
		if (articleMap.containsKey("copyright_id")) {
			updatestr += "`copyright_id` = " + articleMap.get("copyright_id") + ",\r\n";
		}
		if (articleMap.containsKey("create_date")) {
			updatestr += "`create_date` = '" + articleMap.get("create_date") + "',\r\n";
		}
//		if (articleMap.containsKey("published_date")) {
//			updatestr += "`published_date` = '" + articleMap.get("published_date") + "',\r\n";
//		}
		if (articleMap.containsKey("pubstatus_id")) {
			updatestr += "`pubstatus_id` = " + articleMap.get("pubstatus_id") + ",\r\n";
			// in case article is set to be PUBLISHED status_id=3 set published_date to
			// current date
			if ((int) articleMap.get("pubstatus_id") == 3) {
				java.util.Date date = new java.util.Date();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				updatestr += "`published_date` = '" + sqlDate + "',\r\n";
			}
		}
		if (articleMap.containsKey("language_id")) {
			updatestr += "`language_id` = " + articleMap.get("language_id") + ",\r\n";
		}
		if (articleMap.containsKey("introduction")) {
			updatestr += "`introduction` = " + articleMap.get("introduction") + ",\r\n";
		}
		if (articleMap.containsKey("content_small")) {
			String content_small = (String ) articleMap.get("content_small");
			updatestr += "`content` = '" + content_small + "',\r\n";
		}	
		
		String content = articleMap.get("articleContent") == null ? "" : (String) articleMap.get("articleContent");
//		if (articleMap.containsKey("articleContent")) {
//			int n = 750;
//			String upToNCharacters = content.substring(0, Math.min(content.length(), n));
//			String upToNCharacters_decoded;
//			try {
//				upToNCharacters_decoded = URLDecoder
//						.decode(upToNCharacters.substring(0, upToNCharacters.lastIndexOf("%")), "UTF-8");
//
//				String content500 = upToNCharacters_decoded;
//				int lastInd = upToNCharacters_decoded.lastIndexOf("},");
//				
//				if (lastInd != -1) {
//					content500 = upToNCharacters_decoded.substring(0, lastInd) + "}]}";
//				}else {
//					if (upToNCharacters_decoded.startsWith("{") && !upToNCharacters_decoded.endsWith("}")) {
//						content500 = upToNCharacters_decoded + "}";
//					}
//				}
//				// article.setContent(URLDecoder.decode(upToNCharacters,
//				// StandardCha"rsets.UTF_8));
//				updatestr += "`content` = '" + EncodingDecodingUtil.encodeURIComponent(content500) + "',\r\n";
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		if (articleMap.containsKey("comments")) {
			updatestr += "`comments` = '" + articleMap.get("comments") + "',\r\n";
		}
		if (articleMap.containsKey("country_id")) {
			updatestr += "`country_id` = " + articleMap.get("country_id") + ",\r\n";
		}
		if (articleMap.containsKey("disease_condition_id")) {
			updatestr += "`disease_condition_id` = " + articleMap.get("disease_condition_id") + ",\r\n";
		}
		if (articleMap.containsKey("medicine_type")) {
			updatestr += "`medicine_type` = " + articleMap.get("medicine_type") + ",\r\n";
		}
//		if (articleMap.containsKey("featured_article")) {
//			updatestr += "`featured_article` = '" + articleMap.get("featured_article") + "',\r\n";
//		}
		
		if (articleMap.containsKey("featured_article")) {
			updatestr += "`featured_article` = '" + articleMap.get("featured_article") + "',\r\n";
		}
		if (articleMap.containsKey("description")) {
			updatestr += "`description` = '" + articleMap.get("description") + "',\r\n";
		}
		/*
		 * if (articleMap.containsKey("articleContent")) { updatestr += "`content` = '"
		 * + articleMap.get("articleContent") + "',\r\n"; }
		 */

		// java.sql.Timestamp date = new java.sql.Timestamp(new
		// java.util.Date().getTime());
		// check if DEFAULT type for disease_condition_id is already present or not
		Article artExisting = contentDao.findByArticleId(article_id);
		String type = artExisting.getType();
		Integer iDiseaseConditionId = artExisting.getDisease_condition_id();
		String diseaseConditionIdStr = "";
		if (articleMap.containsKey("disease_condition_id") || articleMap.containsKey("type")) {
		System.out.println(type);
		System.out.println(iDiseaseConditionId);
			if (articleMap.containsKey("type"))
				type = (String) articleMap.get("type").toString();
			if (articleMap.containsKey("disease_condition_id")) {
				diseaseConditionIdStr = (String) ("" + articleMap.get("disease_condition_id"));
				iDiseaseConditionId = Integer.parseInt(diseaseConditionIdStr);
			}

			if (type.contains("1")) {
				boolean foundDefaultInTable = false;
				List<Article> countMatchArticles = (List<Article>) contentDao.findByArticleTypeAndDC(iDiseaseConditionId);
				if (countMatchArticles.size() > 0) {
					for (Article article : countMatchArticles) {
						if (article.getArticle_id().equals(artExisting.getArticle_id()) ) {
							foundDefaultInTable = true;
						}
					}
					//if article in table is default and we want to update it as default then only allow else return -2
					if (!foundDefaultInTable) {
						Constant.log("Default Article for Disease_condition_id already present", 0);
						return -2;
					}
				}
			}
		}

		updatestr = updatestr.replaceAll(",$", "");
		Query query = session.createNativeQuery(
				"UPDATE `article`\r\n" + "SET\r\n" + updatestr + "WHERE `article_id` = " + article_id + ";");

		// needs other condition too but unable to find correct column
		int ret = 0;
		try {
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("updated article table for article_id =  " + article_id);
			Boolean value = true;
			String article_location_relative_full =  "https://etheriumtech.com/images/illustrations/favicon.png";

			try {
				// Update the Content First
				Article_dc_name art = new ArticleDaoImpl().getArticleDetails(article_id);
				String art_location = art.getContent_location();
				System.out.println(article_id + art_location);
				// String content =
				// "{\"time\":1625577023180,\"blocks\":[{\"id\":\"w6K2r9k_v4\",\"type\":\"paragraph\",\"data\":{\"text\":\"hellow
				// anil article
				// 12341111111\"}},{\"id\":\"kuKfW7EeAv\",\"type\":\"paragraph\",\"data\":{\"text\":\"adhdsfa\"}},{\"id\":\"Rh0WezPDqG\",\"type\":\"paragraph\",\"data\":{\"text\":\"thanks,\"}},{\"id\":\"4C5yZUQ9GV\",\"type\":\"paragraph\",\"data\":{\"text\":\"anil
				// 123211111\"}}],\"version\":\"2.21.0\"}";
				value = ArticleUtils.updateArticleContent(art_location, content, article_id, 1);
				value = true;
				// new SendEmailUtil().shootEmail(null, "Article updated ", "Hi
				// aritcleid="+article_id);
				// String returnEmail = emailUtil.shootEmail("anilraina@etheriumtech.com", "test
				// sub 3", message);

				if ((int) articleMap.get("pubstatus_id") == 3) {
					String curesProperties = "cures.properties";
					Properties prop = null;
					try {
						prop = new ArticleUtils().readPropertiesFile(curesProperties);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//System.out.println("ARTICLES_UPLOAD_DIR 123: " + prop.getProperty("ARTICLES_UPLOAD_DIR"));
					//String articleUploadDir = prop.getProperty("ARTICLES_UPLOAD_DIR");

//					String splitON = articleUploadDir.substring(articleUploadDir.lastIndexOf(File.separator) , articleUploadDir.length());
//					String article_location_relative_ending = art_location.split(splitON)[1];
					String cures_articleimages = prop.getProperty("cures_articleimages");
					String article_location_relative_ending = art_location.split(cures_articleimages)[1];
//					String path = System.getProperty( "catalina.base" ) + "/webapps/"+cures_articleimages;

					//String path = System.getProperty( "catalina.base" ) + "/webapps/"+cures_articleimages;
					//String article_location_relative_full = path+"/"+article_location_relative_ending;

					String article_location_relative_image = article_location_relative_ending.replace(".json", ".png");
					//adding Non_secure port for bug fix in Interakt for header image.
					baseUrl = "https://all-cures.com:444";
					File f = new File(art_location.replace(".json", ".png"));
			        // Checking if the specified file exists or not
			        if (f.exists()) {
			            // Show if the file exists
//			            System.out.println("Image File Exists");
//						System.out.println(baseUrl);
						article_location_relative_full =  baseUrl + "/"+cures_articleimages + article_location_relative_image;
//						System.out.println(article_location_relative_full);
			        }
			        String author_medicine_type = "";
			        String disease_name = "";
			        if (null != art.getAuthored_by() && !art.getAuthored_by().equals("[]") && !art.getAuthored_by().equals("") 
			        		&& art.getAuthored_by() !="7" && !art.getAuthors_name().equals("All Cures Team") ) {
			        	author_medicine_type +="*Author*: "+art.getAuthors_name()+" ";
			        }
			        if (null != art.getMedicine_type_name() && !art.getMedicine_type_name().equals("null") && !art.getMedicine_type_name().equals("")) {
			        	author_medicine_type +="*Type*: "+art.getMedicine_type_name();
			        }
			        if (null != art.getDc_name() && !art.getDc_name().equals("")) {
			        	disease_name ="_"+art.getDc_name()+"_";
			        }
//			        if (null != art.getContent() && !art.getContent().equals("")) {
//			        	author_medicine_type += "   "+art.getContent();
//			        }
					if (articleMap.containsKey("update_subscribers") && (Boolean) articleMap.get("update_subscribers")) {
						String title="New Article";
						String action="article";
						String article_title=(String) articleMap.get("title");
						List<String> recipientTokens = FCMDao.getTokens();
						String id=Integer.toString(article_id);
						for (String token : recipientTokens) {
	//						NotificationService.sendNotification(token,title,  article_title,action,id);
						}
	//					NotificationService.sendNotification(recipientTokens,title,  article_title,action,id);
	//					System.out.println("Notification Sent");
	//					WhatsAPITrackEvents.POSTRequestTrackEventsByArticleId(article_id);
						WhatsAPITemplateMessage.POSTRequestTrackEventsByArticleId(art.getTitle()+"?whatsapp", article_id, type, art.getDisease_condition_id(), article_location_relative_full, author_medicine_type, disease_name);
	//					System.out.println("Subscription WhatsApp Message sent.");
					}
				}
				
//				EmailDTO emaildto = new EmailDTO();
//				emaildto.setSubject("Article updated ");
//				emaildto.setEmailtext("Hi aritcleid=" + article_id);
//
//				String returnEmail = emailUtil.shootEmail(emaildto);
				EmailDTO emaildto = new EmailDTO();

//				emaildto.setTo(email);
//				emaildto.setSubject("Registration user email..");
//				emaildto.setEmailtext("Hi " + f_name + "," + " Thanks for the registration with allcures.");
//				EmailDTO emaildto2 = new EmailDTO();

//				emaildto.setTo(email);
//				emaildto.setFrom("All-Cures INFO");
//				emaildto.setSubject("Cures Update #"+article_id+": All-Cures ");
				emaildto.setSubject("Cures Update #"+art.getTitle()+": All-Cures ");
				// Populate the template data
				Map<String, Object> templateData = new HashMap<>();
				templateData.put("templatefile", "email/articleupdate.ftlh");
				//templateData.put("first_name", f_name);
				// String link = "http://localhost:3000";
//				String link = "https://all-cures.com/cure/"+URLEncoder.encode(art.getTitle(),"UTF-8");
				String link = "https://www.all-cures.com/cure/"+art.getArticle_id()+"-"+art.getTitle().replaceAll(" ", "-");
				templateData.put("linkverfiy", link);
				templateData.put("title", art.getTitle());
				templateData.put("type", type);
				templateData.put("dc_id", art.getDisease_condition_id());
				templateData.put("article_image_path", article_location_relative_full);

				// object -> Map
//				ObjectMapper oMapper = new ObjectMapper();
//				Map<String, Object> mapUser = oMapper.convertValue(user, Map.class);
//				templateData.putAll(mapUser);
				emaildto.setEmailTemplateData(templateData);
//				System.out.println(emaildto);

				String returnEmail = emailUtil.shootEmail(emaildto);

			} catch (Exception e) {
				e.printStackTrace();
				value = false;
			}

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
			// session.getTransaction().commit();   //session.close();
//			session.getTransaction().commit();   
			//session.close();
		}
		// session.getTransaction().commit();   //session.close();

		return ret;
	}

	public static int deleteArticleId(int article_id) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;

		// creating transaction object
		session.beginTransaction();

		// Query query = session.createNativeQuery("DELETE FROM ARTICLE WHERE ARTICLE_ID
		// = " + article_id + ";");
		// SOFT delte done instead of hard delete form database
		Query query = session
				.createNativeQuery("UPDATE article SET pubstatus_id=0 WHERE article_id = " + article_id + ";");
		// needs other condition too but unable to find correct column
		int ret = 0;
		try {
			ret = query.executeUpdate();
//			System.out.println("soft deleteed article_id =  " + article_id);
			session.getTransaction().commit();
		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
			// session.getTransaction().commit();   //session.close();
//			session.getTransaction().commit();   
			//session.close();
		}

		return ret;
	}
	
	public static List getArticlesListAllKeysList(Integer limit, Integer offset, String searchStr, String orderByStr) {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();		
		// creating session object
		//Session session = factory;
		
		// creating transaction object
//		session.beginTransaction();
		String limit_str = "";
		if (null != limit)
			limit_str = " limit " + limit;
		String offset_str = "";
		if (null != offset)
			offset_str = " offset " + offset;
		String orderby_str = " order by `article`.`published_date` desc ";
		if (null != orderByStr) {
			String[] orderArr = orderByStr.split(":");
			orderby_str = " order by  `article`.`" + orderArr[0] +"` "+orderArr[1];
		}
		String search_str = "";
		if (null != searchStr) {
			search_str = " where ";
//			search_str = " where title like '%" + searchStr + "%'  or  article_id like '%"+searchStr+"%'";
			String[] searchArrColums = searchStr.split("~");
			for (String columsDetail : searchArrColums) {
				String[] columsDetailArr = columsDetail.split(":");
				search_str += columsDetailArr[0] +" like '%"+ columsDetailArr[1] + "%' AND ";
			}
			//replace last AND with blank
			search_str = search_str.substring(0,search_str.lastIndexOf("AND"));
		}
		
		Query query = session.createNativeQuery("SELECT `article`.`article_id`,\r\n" + "    `article`.`title`,\r\n"
				+ "    `article`.`friendly_name`,\r\n" + "    `article`.`subheading`,\r\n"
				+ "    `article`.`content_type`,\r\n" + "    `article`.`keywords`,\r\n"
				+ "    `article`.`window_title`,\r\n" + "    `article`.`content_location`,\r\n"
				+ "    `article`.`authored_by`,\r\n" + "    `article`.`published_by`,\r\n"
				+ "    `article`.`edited_by`,\r\n" + "    `article`.`copyright_id`,\r\n"
				+ "    `article`.`disclaimer_id`,\r\n" + "    `article`.`create_date`,\r\n"
				+ "    `article`.`published_date`,\r\n" + "    `article`.`pubstatus_id`,\r\n"
				+ "    `article`.`language_id`,\r\n" + "    `article`.`content`,\r\n" + "    `dc`.`dc_name`\r\n,"
				+ "	`article`.`comments`\r\n," + " `article`.`type`\r\n, `article`.`country_id`\r\n,  "
				+ " `article`.`over_allrating`, \r\n "
				
				+ " (select group_concat(a.author_firstname,\" \",a.author_lastname) from author a \r\n"
				+ " where a.author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))  \r\n"
				+ " ) as authors_name, "
				+ " (select count(*) from article) as count ,"
				+ " (select reg_doc_pat_id from author where author_id in (trim(trailing ']' from trim(leading '[' from `article`.`authored_by`)))) as docID  \r\n" 
				+ " , `article`.`medicine_type` \r\n"
				+ " FROM `article`  \r\n"
				
				+ " left join disease_condition dc on dc.dc_id = `article`.`disease_condition_id` "
				
				+  search_str + orderby_str
				+ limit_str + offset_str + 
				" ;");
		// needs other condition too but unable to find correct column
		List<Object[]> results = (List<Object[]>) query.getResultList();
//		System.out.println("result list article@@@@@@@@@@@@@" + results.size());
//		session.getTransaction().commit();   //session.close();
		
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			int article_id = (int) objects[0];
			String title = (String) objects[1];
			String friendly_name = (String) objects[2];
			String subheading = (String) objects[3];
			String content_type = (String) objects[4];
			String keywords = (String) objects[5];
			String window_title = (String) objects[6];
			String content_location = (String) objects[7];
			String authored_by = (String) objects[8];
			int published_by = objects[9] != null ? (int) objects[9] : 0;
			int edited_by = (int) objects[10];
			int copyright_id = (int) objects[11];
			int disclaimer_id = (int) objects[12];
			java.sql.Date create_date = (java.sql.Date) objects[13];
			java.sql.Date published_date = (java.sql.Date) objects[14];
			int pubstatus_id = (int) objects[15];
			int language_id = (int) objects[16];
			String content = (String) objects[17];
			String dc_name = (String) objects[18];
			String comments = (String) objects[19];
			String type = (String) objects[20];
			int country_id = objects[21] != null ? (int) objects[21] : 0;
			float over_allrating = (float) (objects[22] != null ? (Float) objects[22] : 0.0);
			String authors_name = (String) objects[23];
			BigInteger count = (BigInteger) objects[24];
			int docID = objects[25] != null ? (int) objects[25] : 0;
			int medicine_type = objects[26] != null ? (int) objects[26] : 0;
			
			
			
			hm.put("article_id", article_id);
			hm.put("title", title);
			hm.put("friendly_name", friendly_name);
			hm.put("subheading", subheading);
			hm.put("content_type", content_type);
			hm.put("keywords", keywords);
			hm.put("window_title", window_title);
			hm.put("content_location", content_location);
			hm.put("authored_by", authored_by);
			hm.put("published_by", published_by);
			hm.put("edited_by", edited_by);
			hm.put("copyright_id", copyright_id);
			hm.put("disclaimer_id", disclaimer_id);
			hm.put("create_date", create_date);
			hm.put("published_date", published_date);
			hm.put("pubstatus_id", pubstatus_id);
			hm.put("language_id", language_id);
			hm.put("content", content);
			hm.put("dc_name", dc_name);
			hm.put("comments", comments);
			hm.put("type", type);
			hm.put("country_id", country_id);
			hm.put("over_allrating", over_allrating);
			hm.put("authors_name", authors_name);
			hm.put("count", count);
			hm.put("docID", docID);
			hm.put("medicine_type", medicine_type);
			
			hmFinal.add(hm);
//			System.out.println(hm);
		}
//		session.getTransaction().commit();   
		//session.close();
		
		return hmFinal;
	}

	// Method to call Procedure that will create the view of ranked articles
	public static int create_ranked_articles()
	{
		Session session = HibernateUtil.buildSessionFactory();	
		int ret=0;
		try {
		    session.beginTransaction();

		    StoredProcedureQuery storedProcedure = session.createStoredProcedureQuery("DropAndCreateRankedArticles");
		    storedProcedure.execute();

		    session.getTransaction().commit();
		    ret=1;
		} catch (Exception e) {
		    if (session.getTransaction() != null) {
		        session.getTransaction().rollback();
		    }
		    e.printStackTrace();
		} finally {
	//	    session.close();
		}

		return ret;
		
	}
	
public static List getArticlesListAllKeysRanked(Integer limit, Integer offset, String searchStr, String orderByStr) {
		int ret=create_ranked_articles();
		List hmFinal = new ArrayList();
		if(ret==0)
		{
//			System.out.println("Failed to call procedure");
		}
		
		else
		{
			// creating seession factory object
			Session session = HibernateUtil.buildSessionFactory();
			
			// creating session object
			//Session session = factory;
			
			// creating transaction object
//			session.beginTransaction();
			String limit_str = "";
			if (null != limit)
				limit_str = " limit " + limit;
			String offset_str = "";
			if (null != offset)
				offset_str = " offset " + offset;
			String orderby_str = " order by `rnk`,`published_date` desc ";
			
			String search_str = "";

			Query query = session.createNativeQuery("SELECT\r\n"
					+ "        `article_id`,\r\n"
					+ "    `title`,\r\n"
					+ " `friendly_name`,\r\n"
					+ "    `subheading`,\r\n"
					+ "    `content_type`,\r\n"
					+ "    `keywords`,\r\n"
					+ "    `window_title`,\r\n"
					+ "    `content_location`,\r\n"
					+ "    `authored_by`,\r\n"
					+ "    `published_by`,\r\n"
					+ "    `edited_by`,\r\n"
					+ "    `copyright_id`,\r\n"
					+ "    `disclaimer_id`,\r\n"
					+ "    `create_date`,\r\n"
					+ "    `published_date`,\r\n"
					+ "    `pubstatus_id`,\r\n"
					+ "    `language_id`,\r\n"
					+ "    `content`,\r\n"
					+ "    `dc_name`,\r\n"
					+ "`comments`,\r\n"
					+ "`type`,\r\n"
					+ "`country_id`,\r\n"
					+ "`over_allrating`, \r\n"
					+ "`medicine_type_name`,\r\n"
					+ "`authors_name`,\r\n"
					+ "`count`,\r\n"
					+ "`docID`,\r\n"
					
					+ "`rnk`\r\n"
					+ "\r\n"
					+ "    FROM ranked\r\n"
					+ orderby_str
					+ limit_str + offset_str + " ;");
			// needs other condition too but unable to find correct column
			List<Object[]> results = (List<Object[]>) query.getResultList();
//			System.out.println("result list article@@@@@@@@@@@@@" + results.size());
//			session.getTransaction().commit();   //session.close();
			
			
			for (Object[] objects : results) {
				HashMap hm = new HashMap();
				int article_id = (int) objects[0];
				String title = (String) objects[1];
				String friendly_name = (String) objects[2];
				String subheading = (String) objects[3];
				String content_type = (String) objects[4];
				String keywords = (String) objects[5];
				String window_title = (String) objects[6];
				String content_location = (String) objects[7];
				String authored_by = (String) objects[8];
				int published_by = objects[9] != null ? (int) objects[9] : 0;
				int edited_by = (int) objects[10];
				int copyright_id = (int) objects[11];
				int disclaimer_id = (int) objects[12];
				java.sql.Date create_date = (java.sql.Date) objects[13];
				java.sql.Date published_date = (java.sql.Date) objects[14];
				int pubstatus_id = (int) objects[15];
				int language_id = (int) objects[16];
				String content = (String) objects[17];
				String dc_name = (String) objects[18];
				String comments = (String) objects[19];
				String type = (String) objects[20];
				int country_id = objects[21] != null ? (int) objects[21] : 0;
				float over_allrating = (float) (objects[22] != null ? (Float) objects[22] : 0.0);
				String med_type_name = (String) objects[23];
				String authors_name = (String) objects[24];
				BigInteger count = (BigInteger) objects[25];
				BigInteger docID = (BigInteger) objects[26];
				hm.put("article_id", article_id);
				hm.put("title", title);
				hm.put("friendly_name", friendly_name);
				hm.put("subheading", subheading);
				hm.put("content_type", content_type);
				hm.put("keywords", keywords);
				hm.put("window_title", window_title);
				hm.put("content_location", content_location);
				hm.put("authored_by", authored_by);
				hm.put("published_by", published_by);
				hm.put("edited_by", edited_by);
				hm.put("copyright_id", copyright_id);
				hm.put("disclaimer_id", disclaimer_id);
				hm.put("create_date", create_date);
				hm.put("published_date", published_date);
				hm.put("pubstatus_id", pubstatus_id);
				hm.put("language_id", language_id);
				hm.put("content", content);
				hm.put("dc_name", dc_name);
				hm.put("comments", comments);
				hm.put("type", type);
				hm.put("country_id", country_id);
				hm.put("over_allrating", over_allrating);
				hm.put("authors_name", authors_name);
				hm.put("count", count);
				hm.put("docID", docID);
		
				hm.put("med_type_name", med_type_name);
				hmFinal.add(hm);

			}	
		}
		
//		session.getTransaction().commit(); 
		
		//session.close();
		
		return hmFinal;
	}

	public static int updateLikes(Long articleId, boolean isLike) {
    String columnName = isLike ? "likes" : "dislikes";
    int ret=0;
 // creating seession factory object
 			Session session = HibernateUtil.buildSessionFactory();
 			
    try  {
		 session.beginTransaction();
        
        // Check if the record for the article exists
        boolean articleExists = session.createNativeQuery("SELECT 1 FROM article_likes WHERE article_id = :articleId")
                .setParameter("articleId", articleId)
                .uniqueResult() != null;

        if (articleExists) {
        Query query=    session.createNativeQuery("UPDATE article_likes SET " + columnName + " = " + columnName + " + 1 WHERE article_id = :articleId")
                    .setParameter("articleId", articleId);
              ret= query.executeUpdate();
        } else {
            Query query = session.createNativeQuery("INSERT INTO article_likes (article_id, " + columnName + ") VALUES (:articleId, 1)")
                    .setParameter("articleId", articleId);
             ret = query.executeUpdate();
        }
        
session.getTransaction().commit();
        
    } catch (Exception e) {
        if (session.getTransaction() != null && session.getTransaction().isActive()) {
        	session.getTransaction().rollback();
        }
        throw e;
    }
    return ret;
}
	

public static Map<String, Integer> getLikesAndDislikesCount(Long articleId) {
    Map<String, Integer> likesAndDislikes = new HashMap<>();
    Session session = HibernateUtil.buildSessionFactory();
    try {
        // Query to retrieve likes count
        NativeQuery<Integer> likesQuery = session.createNativeQuery(
                "SELECT likes FROM article_likes WHERE article_id = :articleId")
                .setParameter("articleId", articleId);
        Integer likesCount = (Integer) likesQuery.uniqueResult();
        likesAndDislikes.put("likes", likesCount != null ? likesCount : 0);

        // Query to retrieve dislikes count
        NativeQuery<Integer> dislikesQuery = session.createNativeQuery(
                "SELECT dislikes FROM article_likes WHERE article_id = :articleId")
                .setParameter("articleId", articleId);
        Integer dislikesCount = (Integer) dislikesQuery.uniqueResult();
        likesAndDislikes.put("dislikes", dislikesCount != null ? dislikesCount : 0);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return likesAndDislikes;
}

	public static int createArticle(HttpServletRequest request, HttpServletResponse response, Registration user) throws IOException
	{
		String requestJsonStr = null;
		try {
		    requestJsonStr = IOUtils.toString(request.getInputStream(), "UTF-8");
		} catch (IOException e) {
		    e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();
    	Map<String,Object> requestJsonMap = mapper.readValue(requestJsonStr, Map.class);
    	
    	String title= (String) requestJsonMap.get("title");
    	Constant.log("Creating Article with Title:"+title, 1);
    	
		Article_dc_name artExistingTitle = new ArticleDaoImpl().getArticleDetails(title);
		
		if (null != artExistingTitle.getTitle() && artExistingTitle.getTitle().equalsIgnoreCase(title)) {
			Constant.log("Article Title already exist for article_id "+artExistingTitle.getArticle_id(), 0); 
			return -3;
		}
    	
    	String artFrndlyNm= (String) requestJsonMap.get("friendlyName");
    	Constant.log("Creating Article with Friendly Name:"+artFrndlyNm, 0);
		int iLang= requestJsonMap.get("language") !=null ? (int) requestJsonMap.get("language") : 1;//English By Default
		Constant.log("strLanguage:"+iLang, 0);
//		int iLang = 1; //English By Default
//		if(lang != null && !"".equals(lang.trim())){
//			iLang = Integer.parseInt(lang);
//			Constant.log("intLanguage:"+iLang, 0);
//		}
//		Constant.log("Creating Article with Language:"+lang, 0);
		String articleContent= (String) requestJsonMap.get("articleContent");
			//TODO: Capture Metadata of the Article and Persist in backend
		String subHead=(String) requestJsonMap.get("subHeading");
		Constant.log("Creating Article with subHeading:"+subHead, 0);
		//TODO: Need to create FK constraint in the DB for Content Types
		String content_type=(String) requestJsonMap.get("contentType");
//		int iContentType = 1; //Article By Default
//		if(content_type != null && !"".equals(content_type.trim())){
//			iContentType = Integer.parseInt(content_type);
//		}
		Constant.log("Creating Article with Content Type:"+content_type, 0);
		
		String type=(String) ""+requestJsonMap.get("type");
		Constant.log("Creating Article with Type:"+type, 0);
		

		
		String featured_article=(String) requestJsonMap.get("featured_article");
		Constant.log("Creating Article with Featured Article:"+featured_article, 0);
		
		String status= (String) ""+ requestJsonMap.get("articleStatus");
		System.out.println("Test articleStatus Val"+status);
		int iStatus = 1; //WIP By Default
		if(status  != null && !"".equals(status.trim())){
			iStatus = Integer.parseInt(status);
		}
		Constant.log("Creating Article with Status:"+status, 0);
		//disclaimer (should be an id)
		int iDiscId=(int) requestJsonMap.get("disclaimerId");
//		int iDiscId = -1; //Negative indicates error
//		if(d_loc != null && !"".equals(d_loc.trim())){
//			iDiscId = Integer.parseInt(d_loc);
//		}
		Constant.log("Creating Article with Disclaimer:"+iDiscId, 0);
		//copyright (should be an id)
		int iCopyId=(int) requestJsonMap.get("copyId");;
//		int iCopyId = -1; //Negative indicates error
//		if(c_loc != null && !"".equals(c_loc.trim())){
//			iCopyId = Integer.parseInt(c_loc);
//		}
		Constant.log("Creating Article with Copyright:"+iCopyId, 0);
		
		String authIdS = (String) ""+requestJsonMap.get("authById");;
//		int iAuthId = -1; //Negative indicates error
//		if(authId != null && !"".equals(authId.trim())){
//			iAuthId = Integer.parseInt(authId);
//		}
		Constant.log("Creating Article with Author:"+authIdS, 0);
		String keyword=(String) requestJsonMap.get("keywords");
		String window_title=(String) requestJsonMap.get("winTitle");;
		String articlecontent= (String) (String) requestJsonMap.get("articleContent");
	//	System.out.println("##############articlecontent###"+articlecontent);
		String countryId = (String) requestJsonMap.get("countryId");;
		int iCountryId = -1; //Negative indicates error
		if(countryId != null && !"".equals(countryId.trim())){
			iCountryId = Integer.parseInt(countryId);
		}
		Constant.log("Creating Article with Authors:"+authIdS, 0);
		
		String diseaseConditionId = (String) requestJsonMap.get("diseaseConditionId");;
		int iDiseaseConditionId = -1; //Negative indicates error
		if(diseaseConditionId != null && !"".equals(diseaseConditionId.trim())){
			iDiseaseConditionId = Integer.parseInt(diseaseConditionId);
		}
		Constant.log("Creating Article with Author:"+diseaseConditionId, 0);
		
		String promoId = (String) requestJsonMap.get("promoId");;
		int ipromoId = -1; //Negative indicates error
		int promoStage = -1; //Negative indicates error
		if(promoId != null && !"".equals(promoId.trim())){
			ipromoId = Integer.parseInt(promoId);
			promoStage = 0 ; // promoStage < 0 or null ==> No promo applied, promo promoStage = 0 ==> promo applied no paid, promoStage =1 ==> promo applied and paid
		}
		Constant.log("Creating Article with promoId:"+promoId, 0);
		
		int imedicineTypeId= requestJsonMap.get("medicine_type") !=null ? (int) requestJsonMap.get("medicine_type") : -1;//-1 By Default

		Constant.log("Creating Article with imedicineTypeId:"+imedicineTypeId, 0);
		
		Constant.log("Saving Content in Dao", 1);
		String comments= (String) requestJsonMap.get("comments");
		String description= (String) requestJsonMap.get("description");
		String introduction= (String) requestJsonMap.get("introduction");
		Constant.log("comments:"+comments, 0);
		//User Object in Session is coming Null Here and Causing a Null Pointer Exception on the next line
		//This should never be the case as the user has to be logged  in to create an article
		int result=-1;		
		if(user == null){
			Constant.log("Missing user object in session; User is not logged In; Send to login", 0); 
			result = -5;
		}else{
			//check DEFAULT i.e type =1 and disease_condition id is unique
			if (type.contains("1")) {
				List<Article> countMatchArticles = contentDao.findByArticleTypeAndDC(iDiseaseConditionId);
				if(countMatchArticles.size()>0) {
					Constant.log("Default Article for Disease_condition_id already present", 0); 
					return -2;
				} 
			}
			//TODO: Remove this hardcoding	
//			Constant.log("User object is in session; User is logged In; Adding Article Now", 0);
			boolean bResult = contentDao.createArticle(iStatus, iLang, iDiscId, iCopyId, authIdS, title, artFrndlyNm, subHead, 
					content_type, keyword, window_title, null, user.getRegistration_id().intValue(), articlecontent, iDiseaseConditionId, iCountryId,comments,
					ipromoId,promoStage,type,imedicineTypeId,featured_article,description,introduction);
			if(bResult == true){
				result = 1;
			}
		}    	
		return result;
	}

	public static List<Map<String, Object>> getArticlesListAllKeysDC() {
	    Session session = HibernateUtil.buildSessionFactory(); // Consider moving this to a static init block

	    String sql = "WITH ranked_articles AS (\n" +
	            "    SELECT a.*, ROW_NUMBER() OVER (PARTITION BY a.disease_condition_id ORDER BY a.published_date DESC) AS rn\n" +
	            "    FROM article a\n" +
	            "    WHERE a.type = '[2]' AND a.pubstatus_id = 3 AND a.disease_condition_id IN (113, 12, 31, 74, 66, 129, 85, 137)\n" +
	            ")\n" +
	            "SELECT ra.article_id, ra.title, ra.friendly_name, ra.subheading, ra.content_type, ra.keywords, ra.window_title,\n" +
	            "       ra.content_location, ra.authored_by, ra.published_by, ra.edited_by, ra.copyright_id, ra.disclaimer_id,\n" +
	            "       ra.create_date, ra.published_date, ra.pubstatus_id, ra.language_id, ra.content, dc.dc_name, ra.comments,\n" +
	            "       ra.type, ra.country_id, ra.over_allrating,\n" +
	            "       (SELECT GROUP_CONCAT(a.author_firstname, ' ', a.author_lastname)\n" +
	            "        FROM author a\n" +
	            "        WHERE a.author_id = CAST(TRIM(TRAILING ']' FROM TRIM(LEADING '[' FROM ra.authored_by)) AS UNSIGNED)) AS authors_name,\n" +
	            "       (SELECT reg_doc_pat_id FROM author\n" +
	            "        WHERE author_id = CAST(TRIM(TRAILING ']' FROM TRIM(LEADING '[' FROM ra.authored_by)) AS UNSIGNED)\n" +
	            "        LIMIT 1) AS docID,\n" +
	            "       ra.medicine_type,\n" +
	            "       (SELECT COUNT(*) FROM article) AS count\n" +
	            "FROM ranked_articles ra\n" +
	            "LEFT JOIN disease_condition dc ON dc.dc_id = ra.disease_condition_id\n" +
	            "WHERE ra.rn = 1\n" +
	            "ORDER BY ra.published_date DESC";

	    Query query = session.createNativeQuery(sql);
	    List<Object[]> results = query.getResultList();

	    List<Map<String, Object>> finalResult = new ArrayList<>();

	    for (Object[] row : results) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("article_id", row[0]);
	        map.put("title", row[1]);
	        map.put("friendly_name", row[2]);
	        map.put("subheading", row[3]);
	        map.put("content_type", row[4]);
	        map.put("keywords", row[5]);
	        map.put("window_title", row[6]);
	        map.put("content_location", row[7]);
	        map.put("authored_by", row[8]);
	        map.put("published_by", row[9] != null ? row[9] : 0);
	        map.put("edited_by", row[10]);
	        map.put("copyright_id", row[11]);
	        map.put("disclaimer_id", row[12]);
	        map.put("create_date", row[13]);
	        map.put("published_date", row[14]);
	        map.put("pubstatus_id", row[15]);
	        map.put("language_id", row[16]);
	        map.put("content", row[17]);
	        map.put("dc_name", row[18]);
	        map.put("comments", row[19]);
	        map.put("type", row[20]);
	        map.put("country_id", row[21] != null ? row[21] : 0);
	        map.put("over_allrating", row[22] != null ? ((Number) row[22]).floatValue() : 0.0f);
	        map.put("authors_name", row[23]);
	        map.put("docID", row[24] != null ? row[24] : 0);
	        map.put("medicine_type", row[25]);
	        map.put("count", row[26]);

	        finalResult.add(map);
	    }

//	    session.close(); // Always close session
	    return finalResult;
	}

}
