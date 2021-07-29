package dao;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import model.Article;
import util.ArticleUtils;
import util.HibernateUtil;
import util.ReadFileUtil;

//1	active
//7	WorkInProgress
//@Component makes sure it is picked up by the ComponentScan (if it is in the right package). This allows @Autowired to work in other classes for instances of this class
@Component
public class ArticleDaoImpl {
	private static ArrayList list = new ArrayList();

	public static ArrayList<Article> findPublishedArticle(int reg_id) {

//		HibernateUtil hu = new HibernateUtil();
//		Session session = hu.getSession();
		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object 
		Session session = factory;
		 
		/*
		 * HibernateUtil hu = new HibernateUtil(); 
		 Session session = hu.getSession();
		 */
		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session.createNativeQuery(
				"select  article_id  from article  where pubstatus_id = 3 and authored_by = " + reg_id + ";");
		ArrayList<Article> list = (ArrayList<Article>) query.getResultList();
		System.out.println("result list article@@@@@@@@@@@@@" + list);
		
		session.close();
		return list;
	}

	public static ArrayList<Article> findDraftAricle(int reg_id) {

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object 
		Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session
				.createNativeQuery("select  article_id  from article where pubstatus_id = 1 and authored_by = " + reg_id
						+ " or edited_by = " + reg_id + ";");
		ArrayList<Article> list = (ArrayList<Article>) query.getResultList();
		System.out.println("result list article@@@@@@@@@@@@@" + list);
		session.close();
		session.close();

		return list;
	}

	public static ArrayList<Article> findReviwArticle(int reg_id) {

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object 
		Session session = factory;
		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session.createNativeQuery("select  article_id  from article  where pubstatus_id = 2;");
		ArrayList<Article> list = (ArrayList<Article>) query.getResultList();
		System.out.println("result list article@@@@@@@@@@@@@" + list);
		session.close();
		return list;
	}

	public static ArrayList<Article> findApprovalArticle(int reg_id) {

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object 
		Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session.createNativeQuery("select  article_id  from article  where pubstatus_id = 2;");
		// needs other condition too but unable to find correct column
		ArrayList<Article> list = (ArrayList<Article>) query.getResultList();
		System.out.println("result list article@@@@@@@@@@@@@" + list);
		session.close();

		return list;
	}

	public Article getArticleDetails(int reg_id) {

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object 
		Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session.createNativeQuery(
				"select article_id,  title , friendly_name,  subheading, content_type, keywords,  window_title,  content_location\r\n"
						+ " ,authored_by, published_by, edited_by, copyright_id, disclaimer_id, create_date, published_date, pubstatus_id, language_id, disease_condition_id, country_id from article  where article_id = "
						+ reg_id + ";");
		ArrayList<Article> articleList = (ArrayList<Article>) query.getResultList();
		Article article = new Article();
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
//			String file = (String) obj[7];
			String file = "C:\\" + (String) obj[7];
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

			article.setAuthored_by((Integer) obj[8]);
			article.setPublished_by((Integer) obj[9]);
			article.setEdited_by((Integer) obj[10]);
			article.setCopyright_id((Integer) obj[11]);
			article.setDisclaimer_id((Integer) obj[12]);
			article.setCreate_date((Date) obj[13]);
			article.setPublished_date((Date) obj[14]);
			article.setPubstatus_id((Integer) obj[15]);
			article.setLanguage_id((Integer) obj[16]);
			article.setDisease_condition_id((Integer) obj[17]);
			article.setCountry_id((Integer) obj[18]);
			article.setContent(contents);
		}
		session.close();

