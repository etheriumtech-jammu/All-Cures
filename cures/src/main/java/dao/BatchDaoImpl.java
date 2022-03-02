package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.EmailDTO;
import service.SendEmailService;
import util.HibernateUtil;

@Component
public class BatchDaoImpl {

	@Autowired
	private SendEmailService emailUtil;

	public int updateAverageRatingArticles() {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();

		String SQL = "UPDATE article dest, ( SELECT \r\n" + "    `article`.`article_id`,\r\n"
				+ "    `article`.`title`,\r\n" + "    group_concat(`doctorsrating`.`ratingVal`),\r\n"
				+ "    AVG(IFNULL(`doctorsrating`.`ratingVal`, 0)) AS ratingValAVG,\r\n" + "    COUNT(*)\r\n"
				+ "FROM\r\n" + "    `article`\r\n" + "        right JOIN\r\n"
				+ "    `doctorsrating` ON `doctorsrating`.`target_id` = `article`.`article_id`\r\n"
				+ "        AND `doctorsrating`.`target_type_id` = 2\r\n" + "GROUP BY article_id\r\n" + ") src\r\n"
				+ "SET dest.over_allrating = src.ratingValAVG\r\n" + "where dest.article_id = src.article_id ; ";

		Query query = session.createNativeQuery(SQL);

		int ret = 0;
		try {
			ret = query.executeUpdate();
			System.out.println(ret);
			EmailDTO emaildto = new EmailDTO();
			emaildto.setSubject("Batch AVG Rating Article");
			emaildto.setEmailtext("Hi, \r\n" + "Batch Process for  AVG Rating sync for Articles completed.");
			ret = 1;
			String returnEmail = emailUtil.shootEmail(emaildto);
			session.getTransaction().commit();

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
//			session.getTransaction().commit();   //session.close();
		}

		return ret;
	}

	public int updateAverageRatingDoctors() {
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();

		String SQL = "UPDATE doctors dest, ( SELECT \r\n" + "    `doctors`.`rowno`,\r\n" + "    `doctors`.`docid`,\r\n"
				+ "    group_concat(`doctorsrating`.`ratingVal`),\r\n"
				+ "    AVG(IFNULL(`doctorsrating`.`ratingVal`, 0)) AS ratingValAVG,\r\n" + "    COUNT(*)\r\n"
				+ "FROM\r\n" + "    `doctors`\r\n" + "        right JOIN\r\n"
				+ "    `doctorsrating` ON `doctorsrating`.`target_id` = `doctors`.`rowno`\r\n"
				+ "        AND `doctorsrating`.`target_type_id` = 1\r\n" + "GROUP BY rowno\r\n" + ") src\r\n"
				+ "SET dest.over_allrating = src.ratingValAVG\r\n" + "where dest.rowno = src.rowno ; ";

		Query query = session.createNativeQuery(SQL);

		int ret = 0;
		try {
			ret = query.executeUpdate();
			System.out.println(ret);
			EmailDTO emaildto = new EmailDTO();
			emaildto.setSubject("Batch AVG Rating Doctors");
			emaildto.setEmailtext("Hi,\r\n" + "Batch Process for AVG Rating sync for Doctors completed.");
			ret = 1;
			String returnEmail = emailUtil.shootEmail(emaildto);
			session.getTransaction().commit();

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
//			session.getTransaction().commit();   //session.close();
		}

		return ret;
	}

}
