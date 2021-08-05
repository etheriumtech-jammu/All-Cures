package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import util.HibernateUtil;

@Component
public class RatingDaoImpl {

	public static List findRatingByIdandType(int targetid, int targettypeid) {
		// creating seession factory object
		Session factory = HibernateUtil.buildSessionFactory();

		Session session = factory;

		// creating transaction object
		// Transaction trans = (Transaction) session.beginTransaction();

		Query query = session.createNativeQuery("SELECT * FROM doctorsrating where target_id=" + targetid
				+ " and target_type_id=" + targettypeid + ";");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			HashMap hm = new HashMap();
			Integer rate_id = (Integer) objects[0];
			String comments = (String) objects[1];
			Integer ratedBy_id = (Integer) objects[2];
			Integer ratedBy_type_id = (Integer) objects[3];
			Integer target_id = (Integer) objects[4];
			Integer target_type_id = (Integer) objects[5];
			Float ratingVal = (Float) objects[6];
			hm.put("rate_id", rate_id);
			hm.put("comments", comments);
			hm.put("ratedBy_id", ratedBy_id);
			hm.put("ratedBy_type_id", ratedBy_type_id);
			hm.put("target_id", target_id);
			hm.put("target_type_id", target_type_id);
			hm.put("ratingVal", ratingVal);
			hmFinal.add(hm);
		}
		session.close();
		return hmFinal;

	}

}
