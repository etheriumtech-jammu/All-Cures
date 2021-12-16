package dao;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.Article;
import model.Article_dc_name;
import model.EmailDTO;
import model.Registration;
import service.SendEmailService;
import util.ArticleUtils;
import util.Constant;
import util.HibernateUtil;
import util.WhatsAPITemplateMessage;
import util.WhatsAPITrackEvents;
import util.WhatsAPITrackUsers;

//1	active
//7	WorkInProgress
//@Component makes sure it is picked up by the ComponentScan (if it is in the right package). This allows @Autowired to work in other classes for instances of this class
@Component
public class ArticleDaoImpl {

	@Autowired
	private SendEmailService emailUtil;

	private static ArrayList list = new ArrayList();

	ContentDaoImpl contentDao = new ContentDaoImpl();

	public static ArrayList<Article> findPublishedArticle(Registration user) {

//		HibernateUtil hu = new HibernateUtil();
//		Session session = hu.getSession();
		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory;

		/*
		 * HibernateUtil hu = new HibernateUtil(); Session session = hu.getSession();
		 */
		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();
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
		System.out.println("result list article@@@@@@@@@@@@@" + list);

		session.close();
		return list;
	}

	public static ArrayList<Article> findDraftAricle(Registration user) {

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

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
		System.out.println("result list article@@@@@@@@@@@@@" + list);
		session.close();
		session.close();

		return list;
	}

	public static ArrayList<Article> findReviwArticle(Registration user) {

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory;
		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

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
		System.out.println("result list article@@@@@@@@@@@@@" + list);
		session.close();
		return list;
	}

	public static ArrayList<Article> findApprovalArticle(Registration user) {

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

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
		System.out.println("result list article@@@@@@@@@@@@@" + list);
		session.close();

		return list;
	}

	public Article_dc_name getArticleDetails(int reg_id) {

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

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
				+ "    `article`.`type`,\r\n" + "    `dc`.`dc_name`,\r\n" + "    `article`.`comments`\r\n"
				+ "FROM `allcures_schema`.`article`\r\n"
				+ " left join disease_condition dc on dc.dc_id = `article`.`disease_condition_id` \r\n"
				+ " where article_id =  " + reg_id + ";");
		ArrayList<Article> articleList = (ArrayList<Article>) query.getResultList();
		Article_dc_name article = new Article_dc_name();
		Iterator itr = articleList.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			System.out.println((Integer) obj[0]);
			System.out.println((String) obj[1]);
			article.setArticle_id((Integer) obj[0]);
			article.setTitle((String) obj[1]);
			article.setFriendly_name((String) obj[2]);
			article.setSubheading((String) obj[3]);
			article.setContent_type((String) obj[4]);
			article.setKeywords((String) obj[5]);
			article.setWindow_title((String) obj[6]);
			article.setContent_location((String) obj[7]);
			System.out.println((String) obj[7]);
			String file = (String) obj[7];
			// file = "/home/administrator/uat/"+
			// file = file.replace("\\", "/");//.replace("/", "/");
			System.out.println("FILENAME===>>>>>>>>>>" + file);
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
					System.out.print(c);
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
			System.out.println(contents);