		return article;
	}

	public static ArrayList<Article> getArticlesListAll() {

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object 
		Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session.createNativeQuery("SELECT `article`.`article_id`,\r\n" + "    `article`.`title`,\r\n"
				+ "    `article`.`friendly_name`,\r\n" + "    `article`.`subheading`,\r\n"
				+ "    `article`.`content_type`,\r\n" + "    `article`.`keywords`,\r\n"
				+ "    `article`.`window_title`,\r\n" + "    `article`.`content_location`,\r\n"
				+ "    `article`.`authored_by`,\r\n" + "    `article`.`published_by`,\r\n"
				+ "    `article`.`edited_by`,\r\n" + "    `article`.`copyright_id`,\r\n"
				+ "    `article`.`disclaimer_id`,\r\n" + "    `article`.`create_date`,\r\n"
				+ "    `article`.`published_date`,\r\n" + "    `article`.`pubstatus_id`,\r\n"
				+ "    `article`.`language_id`,\r\n" + "    `article`.`content`\r\n"
				+ "FROM `allcures_schema`.`article`;\r\n" + ";");
		// needs other condition too but unable to find correct column
		ArrayList<Article> list = (ArrayList<Article>) query.getResultList();
		System.out.println("result list article@@@@@@@@@@@@@" + list);
		session.close();

		return list;
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

	public static int updateArticleId(int article_id, HashMap articleMap) {

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
			updatestr += "`content_type` = " + articleMap.get("content_type") + ",\r\n";
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
			updatestr += "`authored_by` = " + articleMap.get("authored_by") + ",\r\n";
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
		if (articleMap.containsKey("published_date")) {
			updatestr += "`published_date` = '" + articleMap.get("published_date") + "',\r\n";
		}
		if (articleMap.containsKey("pubstatus_id")) {
			updatestr += "`pubstatus_id` = " + articleMap.get("pubstatus_id") + ",\r\n";
			// in case article is set to be PUBLISHED status_id=3 set published_date to
			// current date
			if ((String) articleMap.get("pubstatus_id") == "3") {
				java.util.Date date = new java.util.Date();
				updatestr += "`published_date` = '" + date + "',\r\n";
			}
		}
		if (articleMap.containsKey("language_id")) {
			updatestr += "`language_id` = " + articleMap.get("language_id") + ",\r\n";
		}
		if (articleMap.containsKey("content")) {
			updatestr += "`content` = '" + articleMap.get("content") + "',\r\n";
		}
		/*
		 * if (articleMap.containsKey("articleContent")) { updatestr += "`content` = '"
		 * + articleMap.get("articleContent") + "',\r\n"; }
		 */

		// java.sql.Timestamp date = new java.sql.Timestamp(new
		// java.util.Date().getTime());

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
				Article art = new ArticleDaoImpl().getArticleDetails(article_id);
				String art_location = art.getContent_location();
				String content = articleMap.get("articleContent") == null ? ""
						: (String) articleMap.get("articleContent");
				// String content =
				// "{\"time\":1625577023180,\"blocks\":[{\"id\":\"w6K2r9k_v4\",\"type\":\"paragraph\",\"data\":{\"text\":\"hellow
				// anil article
				// 12341111111\"}},{\"id\":\"kuKfW7EeAv\",\"type\":\"paragraph\",\"data\":{\"text\":\"adhdsfa\"}},{\"id\":\"Rh0WezPDqG\",\"type\":\"paragraph\",\"data\":{\"text\":\"thanks,\"}},{\"id\":\"4C5yZUQ9GV\",\"type\":\"paragraph\",\"data\":{\"text\":\"anil
				// 123211111\"}}],\"version\":\"2.21.0\"}";
				value = ArticleUtils.updateArticleContent(art_location, content, article_id, 1);
				value = true;
			} catch (Exception e) {
				e.printStackTrace();
				value = false;
			}

		} catch (Exception ex) {
			trans.rollback();
		} finally {
			//session.close();
			session.close();
		}
		//session.close();

		return ret;
	}

	public static int deleteArticleId(int article_id) {

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object 
		Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session.createNativeQuery("DELETE FROM ARTICLE WHERE ARTICLE_ID = " + article_id + ";");
		// needs other condition too but unable to find correct column
		int ret = 0;
		try {
			ret = query.executeUpdate();
			System.out.println("delete article_id =  " + article_id);
			trans.commit();
		} catch (Exception ex) {
			trans.rollback();
		} finally {
			//session.close();
			session.close();
		}

		return ret;
	}

	public static String readFile(String filePath) {
		return new ReadFileUtil().readFilebyPath(filePath);

	}

}
