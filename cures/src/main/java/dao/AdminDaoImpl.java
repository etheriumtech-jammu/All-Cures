package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.stereotype.Component;

import util.Constant;
import util.HibernateUtil;

@Component
public class AdminDaoImpl {

	public int createTable(String table, HashMap<String, Object> tableDetails) {
		Constant.log("Saving New Table in DB", 1);
		Session session = HibernateUtil.buildSessionFactory();
		session.beginTransaction();
		int ret = 0;
		String insertStr = "INSERT into " + table + " (";
		String insertStr_values = "(";
		try {
			// Iterating HashMap through for loop
			for (Map.Entry<String, Object> docDetail : tableDetails.entrySet()) {

				insertStr += docDetail.getKey() + ", ";
				if (docDetail.getValue() instanceof Integer) {
					insertStr_values += (Integer) (docDetail.getValue()) + ", ";
				} else {
					insertStr_values += "'" + (String) (docDetail.getValue()) + "' , ";
				}
			}

			insertStr = insertStr.substring(0, insertStr.lastIndexOf(","));
			insertStr_values = insertStr_values.substring(0, insertStr_values.lastIndexOf(","));
			String completInsertStr = insertStr + ")" + " values " + insertStr_values + " );";
			Constant.log("Saving " + table + " Meta Data", 1);

			Query query = session.createNativeQuery(completInsertStr);

			// needs other condition too but unable to find correct column
			ret = query.executeUpdate();
			session.getTransaction().commit();
			Constant.log("New " + table + " CREATED in DB", 1);
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
//			session.getTransaction().commit(); // session.close();
		}

		return ret;
	}

	public List<Map<String, Object>> fetchTable(String table) {
		// TODO Auto-generated method stub
		Constant.log("Fetch New table details from database", 1);
		Session session = HibernateUtil.buildSessionFactory();
		session.beginTransaction();
//		int ret = 0;
		String fetchStr = "SELECT * from " + table + ";";
//		List hmFinal = new ArrayList();
		List<Map<String, Object>> aliasToValueMapList = null;
		try {

			Query query = session.createNativeQuery(fetchStr);

			List<Object[]> results = (List<Object[]>) query.getResultList();
			List<Tuple> result = query.getResultList();
			query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			aliasToValueMapList = query.list();

			session.getTransaction().commit();
			Constant.log("details " + table + " FETCH from DB", 1);
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
//			session.getTransaction().commit(); // session.close();
		}
		return aliasToValueMapList;
	}

}