			article.setAuthored_by((String) obj[8]);
			article.setPublished_by((Integer) obj[9]);
			article.setEdited_by((Integer) obj[10]);
			article.setCopyright_id((Integer) obj[11]);
			article.setDisclaimer_id((Integer) obj[12]);
			article.setCreate_date((Date) obj[13]);
			article.setPublished_date((Date) obj[14]);
			article.setPubstatus_id((Integer) obj[15]);
			article.setLanguage_id((Integer) obj[16]);
			article.setCountry_id((Integer) obj[18]);
			article.setDisease_condition_id((Integer) obj[19]);
			article.setType((String) obj[20]);
			article.setContent(contents);
			article.setDc_name((String) obj[21]);
			article.setComments((String) obj[22]);
		}
		session.close();

		return article;
	}

	public static ArrayList<Article> getArticlesListAll(Integer limit) {

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		String limit_str = "";
		if (null != limit)
			limit_str = " limit " + limit;

		Query query = session.createNativeQuery("SELECT `article`.`article_id`,\r\n" + "    `article`.`title`,\r\n"
				+ "    `article`.`friendly_name`,\r\n" + "    `article`.`subheading`,\r\n"
				+ "    `article`.`content_type`,\r\n" + "    `article`.`keywords`,\r\n"
				+ "    `article`.`window_title`,\r\n" + "    `article`.`content_location`,\r\n"
				+ "    `article`.`authored_by`,\r\n" + "    `article`.`published_by`,\r\n"
				+ "    `article`.`edited_by`,\r\n" + "    `article`.`copyright_id`,\r\n"
				+ "    `article`.`disclaimer_id`,\r\n" + "    `article`.`create_date`,\r\n"
				+ "    `article`.`published_date`,\r\n" + "    `article`.`pubstatus_id`,\r\n"
				+ "    `article`.`language_id`,\r\n" + "    `article`.`content`,\r\n" + "    `article`.`type`,\r\n"
				+ "    `article`.`comments`\r\n" + "FROM `allcures_schema`.`article`"
				+ " order by `article`.`article_id` desc " + limit_str + ";\r\n" + ";");
		// needs other condition too but unable to find correct column
		ArrayList<Article> list = (ArrayList<Article>) query.getResultList();
		System.out.println("result list article@@@@@@@@@@@@@" + list);
		session.close();

		return list;
	}

	public static List getArticlesListAllKeys(Integer limit) {

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();
		String limit_str = "";
		if (null != limit)
			limit_str = " limit " + limit;

		Query query = session.createNativeQuery("SELECT `article`.`article_id`,\r\n" + "    `article`.`title`,\r\n"
				+ "    `article`.`friendly_name`,\r\n" + "    `article`.`subheading`,\r\n"
				+ "    `article`.`content_type`,\r\n" + "    `article`.`keywords`,\r\n"
				+ "    `article`.`window_title`,\r\n" + "    `article`.`content_location`,\r\n"
				+ "    `article`.`authored_by`,\r\n" + "    `article`.`published_by`,\r\n"
				+ "    `article`.`edited_by`,\r\n" + "    `article`.`copyright_id`,\r\n"
				+ "    `article`.`disclaimer_id`,\r\n" + "    `article`.`create_date`,\r\n"
				+ "    `article`.`published_date`,\r\n" + "    `article`.`pubstatus_id`,\r\n"
				+ "    `article`.`language_id`,\r\n" + "    `article`.`content`,\r\n" + "    `dc`.`dc_name`\r\n,"
				+ " `article`.comments\r\n," + " `article`.`type`\r\n, `article`.`country_id`  "

				+ "FROM `allcures_schema`.`article`\r\n"
				+ "left join disease_condition dc on dc.dc_id = `article`.`disease_condition_id` "
				+ " order by `article`.`article_id` desc \r\n" + limit_str + ";");
		// needs other condition too but unable to find correct column
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println("result list article@@@@@@@@@@@@@" + results);
		session.close();

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

			hmFinal.add(hm);
			System.out.println(hm);
		}
		session.close();

		return hmFinal;
	}

	public static ArrayList getTablesDataListAll(String table_name) {

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session.createNativeQuery("SELECT * FROM `allcures_schema`.`" + table_name + "`;\r\n" + ";");
		// needs other condition too but unable to find correct column
		ArrayList list = (ArrayList) query.getResultList();
		System.out.println("result list " + table_name + " all@@@@@@@@@" + list);
		session.close();

		return list;
	}

	public int updateArticleId(int article_id, HashMap articleMap) {

		// SendEmailUtil.shootEmail(null, "Article updated top ", "Hi
		// aritcleid="+article_id);

		String[] params = new String[6];

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory;
		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		String updatestr = "";
		if (articleMap.containsKey("title")) {
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
		String content = articleMap.get("articleContent") == null ? "" : (String) articleMap.get("articleContent");
		if (articleMap.containsKey("articleContent")) {
			int n = 2000;
			String upToNCharacters = content.substring(0, Math.min(content.length(), n));
			String upToNCharacters_decoded;
			try {
				upToNCharacters_decoded = URLDecoder
						.decode(upToNCharacters.substring(0, upToNCharacters.lastIndexOf("%")), "UTF-8");

				String content500 = upToNCharacters_decoded;
				int lastInd = upToNCharacters_decoded.lastIndexOf("},");
				if (lastInd != -1) {
					content500 = upToNCharacters_decoded.substring(0, lastInd) + "}]}";
				}
				// article.setContent(URLDecoder.decode(upToNCharacters,
				// StandardCha"rsets.UTF_8));
				updatestr += "`content` = '" + URLEncoder.encode(content500, "UTF-8") + "',\r\n";
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (articleMap.containsKey("comments")) {
			updatestr += "`comments` = '" + articleMap.get("comments") + "',\r\n";
		}
		if (articleMap.containsKey("country_id")) {
			updatestr += "`country_id` = " + articleMap.get("country_id") + ",\r\n";
		}
		if (articleMap.containsKey("disease_condition_id")) {
			updatestr += "`disease_condition_id` = " + articleMap.get("disease_condition_id") + ",\r\n";
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

			if (articleMap.containsKey("type"))
				type = (String) articleMap.get("type").toString();
			if (articleMap.containsKey("disease_condition_id")) {
				diseaseConditionIdStr = (String) ("" + articleMap.get("disease_condition_id"));
				iDiseaseConditionId = Integer.parseInt(diseaseConditionIdStr);
			}

			if (type.contains("1")) {
				List<Article> countMatchArticles = contentDao.findByArticleTypeAndDC(iDiseaseConditionId);
				if (countMatchArticles.size() > 0) {
					Constant.log("Default Article for Disease_condition_id already present", 0);
					return -2;
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
			trans.commit();
			System.out.println("updated article table for article_id =  " + article_id);
			Boolean value = true;
			try {
				// Update the Content First
				Article_dc_name art = new ArticleDaoImpl().getArticleDetails(article_id);
				String art_location = art.getContent_location();

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
//					WhatsAPITrackEvents.POSTRequestTrackEventsByArticleId(article_id);
					WhatsAPITemplateMessage.POSTRequestTrackEventsByArticleId(article_id, type);
					System.out.println("Subscription WhatsApp Message sent.");
				}
				EmailDTO emaildto = new EmailDTO();
				emaildto.setSubject("Article updated ");
				emaildto.setEmailtext("Hi aritcleid=" + article_id);

				String returnEmail = emailUtil.shootEmail(emaildto);

			} catch (Exception e) {
				e.printStackTrace();
				value = false;
			}

		} catch (Exception ex) {
			trans.rollback();
		} finally {
			// session.close();
			session.close();
		}
		// session.close();

		return ret;
	}

	public static int deleteArticleId(int article_id) {

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		// Query query = session.createNativeQuery("DELETE FROM ARTICLE WHERE ARTICLE_ID
		// = " + article_id + ";");
		// SOFT delte done instead of hard delete form database
		Query query = session
				.createNativeQuery("UPDATE article SET pubstatus_id=0 WHERE article_id = " + article_id + ";");
		// needs other condition too but unable to find correct column
		int ret = 0;
		try {
			ret = query.executeUpdate();
			System.out.println("soft deleteed article_id =  " + article_id);
			trans.commit();
		} catch (Exception ex) {
			trans.rollback();
		} finally {
			// session.close();
			session.close();
		}

		return ret;
	}

}
