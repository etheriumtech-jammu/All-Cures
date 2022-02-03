package dao;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import util.Constant;
import util.HibernateUtil;

@Component
public class AdminDaoImpl {

	public int createDoctor(String table, HashMap<String, Object> doctorDetails) {
//		Doctor doc = new Doctor();
		Constant.log("Saving New Doctor in DB", 1);
		Session session = HibernateUtil.buildSessionFactory();
		session.beginTransaction();
		int ret = 0;
		String insertStr = "INSERT into "+table+" (";
		String insertStr_values = "(";
		try {
//			if(null != doctorDetails.get("prefix")) doc.setPrefix((String) doctorDetails.get("prefix"));
//			if(null != doctorDetails.get("name_first")) doc.setDocname_first((String) doctorDetails.get("name_first"));
//			if(null != doctorDetails.get("name_last")) doc.setDocname_last((String) doctorDetails.get("name_last"));
			// Iterating HashMap through for loop
			for (Map.Entry<String, Object> docDetail : doctorDetails.entrySet()) {

				// Printing all elements of a Map
//				System.out.println(docDetail.getKey() + " = " + docDetail.getValue());
				insertStr += docDetail.getKey() + ", ";
				if (docDetail.getValue() instanceof Integer) {
//					Integer new_name = (Integer) docDetail.getValue();
					insertStr_values += (Integer) (docDetail.getValue()) + ", ";
				}
				else {
					insertStr_values += "'"+(String) (docDetail.getValue()) + "' , ";
				}

			}

			insertStr = insertStr.substring(0,insertStr.lastIndexOf(","));
			insertStr_values = insertStr_values.substring(0,insertStr_values.lastIndexOf(","));
			String completInsertStr = insertStr + ")" +" values "+ insertStr_values + " );";
			Constant.log("Saving "+table+" Meta Data" + completInsertStr, 1);

			Query query = session.createNativeQuery(completInsertStr);

			// needs other condition too but unable to find correct column
			ret = query.executeUpdate();
			session.getTransaction().commit();
//			session.save(doc);
			Constant.log("New "+table+" CREATED in DB", 1);
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
//			session.getTransaction().commit(); // session.close();
		}

		return ret;
	}

}
