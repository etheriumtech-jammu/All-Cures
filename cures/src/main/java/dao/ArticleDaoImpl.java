package dao;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import model.Article;
import util.HibernateUtil;

//1	active
//7	WorkInProgress
//@Component makes sure it is picked up by the ComponentScan (if it is in the right package). This allows @Autowired to work in other classes for instances of this class
@Component
public class ArticleDaoImpl {
	private static ArrayList list = new ArrayList();

	public static ArrayList<Article> findPublishedArticle(int reg_id) {

		// creating seession factory object
		SessionFactory factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory.getCurrentSession();

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session.createNativeQuery(
				"select  article_id  from article  where pubstatus_id = 3 and authored_by = " + reg_id + ";");
		ArrayList<Article> list = (ArrayList<Article>) query.getResultList();
		System.out.println("result list article@@@@@@@@@@@@@" + list);

		return list;
	}

	public static ArrayList<Article> findDraftAricle(int reg_id) {

		// creating seession factory object
		SessionFactory factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory.getCurrentSession();

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session
				.createNativeQuery("select  article_id  from article where pubstatus_id = 1 and authored_by = " + reg_id
						+ " or edited_by = " + reg_id + ";");
		ArrayList<Article> list = (ArrayList<Article>) query.getResultList();
		System.out.println("result list article@@@@@@@@@@@@@" + list);

		return list;
	}

	public static ArrayList<Article> findReviwArticle(int reg_id) {

		// creating seession factory object
		SessionFactory factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory.getCurrentSession();

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session.createNativeQuery("select  article_id  from article  where pubstatus_id = 2;");
		ArrayList<Article> list = (ArrayList<Article>) query.getResultList();
		System.out.println("result list article@@@@@@@@@@@@@" + list);

		return list;
	}

	public static ArrayList<Article> findApprovalArticle(int reg_id) {

		// creating seession factory object
		SessionFactory factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory.getCurrentSession();

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session.createNativeQuery("select  article_id  from article  where pubstatus_id = 2;");
		// needs other condition too but unable to find correct column
		ArrayList<Article> list = (ArrayList<Article>) query.getResultList();
		System.out.println("result list article@@@@@@@@@@@@@" + list);

		return list;
	}

	public Article getArticleDetails(int reg_id) {

		// creating seession factory object
		SessionFactory factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory.getCurrentSession();

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session.createNativeQuery(
				"select article_id,  title , friendly_name,  subheading, content_type, keywords,  window_title,  content_location\r\n"
						+ " ,authored_by, published_by, edited_by, copyright_id, disclaimer_id, create_date, published_date, pubstatus_id, language_id from article  where article_id = "
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
			// article.setContent_type((Integer) obj[4]);
			article.setKeywords((String) obj[5]);
			article.setWindow_title((String) obj[6]);
			article.setContent_location((String) obj[7]);
			System.out.println((String) obj[7]);
//			String file = (String) obj[7];
			String file = "C:\\" + (String) obj[7];
//			file = "C:\\test\\14\\2021\\05\\26\\article_"+(Integer) obj[0]+".json";
			String contents = null;
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
			article.setContent(contents);
		}
		return article;
	}

}
