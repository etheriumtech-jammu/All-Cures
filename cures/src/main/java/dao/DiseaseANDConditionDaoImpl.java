package dao;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import model.Article;
import util.HibernateUtil;

//1	active
//7	WorkInProgress
//@Component makes sure it is picked up by the ComponentScan (if it is in the right package). This allows @Autowired to work in other classes for instances of this class
@Component
public class DiseaseANDConditionDaoImpl {
	private static ArrayList list = new ArrayList();

	public static ArrayList<Article> getAllMatchingDCList(String search_str) {

		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		// creating session object
		Session session = factory;

		// creating transaction object
		Transaction trans = (Transaction) session.beginTransaction();

		Query query = session.createNativeQuery("SELECT a.article_id,  a.title , a.friendly_name,  a.subheading, a.content_type, a.keywords,  a.window_title,  a.content_location \r\n"
				+ " ,a.authored_by, a.published_by, a.edited_by, a.copyright_id, a.disclaimer_id, a.create_date, a.published_date, a.pubstatus_id, a.language_id, a.disease_condition_id, a.country_id \r\n"
				+ " FROM allcures_schema.article a \r\n"
				+ "inner join disease_condition dc on a.disease_condition_id = dc.dc_id\r\n"
				+ "inner join languages l on a.language_id = l.language_id\r\n"
				+ "inner join countries c on c.countrycodeid = a.country_id\r\n" + "where (dc_name like '%"+search_str+"%' \r\n"
				+ "or dc_desc like '%"+search_str+"%'\r\n" + "or title  like '%"+search_str+"%'\r\n" + "or friendly_name  like '%"+search_str+"%'\r\n"
				+ "or window_title  like '%"+search_str+"%'\r\n" + "or countryname like '%"+search_str+"%'\r\n" + "or lang_name like '%"+search_str+"%'\r\n"
				+ ")\r\n" + "and pubstatus_id = 3 \r\n" + "\r\n" + "");
		// needs other condition too but unable to find correct column
		ArrayList<Article> list = (ArrayList<Article>) query.getResultList();
		System.out.println("result list article@@@@@@@@@@@@@" + list);
		session.close();

		return list;
	}

}
